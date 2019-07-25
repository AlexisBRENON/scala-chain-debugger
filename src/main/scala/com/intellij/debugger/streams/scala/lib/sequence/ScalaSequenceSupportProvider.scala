package com.intellij.debugger.streams.scala.lib.sequence

import java.util

import com.intellij.debugger.streams.trace.dsl.{ArrayVariable, CodeBlock, CompositeCodeBlock, Convertable, Dsl, Expression, ForLoopBody, IfBranch, Lambda, LambdaBody, ListVariable, MapVariable, StatementFactory, TryBlock, Types, Variable, VariableDeclaration}
import com.intellij.debugger.streams.lib.impl.LibrarySupportBase
import com.intellij.debugger.streams.lib.{HandlerFactory, InterpreterFactory, LibrarySupport, LibrarySupportProvider, ResolverFactory}
import com.intellij.debugger.streams.trace.TraceExpressionBuilder
import com.intellij.debugger.streams.trace.dsl.impl.{AssignmentStatement, DslImpl}
import com.intellij.debugger.streams.trace.impl.TraceExpressionBuilderBase
import com.intellij.debugger.streams.trace.impl.handler.`type`.GenericType
import com.intellij.debugger.streams.wrapper.{IntermediateStreamCall, StreamChain, StreamChainBuilder}
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

import scala.collection.JavaConverters._

/**
  * @author Alexis BRENON
  */
class ScalaSequenceSupportProvider extends LibrarySupportProvider {
  override def getLanguageId: String = "SCALA"
  override def getChainBuilder: StreamChainBuilder = ScalaSequenceSupportProvider.builder
  override def getLibrarySupport: LibrarySupport = ScalaSequenceSupportProvider.support
  override def getExpressionBuilder(project: Project): TraceExpressionBuilder = ScalaSequenceSupportProvider.expressionBuilder
}

object ScalaSequenceSupportProvider {
  val builder =new DummyStreamChainBuilder
  val support = new DummyLibrarySupport
  val dsl = new DslImpl(new DummyStatementFactory(new DummyCollectionsDummyPeekCallFactory()))
  val expressionBuilder = new DummyTraceExpressionBuilder(dsl, support.createHandlerFactory(dsl))
}

class DummyStreamChainBuilder extends StreamChainBuilder {
  override def isChainExists(startElement: PsiElement): Boolean = true

  override def build(startElement: PsiElement): util.List[StreamChain] = Seq.empty[StreamChain].asJava
}

class DummyTraceExpressionBuilder(dsl: Dsl, handlerFactory: HandlerFactory) extends
  TraceExpressionBuilderBase(dsl, handlerFactory)

class DummyLibrarySupport extends LibrarySupportBase

class DummyStatementFactory(private val peekCallFactory: DummyPeekCallFactory) extends StatementFactory {
  override def getTypes: Types = ???

  override def and(expression: Expression, expression1: Expression): Expression = ???

  override def createArrayVariable(genericType: GenericType, s: String): ArrayVariable = ???

  override def createAssignmentStatement(variable: Variable, expression: Expression): AssignmentStatement = ???

  override def createEmptyCodeBlock(): CodeBlock = ???

  override def createEmptyCompositeCodeBlock(): CompositeCodeBlock = ???

  override def createEmptyForLoopBody(variable: Variable): ForLoopBody = ???

  override def createEmptyLambdaBody(s: String): LambdaBody = ???

  override def createForEachLoop(variable: Variable, expression: Expression, forLoopBody: ForLoopBody): Convertable = ???

  override def createForLoop(variableDeclaration: VariableDeclaration, expression: Expression, expression1: Expression, forLoopBody: ForLoopBody): Convertable = ???

  override def createIfBranch(expression: Expression, codeBlock: CodeBlock): IfBranch = ???

  override def createLambda(s: String, lambdaBody: LambdaBody): Lambda = ???

  override def createListVariable(genericType: GenericType, s: String): ListVariable = ???

  override def createMapVariable(genericType: GenericType, genericType1: GenericType, s: String, b: Boolean): MapVariable = ???

  override def createNewArrayExpression(genericType: GenericType, expressions: Array[Expression]): Expression = ???

  override def createNewListExpression(genericType: GenericType, expressions: Expression*): Expression = ???

  override def createNewSizedArray(genericType: GenericType, expression: Expression): Expression = ???

  override def createPeekCall(genericType: GenericType, lambda: Lambda): IntermediateStreamCall = ???

  override def createPeekCall(genericType: GenericType, s: String): IntermediateStreamCall = ???

  override def createScope(codeBlock: CodeBlock): Convertable = ???

  override def createTimeVariableDeclaration(): VariableDeclaration = ???

  override def createTryBlock(codeBlock: CodeBlock): TryBlock = ???

  override def createVariable(genericType: GenericType, s: String): Variable = ???

  override def createVariableDeclaration(variable: Variable, expression: Expression, b: Boolean): VariableDeclaration = ???

  override def createVariableDeclaration(variable: Variable, b: Boolean): VariableDeclaration = ???

  override def currentTimeExpression(): Expression = ???

  override def equals(expression: Expression, expression1: Expression): Expression = ???

  override def not(expression: Expression): Expression = ???

  override def same(expression: Expression, expression1: Expression): Expression = ???

  override def updateCurrentTimeExpression(): Expression = ???
}

class DummyCollectionsDummyPeekCallFactory extends DummyPeekCallFactory

trait DummyPeekCallFactory {}

