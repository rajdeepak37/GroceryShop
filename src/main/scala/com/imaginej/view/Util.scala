package com.imaginej {

package view {

import xml.{Text, Attribute, NodeSeq, Null}

import ViewTemplates.baseWithTable
import ViewTemplates.baseWithForm
import ViewTemplates.baseWithAny

// assumes specific directory layout and file naming
//

object Util {
  private def attribute(forWhat: String, that: String) =
    Attribute("what", Text("fragments-hidden/" + forWhat + that), Null)

  def baseWithTableFor(forWhat: String, size: Int): NodeSeq =
    baseWithTable(
        <lift:embed/> % attribute(forWhat, "Title"),
        <lift:embed/> % attribute(forWhat, "H2"),
        <lift:embed/> % attribute(forWhat, "TableHeader"),
        <lift:embed/> % attribute(forWhat, "TableContent"),
        <lift:embed/> % attribute("common/table/" + size, "/lineSeparator"),
        <lift:embed/> % attribute(forWhat, "TableFooter"))

  def baseWithFormFor(forWhat: String): NodeSeq =
    baseWithForm(
        <lift:embed/> % attribute(forWhat, "Title"),
        <lift:embed/> % attribute(forWhat, "H2"),
        <lift:embed/> % attribute(forWhat, "Form"))

  def baseWithAnyFor(forWhat: String): NodeSeq =
    baseWithAny(
        <lift:embed/> % attribute(forWhat, "Title"),
        <lift:embed/> % attribute(forWhat, "H2"),
        <lift:embed/> % attribute(forWhat, "Any"))
}

}

}