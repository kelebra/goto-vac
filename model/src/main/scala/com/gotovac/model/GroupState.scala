package com.gotovac.model

import com.gotovac.model.Types.{Date, Login}

case class GroupState(data: Map[Login, List[Date]])

object GroupState {

  import upickle.default.{macroRW, ReadWriter => RW}

  implicit val groupStateJson: RW[GroupState] = macroRW
}