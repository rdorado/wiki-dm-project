import org.apache.spark.SparkContext 
import org.apache.spark.SparkContext._ 
import org.apache.spark._  

import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.feature.Word2Vec

object SparkWordCount { 
   def main(args: Array[String]) { 
      // create Spark context with Spark configuration
      val sc = new SparkContext(new SparkConf().setAppName("Spark Count"))

      /* Word count example*/
      /*val tokenized = sc.textFile("input[1-3].txt").flatMap(_.split(" "))
      val wordCounts = tokenized.map((_, 1)).reduceByKey(_ + _)
      System.out.println(wordCounts.collect().mkString(", "))
      wordCounts.saveAsTextFile("outfile")*/


      val tokenized = sc.textFile("000/1000.xml").map(line => line.split(" ").toSeq)
      // val tokenized = sc.textFile("1000.xml").map(line => line.split(" ").toSeq)

      val word2vec = new Word2Vec()
      val model = word2vec.fit(tokenized)

      val synonyms = model.findSynonyms("directories", 40)
      for((synonym, cosineSimilarity) <- synonyms) {
        println(s"$synonym $cosineSimilarity")
      }
      //wordCounts.saveAsTextFile("outfile")
	
   } 
} 
