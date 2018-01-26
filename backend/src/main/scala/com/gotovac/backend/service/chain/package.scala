package com.gotovac.backend.service

import com.gotovac.model.{Credentials, Token, UserOnline}
import upickle.default._

import scala.util.Try

package object chain {

  private type Chain = PartialFunction[String, String]

  abstract class Producer[T](implicit val urd: Reader[T]) extends Chain {

    override def isDefinedAt(x: String): Boolean = Try(read[T](x)).isSuccess
  }

  private val credentialsProcess: Chain = new Producer[Credentials]() {

    override def apply(v1: String): String = {
      val login = read[Credentials](v1).login
      write(Token(login))
    }
  }

  private val userOnlineProcess: Chain = new Producer[UserOnline]() {

    override def apply(v1: String): String = v1
  }

  val jsonProcessing: Chain = credentialsProcess.orElse(userOnlineProcess)
}
