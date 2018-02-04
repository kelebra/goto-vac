package com.gotovac.backend.service.chain

import com.gotovac.backend.service.repository.StateRepository
import com.gotovac.model.StateRequest
import prickle.Pickle.{intoString => write}
import prickle.Unpickle

class StateRequestProcess(stateRepository: StateRepository) extends PartialFunction[String, String] {

  override def apply(json: String): String = {
    val request = Unpickle[StateRequest].fromString(json).get
    val state = stateRepository.getState(request.token)
    write(state)
  }

  override def isDefinedAt(x: String): Boolean =
    Unpickle[StateRequest].fromString(x).isSuccess
}
