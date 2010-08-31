package com.imaginej {

package view {

import xml.{Text, Attribute, NodeSeq, Null}

import ViewTemplates.baseWithTable
import ViewTemplates.baseWithForm
import ViewTemplates.baseWithAny

// assumes specific directory layout and file naming
//

object Util {
  private def attribute(what: String, that: String) =
    Attribute("what", Text("fragments-hidden/" + what + that), Null)

  def baseWithTableFor(what: String, size: Int): NodeSeq =
    baseWithTable(
        <lift:embed/> % attribute(what, "Title"),
        <lift:embed/> % attribute(what, "H2"),
        <lift:embed/> % attribute(what, "TableHeader"),
        <lift:embed/> % attribute(what, "TableContent"),
        <lift:embed/> % attribute("common/table/" + size, "/lineSeparator"),
        <lift:embed/> % attribute(what, "TableFooter"))

  def baseWithFormFor(what: String): NodeSeq =
    baseWithForm(
        <lift:embed/> % attribute(what, "Title"),
        <lift:embed/> % attribute(what, "H2"),
        <lift:embed/> % attribute(what, "Form"))

  def baseWithAnyFor(what: String): NodeSeq =
    baseWithAny(
        <lift:embed/> % attribute(what, "Title"),
        <lift:embed/> % attribute(what, "H2"),
        <lift:embed/> % attribute(what, "Any"))
}

}

}