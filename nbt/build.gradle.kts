plugins {
  id("java")
  id("maven-publish")
  id("signing")
}

group = "net.forthecrown"
version = "1.5.2"

repositories {
  mavenCentral()
}

dependencies {
  compileOnly("org.jetbrains:annotations:20.1.0")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

  implementation("it.unimi.dsi:fastutil:8.5.11")
}

tasks {
  test {
    useJUnitPlatform()
  }

  compileJava {
    options.release = 21
  }

  java {
    withSourcesJar()
    withJavadocJar()
  }
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      from(components["java"])

      pom {
        name.set("nbt")
        description.set("Java NamedBinaryTag library")
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