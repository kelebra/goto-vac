package com.gotovac.frontend.socket

object BroadcastSocket extends Socket {

  override val name: String = "broadcast"

  override def onMessage(json: String): Unit = println(json)
}
