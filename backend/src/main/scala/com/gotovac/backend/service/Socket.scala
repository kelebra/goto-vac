package com.gotovac.backend.service

import akka.actor.ActorSystem
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{BroadcastHub, Flow, Keep, MergeHub}

case class Socket(processing: PartialFunction[String, String])
                 (implicit val system: ActorSystem, mat: ActorMaterializer) {

  private val transformation =
    Flow[Message]
      .map { case TextMessage.Strict(json) =>
        system.log.info(s"Received message: $json"); processing(json)
      }
      .map(json => TextMessage(json))

  val replyFlow: Flow[Message, Message, Any] = transformation

  private val (broadcastSink, broadcastSource) =
    MergeHub.source[Message].toMat(BroadcastHub.sink[Message])(Keep.both).run()

  val broadcastFlow: Flow[Message, Message, Any] =
    transformation.via(Flow.fromSinkAndSource(broadcastSink, broadcastSource))
}
