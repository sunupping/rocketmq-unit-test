import java.io.File

object CleanRmqDir {

  var logs = new File(System.getProperty("user.home") + File.separator + "logs")
  var store = new File(System.getProperty("user.home") + File.separator + "store")
  var rmqHome = new File(System.getProperty("user.home") + File.separator + "rocketmq_dev")
  def clean(): Unit ={
    if (logs.exists()){
      deleteDir(logs)
    }
    if (store.exists()){
      deleteDir(store)
    }
    if (rmqHome.exists()){
      deleteDir(rmqHome)
    }
  }

  private def deleteDir(file:File):Boolean ={
    if (file.isDirectory){
      val child = file.list()
      for (i <- child){
        val isSuccess = deleteDir(new File(file,i))
        if (!isSuccess){
          false
        }
      }
    }
    file.delete()
  }
}
