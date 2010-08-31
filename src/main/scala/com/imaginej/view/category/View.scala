package com.imaginej {

package view {

import Util.baseWithTableFor
import Util.baseWithFormFor

package category {

import xml.NodeSeq

object View {
  def doList(): NodeSeq =
    baseWithTableFor("category/list", 1)

  def doAdd(): NodeSeq =
    baseWithFormFor("category/add")
}

}

}

}