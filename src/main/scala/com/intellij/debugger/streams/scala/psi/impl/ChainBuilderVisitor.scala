package com.intellij.debugger.streams.scala.psi.impl

import com.intellij.debugger.streams.scala.psi.{ScalaPsiUtil => PsiUtil}
import com.intellij.debugger.streams.scala.util.ScChainDetector
import org.jetbrains.plugins.scala.lang.psi.api.ScalaRecursiveElementVisitor
import org.jetbrains.plugins.scala.lang.psi.api.expr.{MethodInvocation, ScMethodCall}

import scala.collection.mutable

class ChainBuilderVisitor extends ScalaRecursiveElementVisitor {
  private val visitedCalls = mutable.ListBuffer.empty[MethodInvocation]
  private val previousCalls = mutable.HashMap.empty[MethodInvocation, MethodInvocation]

  override def visitMethodCallExpression(call: ScMethodCall): Unit = {
    super.visitMethodCallExpression(call)
    if (ScChainDetector.isIntermediateCall(call)) {
      println(PsiUtil.debug(call))
      visitedCalls += call
      val previous = PsiUtil.getPreviousCall(call)
      previous.foreach { p => println(s"Previous call: ${PsiUtil.debug(p)}")}
      previous.filter(ScChainDetector.isIntermediateCall).foreach { p =>
        println(s"Adding previous call")
        previousCalls(call) = p
      }
    }
  }

  def chains(): Seq[Seq[MethodInvocation]] = {
    val noLastCall = previousCalls.keySet
    visitedCalls.filter(! noLastCall.contains(_)).map(buildPsiChain)
  }

  private def buildPsiChain(invocation: MethodInvocation): Seq[MethodInvocation] = {
    @scala.annotation.tailrec
    def _buildPsiChain(acc: Seq[MethodInvocation], item: Option[MethodInvocation]): Seq[MethodInvocation] = item match {
      case None => acc
      case Some(invocation) => _buildPsiChain(invocation +: acc, previousCalls.get(invocation))
    }

    _buildPsiChain(Seq.empty, Some(invocation)).reverse
  }
}
