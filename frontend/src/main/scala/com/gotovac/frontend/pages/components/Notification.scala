package com.gotovac.frontend.pages.components

import org.scalajs.dom.{Node, document, window}

import scalatags.JsDom.all._

object Notification {

  def display(text: String): Unit = {
    val notificationElement = element(text)
    document.body.insertBefore(notificationElement, document.body.firstChild)
    window.setTimeout(() => document.body.removeChild(notificationElement), 1000)
  }

  private def element(content: String): Node =
    div(`class` := "message success", content, span(`class` := "close small")).render
}
