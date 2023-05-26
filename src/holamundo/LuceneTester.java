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

import holamundo.indexador;
import holamundo.TextFileFilter;


public class LuceneTester {
	
	// Ruta donde se guardará el índice	
	String direccionIndice = "d:/fi/year 4/Recuperacion avanzada de informacion/tareas/tp4 Lucene funciona";
	// Ruta donde se encuentran los archivos a indexar
	String direccionDatos = "d:/fi/year 4/Recuperacion avanzada de informacion/tareas/BoletinesParaIndexar/txt";
	
	// Declaración del índice y el buscador
	indexador indexador;
	buscador buscador;

	public static void main(String[] args) {
		
	   // Declaración para realizar la prueba
	   LuceneTester tester;
      
	   try {
		   	// Creamos una instancia de esta clase
	    	tester = new LuceneTester();
	    	
	    	// Se crea el índice
	    	tester.createIndex();
	    	
	    	// Entrada de palabras a buscar	    	  
	    	BufferedReader lector;
    		String textInput = "";
    		System.out.println("=============== Indexador LUCENE ===============");
    		System.out.println("Ingresa palabra a buscar:");
    		
    		lector = new BufferedReader(new InputStreamReader(System.in));
    		try {
    			textInput = lector.readLine();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		try {
    			lector.close();
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
	   
	   // Creamos una instancia de la clase indexador
	   indexador = new indexador(direccionIndice);
	   int numeroDeIndexados;
	   long tiempoInicio = System.currentTimeMillis();
	   
	   // Creamos el índice con el metodo createIndex
	   // declarado en la clase indexador
	   numeroDeIndexados = indexador.createIndex(direccionDatos, new TextFileFilter());
	   
	   long tiempoFinal = System.currentTimeMillis();
	   
	   // Cerramos el indexador
	   indexador.close();
	   
	   // Mostramos por pantalla el tiempo de indexado
	   System.out.println("Archivos indexados: "+numeroDeIndexados+" - Tiempo empleado: "+(tiempoFinal-tiempoInicio)+" ms");
   }
   
   // Método utilizado para buscar en el índice   
   private void searchQuery(String searchQuery) throws IOException, ParseException{
	   
      // Creamos una instancia del buscador buscador
	  buscador = new buscador(direccionIndice);
      long startTime = System.currentTimeMillis();

      // Creamos el término a buscar
      Term termino = new Term("contents", searchQuery);
      
      // Crea la consulta 
      Query consulta = new FuzzyQuery(termino);
      
      // Ejecuta la búsqueda
      TopDocs coincidencias = buscador.search(consulta);
      long endTime = System.currentTimeMillis();
      
      // Mostramos por pantalla cantidad de resultados y el tiempo de búsqueda
      System.out.println(coincidencias.totalHits + " documentos encontrados. Time :" + (endTime - startTime) + "ms");
      
      // Para cada documento mostramos su puntuación de búsqueda y su ruta
      for(ScoreDoc puntuacionDoc : coincidencias.scoreDocs) {
         Document doc = buscador.getDocument(puntuacionDoc);
         System.out.print("Puntuacion: "+ puntuacionDoc.score + " ");
         System.out.println("Archivo: "+ doc.get("filename"));
      }
      
      // Cerramos el buscador
      buscador.close();
   }
}