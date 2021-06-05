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
