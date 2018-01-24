package com.gotovac.backend.service

import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.scaladsl.{Flow, Source}

object Rest extends Directives {

  def route: Route =
    get {
      pathSingleSlash {
        getFromResource("index.html")
      } ~
        pathSuffix("frontend-fastopt.js") {
          getFromResource("frontend-fastopt.js")
        }
    } ~ path("socket") {
      handleWebSocketMessages(flow)
    }

  private val flow: Flow[Message, Message, Any] =
    Flow[Message].mapConcat {
      case text: TextMessage =>
        TextMessage(Source.single("Hello ") ++ text.textStream ++ Source.single("!")) :: Nil // TODO: change
    }
}
