package com.imaginej {

package view {

import Util.baseWithTableFor
import Util.baseWithFormFor

package user {

import xml.NodeSeq

object View {
  def doList(): NodeSeq =
    baseWithTableFor("user/list", 3)

  def doAdd(): NodeSeq =
    baseWithFormFor("user/add")

  def doLogin(): NodeSeq =
    baseWithFormFor("user/login")

  def doLogout(): NodeSeq =
    baseWithFormFor("user/logout")
}

}

}

}