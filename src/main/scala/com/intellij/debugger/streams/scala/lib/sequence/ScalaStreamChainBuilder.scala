package com.intellij.debugger.streams.scala.lib.sequence

import java.util

import com.intellij.debugger.streams.psi.PsiUtil
import com.intellij.debugger.streams.scala.psi.impl.{ChainBuilderVisitor, ChainDetectorVisitor}
import com.intellij.debugger.streams.wrapper.{StreamChain, StreamChainBuilder}
import com.intellij.psi.PsiElement
import org.jetbrains.plugins.scala.lang.psi.api.statements.ScPatternDefinition

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

class ScalaStreamChainBuilder extends StreamChainBuilder {

  /**
   * Is the current element part of a chain?
   * @param startElement Element on which debugger stopped
   * @return
   */
  override def isChainExists(startElement: PsiElement): Boolean = {
    getContainingExpression(Success(PsiUtil.ignoreWhiteSpaces(startElement))).flatMap { root =>
      val visitor = new ChainDetectorVisitor
      root.accept(visitor)
      if (visitor.isChain) Success(true)
      else Try(root.getParent).map(isChainExists)
    }.getOrElse(false)
  }


  override def build(startElement: PsiElement): util.List[StreamChain] = {
    val roots = getRoots(Success(PsiUtil.ignoreWhiteSpaces(startElement)), Seq.empty)
    val visitor = new ChainBuilderVisitor
    roots.foreach(_.accept(visitor))
    val chains = visitor.chains()
    Seq.empty[StreamChain].asJava
  }

  @scala.annotation.tailrec
  private def getRoots(element: Try[PsiElement], roots: Seq[PsiElement]): Seq[PsiElement] = (element, roots) match {
    case (Failure(_), s) => s
    case (Success(null), s) => s
    case (Success(e: ScPatternDefinition), s) => getRoots(Try(e.getParent), s :+ e)
    case (Success(e), s) => getRoots(Try(e.getParent), s)
  }

  @scala.annotation.tailrec
  private def getContainingExpression(element: Try[PsiElement]) : Try[PsiElement] = element match {
    case Failure(t) => Failure(t)
    case Success(null) => Failure(new RuntimeException("Not Found"))
    case Success(e: ScPatternDefinition) => Success(e)
    case Success(e) => getContainingExpression(Try(e.getParent))
  }
}


