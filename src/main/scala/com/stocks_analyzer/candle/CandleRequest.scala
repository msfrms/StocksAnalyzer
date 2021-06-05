package com.stocks_analyzer.candle

import com.twitter.finagle.http.{Method, Request}

object CandleRequest {
  def host: String = "api-invest.tinkoff.ru"

  def byFigi(
      figi: String,
      from: String,
      to: String,
      interval: String,
      token: String
  ): Request = {
    val request = Request(
      Method.Get,
      s"/openapi/market/candles?figi=$figi&from=$from&to=$to&interval=$interval"
    )
    request.host = host
    request.userAgent = "StocksAnalyzer"
    request.headerMap.add("Authorization", s"Bearer $token")
    request.setContentTypeJson()
    request
  }
}
