package com.gotovac.frontend.socket

import com.gotovac.frontend.pages.Calendar
import com.gotovac.frontend.socket.storage.LoginStorage
import com.gotovac.model.{Credentials, GroupState, StateRequest, Token}
import prickle.Pickle.{intoString ⇒ write}
import prickle.Unpickle

object ReplySocket extends Socket {

  override def name = "reply"

  def login(credentials: Credentials): Unit = send(write(credentials))

  def requestInitialState(token: Token): Unit = send(write(StateRequest(token)))

  override def onMessage(json: String): Unit = {
    onTokenUpdate(json)(token ⇒ {
      LoginStorage.clear()
      LoginStorage.store(token)
      BroadcastSocket.reportOnline(token.login)
      requestInitialState(token)
    })
    onInitialState(json)(Calendar.renderCalendarView)
  }

  private def onTokenUpdate(json: String)(callback: Token ⇒ Unit): Unit = {
    Unpickle[Token].fromString(json).foreach(callback)
  }

  private def onInitialState(json: String)(callback: GroupState ⇒ Unit): Unit = {
    Unpickle[GroupState].fromString(json).foreach(callback)
  }
}
