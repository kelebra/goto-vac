package com.gotovac.backend.service

import akka.http.scaladsl.server.{Directives, Route}

object Rest extends Directives {

  def route: Route =
    get {
      pathSingleSlash {
        getFromResource("index.html")
      } ~
        pathSuffix("frontend-fastopt.js") {
          getFromResource("frontend-fastopt.js")
        }
    }
}
