package com.stocks_analyzer.core

import com.twitter.conversions.DurationOps.RichDuration
import com.twitter.finagle.Http

object HttpClientService {

  def general: Http.Client =
    Http.client.configuredParams(
      Http.client.withTransport
        .connectTimeout(3.minute)
        .withSessionQualifier
        .noFailFast
        .params
    )
}
