package com.intellij.debugger.streams.scala.util

import org.jetbrains.plugins.scala.lang.psi.api.expr.ScExpression

trait ScChainDetector {
//  def isStartingCall(expr: PsiMethodCallExpression): Boolean
  def isTerminationCall(expr: ScExpression): Boolean
  def isIntermediateCall(expr: ScExpression): Boolean
  def isStreamCall(expr: ScExpression): Boolean =
    isIntermediateCall(expr) || isTerminationCall(expr)
}

object ScChainDetector extends ScChainDetector {
  private val traversableChainDetector = ScTraversableChainDetector
  private val subDetectors = Set(traversableChainDetector)

  override def isIntermediateCall(expr: ScExpression): Boolean =
    subDetectors.exists(_.isIntermediateCall(expr))

  override def isTerminationCall(expr: ScExpression): Boolean =
    subDetectors.exists(_.isTerminationCall(expr))
}
