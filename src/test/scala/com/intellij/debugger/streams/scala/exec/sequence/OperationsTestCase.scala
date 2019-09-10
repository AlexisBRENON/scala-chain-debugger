package com.intellij.debugger.streams.scala.exec.sequence

import com.intellij.debugger.streams.lib.LibrarySupportProvider
import com.intellij.debugger.streams.scala.exec.ScalaTraceEvaluationTestCase
import com.intellij.debugger.streams.scala.lib.sequence.ScalaSequenceSupportProvider

abstract class OperationsTestCase(private val packageName: String) extends ScalaTraceEvaluationTestCase() {
  override val appName: String = "sequence"
  override val librarySupport: LibrarySupportProvider = new ScalaSequenceSupportProvider

  protected def doTestWithResult(): Unit = doTest(false, fullyQualifiedClassName())
  protected def doTestWithoutResult(): Unit = doTest(true, fullyQualifiedClassName())

  private def fullyQualifiedClassName() = s"$packageName.${getTestName(false)}"
}
