plugins {
  id("java")
  id("com.github.johnrengelman.shadow") version "8.0.0"
}

group = "net.forthecrown"
version = "1.2.1"

repositories {
  mavenCentral()

  maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

dependencies {
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

  compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")

  implementation(project(":nbt"))

  // Hacky-ahh solution because I couldn't get the userdev plugin to
  // remap the shaded jars, so we have to include the already remapped jar
  val paperNbt = project(":paper-nbt").dependencyProject
  implementation(
    files(
      paperNbt.buildDir.toPath()
        .resolve("libs")
        .resolve("paper-nbt-1.2.1.jar")
        .toFile()
    )
  )
}

tasks {
  processResources {
    expand("version" to version)
  }
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}