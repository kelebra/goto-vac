package com.gotovac.frontend.socket

import com.gotovac.frontend.socket.storage.LoginStorage
import com.gotovac.model.{Credentials, StateRequest, Token}
import upickle.default.{read, write}

import scala.util.Try

object ReplySocket extends Socket {

  override val name = "reply"

  def login(credentials: Credentials): Unit = send(write(credentials))

  def requestInitialState(token: Token): Unit = send(write(StateRequest(token)))

  override def onMessage(json: String): Unit = {
    onTokenUpdate(json)(token => {
      LoginStorage.store(token)
      BroadcastSocket.reportOnline(token.login)
      requestInitialState(token)
    })
  }

  private def onTokenUpdate(json: String)(callback: Token => Unit): Unit = {
    Try(read[Token](json)).map(callback)
  }
}
