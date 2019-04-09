import scala.io.Source

object KillServerPid {

  def shutdown(className:String="EmebeddedRocketMqServer"):Unit ={
    var pid = ""
    val p = Runtime.getRuntime.exec("jps")
    val reader = Source.fromInputStream(p.getInputStream)
    val jpsLines = reader.getLines()
    for (line <- jpsLines){
      if (line.contains(className)){
        pid = line.split("\\s")(0)
      }
    }
    reader.close()
    if (pid==""){
      throw new IllegalStateException("关闭服务失败![未找到"+className+"服务的进程]")
    }else{
      Runtime.getRuntime.exec("taskkill /F /PID " + pid.toInt)
    }
  }
}
