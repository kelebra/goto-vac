package com.gotovac.model

case class StateUpdate(token: Token, date: SelectedDate, selected: Boolean) {

  def anonymous: StateUpdate = copy(token = token.anonymous)
}
