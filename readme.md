# TradeChannelActor
1. connect to websocket channel
2. keep channel alive and store trades data
3. auto reconnect

### pre-requisition
1. pure-config
2. FP typed actor
3. akka-http websocket
4. akka-stream 

#### binance trade 
{
"e": "trade",     // Event type
"E": 123456789,   // Event time
"s": "BNBBTC",    // Symbol
"t": 12345,       // Trade ID
"p": "0.001",     // Price
"q": "100",       // Quantity
"b": 88,          // Buyer order ID
"a": 50,          // Seller order ID
"T": 123456785,   // Trade time
"m": true,        // Is the buyer the market maker?
"M": true         // Ignore
}

#### TODOs
1. high level abstractions
   1. Source (TradeSource)
   2. Adaptor (incoming/outgoing)