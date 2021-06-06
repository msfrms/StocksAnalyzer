package com.stocks_analyzer.candle

import com.stocks_analyzer.stock.StockService

import com.twitter.conversions.DurationOps.RichDuration
import com.twitter.finagle.Service
import com.twitter.finagle.util.DefaultTimer
import com.twitter.util.{Future, Timer}

import java.text.SimpleDateFormat

final case class StatsStock(ticker: String, name: String, shareUrl: String)

object CandlesForAllStocksService {

  def bestStocks: Service[String, List[StatsStock]] =
    Service.mk { token =>
      implicit val timer: Timer = DefaultTimer
      for {
        stocks <- StockService().apply(())

        stocksBatch = stocks.grouped(200).toList

        bestStocks <-
          Future
            .traverseSequentially(stocksBatch)(stocks =>
              Future
                .collect(
                  stocks.map(stock =>
                    CandleService
                      .fiveYears(stock.figi, token)
                      .apply(())
                      .map { candles =>
                        if (candles.nonEmpty) {
                          val formatter =
                            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

                          val sortByDescDate = candles.sortWith {
                            case (left, right) =>
                              formatter
                                .parse(left.time)
                                .getTime > formatter.parse(right.time).getTime
                          }

                          val historyMinCloseSum = candles.map(_.c).min
                          val todayCloseSum      = sortByDescDate.head.c

                          if (todayCloseSum < historyMinCloseSum) {
                            Some(
                              StatsStock(
                                ticker = stock.ticker,
                                name = stock.name,
                                shareUrl = stock.shareUrl
                              )
                            )
                          } else
                            None
                        } else None
                      }
                  )
                )
                .delayed(2.minute)
            )
            .map(_.flatten.flatten.toList)
      } yield bestStocks
    }
}
