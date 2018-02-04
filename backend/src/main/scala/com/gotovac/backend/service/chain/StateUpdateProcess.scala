package com.gotovac.backend.service.chain

import com.gotovac.backend.service.repository.{StateRepository, UserRepository}
import com.gotovac.model.StateUpdate
import prickle.Pickle.{intoString => write}
import prickle.Unpickle

class StateUpdateProcess(stateRepository: StateRepository,
                         userRepository: UserRepository) extends PartialFunction[String, String] {

  override def apply(json: String): String = {
    val stateUpdate = Unpickle[StateUpdate].fromString(json).get
    if (userRepository.hasValidSession(stateUpdate.token)) {
      stateRepository.updateState(stateUpdate)
      write(stateUpdate.anonymous)
    } else ""
  }

  override def isDefinedAt(x: String): Boolean =
    Unpickle[StateUpdate].fromString(x).isSuccess
}
