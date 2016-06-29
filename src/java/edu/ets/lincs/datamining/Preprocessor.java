package edu.ets.lincs.datamining;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphFactory;
import edu.stanford.nlp.semgraph.semgrex.SemgrexMatcher;
import edu.stanford.nlp.semgraph.semgrex.SemgrexPattern;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import edu.stanford.nlp.util.CoreMap;

public class Preprocessor {

	public static String preprocess(String document) {
		//remove trailing whitespace	
		document = document.trim();

		//remove single words in parentheses.  

		//the stanford parser api messed up on these
		//by removing the parentheses but not the word in them
		document = document.replaceAll("\\(\\S*\\)", "");
		//document = document.replaceAll("\\(\\s*\\)", "");

		//contractions
		document = document.replaceAll("can't", "can not");
		document = document.replaceAll("won't", "will not");
		document = document.replaceAll("n't", " not"); //aren't shouldn't don't isn't
		document = document.replaceAll("are n't", "are not");

		//simply remove other unicode characters
		//if not, the tokenizer replaces them with spaces, 
		//which wreaks havoc on the final parse sometimes
		for (int i = 0; i < document.length(); i++) {
			if (document.charAt(i) > 'z') {
				document = document.substring(0, i) + document.substring(i + 1);
			}
		}
		return document;
	}

	public static List<String> getSentences(String document) {
		List<String> resp = new ArrayList<String>();

		document = preprocess(document);

		return resp;
	}


	public static void main(String[] args) {

		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		
		String modelPath = DependencyParser.DEFAULT_MODEL;
	    String taggerPath = "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger";
	    MaxentTagger tagger = new MaxentTagger(taggerPath);
	    DependencyParser parser = DependencyParser.loadFromModelFile(modelPath);
	    
	    TregexPattern p = TregexPattern.compile("ROOT << (NP=np $++ VP=vp) ");
	    SemgrexPattern semgrex = SemgrexPattern.compile("{}=A <<nsubj {}=B");	    
	    
	    try {
			/*BufferedReader in = new BufferedReader(new FileReader("/home/rdorado/Downloads/miniwiki/text/000/1000.xml"));
			
			String line = null;
			for(int i=0;i<4;i++) in.readLine();
			while((line=in.readLine())!=null){*/
	    		String line = "She looks very beautiful";
				System.out.println("-->\n"+line);
				/*Annotation document = new Annotation(line);
				pipeline.annotate(document);
				List<CoreMap> sentences = document.get(SentencesAnnotation.class);
				for(CoreMap sentence: sentences) {
					System.out.println(sentence);
				}*/
				
				DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(line));
				for (List<HasWord> sentence : tokenizer){
					List<TaggedWord> tagged = tagger.tagSentence(sentence);
					GrammaticalStructure gs = parser.predict(tagged);
					//System.out.println(gs.root());
					
					SemanticGraph graph = SemanticGraphFactory.generateCollapsedDependencies(gs);
					
					SemgrexMatcher matcher = semgrex.matcher(graph);
					
					System.out.println(gs);
					while (matcher.find()) {
					      System.out.println(matcher.getNode("A") + " <<nsubj " + matcher.getNode("B"));
					}
					System.out.println();
					//TreePrint tp = new TreePrint("wordsAndTags");
					//tp.printTree(gs.root());
					
					//TregexMatcher m = p.matcher(gs.root());
				}
				
				
				//document+=line;
		//	}
		//	in.close();

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}	
}
