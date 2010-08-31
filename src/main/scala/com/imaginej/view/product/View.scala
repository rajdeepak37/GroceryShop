package com.imaginej {

package view {

import Util.baseWithTableFor
import Util.baseWithFormFor

package product {

import xml.NodeSeq

object View {
  def doList(): NodeSeq =
    baseWithTableFor("product/list", 6)

  def doAdd(): NodeSeq =
    baseWithFormFor("product/add")

  def doEdit(): NodeSeq =
    baseWithFormFor("product/edit")

  def doTransfer(): NodeSeq =
    baseWithFormFor("product/transfer")
}

}

}

}