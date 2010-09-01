package com.imaginej {

package snippet {

import xml.{Attribute, Text, NodeSeq}

import net.liftweb.http.{SessionVar, RequestVar, S, SHtml}
import net.liftweb.util.Helpers

import Helpers._
import S._
import SHtml._

import domain.grocery._

import I10N.i10n

object categoryRequestVar extends RequestVar(new CategoryEntity)

object categorySessionVar extends SessionVar(new CategoryEntity)

class CategorySnippet {
  def requestCategory = categoryRequestVar.is

  def sessionCategory = categorySessionVar.is

  def list(xhtml: NodeSeq): NodeSeq = for{
    category <- CategoryStore.retrieveAll
    productListXhtml =
    link("/product/list", () => categorySessionVar(category), Text(i10n("Category_ProductList", category.name)))
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
        error(?("Category_Error_CategoryNameCannotBeBlank"))
      } else {
        CategoryStore.retrieveByName(requestCategory.name) match {
          case None => CategoryStore.create(requestCategory.name)
          case _ => error(i10n("Category_Error_CategoryAlreadyExists", requestCategory.name))
        }
        redirectTo("list")
      }
    }

    val nameXhtml = text("", doName(_))
    val submitXhtml = submit(?("Category_Add"), doSubmit)

    bind(
      "category", xhtml,
      "name" -> nameXhtml,
      "submit" -> submitXhtml)
  }

}

}

}