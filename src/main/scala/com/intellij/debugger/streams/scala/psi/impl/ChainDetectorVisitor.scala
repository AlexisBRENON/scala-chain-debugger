package com.intellij.debugger.streams.scala.psi.impl

import com.intellij.debugger.streams.scala.util.ScChainDetector
import org.jetbrains.plugins.scala.lang.psi.api.ScalaRecursiveElementVisitor
import org.jetbrains.plugins.scala.lang.psi.api.expr.{ScInfixExpr, ScMethodCall, ScReferenceExpression}

class ChainDetectorVisitor extends ScalaRecursiveElementVisitor {
  var isChain = false

  override def visitMethodCallExpression(call: ScMethodCall): Unit = {
    super.visitMethodCallExpression(call)
    isChain = isChain || ScChainDetector.isStreamCall(call)
  }
}
