plugins {
    id("java-library")
}

dependencies {
    api(project(":kbrn"))
    
    // Jackson databind with flexible version range
    // Supports Jackson 2.12.x through 2.19.x (Java 8+ required for 2.13+)
    api("com.fasterxml.jackson.core:jackson-databind") {
        version {
            // Minimum 2.12.0 for better compatibility
            // Maximum 2.19.999 to avoid breaking changes in 2.20+
            strictly("[2.12.0, 2.19.999]")
            prefer("2.19.0")
        }
    }
    
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

java {
    withJavadocJar()
    withSourcesJar()
}