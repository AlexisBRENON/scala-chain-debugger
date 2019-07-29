package com.intellij.debugger.streams.scala.lib.sequence

import java.util

import com.intellij.debugger.streams.lib.impl.LibrarySupportBase
import com.intellij.debugger.streams.lib.{LibrarySupport, LibrarySupportProvider}
import com.intellij.debugger.streams.psi.PsiUtil
import com.intellij.debugger.streams.trace.TraceExpressionBuilder
import com.intellij.debugger.streams.wrapper.{StreamChain, StreamChainBuilder}
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.jetbrains.plugins.scala.lang.psi.impl.statements.ScPatternDefinitionImpl

import scala.collection.JavaConverters._

/**
  * @author Alexis BRENON
  */
class ScalaSequenceSupportProvider extends LibrarySupportProvider {
  override def getLanguageId: String = "Scala"
  override def getChainBuilder: StreamChainBuilder = ScalaSequenceSupportProvider.builder
  override def getLibrarySupport: LibrarySupport = ScalaSequenceSupportProvider.support
  override def getExpressionBuilder(project: Project): TraceExpressionBuilder = ScalaSequenceSupportProvider.expressionBuilder
}

object ScalaSequenceSupportProvider {
  val builder =new DummyStreamChainBuilder
  val support = new DummyLibrarySupport
  val expressionBuilder = new DummyTraceExpressionBuilder()
}

class DummyStreamChainBuilder extends StreamChainBuilder {
  override def isChainExists(startElement: PsiElement): Boolean = {
    val expression = getLatestExpressionInScope(PsiUtil.ignoreWhiteSpaces(startElement))
    expression.isDefined
  }

  override def build(startElement: PsiElement): util.List[StreamChain] = Seq.empty[StreamChain].asJava

  @scala.annotation.tailrec
  private def getLatestExpressionInScope(element: PsiElement) : Option[PsiElement] = element match {
    case null => None
    case e: ScPatternDefinitionImpl => Some(e)
    case _ => getLatestExpressionInScope(element.getParent)
  }
}

class DummyTraceExpressionBuilder extends TraceExpressionBuilder {
  override def createTraceExpression(chain: StreamChain): String = ""
}

class DummyLibrarySupport extends LibrarySupportBase
