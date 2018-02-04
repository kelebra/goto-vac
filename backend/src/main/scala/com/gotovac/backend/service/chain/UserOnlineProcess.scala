package com.gotovac.backend.service.chain

import com.gotovac.model.UserOnline
import prickle.Unpickle

object UserOnlineProcess extends PartialFunction[String, String] {

  override def apply(json: String): String = json

  override def isDefinedAt(x: String): Boolean =
    Unpickle[UserOnline].fromString(x).isSuccess
}
