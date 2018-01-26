package com.gotovac.backend.service.chain

import upickle.default.{read, _}

import scala.util.Try

abstract class Producer[T](implicit val urd: Reader[T]) extends PartialFunction[String, String] {

  override def isDefinedAt(x: String): Boolean = Try(read[T](x)).isSuccess
}
