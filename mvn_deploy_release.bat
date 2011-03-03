@echo off
mvn -DaltDeploymentRepository=release-repo::default::file:./mvn-repo/releases clean deploy
