package com.gotovac.backend.service.chain

import com.gotovac.backend.service.repository.StateRepository
import com.gotovac.model.StateUpdate
import prickle.Unpickle

class StateUpdateProcess(stateRepository: StateRepository) extends PartialFunction[String, String] {

  override def apply(json: String): String = {
    val stateUpdate = Unpickle[StateUpdate].fromString(json).get
    stateRepository.updateState(stateUpdate)
    json
  }

  override def isDefinedAt(x: String): Boolean =
    Unpickle[StateUpdate].fromString(x).isSuccess
}
