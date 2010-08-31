package com.imaginej {

package snippet {

import xml.{Attribute, Text, NodeSeq}

import net.liftweb.http.{SessionVar, RequestVar, S, SHtml}
import net.liftweb.util.Helpers

import Helpers._
import S._
import SHtml._

import domain.grocery._

object categoryRequestVar extends RequestVar(new CategoryEntity)

object categorySessionVar extends SessionVar(new CategoryEntity)

class CategorySnippet {
  def requestCategory = categoryRequestVar.is

  def sessionCategory = categorySessionVar.is

  def list(xhtml: NodeSeq): NodeSeq = for{
    category <- CategoryStore.retrieveAll
    productListXhtml = link("/product/list", () => categorySessionVar(category), Text(category.name + " Product List"))
    node <- bind(
      "category", xhtml,
      "productList" -> productListXhtml)
  } yield node

  def add(xhtml: NodeSeq): NodeSeq = {

    def doName(categoryName: String) = {
      requestCategory.name = categoryName
    }

    def doSubmit() = {
      if (requestCategory.name.length == 0) {
        error("Category name cannot be blank")
      } else {
        CategoryStore.retrieveByName(requestCategory.name) match {
          case None => CategoryStore.create(requestCategory.name)
          case _ => error("Category " + requestCategory.name + " already exists")
        }
        redirectTo("list")
      }
    }

    val nameXhtml = text("", doName(_))
    val submitXhtml = submit("Add", doSubmit)

    bind(
      "category", xhtml,
      "name" -> nameXhtml,
      "submit" -> submitXhtml)
  }

  def productListText(xhtml: NodeSeq): NodeSeq = {

    val listXhtml = Text(sessionCategory.name + " Product List")

    bind(
      "category", xhtml,
      "list" -> listXhtml)
  }

  def addProductText(xhtml: NodeSeq): NodeSeq = {

    val addXhtml = Text("Add " + sessionCategory.name + " Product")

    bind(
      "category", xhtml,
      "add" -> addXhtml)
  }

}

}

}