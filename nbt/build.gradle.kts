plugins {
  id("java")
  id("maven-publish")
  id("signing")
}

group = "net.forthecrown"
version = "1.5.1"

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

  java {
    withSourcesJar()
    withJavadocJar()
  }
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      from(components["java"])

      pom {
        name.set("nbt")
        description.set("Java NamedBinaryTag library")
        url.set("https://github.com/ForTheCrown/NBT")

        licenses {
          license {
            name.set("MIT License")
            url.set("https://raw.githubusercontent.com/ForTheCrown/NBT/main/LICENSE.md")
          }
        }

        developers {
          developer {
            name.set("JulieWoolie")
            id.set("JulieWoolie")
          }
        }

        scm {
          connection.set("scm:git:git:github.com/ForTheCrown/NBT/.git")
          developerConnection.set("scm:git:ssh://github.com/ForTheCrown/NBT/.git")
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