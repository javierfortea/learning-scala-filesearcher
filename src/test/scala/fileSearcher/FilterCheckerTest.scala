package fileSearcher

import org.scalatest.FlatSpec
import java.io.File

class FilterCheckerTest extends FlatSpec{
  "FilterChecker passed a list where one file matches the filter" should "return a list with that file" in{
    val listOfFiles = List(FileObject(new File("random")), FileObject(new File("match")))
    val matchedFiles = FilterChecker("match") findMatchedFiles listOfFiles
    assert(matchedFiles == List(FileObject(new File("match"))))
  }
  
  "FilterChecker passed a list with a directory that matches the filter" should "not return the directory" in{
    val listofIOObjects = List(FileObject(new File("random")), DirectoryObject(new File("match")))
    val matchedFiles = FilterChecker("match") findMatchedFiles listofIOObjects
    assert(matchedFiles.length == 0)
  }
}