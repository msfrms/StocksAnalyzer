package com.stocks_analyzer.candle

import com.stocks_analyzer.core.HttpClientService
import com.stocks_analyzer.{Candle, CandleResponse}

import com.twitter.finagle.Service
import com.twitter.util.Future

import io.circe.Json
import io.circe.generic.auto._
import io.circe.parser.parse

import java.time.format.DateTimeFormatter
import java.time.{OffsetDateTime, ZoneId}

object CandleService {

  def fiveYears(figi: String, token: String): Service[Unit, List[Candle]] =
    Service.mk { _ =>
      val to        = OffsetDateTime.now(ZoneId.of("Europe/Moscow"))
      val from      = to.minusYears(5)
      val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
      apply(
        figi = figi,
        from = from.format(formatter),
        to = to.format(formatter),
        token = token
      ).apply(())
    }

  def apply(
      figi: String,
      from: String,
      to: String,
      token: String,
      // Available values : 1min, 2min, 3min, 5min, 10min, 15min, 30min, hour, day, week, month
      interval: String = "month"
  ): Service[Unit, List[Candle]] =
    Service.mk(_ =>
      HttpClientService.general
        .withTls(CandleRequest.host)
        .withTlsWithoutValidation
        .newService(s"${CandleRequest.host}:443", "candles")
        .apply(CandleRequest.byFigi(figi, from, to, interval, token))
        .flatMap { rep =>
          val doc: Json = parse(rep.contentString).getOrElse(Json.Null)

          val result = doc.hcursor
            .downField("payload")
            .as[CandleResponse]

          result match {
            case Left(value) =>
              Future.exception(new Throwable(s"error by figi: $figi", value))
            case Right(value) =>
              Future.value(value.candles)
          }
        }
    )
}
