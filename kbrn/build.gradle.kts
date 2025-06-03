plugins {
    id("java-library")
}

dependencies {
    implementation("org.jspecify:jspecify:1.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

tasks.test {
    useJUnitPlatform()
}

java {
  withJavadocJar()
  withSourcesJar()
}
