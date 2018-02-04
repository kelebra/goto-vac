package com.gotovac.backend

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.gotovac.backend.service.chain.{CredentialsProcess, StateRequestProcess, StateUpdateProcess, UserOnlineProcess}
import com.gotovac.backend.service.database.{Db, StateRepository, UserRepository}
import com.gotovac.backend.service.{Rest, Socket}

import scala.util.{Failure, Success, Try}

object Backend extends App {

  val host = Try(args(0).toString).getOrElse("0.0.0.0")
  val port = Try(args(1).toInt).getOrElse(4567)

  Db.init()

  implicit val system: ActorSystem = ActorSystem("goto-vac-system")

  import system.dispatcher

  implicit val mat: ActorMaterializer = ActorMaterializer()

  val credentialsProcess = new CredentialsProcess(UserRepository)
  val stateRequestProcess = new StateRequestProcess(StateRepository)
  val stateUpdateProcess = new StateUpdateProcess(StateRepository)

  val processing =
    stateRequestProcess
      .orElse(stateUpdateProcess)
      .orElse(UserOnlineProcess)
      .orElse(credentialsProcess)

  val socket = Socket(processing)
  val rest = Rest(socket.replyFlow, socket.broadcastFlow)

  val binding = Http().bindAndHandle(rest.route, host, port)

  binding.onComplete {
    case Success(b) =>
      val local = b.localAddress
      system.log.info("Server is listening to {}:{}", local.getHostName, local.getPort)
    case Failure(e) =>
      system.log.error(e, "Could not start server at {}:{}", host, port)
  }
}
