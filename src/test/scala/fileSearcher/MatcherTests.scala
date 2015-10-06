package fileSearcher

import org.scalatest.FlatSpec
import java.io.File

class MatcherTests extends FlatSpec{
  "Matcher that is passed a file matching the filter" should "return a list with that file name" in {
    val matcher = new Matcher("fake", "fakePath")
    val result = matcher.execute()
    
    assert(result == List("fakePath"))
  }
  
  "Matcher that is not passed a root file location" should "use the current location" in {
    val matcher = new Matcher("filter");
    assert(matcher.rootLocation == new File(".").getCanonicalPath())
  } 
}