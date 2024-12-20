import io.papermc.paperweight.userdev.ReobfArtifactConfiguration

plugins {
  id("java")
  id("io.papermc.paperweight.userdev") version "1.7.1"

  id("maven-publish")
  id("signing")
}

group = "net.forthecrown"
version = "1.8.0"

repositories {
  mavenCentral()
  maven("https://repo.papermc.io/repository/maven-public/")
  maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

  implementation(project(":nbt"))

  paperweight.paperDevBundle("1.21.3-R0.1-SNAPSHOT")
}

tasks {
  compileJava {
    options.release = 21
  }

  java {
    withSourcesJar()
    withJavadocJar()
  }

  test {
    useJUnitPlatform()
  }

  javadoc {
    options.encoding = Charsets.UTF_8.name()
  }
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

paperweight.reobfArtifactConfiguration = ReobfArtifactConfiguration.MOJANG_PRODUCTION

publishing {
  publications {
    create<MavenPublication>("maven") {
      from(components["java"])

      pom {
        name.set("paper-nbt")
        description.set("Java NamedBinaryTag library for PaperMC")
        url.set("https://github.com/ArcadiusMC/NBT")

        licenses {
          license {
            name.set("MIT License")
            url.set("https://raw.githubusercontent.com/ArcadiusMC/NBT/main/LICENSE.md")
          }
        }

        developers {
          developer {
            name.set("JulieWoolie")
            id.set("JulieWoolie")
          }
        }

        scm {
          connection.set("scm:git:git:github.com/ArcadiusMC/NBT/.git")
          developerConnection.set("scm:git:ssh://github.com/ArcadiusMC/NBT/.git")
          url.set("https://github.com/ForTheCrown/NBT")
        }
      }
    }
  }

  repositories {
    maven {
      name = "OSSRH"
      url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
      credentials {
        username = project.properties["ossrhUsername"].toString()
        password = project.properties["ossrhPassword"].toString()
      }
    }
  }
}

signing {
  sign(publishing.publications["maven"])
}