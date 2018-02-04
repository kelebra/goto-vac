package com.gotovac.backend.service.repository

import com.gotovac.model.{GroupState, StateUpdate, Token}

trait StateRepository {

  def getState(token: Token): GroupState

  def updateState(stateUpdate: StateUpdate): Unit
}