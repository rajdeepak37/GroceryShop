package com.imaginej {

package view {

import Util.baseWithTableFor
import Util.baseWithFormFor
import Util.baseWithAnyFor

package cart {

import xml.NodeSeq

object View {
  def doList(): NodeSeq =
    baseWithTableFor("cart/list", 3)

  def doAdd(): NodeSeq =
    baseWithFormFor("cart/add")

  def doRemove(): NodeSeq =
    baseWithFormFor("cart/remove")

  def doCheckedOut(): NodeSeq =
    baseWithAnyFor("cart/checkedOut")
}

}

}

}