package com.gotovac.model

sealed trait StateData

case class StateUpdate(token: Token,
                       date: SelectedDate,
                       selected: Boolean) extends StateData

case class StateRequest(token: Token) extends StateData