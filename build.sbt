import AssemblyKeys._

// General settings

name := "Scalding Examples"

organization := "com.morazow"

version := "1.0"

resolvers ++= Seq("maven.org" at "http://repo2.maven.org/maven2",
                  "conjars.org" at "http://conjars.org/repo",
                  "liveramp-repositories" at "http://repository.liveramp.com/artifactory/liveramp-repositories")

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "com.twitter" %% "scalding-core" % "0.13.1",
  "org.apache.hadoop" % "hadoop-core" % "1.2.1",
  "com.liveramp" % "cascading_ext" % "1.6-SNAPSHOT"
)

// Assembly settings


assemblySettings

jarName in assembly := s"ScaldingExamples-${version.value}.jar"

test in assembly := {}

excludedJars in assembly <<= (fullClasspath in assembly) map { cp =>
  val exludeSet = Set(
    "asm-3.1.jar",
    "minlog-1.2.jar", // conflicts with kryo
    "stax-api-1.0.1.jar",
    "jsp-2.1-6.1.14.jar",
    "jsp-api-2.1-6.1.14.jar",
    "commons-beanutils-1.7.0.jar",
    "commons-beanutils-core-1.8.0.jar", // clashes with each other and with commons-collections
    "hadoop-core-1.2.1.jar" // provided in hadoop env
  )
  cp filter { jar => exludeSet(jar.data.getName) }
}


