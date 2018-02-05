package com.gotovac.backend.service

import akka.actor.ActorSystem
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{BroadcastHub, Flow, Keep, MergeHub}
import com.gotovac.backend.service.chain.Types
import com.gotovac.backend.service.chain.Types.{Json, JsonReply}

import scala.concurrent.{ExecutionContext, Future}

case class Socket(processing: PartialFunction[Json, JsonReply])
                 (implicit val system: ActorSystem, mat: ActorMaterializer) {

  implicit val ctx: ExecutionContext = system.dispatcher

  private val transformation =
    Flow[Message]
      .mapAsync(1) {
        case TextMessage.Strict(json) => processing(json)
        case _                        => Future.successful(Types.noOp)
      }
      .filterNot(_ == Types.noOp)
      .map(TextMessage.apply)

  val replyFlow: Flow[Message, Message, Any] = transformation

  private val (broadcastSink, broadcastSource) =
    MergeHub.source[Message].toMat(BroadcastHub.sink[Message])(Keep.both).run()

  val broadcastFlow: Flow[Message, Message, Any] =
    transformation.via(Flow.fromSinkAndSource(broadcastSink, broadcastSource))
}
