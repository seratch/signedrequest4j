@echo off
mvn clean -DaltDeploymentRepository=snapshot-repo::default::file:"%HOMEPATH%/github/seratch.github.com/mvn-repo/snapshots" clean deploy
