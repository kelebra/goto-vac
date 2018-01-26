package com.gotovac.backend.service.repository

import com.gotovac.model.GroupState
import com.gotovac.model.Types.Login

sealed trait StateRepository {

  def getState(login: Login): GroupState
}

object StateRepository extends StateRepository {

  override def getState(login: Login) = ???
}