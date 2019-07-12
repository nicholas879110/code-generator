@echo off
call %MAVEN_HOME%\bin\mvn  install:install-file -DgroupId=org.beautyeye  -DartifactId=beautyeye  -Dversion=1.0  -Dpackaging=jar  -Dfile=beautyeye-1.0.jar
call %MAVEN_HOME%\bin\mvn  install:install-file -DgroupId=org.jplus  -DartifactId=jplus  -Dversion=1.0  -Dpackaging=jar  -Dfile=J-hyberbin-1.0-SNAPSHOT.jar
echo 'install ok!'
pause