@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@echo off
setlocal

set MAVEN_PROJECTBASEDIR=%~dp0
if not defined MAVEN_PROJECTBASEDIR set MAVEN_PROJECTBASEDIR=.
set WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar
set WRAPPER_PROPERTIES=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties

if "%JAVA_HOME%"=="" (
  set "JAVA_EXE=java.exe"
) else (
  set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
)

if exist "%WRAPPER_PROPERTIES%" (
  for /F "tokens=1,2 delims==" %%A in (%WRAPPER_PROPERTIES%) do (
    if %%A==wrapperUrl set WRAPPER_URL=%%B
  )
) else (
  echo Cannot find %WRAPPER_PROPERTIES%
  exit /B 1
)

if not exist "%WRAPPER_JAR%" (
  if exist "%WRAPPER_URL%" (
    powershell -Command "if (Get-Command curl.exe -ErrorAction SilentlyContinue) { curl.exe -fsSL %WRAPPER_URL% -o %WRAPPER_JAR% } else { (New-Object System.Net.WebClient).DownloadFile('%WRAPPER_URL%', '%WRAPPER_JAR%') }"
  )
)

"%JAVA_EXE%" %MAVEN_OPTS% -classpath "%WRAPPER_JAR%" -Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR% org.apache.maven.wrapper.MavenWrapperMain %*

endlocal
