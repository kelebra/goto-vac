package com.gotovac.model

import com.gotovac.model.Types.{Date, Login}

case class GroupState(data: Map[Login, List[Date]])