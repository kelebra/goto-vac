package com.gotovac.backend.service

import akka.actor.ActorSystem
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{BroadcastHub, Flow, Keep, MergeHub}

case class Socket(implicit val system: ActorSystem, mat: ActorMaterializer) {

  private val (sink, source) =
    MergeHub.source[Message].toMat(BroadcastHub.sink[Message])(Keep.both).run()

  private val transormation =
    Flow[Message].map { case TextMessage.Strict(text) => TextMessage("Hello" + text) }

  val flow: Flow[Message, Message, Any] = transormation.via(Flow.fromSinkAndSource(sink, source))
}
