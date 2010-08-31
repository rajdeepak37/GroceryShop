package com.imaginej {

package view {

import xml.{NodeSeq, Node}

object ViewTemplates {
  def baseWithTable(title: Node,
                    h2: Node,
                    tableHeader: Node,
                    tableContent: Node,
                    tableSeparator: Node,
                    tableFooter: Node): NodeSeq =
    <lift:surround with="baseWithTable" xmlns:lift="http://www.w3.org/1999/xhtml">
      <lift:bind-at name="title">
        {title}
      </lift:bind-at>
      <lift:bind-at name="h2">
        {h2}
      </lift:bind-at>
      <lift:bind-at name="tableHeader">
        {tableHeader}
      </lift:bind-at>
      <lift:bind-at name="tableContent">
        {tableContent}
      </lift:bind-at>
      <lift:bind-at name="tableSeparator">
        {tableSeparator}
      </lift:bind-at>
      <lift:bind-at name="tableFooter">
        {tableFooter}
      </lift:bind-at>
    </lift:surround>

  def baseWithForm(title: Node,
                   h2: Node,
                   table: Node): NodeSeq =
    <lift:surround with="baseWithForm" xmlns:lift="http://www.w3.org/1999/xhtml">
      <lift:bind-at name="title">
        {title}
      </lift:bind-at>
      <lift:bind-at name="h2">
        {h2}
      </lift:bind-at>
      <lift:bind-at name="form">
        {table}
      </lift:bind-at>
    </lift:surround>

  def baseWithAny(title: Node,
                  h2: Node,
                  any: Node): NodeSeq =
    <lift:surround with="baseWithAny" xmlns:lift="http://www.w3.org/1999/xhtml">
      <lift:bind-at name="title">
        {title}
      </lift:bind-at>
      <lift:bind-at name="h2">
        {h2}
      </lift:bind-at>
      <lift:bind-at name="any">
        {any}
      </lift:bind-at>
    </lift:surround>

}

}

}