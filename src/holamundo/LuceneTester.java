package holamundo;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import holamundo.Indexer;
import holamundo.TextFileFilter;


public class LuceneTester {
	
	 String indexDir = "d:/fi/year 4/Recuperacion avanzada de informacion/tareas/tp4 Lucene funciona";
	 String dataDir = "d:/fi/year 4/Recuperacion avanzada de informacion/tareas/BoletinesParaIndexar/txt";
	 Indexer indexer;
   Searcher searcher;

   public static void main(String[] args) {
	   LuceneTester tester;
      
      try {
    	  
    	  tester = new LuceneTester();
    	  tester.createIndex();
    	  //Buscar ek texto aqui
    	  
    	  
    	  BufferedReader br;
    		String choice = "";
    		System.out.println("***** Indexador Lucene, Buscador de Prueba ******");
    		System.out.println("Ingresa palabra a buscar:");
    		
    		br = new BufferedReader(new InputStreamReader(System.in));
    		try {
    			choice = br.readLine();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		try {
    			br.close();
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
    		
    		//Opcional para frases (palabras múltiples)
    		
    	    String[] searcharray = choice.split(" ");
    	    for (int i = 0; i < searcharray.length; i++)
    	    {
    	    	System.out.println("Resultados de búsqueda de palabra " + (i+1) + ": " + searcharray[i]);
    	    	tester.searchUsingFuzzyQuery(searcharray[i]);
    	    }
    		
     		 //descomentar para busqueda de una sola palabra          
    		
    		 //tester.searchUsingFuzzyQuery(choice);

         
      } catch (IOException e) {
         e.printStackTrace();
      } catch (ParseException e) {
         e.printStackTrace();
      }
   }
   
   private void createIndex() throws IOException{
	   indexer = new Indexer(indexDir);
	   int numIndexed;
	   long startTime = System.currentTimeMillis();
	   numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
	   long endTime = System.currentTimeMillis();
	   indexer.close();
	   System.out.println(numIndexed+" Archivo indexado, tiempo empleado: "
	   +(endTime-startTime)+" ms");
	   }
   
   
   private void searchUsingFuzzyQuery(String searchQuery)
      throws IOException, ParseException{
      searcher = new Searcher(indexDir);
      long startTime = System.currentTimeMillis();
      //crea termino para buscar el nombre del archivo
      //Term term = new Term(LuceneConstants.FILE_NAME, searchQuery);
      Term term = new Term(LuceneConstants.CONTENTS, searchQuery);
      //crea el objeto consulta difusa 
      Query query = new FuzzyQuery(term);
      //do the search
      
      TopDocs hits = searcher.search(query);
      long endTime = System.currentTimeMillis();

      System.out.println(hits.totalHits +
         " documentos encontrados. Time :" + (endTime - startTime) + "ms");
      for(ScoreDoc scoreDoc : hits.scoreDocs) {
         Document doc = searcher.getDocument(scoreDoc);
         System.out.print("Puntuacion: "+ scoreDoc.score + " ");
         System.out.println("Archivo: "+ doc.get(LuceneConstants.FILE_PATH));
      }
      searcher.close();
   }
}