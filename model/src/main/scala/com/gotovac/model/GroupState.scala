package com.gotovac.model

import java.time.LocalDate

import com.gotovac.model.Types.Login

case class GroupState(data: Map[Login, List[LocalDate]]) extends AnyVal
