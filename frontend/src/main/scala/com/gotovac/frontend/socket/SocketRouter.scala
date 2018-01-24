package com.gotovac.frontend.socket

import com.gotovac.model.Credentials
import org.scalajs.dom.document.location
import org.scalajs.dom.raw.{Event, WebSocket}
import org.scalajs.dom.{MessageEvent, console}
import upickle.default.{macroRW, ReadWriter => RW, _}

object SocketRouter {

  private implicit val credentialsJson: RW[Credentials] = macroRW

  private val socket: WebSocket = new WebSocket(connectionLine)

  def init(): Unit = {
    socket.onmessage = {
      (e: MessageEvent) => console.log(e)
    }

    socket.onopen = {
      (e: Event) => console.log(e)
    }
  }

  private def connectionLine: String = {
    val port = location.port
    val portString = if (port.isEmpty) "" else s":$port"
    s"ws://${location.hostname}$portString/socket"
  }

  def connect(credentials: Credentials): Unit = socket.send(write(credentials))
}
