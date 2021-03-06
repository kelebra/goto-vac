package com.gotovac.backend.service.chain

import com.gotovac.backend.service.chain.Types.{Json, JsonReply}
import com.gotovac.backend.service.repository.StateRepository
import com.gotovac.model.StateRequest
import prickle.Pickle.{intoString ⇒ write}
import prickle.Unpickle

import scala.concurrent.ExecutionContext

class StateRequestProcess(stateRepository: StateRepository)
                         (implicit ctx: ExecutionContext)
  extends PartialFunction[Json, JsonReply] {

  override def apply(json: Json): JsonReply = {
    val request = Unpickle[StateRequest].fromString(json).get
    stateRepository
      .getState(request.token)
      .map(state ⇒ write(state))
  }

  override def isDefinedAt(json: Json): Boolean =
    Unpickle[StateRequest].fromString(json).isSuccess
}
