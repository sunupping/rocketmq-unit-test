name := "UnitTest_RocketMq"

version := "0.1"

scalaVersion := "2.11.12"

libraryDependencies += "org.apache.rocketmq"%"rocketmq-client"%"4.4.0"

libraryDependencies += "org.apache.rocketmq" %"rocketmq-broker" % "4.4.0"

libraryDependencies += "org.apache.rocketmq" %"rocketmq-namesrv" % "4.4.0"

libraryDependencies +="org.scalatest" %% "scalatest" % "3.0.5" % "test"

libraryDependencies +="log4j"%"log4j"%"1.2.17"