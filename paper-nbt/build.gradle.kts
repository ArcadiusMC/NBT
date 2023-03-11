plugins {
  id("java")
  id("io.papermc.paperweight.userdev") version "1.5.1"

  id("maven-publish")
  id("signing")
}

group = "net.forthecrown"
version = "1.2.2"

repositories {
  mavenCentral()

  maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

dependencies {
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

  compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")

  implementation(project(":nbt"))

  paperweight.paperDevBundle("1.19.3-R0.1-SNAPSHOT")
}

tasks {
  assemble {
    dependsOn(reobfJar)
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
  toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      from(components["java"])

      pom {
        name.set("paper-nbt")
        description.set("Java NamedBinaryTag library for PaperMC")
        url.set("https://github.com/ForTheCrown/NBT")

        val jarPath = buildDir.path + "/libs/${project.name}-${project.version}.jar"
        val jarFile = file(jarPath)

        artifact(jarFile)

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