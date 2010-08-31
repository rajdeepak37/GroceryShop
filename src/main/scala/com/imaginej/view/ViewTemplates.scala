package com.imaginej {

package view {

import xml.{NodeSeq, Node}

object ViewTemplates {
  def baseWithTable(titleNode: Node,
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

  def baseWithForm(titleNode: Node,
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

  def baseWithAny(titleNode: Node,
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

}

}

}