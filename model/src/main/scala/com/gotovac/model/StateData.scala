package com.gotovac.model

import com.gotovac.model.Types.{Date, Login}

sealed trait StateData

case class StateUpdate(login: Login,
                       checked: List[Date],
                       unchecked: List[Date]) extends StateData

case class StateRefresh(data: GroupState) extends StateData

case class StateRequest(token: Token)