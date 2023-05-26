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
	
	// Ruta donde se guardará el índice	
	String indexDir = "d:/fi/year 4/Recuperacion avanzada de informacion/tareas/tp4 Lucene funciona";
	// Ruta donde se encuentran los archivos a indexar
	String dataDir = "d:/fi/year 4/Recuperacion avanzada de informacion/tareas/BoletinesParaIndexar/txt";
	
	// Declaración del índice y el buscador
	Indexer indexer;
	Searcher searcher;

	public static void main(String[] args) {
		
	   // Declaración para realizar la prueba
	   LuceneTester tester;
      
	   try {
		   	// Creamos una instancia de esta clase
	    	tester = new LuceneTester();
	    	
	    	// Se crea el índice
	    	tester.createIndex();
	    	
	    	// Entrada de palabras a buscar	    	  
	    	BufferedReader br;
    		String textInput = "";
    		System.out.println("=============== Indexador LUCENE ===============");
    		System.out.println("Ingresa palabra a buscar:");
    		
    		br = new BufferedReader(new InputStreamReader(System.in));
    		try {
    			textInput = br.readLine();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		try {
    			br.close();
    		} catch (IOException e1) {
    			e1.printStackTrace();
    		}
    		
    		// Guardamos la consulta en una lista de cadenas
    	    String[] searcharray = textInput.split(" ");
    	    
    	    // Ejecutamos la busqueda para cada palabra de la lista
    	    for (int i = 0; i < searcharray.length; i++){
    	    	System.out.println("Resultados de búsqueda de palabra " + (i+1) + ": " + searcharray[i]);
    	    	tester.searchQuery(searcharray[i]);
    	    }
    		         		
    		 //tester.searchQuery(textInput);
         
      } catch (IOException e) {
         e.printStackTrace();
      } catch (ParseException e) {
         e.printStackTrace();
      }
   }
   
   // Método utilizado para crear el índice
   private void createIndex() throws IOException{
	   
	   // Creamos una instancia de la clase Indexer
	   indexer = new Indexer(indexDir);
	   int numIndexed;
	   long startTime = System.currentTimeMillis();
	   
	   // Creamos el índice con el metodo createIndex
	   // declarado en la clase Indexer
	   numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
	   
	   long endTime = System.currentTimeMillis();
	   
	   // Cerramos el indexer
	   indexer.close();
	   
	   // Mostramos por pantalla el tiempo de indexado
	   System.out.println("Archivos indexados: "+numIndexed+" - Tiempo empleado: "+(endTime-startTime)+" ms");
   }
   
   // Método utilizado para buscar en el índice   
   private void searchQuery(String searchQuery) throws IOException, ParseException{
	   
      // Creamos una instancia del buscador Searcher
	  searcher = new Searcher(indexDir);
      long startTime = System.currentTimeMillis();

      // Creamos el término a buscar
      Term term = new Term("contents", searchQuery);
      
      // Crea la consulta 
      Query query = new FuzzyQuery(term);
      
      // Ejecuta la búsqueda
      TopDocs hits = searcher.search(query);
      long endTime = System.currentTimeMillis();
      
      // Mostramos por pantalla cantidad de resultados y el tiempo de búsqueda
      System.out.println(hits.totalHits + " documentos encontrados. Time :" + (endTime - startTime) + "ms");
      
      // Para cada documento mostramos su puntuación de búsqueda y su ruta
      for(ScoreDoc scoreDoc : hits.scoreDocs) {
         Document doc = searcher.getDocument(scoreDoc);
         System.out.print("Puntuacion: "+ scoreDoc.score + " ");
         System.out.println("Archivo: "+ doc.get("filename"));
      }
      
      // Cerramos el buscador
      searcher.close();
   }
}