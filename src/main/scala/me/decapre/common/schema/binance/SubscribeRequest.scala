package me.decapre.common.schema.binance

/** @author Chenyu.Liu
  * @date 2022/1/30
  * @description
  * {
  *  "method": "SUBSCRIBE",
  *  "params": [
  *    "btcusdt@trade"
  *  ],
  *  "id": 1
  * }
  */
case class SubscribeRequest(
    method: String = "SUBSCRIBE",
    params: Seq[String],
    id: Int
)
