package com.gotovac.frontend.socket

import com.gotovac.frontend.pages.components.Notification
import com.gotovac.model.Forbidden
import org.scalajs.dom.document.location
import org.scalajs.dom.raw.{Event, WebSocket}
import org.scalajs.dom.{MessageEvent, console}
import prickle.Unpickle

trait Socket {

  val name: String
  private var underlying = refreshSocket()

  def connect(): Unit = {
    underlying = refreshSocket()

    underlying.onopen = {
      (_: Event) => console.log(s"$name - connection was opened")
    }
    underlying.onmessage = {
      (e: MessageEvent) => {
        val json = e.data.toString
        onForbidden(json) { forbidden => Notification.error(forbidden.message) }
        onMessage(json)
      }
    }
    underlying.onclose = {
      (_: Event) =>
        console.log(s"$name - connection lost with backend")
        connect()
    }
  }

  private def refreshSocket(): WebSocket = new WebSocket(socketUrl(name))

  private def onForbidden(json: String)(callback: Forbidden => Unit): Unit =
    Unpickle[Forbidden].fromString(json).foreach(callback)

  private def socketUrl(name: String): String = {
    val port = location.port
    val portString = if (port.isEmpty) "" else s":$port"
    s"ws://${location.hostname}$portString/$name"
  }

  def send(json: String): Unit = underlying.send(json)

  def onMessage(json: String): Unit
}