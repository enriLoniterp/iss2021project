import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.4.5"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.4.32"
	kotlin("plugin.spring") version "1.4.32"
	kotlin("plugin.serialization") version "1.4.32"
	java
	application
	jacoco
	distribution
}

group = "it.unibo"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_15

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	kotlinOptions {
		jvmTarget = "15"
	}
}

repositories {
	mavenCentral()
	jcenter() 	//required by andrea pivetta
	flatDir{ dirs("../unibolibs")   }   //Our libraries
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation ("org.springframework.boot:spring-boot-starter-websocket")
	implementation ("org.webjars:webjars-locator-core")
	implementation ("org.webjars:sockjs-client:1.5.1")
	implementation ("org.webjars:stomp-websocket:2.3.3")
	implementation ("org.webjars:bootstrap:3.3.7")
	implementation ("org.webjars:jquery:3.1.1-1")

	//See https://mkyong.com/spring-boot/intellij-idea-spring-boot-template-reload-is-not-working/
	/* INTELLIJ
	File –> Setting –> Build, Execution, Deployment –> Compiler –> check this Build project automatically
	SHIFT+CTRL+A registry | compiler.automake.allow.when.app.running
	If the static files are not reloaded, press CTRL+F9 to force a reload.
	 */
	testImplementation("org.springframework.boot:spring-boot-starter-test")
//KOTLIN

	// Align versions of all Kotlin components
	implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

	// Use the Kotlin JDK 8 standard library.
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	// This dependency is used by the application.
	implementation("com.google.guava:guava:29.0-jre")
	implementation("org.eclipse.californium:californium-core:2.0.0-M12")
	implementation("org.eclipse.californium:californium-proxy:2.0.0-M12")

	//COROUTINE
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.1.0")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.0")


//JSON
	// https://mvnrepository.com/artifact/org.json/json
	implementation("org.json:json:20201115" )
	//implementation("org.json:json:20210307")

	//OkHttp library for websockets with Kotlin
	implementation("com.squareup.okhttp3:okhttp:4.9.0")

//OkHttp library for websockets with Kotlin
	implementation( "com.squareup.okhttp3:okhttp:4.9.0" )

//ADDED FOR THE HTTP CLIENT
	// https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient
	implementation ("org.apache.httpcomponents:httpclient:4.5")
	// https://mvnrepository.com/artifact/commons-io/commons-io
	implementation ("commons-io:commons-io:2.6")


//PLANNER aimacode
// https://mvnrepository.com/artifact/com.googlecode.aima-java/aima-core
	implementation("com.googlecode.aima-java:aima-core:3.0.0")

//STRING COLORS
	// https://mvnrepository.com/artifact/com.andreapivetta.kolor/kolor
	implementation( "com.andreapivetta.kolor:kolor:1.0.0" )

//UNIBO

	implementation("IssActorKotlinRobotSupport:IssActorKotlinRobotSupport:2.0")
	implementation("uniboIssSupport:IssWsHttpJavaSupport:1.0")
	//implementation("uniboInterfaces:uniboInterfaces")
	implementation("uniboProtocolSupport:unibonoawtsupports")
	implementation("uniboplanner20:it.unibo.planner20:1.0")
	implementation("tuprolog:2p301")
	implementation("qak:it.unibo.qakactor:2.4")
	//implementation("uniboIssActorKotlin:IssActorKotlinRobotSupport:2.0")
	implementation("uniboInterfaces:uniboInterfaces")
	//implementation("uniboProtocolSupport:unibonoawtsupports")
	implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.1")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")



}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "15"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
application {
	// Define the main class for the application.
	mainClass.set("controller.MainCtxcarparkingKt")
}

version = "1.0.1"

tasks.jar {
	manifest {
		attributes["Main-Class"] = "controller.MainCtxcarparkingKt"
		attributes(mapOf("Implementation-Title" to project.name,
			"Implementation-Version" to project.version))
	}
}