plugins {
  id("java")
  id("com.github.johnrengelman.shadow") version "8.0.0"
}

group = "net.forthecrown"
version = "1.6.0"

repositories {
  mavenCentral()
  maven("https://repo.papermc.io/repository/maven-public/")
  maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

  compileOnly("io.papermc.paper:paper-api:1.20.5-R0.1-SNAPSHOT")

  implementation(project(":nbt"))
  implementation(project(":paper-nbt"))
}

tasks {
  processResources {
    expand("version" to version)
  }
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(22))
}