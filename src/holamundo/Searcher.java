package holamundo;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Searcher {
	
	// Atributos
	// Buscador de índice
	IndexSearcher buscadorIndice;
	// Analizador de consultas
	QueryParser analizadorConsulta;
	// Consulta
	Query consulta;
   
   // Constructor
   public Searcher(String rutaDirectorioIndice) throws IOException{
	   
	  // Abrimos el directorio del índice
      Directory directorioIndice = FSDirectory.open(new File(rutaDirectorioIndice));
      
      // Instanciamos el analizador y la consulta
      buscadorIndice = new IndexSearcher(directorioIndice);
      analizadorConsulta = new QueryParser(Version.LUCENE_36,"contents",new StandardAnalyzer(Version.LUCENE_36));
   }
   
   // **************** METODOS DE LA CLASE SEARCHER ****************

   // Método que realiza la búsqueda con una cadena
   // Retorna un objeto TopDocs
   public TopDocs search(String busquedaDeConsulta) throws IOException, ParseException{
	   consulta = analizadorConsulta.parse(busquedaDeConsulta);
      return buscadorIndice.search(query, 10);
   }
   
   // Método que realiza la búsqueda con un objeto Query
   // Retorna un objeto TopDocs
   public TopDocs search(Query consulta) throws IOException, ParseException{
      return buscadorIndice.search(consulta, 10);
   }
   
   // Método que retorna un objeto de tipo Document
   public Document getDocument(ScoreDoc puntuacionDoc) throws CorruptIndexException, IOException{
	  return buscadorIndice.doc(puntuacionDoc.doc);	
   }
   
   // Método que cierra el buscador
   public void close() throws IOException{
	   buscadorIndice.close();
   }
}