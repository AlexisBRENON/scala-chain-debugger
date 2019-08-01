package com.intellij.debugger.streams.scala.lib.sequence

import org.jetbrains.plugins.scala.lang.psi.api.ScalaRecursiveElementVisitor
import org.jetbrains.plugins.scala.lang.psi.api.expr.{ScInfixExpr, ScMethodCall, ScReferenceExpression}

class IsChainVisitor extends ScalaRecursiveElementVisitor {
  var isChain = false

  private val chainInfix = Set("to", "until")
  override def visitInfixExpression(infix: ScInfixExpr): Unit = {
    if (! isChain) infix.getEffectiveInvokedExpr match {
      case expr: ScReferenceExpression if chainInfix.contains(expr.refName) => isChain = true
      case _ => super.visitInfixExpression(infix)
    }
  }

  private val chainMethods = Set("map", "flatMap", "foreach", "collect", "zip", "zipWithIndex")
  override def visitMethodCallExpression(call: ScMethodCall): Unit = {
    if (! isChain) call.getEffectiveInvokedExpr match {
      case expr: ScReferenceExpression if chainMethods.contains(expr.refName) => isChain = true
      case _ => super.visitMethodCallExpression(call)
    }
  }
}
