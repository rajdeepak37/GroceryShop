package com.imaginej {

package view {

import xml.{Text, Attribute, NodeSeq, Null}

import ViewTemplates.baseWithTable
import ViewTemplates.baseWithForm
import ViewTemplates.baseWithAny

// assumes specific directory layout and file naming
//

object Util {
  private def attribute(prefix: String, that: String, suffix: String) =
    Attribute("what", Text(prefix + that + suffix), Null)

  def baseWithTableFor(what: String, size: Int): NodeSeq =
    baseWithTable(
        <lift:embed/> % attribute("fragments-hidden/", what, "Title"),
        <lift:embed/> % attribute("fragments-hidden/", what, "H2"),
        <lift:embed/> % attribute("fragments-hidden/", what, "TableHeader"),
        <lift:embed/> % attribute("fragments-hidden/", what, "TableContent"),
        <lift:embed/> % attribute("fragments-hidden/common/table/", size.toString, "/lineSeparator"),
        <lift:embed/> % attribute("fragments-hidden/", what, "TableFooter"))

  def baseWithFormFor(what: String): NodeSeq =
    baseWithForm(
        <lift:embed/> % attribute("fragments-hidden/", what, "FormTitle"),
        <lift:embed/> % attribute("fragments-hidden/", what, "FormH2"),
        <lift:embed/> % attribute("fragments-hidden/", what, "FormTable"))

  def baseWithAnyFor(what: String): NodeSeq =
    baseWithAny(
        <lift:embed/> % attribute("fragments-hidden/", what, "AnyTitle"),
        <lift:embed/> % attribute("fragments-hidden/", what, "AnyH2"),
        <lift:embed/> % attribute("fragments-hidden/", what, "Any"))
}

}

}