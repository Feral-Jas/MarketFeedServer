package me.decapre.actor

import akka.Done
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior, SupervisorStrategy}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws._
import akka.stream.scaladsl.{Keep, RunnableGraph, Sink, Source}
import me.decapre.common.schema.TradeSource
import me.decapre.common.schema.binance.SubscribeRequest
import me.decapre.util.{JsonSupport, LoggingSupport}
import spray.json._

import scala.concurrent.{ExecutionContext, Future}

/** @author Chenyu.Liu
  * @date 2022/1/30
  * @description
  * state:
  *  websocket connection
  *  current channel trade data(merged) by seconds
  */
object TradeChannelActor extends LoggingSupport with JsonSupport {
  final val name = "TradeChannelActor"
  sealed trait Command
  case object SubscribeChannel extends Command

  // store merged trades data
  private var tradeRawData = ""

  def initFlow(
      tradeSource: TradeSource,
      messageHandler: TextMessage.Strict => Unit
  )(implicit
      system: ActorSystem[_]
  ): RunnableGraph[(Future[WebSocketUpgradeResponse], Future[Done])] = {
    logger.info(s"Init stream for ${tradeSource.toString}")
    val incoming: Sink[Message, Future[Done]] =
      Sink.foreach[Message] {
        case message: TextMessage.Strict =>
          messageHandler(message)
        case _ =>
      }
    val outgoing = Source
      .single(
        TextMessage(
          SubscribeRequest(
            params = Seq(tradeSource.channel),
            id = 1
          ).toJson.toString
        )
      )
      .concatMat(Source.maybe[TextMessage])(Keep.right)
    // flow to use (note: not re-usable!)
    val webSocketFlow =
      Http().webSocketClientFlow(WebSocketRequest(tradeSource.baseEndpoint))
    outgoing
      .viaMat(webSocketFlow)(
        Keep.right
      ) // keep the materialized Future[WebSocketUpgradeResponse]
      .toMat(incoming)(Keep.both) // also keep the Future[Done]
  }

  private def onReceive(
      flow: RunnableGraph[(Future[WebSocketUpgradeResponse], Future[Done])]
  )(implicit
      actorSystem: ActorSystem[_],
      ec: ExecutionContext
  ): Behavior[Command] = {
    Behaviors.receiveMessage { case SubscribeChannel =>
      val (upgradeResponse, closed) = flow.run()
      upgradeResponse.map {
        case ValidUpgrade(response, chosenSubprotocol) =>
          logger.info(
            s"Channel connection established - ${response.status.value} | ${chosenSubprotocol.getOrElse("")}"
          )
        case InvalidUpgradeResponse(response, cause) =>
          logger.error(
            s"Failed to establish connection - ${response.status.value} | $cause"
          )
      }
      closed.map(_ => logger.error("closed"))
      Behaviors.same
    }
  }

  def apply(tradeSource: TradeSource): Behavior[Command] = Behaviors
    .supervise(
      Behaviors.setup[Command] { ctx =>
        implicit val system: ActorSystem[Nothing] = ctx.system
        implicit val ec: ExecutionContext = ctx.executionContext
        val flow = TradeChannelActor.initFlow(
          tradeSource,
          msg => logger.info(msg.text.parseJson.toString)
        )
        TradeChannelActor.onReceive(flow)
      }
    )
    .onFailure(SupervisorStrategy.restart)
}
