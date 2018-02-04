package com.gotovac.backend.service.repository

import com.gotovac.model.{Credentials, Token}

trait UserRepository {

  def authorize(attempt: Credentials): Option[Token]

  def hasValidSession(token: Token): Boolean
}