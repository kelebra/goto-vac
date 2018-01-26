package com.gotovac.model

import com.gotovac.model.Types.Login

case class Credentials(login: Login, password: String)

object Credentials {

  import upickle.default.{macroRW, ReadWriter => RW}

  implicit val credentialsJson: RW[Credentials] = macroRW
}