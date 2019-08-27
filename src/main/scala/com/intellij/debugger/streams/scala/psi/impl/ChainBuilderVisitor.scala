package com.intellij.debugger.streams.scala.psi.impl

import com.intellij.debugger.streams.scala.util.ScChainDetector
import org.jetbrains.plugins.scala.lang.psi.api.ScalaRecursiveElementVisitor
import org.jetbrains.plugins.scala.lang.psi.api.expr.ScMethodCall

import scala.collection.mutable

class ChainBuilderVisitor extends ScalaRecursiveElementVisitor {
  private val previousCalls = mutable.ListBuffer.empty[ScMethodCall]

  override def visitMethodCallExpression(call: ScMethodCall): Unit = {
    super.visitMethodCallExpression(call)
    val contains = previousCalls.contains(call)
    val isStream = ScChainDetector.isStreamCall(call)
    if (! contains && isStream) previousCalls += call
  }

  def chains(): Seq[ScMethodCall] = previousCalls
}
