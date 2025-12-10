import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.0.21"
    application
}

group = "me.wverl"
version = "1.0-SNAPSHOT"

val dyldLibraryPath = "libs/z3"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testImplementation(kotlin("test"))
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("com.google.guava:guava:31.0.1-jre")
    implementation(files("libs/com.microsoft.z3.jar"))
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
    environment("DYLD_LIBRARY_PATH", dyldLibraryPath)
    // Linux: use system-installed z3 JNI library
    environment("LD_LIBRARY_PATH", "/usr/lib/x86_64-linux-gnu/jni")
    systemProperty("java.library.path", "/usr/lib/x86_64-linux-gnu/jni:$dyldLibraryPath")
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

application {
    mainClass.set("MainKt")
    applicationDefaultJvmArgs = listOf("-Djava.library.path=/usr/lib/x86_64-linux-gnu/jni:$dyldLibraryPath")
}
