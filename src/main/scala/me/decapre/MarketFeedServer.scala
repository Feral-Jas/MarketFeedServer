package me.decapre

import akka.Done
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import me.decapre.actor.TradeChannelActor
import me.decapre.actor.TradeChannelActor.SubscribeChannel
import me.decapre.common.schema.ServerConfig
import pureconfig.ConfigSource
import pureconfig.generic.auto._

/** @author Chenyu.Liu
  * @date 2022/1/30
  * @description ${DESCRIPTION}
  */
object MarketFeedServer {
  def main(args: Array[String]): Unit = {
    val serverConfig = ConfigSource.default.loadOrThrow[ServerConfig]
    ActorSystem(
      Behaviors.setup[Done] { ctx =>
        serverConfig.tradeSources.foreach { tradeSource =>
          val tradeActor = ctx
            .spawn(TradeChannelActor.apply(tradeSource.symbol("btcusdt")), TradeChannelActor.name)
          tradeActor ! SubscribeChannel
        }
        Behaviors.same
      },
      "MarketFeedServer"
    )
  }
}
