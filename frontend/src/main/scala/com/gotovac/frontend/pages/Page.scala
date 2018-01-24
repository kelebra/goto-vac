package com.gotovac.frontend.pages

import org.scalajs.dom
import org.scalajs.dom.document

trait Page {

  val elements: List[dom.Element]

  def render(): Unit = {
    document.body.innerHTML = ""
    elements.foreach(document.body.appendChild)
  }
}
