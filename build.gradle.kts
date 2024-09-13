import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.dependencies

plugins {
    kotlin("jvm") version "2.0.20"
    id("io.github.goooler.shadow") version "8.1.7"
}

val targetJavaVersion = 21

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.leavesmc.org/snapshots")
    maven("https://repo.codemc.org/repository/maven-public/")
    mavenCentral()
}

dependencies {
    compileOnly("org.leavesmc.leaves:leaves-api:1.21.1-R0.1-SNAPSHOT")
    implementation("dev.jorel:commandapi-bukkit-shade-mojang-mapped:9.5.2")
    implementation("dev.jorel:commandapi-bukkit-kotlin:9.5.2")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-folia-api:2.19.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-folia-core:2.19.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC.2")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.spongepowered:configurate-hocon:4.2.0-SNAPSHOT")
}

kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.withType<ShadowJar> {
    relocate("dev", "${project.group}.feaves.dev")
    relocate("io", "${project.group}.feaves.io")
    relocate("com", "${project.group}.feaves.com")
    relocate("org", "${project.group}.feaves.org")
    relocate("kotlin", "${project.group}.feaves.kotlin")
    relocate("kotlinx", "${project.group}.feaves.kotlinx")
    relocate("_COROUTINE", "${project.group}.feaves._COROUTINE")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("leaves-plugin.conf") {
        expand(props)
    }
}