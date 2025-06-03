plugins {
  id("java")
  id("maven-publish")
}

allprojects {
  repositories {
    mavenCentral()
  }
  group = "io.github.realrains.kbrn"
}

subprojects {
  apply(plugin = "java")
  apply(plugin = "maven-publish")
  apply(plugin = "signing")

  version = extra["artifactVersion"] as String

  java {
    toolchain {
      languageVersion.set(JavaLanguageVersion.of(21))
    }

    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  publishing {
    repositories {
      maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/realrain/kbrn")
        credentials {
          username = System.getenv("GITHUB_ACTOR")
          password = System.getenv("GITHUB_TOKEN")
        }
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
}
