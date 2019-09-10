package com.intellij.debugger.streams.scala.psi

import org.jetbrains.plugins.scala.lang.psi.api.expr.{MethodInvocation, ScReferenceExpression}

object ScalaPsiUtil {
  def getPreviousCall(expr: MethodInvocation): Option[MethodInvocation] = expr.getParent match {
    case invocation: MethodInvocation => Some(invocation)
    case _ => None
  }

  def debug(invocation: MethodInvocation): String = {
    s"""
       |${Thread.currentThread().getStackTrace.slice(2, 3).mkString("\n")}
       |${
      invocation.getEffectiveInvokedExpr match {
        case expr: ScReferenceExpression => s"Ref name: ${expr.refName}"
        case _ => ""
      }
    }
       |----------""".stripMargin
  }
}
