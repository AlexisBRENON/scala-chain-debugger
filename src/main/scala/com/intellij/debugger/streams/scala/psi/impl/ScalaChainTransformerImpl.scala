package com.intellij.debugger.streams.scala.psi.impl

import java.util

import com.intellij.debugger.streams.psi.ChainTransformer
import com.intellij.debugger.streams.scala.trace.dsl.ScalaTypes
import com.intellij.debugger.streams.trace.impl.handler.`type`.GenericType
import com.intellij.debugger.streams.wrapper.impl._
import com.intellij.debugger.streams.wrapper.{CallArgument, IntermediateStreamCall, QualifierExpression, StreamChain}
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.jetbrains.plugins.scala.conversion.ast.QualifiedExpression
import org.jetbrains.plugins.scala.lang.psi.api.expr.{MethodInvocation, ScExpression, ScReferenceExpression}

import scala.collection.JavaConverters._
import scala.collection.mutable

class ScalaChainTransformerImpl extends ChainTransformer[MethodInvocation] {
  private val typeExtractor = new DummyTypeExtractor

  override def transform(_callChain: util.List[MethodInvocation], context: PsiElement): StreamChain = {
    val callChain: Seq[MethodInvocation] = _callChain.asScala
    val intermediateCalls = mutable.ListBuffer.empty[IntermediateStreamCall]
    callChain.dropRight(1).foreach { call =>
      val (typeBefore, typeAfter) = typeExtractor.extractIntermediateCallTypes(call)

      intermediateCalls += new IntermediateStreamCallImpl(
        call.getEffectiveInvokedExpr.asInstanceOf[ScReferenceExpression].refName,
        call.argumentExpressions.map { createCallArgument(call) }.asJava,
        typeBefore, typeAfter,
        call.getEffectiveInvokedExpr.getTextRange)
    }

    val terminationsPsiCall = callChain.last
    val (typeBeforeTerminator, resultType) = typeExtractor.extractIntermediateCallTypes(terminationsPsiCall)
    val terminationCall = new TerminatorStreamCallImpl(
      terminationsPsiCall.getEffectiveInvokedExpr.asInstanceOf[ScReferenceExpression].refName,
      terminationsPsiCall.argumentExpressions.map { createCallArgument(terminationsPsiCall) }.asJava,
      typeBeforeTerminator, resultType,
      terminationsPsiCall.getEffectiveInvokedExpr.getTextRange)

    val typeAfterQualifier =
      if (intermediateCalls.isEmpty) typeBeforeTerminator else intermediateCalls.head.getTypeBefore

    val qualifier = createQualifier(callChain.head, typeAfterQualifier)

    new StreamChainImpl(qualifier, intermediateCalls.asJava, terminationCall, context)
  }

  private def createCallArgument(callExpression: MethodInvocation)(arg: ScExpression): CallArgument = {
    new CallArgumentImpl("Dummy", "Dummy")
  }

  private def createQualifier(expression: PsiElement, typeAfter: GenericType): QualifierExpression = {
    expression.getParent match {
      case q: QualifiedExpression => new QualifierExpressionImpl(
        q.getText, q.getTextRange, typeAfter
      )
      case _ => new QualifierExpressionImpl("", TextRange.EMPTY_RANGE, typeAfter)
    }
  }
}

class DummyTypeExtractor {
  def extractIntermediateCallTypes(call: MethodInvocation): (GenericType, GenericType) =
    (ScalaTypes.list(ScalaTypes.getANY), ScalaTypes.list(ScalaTypes.getANY))
}
