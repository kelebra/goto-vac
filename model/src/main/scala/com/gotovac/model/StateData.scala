package com.gotovac.model

import com.gotovac.model.Types.{Date, Login}
import upickle.default.{macroRW, ReadWriter => RW}

sealed trait StateData

case class StateUpdate(login: Login,
                       checked: List[Date],
                       unchecked: List[Date]) extends StateData

object StateUpdate {
  implicit val stateUpdateJson: RW[StateUpdate] = macroRW
}

case class StateRefresh(data: GroupState) extends StateData

object StateRefresh {
  implicit val stateRefreshJson: RW[StateRefresh] = macroRW
}

case class StateRequest(token: Token)

object StateRequest {
  implicit val stateRefreshJson: RW[StateRequest] = macroRW
}