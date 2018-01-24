package com.gotovac.backend.service

import akka.http.scaladsl.model.ws.Message
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.scaladsl.Flow

case class Rest(socketFlow: Flow[Message, Message, Any]) extends Directives {

  def route: Route =
    get {
      pathSingleSlash {
        getFromResource("index.html")
      } ~
        pathSuffix("frontend-fastopt.js") {
          getFromResource("frontend-fastopt.js")
        }
    } ~ path("socket") {
      handleWebSocketMessages(socketFlow)
    }

}
