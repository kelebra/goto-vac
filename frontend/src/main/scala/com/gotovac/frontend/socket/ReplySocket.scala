package com.gotovac.frontend.socket

import com.gotovac.frontend.socket.storage.LoginStorage
import com.gotovac.model.{Credentials, Token}
import upickle.default.{read, write}

object ReplySocket extends Socket {

  override val name = "reply"

  def login(credentials: Credentials): Unit = send(write(credentials))

  override def onMessage(json: String): Unit = {
    val token = read[Token](json)
    LoginStorage.store(token)
    BroadcastSocket.reportOnline(token.login)
  }
}
