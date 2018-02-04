package com.gotovac.model

import java.util.UUID

import com.gotovac.model.Types.Login

case class Token(login: Login, value: String = UUID.randomUUID().toString) {

  def anonymous: Token = copy(value = "")
}
