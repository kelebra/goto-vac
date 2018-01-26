package com.gotovac.backend.service.chain

import com.gotovac.model.UserOnline

object UserOnlineProcess extends Producer[UserOnline] {

  override def apply(json: String): String = json
}
