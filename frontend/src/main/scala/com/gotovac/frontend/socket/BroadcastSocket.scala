package com.gotovac.frontend.socket

import com.gotovac.frontend.pages.components.Notification
import com.gotovac.frontend.socket.storage.LoginStorage
import com.gotovac.model.Types.Login
import com.gotovac.model.UserOnline
import upickle.default.{read, write}

import scala.util.Try

object BroadcastSocket extends Socket {

  override val name: String = "broadcast"

  override def onMessage(json: String): Unit = {
    onOnline(json, status => Notification.display(
      s"User ${status.login} is ${if (status.online) "online" else "offline"}")
    )
  }

  def reportOnline(login: Login): Unit = send(write(UserOnline(login, online = true)))

  private def onOnline(json: String, callback: UserOnline => Unit): Unit =
    Try(read[UserOnline](json))
      .filter(status => status.login != LoginStorage.login)
      .foreach(callback)
}
