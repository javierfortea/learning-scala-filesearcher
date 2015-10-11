package fileSearcher

import java.io.File
import scala.util.control.NonFatal

class FilterChecker(filter: String) {
  
  val filterAsRegex = filter.r
  
  def matches(content : String) = 
    filterAsRegex findFirstMatchIn content match {
      case Some(_) => true
      case None => false
  }
    
  def findMatchedFiles(IOObjects : List[IOObject]) = {
    for(IOObject <- IOObjects
        if(IOObject.isInstanceOf[FileObject])
        if(matches(IOObject.name)))
      yield IOObject
  }
  
  def findMatchedContentCount(file: File): Int = {
    
    def getFilterMatchCount(content: String) =
      (filterAsRegex findAllIn content).length
    
    import scala.io.Source
    
    try{
      val fileSource = Source.fromFile(file)
      
      try{
        fileSource.getLines().foldLeft(0)(
            (accumulator, line) => accumulator + getFilterMatchCount(line))
      } catch {
        case NonFatal(_) => 0
      } finally {
        fileSource.close()
      }
    } catch {
      case NonFatal(_) => 0
    }
  }
}

object FilterChecker{
  def apply(filter: String) = new FilterChecker(filter)
}