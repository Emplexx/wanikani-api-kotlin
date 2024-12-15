plugins {
	id("java-library")
	kotlin("jvm")
	kotlin("plugin.serialization")
}

group = "moe.emi.wanikani"
version = "1.0-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
	
	val ktor = "3.0.2"
	implementation("io.ktor:ktor-client-core:$ktor")
	implementation("io.ktor:ktor-client-content-negotiation:$ktor")
	implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor")
	implementation("io.ktor:ktor-client-cio:$ktor")
	implementation("io.ktor:ktor-client-logging:$ktor")
	
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
	implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
	
//	implementation("ch.qos.logback:logback-classic:1.5.12")
}

