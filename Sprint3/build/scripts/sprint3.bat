@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  sprint3 startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and SPRINT3_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\sprint3-1.0.1.jar;%APP_HOME%\lib\spring-boot-devtools-2.4.5.jar;%APP_HOME%\lib\spring-boot-starter-thymeleaf-2.4.5.jar;%APP_HOME%\lib\spring-boot-starter-websocket-2.4.5.jar;%APP_HOME%\lib\spring-boot-starter-web-2.4.5.jar;%APP_HOME%\lib\jackson-module-kotlin-2.11.4.jar;%APP_HOME%\lib\kotlin-reflect-1.4.32.jar;%APP_HOME%\lib\kolor-1.0.0.jar;%APP_HOME%\lib\kotlin-stdlib-jdk8-1.4.32.jar;%APP_HOME%\lib\spring-boot-starter-webflux-2.4.5.jar;%APP_HOME%\lib\webjars-locator-core-0.46.jar;%APP_HOME%\lib\sockjs-client-1.5.1.jar;%APP_HOME%\lib\stomp-websocket-2.3.3.jar;%APP_HOME%\lib\bootstrap-3.3.7.jar;%APP_HOME%\lib\jquery-3.1.1-1.jar;%APP_HOME%\lib\californium-proxy-2.0.0-M12.jar;%APP_HOME%\lib\guava-29.0-jre.jar;%APP_HOME%\lib\californium-core-2.0.0-M12.jar;%APP_HOME%\lib\kotlinx-coroutines-core-1.1.0.jar;%APP_HOME%\lib\kotlinx-coroutines-core-common-1.1.0.jar;%APP_HOME%\lib\json-20201115.jar;%APP_HOME%\lib\okhttp-4.9.0.jar;%APP_HOME%\lib\httpasyncclient-4.1.4.jar;%APP_HOME%\lib\httpclient-4.5.jar;%APP_HOME%\lib\commons-io-2.6.jar;%APP_HOME%\lib\aima-core-3.0.0.jar;%APP_HOME%\lib\IssActorKotlinRobotSupport-2.0.jar;%APP_HOME%\lib\IssWsHttpJavaSupport-1.0.jar;%APP_HOME%\lib\unibonoawtsupports.jar;%APP_HOME%\lib\it.unibo.planner20-1.0.jar;%APP_HOME%\lib\2p301.jar;%APP_HOME%\lib\it.unibo.qakactor-2.4.jar;%APP_HOME%\lib\uniboInterfaces.jar;%APP_HOME%\lib\org.eclipse.paho.client.mqttv3-1.2.1.jar;%APP_HOME%\lib\kotlin-stdlib-jdk7-1.4.32.jar;%APP_HOME%\lib\okio-jvm-2.8.0.jar;%APP_HOME%\lib\kotlinx-serialization-json-jvm-1.2.2.jar;%APP_HOME%\lib\kotlinx-serialization-core-jvm-1.2.2.jar;%APP_HOME%\lib\kotlin-stdlib-1.4.32.jar;%APP_HOME%\lib\kotlin-stdlib-common-1.4.32.jar;%APP_HOME%\lib\spring-boot-starter-json-2.4.5.jar;%APP_HOME%\lib\spring-boot-starter-2.4.5.jar;%APP_HOME%\lib\spring-boot-autoconfigure-2.4.5.jar;%APP_HOME%\lib\spring-boot-2.4.5.jar;%APP_HOME%\lib\thymeleaf-spring5-3.0.12.RELEASE.jar;%APP_HOME%\lib\thymeleaf-extras-java8time-3.0.4.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-tomcat-2.4.5.jar;%APP_HOME%\lib\spring-webmvc-5.3.6.jar;%APP_HOME%\lib\spring-webflux-5.3.6.jar;%APP_HOME%\lib\spring-websocket-5.3.6.jar;%APP_HOME%\lib\spring-web-5.3.6.jar;%APP_HOME%\lib\jackson-datatype-jdk8-2.11.4.jar;%APP_HOME%\lib\jackson-datatype-jsr310-2.11.4.jar;%APP_HOME%\lib\jackson-module-parameter-names-2.11.4.jar;%APP_HOME%\lib\jackson-databind-2.11.4.jar;%APP_HOME%\lib\jackson-annotations-2.11.4.jar;%APP_HOME%\lib\spring-boot-starter-reactor-netty-2.4.5.jar;%APP_HOME%\lib\spring-messaging-5.3.6.jar;%APP_HOME%\lib\element-connector-2.0.0-M12.jar;%APP_HOME%\lib\thymeleaf-3.0.12.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-logging-2.4.5.jar;%APP_HOME%\lib\logback-classic-1.2.3.jar;%APP_HOME%\lib\log4j-to-slf4j-2.13.3.jar;%APP_HOME%\lib\jul-to-slf4j-1.7.30.jar;%APP_HOME%\lib\slf4j-api-1.7.30.jar;%APP_HOME%\lib\classgraph-4.8.69.jar;%APP_HOME%\lib\jackson-core-2.11.4.jar;%APP_HOME%\lib\failureaccess-1.0.1.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\checker-qual-2.11.1.jar;%APP_HOME%\lib\error_prone_annotations-2.3.4.jar;%APP_HOME%\lib\j2objc-annotations-1.3.jar;%APP_HOME%\lib\httpcore-nio-4.4.14.jar;%APP_HOME%\lib\httpcore-4.4.14.jar;%APP_HOME%\lib\commons-codec-1.15.jar;%APP_HOME%\lib\spring-context-5.3.6.jar;%APP_HOME%\lib\spring-aop-5.3.6.jar;%APP_HOME%\lib\spring-beans-5.3.6.jar;%APP_HOME%\lib\spring-expression-5.3.6.jar;%APP_HOME%\lib\spring-core-5.3.6.jar;%APP_HOME%\lib\jakarta.annotation-api-1.3.5.jar;%APP_HOME%\lib\snakeyaml-1.27.jar;%APP_HOME%\lib\tomcat-embed-websocket-9.0.45.jar;%APP_HOME%\lib\tomcat-embed-core-9.0.45.jar;%APP_HOME%\lib\jakarta.el-3.0.3.jar;%APP_HOME%\lib\annotations-13.0.jar;%APP_HOME%\lib\reactor-netty-http-1.0.6.jar;%APP_HOME%\lib\reactor-netty-core-1.0.6.jar;%APP_HOME%\lib\reactor-core-3.4.5.jar;%APP_HOME%\lib\spring-jcl-5.3.6.jar;%APP_HOME%\lib\attoparser-2.0.5.RELEASE.jar;%APP_HOME%\lib\unbescape-1.1.6.RELEASE.jar;%APP_HOME%\lib\netty-codec-http2-4.1.63.Final.jar;%APP_HOME%\lib\netty-handler-proxy-4.1.63.Final.jar;%APP_HOME%\lib\netty-codec-http-4.1.63.Final.jar;%APP_HOME%\lib\netty-resolver-dns-native-macos-4.1.63.Final-osx-x86_64.jar;%APP_HOME%\lib\netty-resolver-dns-4.1.63.Final.jar;%APP_HOME%\lib\netty-transport-native-epoll-4.1.63.Final-linux-x86_64.jar;%APP_HOME%\lib\reactive-streams-1.0.3.jar;%APP_HOME%\lib\logback-core-1.2.3.jar;%APP_HOME%\lib\log4j-api-2.13.3.jar;%APP_HOME%\lib\netty-handler-4.1.63.Final.jar;%APP_HOME%\lib\netty-codec-dns-4.1.63.Final.jar;%APP_HOME%\lib\netty-codec-socks-4.1.63.Final.jar;%APP_HOME%\lib\netty-codec-4.1.63.Final.jar;%APP_HOME%\lib\netty-transport-native-unix-common-4.1.63.Final.jar;%APP_HOME%\lib\netty-transport-4.1.63.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.63.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.63.Final.jar;%APP_HOME%\lib\netty-common-4.1.63.Final.jar


@rem Execute sprint3
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %SPRINT3_OPTS%  -classpath "%CLASSPATH%" controller.MainCtxcarparkingKt %*

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable SPRINT3_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%SPRINT3_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
