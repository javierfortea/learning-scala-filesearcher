package fileSearcher

import java.io.File
import scala.annotation.tailrec

class Matcher(filter: String, val rootLocation: String = new File(".").getCanonicalPath(), checkSubFolders: Boolean = false, contentFilter: Option[String] = None) {
  val rootIOObject = FileConverter.convertToIOObject(new File(rootLocation))
  
  def execute(): List[(String, Option[Int])] = {
    
    @tailrec
    def recursiveMatch(files: List[IOObject], currentList: List[FileObject]): List[FileObject] =
      files match {
        case List() => currentList
        case iOObject :: rest =>
          iOObject match {
            case file: FileObject if FilterChecker(filter) matches file.name =>
              recursiveMatch(rest, file :: currentList)
            case directory: DirectoryObject =>
              recursiveMatch(rest ::: directory.children(), currentList)
            case _ =>
              recursiveMatch(rest, currentList)
          }
      }
    
    val matchedFiles = rootIOObject match {
      case file: FileObject if FilterChecker(filter) matches file.name => List(file)
      case directory: DirectoryObject =>
        if (checkSubFolders) recursiveMatch(directory.children(), List())
        else FilterChecker(filter) findMatchedFiles directory.children()
      case _ => List()
    }
    
    val contentFilteredFiles = contentFilter match {
      case Some(dataFilter) => 
        matchedFiles.map(
            iOObject  => (iOObject, Some(FilterChecker(dataFilter).findMatchedContentCount(iOObject.file))))
        .filter(matchTuple => matchTuple._2.get > 0)
      case None => 
        matchedFiles map (iOObject => (iOObject, None))
    }
    
    contentFilteredFiles map{
      case (iOObject, count) => (iOObject.name, count)
    }
  }
}