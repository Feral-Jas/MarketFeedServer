package me.decapre.util

import me.decapre.common.schema.binance.SubscribeRequest
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

/** @author Chenyu.Liu
  * @date 2022/1/30
  * @description ${DESCRIPTION}
  */
trait JsonSupport extends DefaultJsonProtocol {
  implicit val binanceSubscribeFormatter: RootJsonFormat[SubscribeRequest] = jsonFormat3(SubscribeRequest)
}
