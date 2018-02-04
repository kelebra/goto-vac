package com.gotovac.frontend.pages.components

import com.gotovac.model.Types
import com.gotovac.model.Types.Login
import org.scalajs.dom.document

import scala.scalajs.js.Date

object CalendarSelection {

  private val delimiter = "-"
  private val selectedClass = "success"

  def isSelected(identifier: String): Boolean =
    document.getElementById(identifier).classList.contains(selectedClass)

  def cellId(login: Login, date: Date): String =
    s"$login$delimiter${date.getTime().toLong}"

  def cellId(login: Login, date: Types.Date): String =
    s"$login$delimiter$date"

  def cellUTCDate(identifier: String): Types.Date =
    identifier.split(delimiter)(1).toLong

  def select(identifier: String): Unit =
    if (!isSelected(identifier))
      document.getElementById(identifier).classList.add(selectedClass)

  def unSelect(identifier: String): Unit =
    document.getElementById(identifier).classList.remove(selectedClass)
}
