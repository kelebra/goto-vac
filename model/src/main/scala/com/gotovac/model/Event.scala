package com.gotovac.model

import java.time.LocalDate

import com.gotovac.model.Types.Login

sealed trait Event {

  val id: Long = System.currentTimeMillis()
}

case class UserLoggedIn(data: GroupState) extends AnyVal with Event

case class Update(login: Login, checked: List[LocalDate], unchecked: List[LocalDate])