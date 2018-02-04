package com.gotovac.model

import com.gotovac.model.Types.Login

case class GroupState(data: Map[Login, Seq[SelectedDate]] = Map.empty)