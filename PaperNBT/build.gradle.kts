plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.5.1"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "net.forthecrown"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")

    implementation(project(":")) {
        exclude("it.unimi.dsi")
    }

    paperweight.paperDevBundle("1.19.3-R0.1-SNAPSHOT")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }

    java {
        withSourcesJar()
    }

    test {
        useJUnitPlatform()
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name();
    }

    shadow {
        dependencies {

        }
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}