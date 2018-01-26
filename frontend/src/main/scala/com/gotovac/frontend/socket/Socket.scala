package com.gotovac.frontend.socket

import org.scalajs.dom.document.location
import org.scalajs.dom.raw.{Event, WebSocket}
import org.scalajs.dom.{MessageEvent, console}

trait Socket {

  val name: String
  private lazy val underlying: WebSocket = new WebSocket(socketUrl(name))

  def connect(): Unit = {
    underlying.onopen = {
      (_: Event) => console.log(s"$name - connection was opened")
    }
    underlying.onmessage = {
      (e: MessageEvent) => onMessage(e.data.toString)
    }
  }

  private def socketUrl(name: String): String = {
    val port = location.port
    val portString = if (port.isEmpty) "" else s":$port"
    s"ws://${location.hostname}$portString/$name"
  }

  def send(json: String): Unit = underlying.send(json)

  def onMessage(json: String): Unit
}