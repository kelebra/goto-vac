package com.gotovac.frontend.socket

import com.gotovac.model.Credentials
import upickle.default.write

object ReplySocket extends Socket {

  override val name = "reply"

  def login(credentials: Credentials): Unit = send(write(credentials))

  override def onMessage(json: String): Unit = println(json)
}
