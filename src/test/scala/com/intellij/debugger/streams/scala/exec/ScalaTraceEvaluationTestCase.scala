package com.intellij.debugger.streams.scala.exec

import java.nio.file.FileSystems

import com.intellij.debugger.impl.OutputChecker
import com.intellij.debugger.streams.lib.LibrarySupportProvider
import com.intellij.debugger.streams.scala.LibraryUtil
import com.intellij.debugger.streams.test.TraceExecutionTestCase
import com.intellij.execution.configurations.JavaParameters
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.Computable
import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.{JavaPsiFacade, PsiClass}
import com.intellij.testFramework.PsiTestUtil

abstract class ScalaTraceEvaluationTestCase() extends TraceExecutionTestCase {
  val appName: String
  val librarySupport: LibrarySupportProvider

  override final def getLibrarySupportProvider: LibrarySupportProvider = librarySupport

  override def setUpModule(): Unit = {
    super.setUpModule()
    ApplicationManager.getApplication.runWriteAction(new Runnable {
      override def run(): Unit = {
        VfsRootAccess.allowRootAccess(myModule, LibraryUtil.LIBRARIES_DIRECTORY)
        PsiTestUtil.addLibrary(myModule, s"${LibraryUtil.LIBRARIES_DIRECTORY}/${LibraryUtil.SCALA_STD_LIBRARY_JAR_NAME}")
      }
    })
  }

  override def createJavaParameters(mainClass: String): JavaParameters = {
    val javaParameters = super.createJavaParameters(mainClass)
    javaParameters.getClassPath.add(s"${LibraryUtil.LIBRARIES_DIRECTORY}/${LibraryUtil.SCALA_STD_LIBRARY_JAR_NAME}")
    javaParameters
  }

  override def initOutputChecker(): OutputChecker = {
    new ScalaOutputChecker(getTestAppPath, getAppOutputPath)
  }

  override def createLocalProcess(className: String) : Unit = {
    super.createLocalProcess(className + "Scala")
  }

  override def createBreakpoints(className: String) : Unit = {
    val psiClasses = ApplicationManager.getApplication.runReadAction(new Computable[Seq[PsiClass]] {
      override def compute(): Seq[PsiClass] = {
        JavaPsiFacade.getInstance(myProject)
          .findClasses(className, GlobalSearchScope.allScope(myProject))
      }
    })

    psiClasses.map(_.getContainingFile).foreach(createBreakpoints)
  }

  override def getTestAppPath: String = {
    FileSystems.getDefault.getPath("test", "resources", "exec", appName).toAbsolutePath.toString
  }
}
