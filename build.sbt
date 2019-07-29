name := "ScalaChainDebugger"
version := "0.1"
scalaVersion := "2.13.0-M5"

ideaPluginName in ThisBuild := name.value
ideaBuild in ThisBuild := "192.5728.98"

ideaInternalPlugins += "stream-debugger"
ideaExternalPlugins += IdeaPlugin.Id("Scala", "org.intellij.scala", Some("Stable"))

lazy val ScalaChainDebugger: sbt.Project = project.in(file("."))
lazy val ideaRunner = createRunnerProject(ScalaChainDebugger, "idea-runner")
