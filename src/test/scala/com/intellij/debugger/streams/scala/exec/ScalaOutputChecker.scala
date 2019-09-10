package com.intellij.debugger.streams.scala.exec

import java.io.File
import java.util.regex.Pattern

import com.intellij.debugger.impl.OutputChecker
import com.intellij.idea.IdeaLogger
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.{FileUtil, FileUtilRt}
import com.intellij.openapi.util.text.StringUtilRt
import com.intellij.openapi.vfs.CharsetToolkit
import org.junit.Assert

import scala.collection.JavaConverters._


class ScalaOutputChecker(appPath: String, outputPath: String) extends OutputChecker(appPath, outputPath) {
  protected val myAppPath: String = appPath
  protected val myOutputPath: String = outputPath
  protected var myTestName: String = _


  override def init(testName: String): Unit = {
    super.init(testName)
    myTestName = Character.toLowerCase(testName.charAt(0)) + testName.substring(1)
  }

  // Copied from the base OutputChecker.checkValid(). Need to intercept call to base preprocessBuffer() method
  override def checkValid(jdk: Sdk, sortClassPath: Boolean) {
    if (IdeaLogger.ourErrorsOccurred != null) {
      throw IdeaLogger.ourErrorsOccurred
    }

    val actual = preprocessBuffer(buildOutputString())

    val outDir = new File(myAppPath + File.separator + "outs")
    val defaultOutFile = new File(outDir, myTestName + ".out")
    val outFile =
      if (defaultOutFile.exists()) defaultOutFile
      else {
        if (SystemInfo.isWindows) {
          val winOut = new File(outDir, myTestName + ".win.out")
          if (winOut.exists()) winOut
          else defaultOutFile
        } else if (SystemInfo.isUnix) {
          val unixOut = new File(outDir, myTestName + ".unx.out")
          if (unixOut.exists()) unixOut
          else defaultOutFile
        } else defaultOutFile
      }

    if (!outFile.exists()) {
      FileUtil.writeToFile(outFile, actual)
      ScalaOutputChecker.LOG.error("Test file created ${outFile.path}\n**************** Don't forget to put it into VCS! *******************")
    } else {
      val originalText = FileUtilRt.loadFile(outFile, CharsetToolkit.UTF8)
      val expected = StringUtilRt.convertLineSeparators(originalText)
      if (expected != actual) {
        System.out.println("expected:")
        System.out.println(originalText)
        System.out.println("actual:")
        System.out.println(actual)

        val len = Math.min(expected.length, actual.length)
        if (expected.length != actual.length) {
          System.out.println("Text sizes differ: expected " + expected.length + " but actual: " + actual.length)
        }
        if (expected.length > len) {
          System.out.println("Rest from expected text is: \"" + expected.substring(len) + "\"")
        } else if (actual.length > len) {
          System.out.println("Rest from actual text is: \"" + actual.substring(len) + "\"")
        }

        Assert.assertEquals(originalText, actual)
      }
    }
  }

  private def preprocessBuffer(buffer: String): String = {
    val lines = buffer.lines().iterator().asScala
    val connectedIndex = lines.indexWhere(_.startsWith(ScalaOutputChecker.CONNECT_PREFIX))
    val runCommandIndex = connectedIndex - 1
    val disconnectedIndex = lines.indexWhere(_.startsWith(ScalaOutputChecker.DISCONNECT_PREFIX))

    lines.zipWithIndex.map {
      case (_, i) if i == connectedIndex => ScalaOutputChecker.CONNECT_PREFIX
      case (_, i) if i == runCommandIndex => ScalaOutputChecker.RUN_JAVA
      case (_, i) if i == disconnectedIndex => ScalaOutputChecker.DISCONNECT_PREFIX
      case (l, _) => l
    }.filterNot { l =>
      l.matches(ScalaOutputChecker.JDI_BUG_OUTPUT_PATTERN_1.pattern()) ||
        l.matches(ScalaOutputChecker.JDI_BUG_OUTPUT_PATTERN_2.pattern())
    }.mkString("\n")
  }

  private def buildOutputString(): String = {
    // Call base method with reflection
    val m = classOf[OutputChecker].getDeclaredMethod("buildOutputString")
    val isAccessible = m.isAccessible

    try {
      m.setAccessible(true)
      m.invoke(this).asInstanceOf[String]
    } finally {
      m.setAccessible(isAccessible)
    }
  }
}

object ScalaOutputChecker {
  private val LOG = Logger.getInstance(ScalaOutputChecker.getClass)

  private val CONNECT_PREFIX = "Connected to the target VM"
  private val DISCONNECT_PREFIX = "Disconnected from the target VM"
  private val RUN_JAVA = "Run Java"

  private val JDI_BUG_OUTPUT_PATTERN_1 = Pattern.compile("ERROR:\\s+JDWP\\s+Unable\\s+to\\s+get\\s+JNI\\s+1\\.2\\s+environment,\\s+jvm->GetEnv\\(\\)\\s+return\\s+code\\s+=\\s+-2\n")
  private val JDI_BUG_OUTPUT_PATTERN_2 = Pattern.compile("JDWP\\s+exit\\s+error\\s+AGENT_ERROR_NO_JNI_ENV.*]\n")
}
