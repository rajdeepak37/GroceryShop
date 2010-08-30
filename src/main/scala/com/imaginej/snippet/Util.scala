package com.imaginej {

package snippet {
import xml._

object Util {
  def baseWithTableTemplate(titleNode: Node,
                            h2Node: Node,
                            tableHeaderNode: Node,
                            tableContentNode: Node,
                            tableSeparatorNode: Node,
                            tableFooterNode: Node): NodeSeq =
    <lift:surround with="baseWithTable" xmlns:lift="http://www.w3.org/1999/xhtml">
      <lift:bind-at name="title">
        {titleNode}
      </lift:bind-at>
      <lift:bind-at name="h2">
        {h2Node}
      </lift:bind-at>
      <lift:bind-at name="tableHeader">
        {tableHeaderNode}
      </lift:bind-at>
      <lift:bind-at name="tableContent">
        {tableContentNode}
      </lift:bind-at>
      <lift:bind-at name="tableSeparator">
        {tableSeparatorNode}
      </lift:bind-at>
      <lift:bind-at name="tableFooter">
        {tableFooterNode}
      </lift:bind-at>
    </lift:surround>


  // assumes specific fragment directory layout and file naming
  //
  def baseWithTableFor(what: String, size: Int): NodeSeq =
    baseWithTableTemplate(
        <lift:embed/> % Attribute("what", Text("fragments/" + what + "Title"), Null),
        <lift:embed/> % Attribute("what", Text("fragments/" + what + "H2"), Null),
        <lift:embed/> % Attribute("what", Text("fragments/" + what + "TableHeader"), Null),
        <lift:embed/> % Attribute("what", Text("fragments/" + what + "TableContent"), Null),
        <lift:embed/> % Attribute("what", Text("fragments/common/table/" + size + "/lineSeparator"), Null),
        <lift:embed/> % Attribute("what", Text("fragments/" + what + "TableFooter"), Null))

  def baseWithFormTemplate(titleNode: Node,
                           h2Node: Node,
                           tableNode: Node): NodeSeq =
    <lift:surround with="baseWithForm" xmlns:lift="http://www.w3.org/1999/xhtml">
      <lift:bind-at name="title">
        {titleNode}
      </lift:bind-at>
      <lift:bind-at name="h2">
        {h2Node}
      </lift:bind-at>
      <lift:bind-at name="form">
        {tableNode}
      </lift:bind-at>
    </lift:surround>

  // assumes specific fragment directory layout and file naming
  //
  def baseWithFormFor(what: String): NodeSeq =
    baseWithFormTemplate(
        <lift:embed/> % Attribute("what", Text("fragments/" + what + "FormTitle"), Null),
        <lift:embed/> % Attribute("what", Text("fragments/" + what + "FormH2"), Null),
        <lift:embed/> % Attribute("what", Text("fragments/" + what + "FormTable"), Null))

  def baseWithAnyTemplate(titleNode: Node,
                          h2Node: Node,
                          anyNode: Node): NodeSeq =
    <lift:surround with="baseWithAny" xmlns:lift="http://www.w3.org/1999/xhtml">
      <lift:bind-at name="title">
        {titleNode}
      </lift:bind-at>
      <lift:bind-at name="h2">
        {h2Node}
      </lift:bind-at>
      <lift:bind-at name="any">
        {anyNode}
      </lift:bind-at>
    </lift:surround>

  // assumes specific fragment directory layout and file naming
  //  
  def baseWithAnyFor(what: String): NodeSeq =
    baseWithAnyTemplate(
        <lift:embed/> % Attribute("what", Text("fragments/" + what + "AnyTitle"), Null),
        <lift:embed/> % Attribute("what", Text("fragments/" + what + "AnyH2"), Null),
        <lift:embed/> % Attribute("what", Text("fragments/" + what + "Any"), Null))
}

}

}