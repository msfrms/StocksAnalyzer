package com.stocks_analyzer

final case class Stock(
    figi: String,
    ticker: String,
    isin: String,
    minPriceIncrement: Option[Double],
    lot: Int,
    currency: String,
    name: String
)

final case class CandleResponse(
    figi: String,
    interval: String,
    candles: List[Candle]
)

final case class Candle(
    figi: String,
    interval: String,
    // open
    o: Double,
    // close
    c: Double,
    // max
    h: Double,
    // min
    l: Double,
    // value
    v: Int,
    time: String
)
