resolvers += "TNM" at "http://nexus.thenewmotion.com/content/groups/public"

// https://github.com/NewMotion/sbt-build-seed
addSbtPlugin("com.thenewmotion" % "sbt-build-seed" % "2.1.0")

// https://github.com/sbt/sbt-native-packager
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.2.0-M8")

// https://github.com/ebiznext/sbt-cxf-wsdl2java
addSbtPlugin("com.ebiznext.sbt.plugins" % "sbt-cxf-wsdl2java" % "0.1.4")
