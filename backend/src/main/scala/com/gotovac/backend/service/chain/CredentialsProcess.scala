package com.gotovac.backend.service.chain

import com.gotovac.backend.service.chain.Types.{Json, JsonReply}
import com.gotovac.backend.service.repository.UserRepository
import com.gotovac.model.{Credentials, Forbidden}
import prickle.Pickle.{intoString => write}
import prickle.Unpickle

import scala.concurrent.ExecutionContext

class CredentialsProcess(userRepository: UserRepository)
                        (implicit ctx: ExecutionContext)
  extends PartialFunction[Json, JsonReply] {

  override def apply(json: Json): JsonReply = {
    val credentials = Unpickle[Credentials].fromString(json).get
    userRepository.authorize(credentials)
      .map {
        case Some(value) => write(value)
        case _           => write(Forbidden("Invalid login/password"))
      }
  }

  override def isDefinedAt(json: Json): Boolean =
    Unpickle[Credentials].fromString(json).isSuccess
}
