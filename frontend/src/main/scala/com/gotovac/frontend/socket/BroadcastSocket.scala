package com.gotovac.frontend.socket

import com.gotovac.frontend.pages.Calendar
import com.gotovac.frontend.pages.components.Notification
import com.gotovac.frontend.socket.storage.LoginStorage
import com.gotovac.model.Types.Login
import com.gotovac.model.{SelectedDate, StateUpdate, UserOnline}
import prickle.Pickle.{intoString ⇒ write}
import prickle.Unpickle

object BroadcastSocket extends Socket {

  override def name: String = "broadcast"

  override def onMessage(json: String): Unit = {
    onOnline(json)(status ⇒
      Notification.info(
        s"User ${status.login} is ${if (status.online) "online" else "offline"}")
    )
    onStateUpdate(json)(Calendar.update)
  }

  def reportOnline(login: Login): Unit = send(write(UserOnline(login, online = true)))

  def modifyDate(selectedDate: SelectedDate, selected: Boolean): Unit =
    send(write(StateUpdate(LoginStorage.token, selectedDate, selected)))

  private def onOnline(json: String)(callback: UserOnline ⇒ Unit): Unit =
    Unpickle[UserOnline].fromString(json)
      .filter(status ⇒ status.login != LoginStorage.token.login)
      .foreach(callback)

  private def onStateUpdate(json: String)(callback: StateUpdate ⇒ Unit): Unit =
    Unpickle[StateUpdate].fromString(json).foreach(callback)
}
