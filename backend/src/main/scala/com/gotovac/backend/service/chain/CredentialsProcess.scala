package com.gotovac.backend.service.chain

import com.gotovac.model.{Credentials, Token}
import upickle.default.{read, write}

object CredentialsProcess extends Producer {

  override def apply(json: String): String = {
    val login = read[Credentials](json).login
    write(Token(login))
  }
}
