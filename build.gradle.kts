import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.dependencies

plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
    id("org.jetbrains.kotlinx.kover") version "0.9.0-RC"
    id("io.github.goooler.shadow") version "8.1.7"
}

val targetJavaVersion = 21
val mccoroutineVersion = "2.20.0"
val commandAPIVersion = "9.5.3"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.leavesmc.org/snapshots")
    maven("https://repo.codemc.org/repository/maven-public/")
    mavenCentral()
}

dependencies {
    compileOnly("net.luckperms:api:5.4")
    compileOnly("org.leavesmc.leaves:leaves-api:1.21.1-R0.1-SNAPSHOT")
    compileOnly("com.github.shynixn.mccoroutine:mccoroutine-folia-api:$mccoroutineVersion")
    compileOnly("com.github.shynixn.mccoroutine:mccoroutine-folia-core:$mccoroutineVersion")
    implementation("dev.jorel:commandapi-bukkit-shade-mojang-mapped:$commandAPIVersion")
    implementation("dev.jorel:commandapi-bukkit-kotlin:$commandAPIVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.spongepowered:configurate-hocon:4.2.0-SNAPSHOT")

    testImplementation(kotlin("test"))
    testImplementation("org.leavesmc.leaves:leaves-api:1.21.1-R0.1-SNAPSHOT")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.21:3.107.0") {
        exclude(group = "io.papermc.paper", module = "paper-api")
    }
    testImplementation("com.github.shynixn.mccoroutine:mccoroutine-folia-api:$mccoroutineVersion")
    testImplementation("com.github.shynixn.mccoroutine:mccoroutine-folia-test:$mccoroutineVersion")
}

kotlin {
    jvmToolchain(targetJavaVersion)
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.uuid.ExperimentalUuidApi")
    }
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy("koverHtmlReport")
    val file = file("./run/test")
    if (!file.exists()) {
        file.mkdirs()
    }
    workingDir = file
}

tasks.withType<ShadowJar> {
    relocate("dev.jorel.commandapi", "${project.group}.feaves.commandapi")
    relocate("org.spongepowered.configurate", "${project.group}.feaves.configurate")
    relocate("kotlin", "${project.group}.feaves.kotlin")
    relocate("kotlinx", "${project.group}.feaves.kotlinx")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("leaves-plugin.conf") {
        expand(props)
    }
}