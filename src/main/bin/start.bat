@echo off
rem -----------------------------
rem jar启动脚本
rem -----------------------------
rem CURRENT_DIR is settting

echo %JAVA_HOME%

if not "%JAVA_HOME%" == "" goto javaend
echo java_home is not set!
pause
:javaend

set JAVA_EXE=%JAVA_HOME%/bin/java
echo %JAVA_EXE%
java -version >nul 2>&1
if errorlevel 1 echo java not installed! goto end
java -jar code-generator.jar
:end
pause