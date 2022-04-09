import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
}

group = "me.filippo.manfreda"

repositories {
    mavenCentral()
    flatDir {
        dirs("../../unibolibs")
    }
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testImplementation(kotlin("test"))
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:30.1-jre")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    // testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")

    implementation("org.eclipse.californium:californium-core:2.0.0-M12")
    implementation("org.eclipse.californium:californium-proxy:2.0.0-M12")
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.1")

    implementation("tuprolog:2p301")
    implementation("qak:it.unibo.qakactor:2.4")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}