package com.gotovac.model

import com.gotovac.model.Types.Login

sealed trait Event

case class UserOnline(login: Login, online: Boolean) extends Event

object UserOnline {

  import upickle.default.{macroRW, ReadWriter => RW}

  implicit val userOnlineJson: RW[UserOnline] = macroRW
}