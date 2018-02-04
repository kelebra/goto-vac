package com.gotovac.backend.service.chain

import com.gotovac.backend.service.repository.UserRepository
import com.gotovac.model.{Credentials, Forbidden}
import prickle.Pickle.{intoString => write}
import prickle.Unpickle

class CredentialsProcess(userRepository: UserRepository)
  extends PartialFunction[String, String] {

  override def apply(json: String): String = {
    val credentials = Unpickle[Credentials].fromString(json).get
    val token = userRepository.authorize(credentials)
    token
      .map(value => write(value))
      .getOrElse(write(Forbidden("Invalid login/password")))
  }

  override def isDefinedAt(x: String): Boolean =
    Unpickle[Credentials].fromString(x).isSuccess
}
