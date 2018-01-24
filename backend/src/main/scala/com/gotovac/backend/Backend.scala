package com.gotovac.backend

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.gotovac.backend.service.{Rest, Socket}

import scala.util.{Failure, Success, Try}

object Backend extends App {

  val host = Try(args(0).toString).getOrElse("0.0.0.0")
  val port = Try(args(1).toInt).getOrElse(4567)

  implicit val system: ActorSystem = ActorSystem("goto-vac-system")

  import system.dispatcher

  implicit val mat: ActorMaterializer = ActorMaterializer()

  val socket = Socket()
  val rest = Rest(socket.flow)

  val binding = Http().bindAndHandle(rest.route, host, port)

  binding.onComplete {
    case Success(b) =>
      val local = b.localAddress
      system.log.info("Server is listening to {}:{}", local.getHostName, local.getPort)
    case Failure(e) =>
      system.log.error(e, "Could not start server at {}:{}", host, port)
  }
}
