package com.intellij.debugger.streams.scala.lib.sequence

import com.intellij.debugger.streams.lib.impl.LibrarySupportBase
import com.intellij.debugger.streams.lib.{LibrarySupport, LibrarySupportProvider}
import com.intellij.debugger.streams.trace.TraceExpressionBuilder
import com.intellij.debugger.streams.wrapper.{StreamChain, StreamChainBuilder}
import com.intellij.openapi.project.Project
import org.jetbrains.plugins.scala.ScalaLanguage

/**
  * @author Alexis BRENON
  */
class ScalaSequenceSupportProvider extends LibrarySupportProvider {
  override def getLanguageId: String = ScalaLanguage.INSTANCE.getID
  override def getChainBuilder: StreamChainBuilder = ScalaSequenceSupportProvider.builder
  override def getLibrarySupport: LibrarySupport = ScalaSequenceSupportProvider.support
  override def getExpressionBuilder(project: Project): TraceExpressionBuilder = ScalaSequenceSupportProvider.expressionBuilder
}

object ScalaSequenceSupportProvider {
  val builder =new ScalaStreamChainBuilder
  val support = new DummyLibrarySupport
  val expressionBuilder = new DummyTraceExpressionBuilder()
}

class DummyTraceExpressionBuilder extends TraceExpressionBuilder {
  override def createTraceExpression(chain: StreamChain): String = ""
}

class DummyLibrarySupport extends LibrarySupportBase
