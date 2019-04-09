import java.io.{File, PrintWriter}

import org.apache.rocketmq.broker.BrokerStartup
import org.apache.rocketmq.namesrv.NamesrvStartup

import scala.io.Source

object EmebeddedRocketMqServer {
  System.setProperty("rocketmq.namesrv.addr", "localhost:9876")

//  def main(args:Array[String]): Unit = {
//    start()
//  }
  def start(): Unit ={
    val home = System.getProperty("user.home") + File.separator + "rocketmq_dev"
    System.setProperty("rocketmq.home.dir", home)
    checkAndCreateDirs(home)
    deployConfigResources(home)
    NamesrvStartup.main(null)
    BrokerStartup.main(null)
  }

  def checkAndCreateDirs(path:String):Unit={
    val file:File = new File(path)
    if (!file.isDirectory || !file.exists) {
      if (!file.mkdirs) {
        System.exit(1)
      }
    }
  }

  private def deployConfigResources(home: String):Unit = {
    val confDir = home + File.separator + "conf"
    val confDirFile = checkAndCreateDirs(confDir)
    deployConfig(confDir, "logback_broker.xml")
    deployConfig(confDir, "logback_namesrv.xml")
  }

  private def deployConfig(dstFolder: String, config: String):Unit = {
    val dstBrokerConfigFile = new File(dstFolder, config)
    if (dstBrokerConfigFile.exists && dstBrokerConfigFile.isFile) return
    val writer = new PrintWriter(dstBrokerConfigFile)
    val inputStream = EmebeddedRocketMqServer.getClass.getClassLoader.getResourceAsStream(config)
    val file = Source.fromInputStream(inputStream)
    for(line <- file.getLines()){
      writer.write(line)
    }
    file.close()
    writer.flush()
    writer.close()
  }

}
