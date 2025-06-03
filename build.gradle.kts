import org.jreleaser.model.Active

plugins {
  id("java")
  id("maven-publish")
  id("signing")
  id("org.jreleaser") version "1.18.0"
}

allprojects {
  repositories {
    mavenCentral()
  }
  group = "io.github.realrains.kbrn"
  version = ext["artifactVersion"] as String
}

subprojects {
  apply(plugin = "java")
  apply(plugin = "maven-publish")
  apply(plugin = "signing")
  apply(plugin = "org.jreleaser")

  java {
    toolchain {
      languageVersion.set(JavaLanguageVersion.of(21))
    }

    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  // https://jreleaser.org/guide/latest/examples/maven/maven-central.html#_portal_publisher_api
  jreleaser {
    project {
      name.set("kbrn")
      authors.add("Jinwoo Jang")
      license.set("MIT License")

      links {
        homepage.set("https://maven.pkg.github.com/realrains/kbrn")
      }
    }
    signing {
      active.set(Active.NEVER)
      armored.set(true)
    }
    deploy {
      maven {
        mavenCentral {
          create("release-deploy") {
            active.set(Active.RELEASE)
            url = "https://central.sonatype.com/api/v1/publisher"
            stagingRepository("build/staging-deploy")
            sign = false
            applyMavenCentralRules = true
          }
        }
      }
    }
  }

  publishing {
    repositories {
      maven {
        url = uri(layout.buildDirectory.dir("staging-deploy").get().asFile)
      }
    }
    publications {
      create<MavenPublication>("maven") {
        artifactId = "kbrn"
        from(components["java"])

        pom {
          name.set("kbrn")
          description.set("Korean Business Registration Number validation library for Java")
          url.set("https://github.com/realrains/kbrn")

          licenses {
            license {
              name.set("MIT License")
              url.set("https://opensource.org/licenses/MIT")
            }
          }
          developers {
            developer {
              id.set("realrains")
              name.set("Jinwoo Jang")
              email.set("real.longrain@gmail.com")
            }
          }
          scm {
            connection.set("https://github.com/realrains/kbrn.git")
            developerConnection.set("https://github.com/realrains/kbrn.git")
            url.set("https://github.com/realrains/kbrn")
          }
        }
      }
    }
  }
  signing {
    val pgpKey = System.getenv("PGP_KEY")
    val pgpPassword = System.getenv("PGP_PASSWORD")
    useInMemoryPgpKeys(pgpKey, pgpPassword)
    sign(publishing.publications["maven"])
  }
}
