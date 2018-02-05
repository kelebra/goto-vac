package com.gotovac.backend.service.repository

import com.gotovac.model.{Credentials, Token}

import scala.concurrent.Future

trait UserRepository {

  def authorize(attempt: Credentials): Future[Option[Token]]

  def hasValidSession(token: Token): Future[Boolean]
}