package com.imaginej {

package snippet {
import xml._

object Util {
  def baseWithTableTemplate(titleNode: Node,
                            h2Node: Node,
                            tableHeaderNode: Node,
                            tableContentNode: Node, tableContentChildNode: Node,
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
        {tableContentNode match {
        case Elem(prefix, label, attribs, scope, children@_*) =>
          Elem(prefix, label, attribs, scope, children ++ tableContentChildNode: _*)
        case _ =>
          error("Can only add children to elements!")
      }}
      </lift:bind-at>
      <lift:bind-at name="tableSeparator">
        {tableSeparatorNode}
      </lift:bind-at>
      <lift:bind-at name="tableFooter">
        {tableFooterNode}
      </lift:bind-at>
    </lift:surround>

  def baseWithTableFor(what: String, size: Int, tableContentNode: Node): NodeSeq =
    baseWithTableTemplate(
        <lift:embed/> % Attribute("what", Text("fragments/" + what + "Title"), Null),
        <lift:embed/> % Attribute("what", Text("fragments/" + what + "H2"), Null),
        <lift:embed/> % Attribute("what", Text("fragments/" + what + "TableHeader"), Null),
      tableContentNode, <lift:embed/> % Attribute("what", Text("fragments/" + what + "TableContent"), Null),
      size match {
        case 1 =>
            <lift:embed/> % Attribute("what", Text("fragments/common/table/oneLineSeparator"), Null)
        case 3 =>
            <lift:embed/> % Attribute("what", Text("fragments/common/table/threeLinesSeparator"), Null)
        case 6 =>
            <lift:embed/> % Attribute("what", Text("fragments/common/table/sixLinesSeparator"), Null)
        case _ =>
          <b>
            {"separator fragment not defined"}
          </b>
      },
        <lift:embed/> % Attribute("what", Text("fragments/" + what + "TableFooter"), Null))

  def baseWithFormTemplate(titleNode: Node,
                           h2Node: Node,
                           tableNode: Node, tableChildNode: Node): NodeSeq =
    <lift:surround with="baseWithForm" xmlns:lift="http://www.w3.org/1999/xhtml">
      <lift:bind-at name="title">
        {titleNode}
      </lift:bind-at>
      <lift:bind-at name="h2">
        {h2Node}
      </lift:bind-at>
      <lift:bind-at name="form">
        {tableNode match {
        case Elem(prefix, label, attribs, scope, children@_*) =>
          Elem(prefix, label, attribs, scope, children ++ tableChildNode: _*)
        case _ =>
          error("Can only add children to elements!")
      }}
      </lift:bind-at>
    </lift:surround>

  def baseWithFormFor(what: String, tableNode: Node): NodeSeq =
    baseWithFormTemplate(
        <lift:embed/> % Attribute("what", Text("fragments/" + what + "FormTitle"), Null),
        <lift:embed/> % Attribute("what", Text("fragments/" + what + "FormH2"), Null),
      tableNode, <lift:embed/> % Attribute("what", Text("fragments/" + what + "FormTable"), Null))

  def baseWithNestedFormTemplate(titleNode: Node,
                                 h2Node: Node,
                                 nestedTableNode: Node, tableChildNode: Node): NodeSeq =
    <lift:surround with="baseWithForm" xmlns:lift="http://www.w3.org/1999/xhtml">
      <lift:bind-at name="title">
        {titleNode}
      </lift:bind-at>
      <lift:bind-at name="h2">
        {h2Node}
      </lift:bind-at>
      <lift:bind-at name="form">
        {nestedTableNode match {
        case Elem(prefix, label, attribs, scope, Elem(n_prefix, n_label, n_attribs, n_scope, n_children@_*)) =>
          Elem(prefix, label, attribs, scope, Elem(n_prefix, n_label, n_attribs, n_scope, n_children ++ tableChildNode: _*))
        case _ =>
          error("Can only add children to nested elements!")
      }}
      </lift:bind-at>
    </lift:surround>

  def baseWithNestedFormFor(what: String, nestedTableNode: Node): NodeSeq =
    baseWithNestedFormTemplate(
        <lift:embed/> % Attribute("what", Text("fragments/" + what + "FormTitle"), Null),
        <lift:embed/> % Attribute("what", Text("fragments/" + what + "FormH2"), Null),
      nestedTableNode, <lift:embed/> % Attribute("what", Text("fragments/" + what + "FormTable"), Null))

  def baseWithSnippetTemplate(titleNode: Node,
                              h2Node: Node,
                              snippetNode: Node, snippetChildNode: Node): NodeSeq =
    <lift:surround with="baseWithSnippet" xmlns:lift="http://www.w3.org/1999/xhtml">
      <lift:bind-at name="title">
        {titleNode}
      </lift:bind-at>
      <lift:bind-at name="h2">
        {h2Node}
      </lift:bind-at>
      <lift:bind-at name="snippet">
        {snippetNode match {
        case Elem(prefix, label, attribs, scope, children@_*) =>
          Elem(prefix, label, attribs, scope, children ++ snippetChildNode: _*)
        case _ =>
          error("Can only add children to elements!")
      }}
      </lift:bind-at>
    </lift:surround>

  def baseWithSnippetFor(what: String, snippetNode: Node): NodeSeq =
    baseWithSnippetTemplate(
        <lift:embed/> % Attribute("what", Text("fragments/" + what + "SnippetTitle"), Null),
        <lift:embed/> % Attribute("what", Text("fragments/" + what + "SnippetH2"), Null),
      snippetNode, <lift:embed/> % Attribute("what", Text("fragments/" + what + "SnippetChild"), Null))
}

}

}