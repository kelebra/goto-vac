package com.gotovac.backend.service.chain

import com.gotovac.model.{Credentials, Token}
import prickle.Pickle.{intoString => write}
import prickle.Unpickle

object CredentialsProcess extends PartialFunction[String, String] {

  override def apply(json: String): String = {
    val login = Unpickle[Credentials].fromString(json).get.login
    write(Token(login))
  }

  override def isDefinedAt(x: String): Boolean = Unpickle[Credentials].fromString(x).isSuccess
}
