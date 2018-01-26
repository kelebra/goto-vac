package com.gotovac.model

import java.util.UUID

case class Token(value: String = UUID.randomUUID().toString)

object Token {

  import upickle.default.{macroRW, ReadWriter => RW}

  implicit val tokenJson: RW[Token] = macroRW
}
