import java.util.Properties
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.pipeline.Annotation
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation
//import edu.stanford.nlp.parser.lexparser.LexicalizedParser

object ParserExample {
  def main(args: Array[String]): Unit = {
    
    val properties = new Properties()
    properties.setProperty("annotators", "tokenize,ssplit,pos,depparse")
    val pipeline = new StanfordCoreNLP(properties)

    var text = "Stanford_University is located in California. It is a great university, founded in 1891.";
    val document = new Annotation(text);

    pipeline.annotate(document)

    //val sentences: List[String] = document.get(classOf[SentencesAnnotation])
    val sentences = document.get(classOf[SentencesAnnotation])
    for (i <- 0 to sentences.size() - 1) {
      val sentence = sentences.get(i)
      println(sentence)
      val tree = sentence.get(classOf[TreeAnnotation]) 
      println(tree)
    }
    //LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
    //println("Hello, world!")
  }
}
