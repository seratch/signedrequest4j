name := "scalikesolr"

organization := "com.github.seratch"

scalaVersion := "2.9.1"

crossScalaVersions := Seq("2.9.1", "2.9.0-1", "2.9.0", "2.8.1")

libraryDependencies <++= (scalaVersion) { scalaVersion =>
  val scalatestVersion = scalaVersion match {
    case "2.8.1" => "1.5.1"
    case _       => "1.6.1"
  }
  val specsArtifactId = scalaVersion match {
    case "2.9.1" => "specs_2.9.0"
    case _       => "specs_" + scalaVersion
  }
  Seq(
    "junit"                    % "junit"              % "4.9"           % "test",
    "org.mockito"              % "mockito-all"        % "1.8.2"           % "test",
    "org.scalatest"            %% "scalatest"         % scalatestVersion  % "test",
    "org.scala-tools.testing"  % specsArtifactId      % "1.6.8"           % "test"
  )
}

scalacOptions ++= Seq("-deprecation", "-unchecked")

defaultExcludes ~= (_ || "*~")
