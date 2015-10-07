package fileSearcher

import java.io.File
import scala.util.control.NonFatal

class FilterChecker(filter: String) {
  def matches(content : String) = content.contains(filter)
  
  def findMatchedFiles(IOObjects : List[IOObject]) = {
    for(IOObject <- IOObjects
        if(IOObject.isInstanceOf[FileObject])
        if(matches(IOObject.name)))
      yield IOObject
  }
  
  def matchesFileContent(file: File): Boolean = {
    import scala.io.Source
    
    try{
      val fileSource = Source.fromFile(file)
      
      try{
        fileSource.getLines() exists(line => matches(line))
      } catch {
        case NonFatal(_) => false
      } finally {
        fileSource.close()
      }
    } catch {
      case NonFatal(_) => false
    }
  }
}

object FilterChecker{
  def apply(filter: String) = new FilterChecker(filter)
}