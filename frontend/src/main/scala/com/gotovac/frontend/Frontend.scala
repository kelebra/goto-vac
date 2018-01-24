package com.gotovac.frontend

import com.gotovac.frontend.pages.Login
import com.gotovac.frontend.socket.SocketRouter

import scala.scalajs.js

object Frontend extends js.JSApp {

  override def main(): Unit = {
    SocketRouter.init()
    Login.render()
  }
}
