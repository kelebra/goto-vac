package com.gotovac.frontend

import com.gotovac.frontend.pages.Login
import com.gotovac.frontend.socket.{BroadcastSocket, ReplySocket}

import scala.scalajs.js

object Frontend extends js.JSApp {

  override def main(): Unit = {
    ReplySocket.connect()
    BroadcastSocket.connect()
    ReplySocket.loginWithToken()
    Login.render()
  }
}
