package com.intellij.debugger.streams.scala.util

import org.jetbrains.plugins.scala.lang.psi.api.expr.{ScExpression, MethodInvocation, ScReferenceExpression}

object ScTraversableChainDetector extends ScChainDetector {
  val methods: Map[String, Set[String]] = Map(
    "addition" -> Set("++"),
    "map" -> Set("map", "flatMap", "collect"),
    "conversion" -> Set("toArray", "toList", "toIterable", "toSeq", "toIndexedSeq", "toStream", "toSet", "toMap"),
    "copy" -> Set("copyToBuffer", "copyToArray"),
    "size" -> Set("isEmpty", "nonEmpty", "size", "hasDefiniteSize"),
    "elementRetrieval" -> Set("head", "last", "headOption", "lastOption", "find"),
    "subCollectionRetrieval" -> Set("tail", "init", "slice", "take", "drop", "takeWhile", "dropWhile", "filter", "filterNot", "withFilter"),
    "subdivision" -> Set("splitAt", "span", "partition", "groupBy"),
    "test" -> Set("exists", "forall", "count"),
    "fold" -> Set("foldLeft", "foldRight", "/:", ":\\", "reduceLeft", "reduceRight", "sum", "product", "min", "max"),
    "string" -> Set("mkString", "addString", "stringPrefix"),
    "view" -> Set("view"),
  )

  val intermediateMethods: Seq[String] = Seq("addition", "map", "conversion", "copy", "subCollectionRetrieval", "subdivision").flatMap(methods)
  val terminationMethods: Seq[String] = Seq("size", "elementRetrieval", "test", "fold", "string", "view").flatMap(methods)

  protected def checkMethodName(call: MethodInvocation, matchingNames: Seq[String]): Boolean =
    call.getEffectiveInvokedExpr match {
      case expr: ScReferenceExpression => matchingNames.contains(expr.refName)
      case _ => false
    }

  override def isIntermediateCall(expr: ScExpression): Boolean = expr match {
    case call: MethodInvocation => checkMethodName(call, intermediateMethods)
    case _ => false
  }
  override def isTerminationCall(expr: ScExpression): Boolean = expr match {
    case call: MethodInvocation => checkMethodName(call, terminationMethods)
    case _ => false
  }
}
