package me.decapre.common.schema.ftx

/** @author Chenyu.Liu
  * @date 2022/1/31
  * @description
  * {'op': 'subscribe', 'channel': 'trades', 'market': 'BTC-PERP'}
  */
case class SubscribeRequest(
    op: String = "subscribe",
    channel: String = "trades",
    market: String
)
