import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val vaadinVersion: String by extra
val vokVersion: String by extra
val karibuDslVersion: String by extra

plugins {
    kotlin("jvm") version "1.9.22"
    id("application")
    id("com.vaadin")
}

defaultTasks("clean", "build")

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile> {
    // Vaadin 24 requires JDK 17+
    kotlinOptions.jvmTarget = "17"
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        // to see the stacktraces of failed tests in the CI console.
        exceptionFormat = TestExceptionFormat.FULL
    }
}

dependencies {
    // Vaadin-related dependencies
    implementation("com.vaadin:vaadin-core:$vaadinVersion") {
        afterEvaluate {
            if (vaadin.productionMode.get()) {
                exclude(module = "vaadin-dev")
            }
        }
    }
    implementation("eu.vaadinonkotlin:vok-framework-vokdb:$vokVersion") {
        exclude(group = "com.vaadin")
    }
    implementation("com.github.mvysny.vaadin-simple-security:vaadin-simple-security:1.0")
    implementation("com.github.mvysny.vaadin-boot:vaadin-boot:12.2")

    // logging
    // currently we are logging through the SLF4J API to SLF4J-Simple. See src/main/resources/simplelogger.properties file for the logger configuration
    implementation("org.slf4j:slf4j-simple:2.0.9")

    implementation(kotlin("stdlib-jdk8"))

    // db
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.flywaydb:flyway-core:9.22.3")
    implementation("com.h2database:h2:2.2.224")

    // test support
    testImplementation("com.github.mvysny.kaributesting:karibu-testing-v24:2.1.2")
    testImplementation("com.github.mvysny.dynatest:dynatest:0.24")
}

java {
    // Vaadin 24 requires JDK 17+
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

application {
    mainClass = "com.vaadin.securitydemo.MainKt"
}
