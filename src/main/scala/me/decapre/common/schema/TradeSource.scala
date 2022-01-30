package me.decapre.common.schema

import me.decapre.util.JsonSupport

/** @author Chenyu.Liu
  * @date 2022/1/30
  * @description store each exchange-channel trade stream info
  */
case class TradeSource(
    name: String,
    baseEndpoint: String,
    channel: String
) extends JsonSupport{
  def symbol(symbol: String): TradeSource =
    this.copy(channel = symbol + channel)

  override def toString: String = {
    s"$name | $baseEndpoint | $channel"
  }

  def buildRequest:String = name match {
    case "Binance" => ""
    case "FTX" => ""
  }
}
