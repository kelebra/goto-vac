package com.gotovac.model

import com.gotovac.model.Types.Login

case class Token(login: Login, value: String) {

  def anonymous: Token = copy(value = "")
}
