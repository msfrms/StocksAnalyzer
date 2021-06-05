package com.stocks_analyzer

import com.twitter.finagle.Service
import com.twitter.util.Future
import io.circe.Json
import io.circe.parser.parse
import io.circe.generic.auto._

import scala.io.Source

object StockService {

  def apply(): Service[Unit, List[Stock]] =
    Service.mk(_ =>
      Future {
        val source = Source.fromFile("stocks.json")
        val json   = source.mkString
        source.close()

        val doc: Json = parse(json).getOrElse(Json.Null)

        val result = doc.hcursor
          .downField("payload")
          .downField("instruments")
          .as[List[Stock]]

        result match {
          case Left(value)  => throw value
          case Right(value) => value
        }
      }
    )
}
