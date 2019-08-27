package com.intellij.debugger.streams.scala.util

import com.intellij.psi.PsiMethodCallExpression
import org.jetbrains.plugins.scala.lang.psi.api.expr.ScMethodCall

trait ScChainDetector {
//  def isStartingCall(expr: PsiMethodCallExpression): Boolean
  def isTerminationCall(call: ScMethodCall): Boolean
  def isIntermediateCall(call: ScMethodCall): Boolean
  def isStreamCall(call: ScMethodCall): Boolean =
    isIntermediateCall(call) || isTerminationCall(call)
}

object ScChainDetector extends ScChainDetector {
  private val traversableChainDetector = ScTraversableChainDetector
  private val subDetectors = Set(traversableChainDetector)

  override def isIntermediateCall(call: ScMethodCall): Boolean =
    subDetectors.exists(_.isIntermediateCall(call))

  override def isTerminationCall(call: ScMethodCall): Boolean =
    subDetectors.exists(_.isTerminationCall(call))
}
