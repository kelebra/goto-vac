package com.gotovac.model

import java.util.UUID

import com.gotovac.model.Types.Login

case class Token(login: Login, value: String = UUID.randomUUID().toString)

object Token {

  import upickle.default.{macroRW, ReadWriter => RW}

  implicit val tokenJson: RW[Token] = macroRW
}
