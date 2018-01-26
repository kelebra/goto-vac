package com.gotovac.model

import com.gotovac.model.Types.{Date, Login}

sealed trait StateData

case class StateUpdate(login: Login,
                       checked: List[Date],
                       unchecked: List[Date]) extends StateData

object StateUpdate {

  import upickle.default.{macroRW, ReadWriter => RW}

  implicit val stateUpdateJson: RW[StateUpdate] = macroRW
}

case class StateRefresh(data: GroupState) extends StateData {

  import upickle.default.{macroRW, ReadWriter => RW}

  implicit val stateRefreshJson: RW[StateRefresh] = macroRW
}
