package com.gotovac.backend.service

import com.gotovac.model.{Credentials, Token, UserOnline}
import upickle.default._

import scala.util.Try

package object chain {

  type Processor = String => String

  def typedMapper[T](json: String)
                    (errorMessage: String)
                    (stage: T => String)
                    (implicit urd: Reader[T]): String =
    Try(read[T](json)).map(stage).getOrElse(errorMessage)

  val credentialsProcess: Processor =
    json => typedMapper[Credentials](json)("Wrong credentials")(_ => write(Token()))

  val onlineProcess: Processor =
    json => typedMapper[UserOnline](json)("")(_ => json)
}
