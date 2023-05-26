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
	IndexSearcher indexSearcher;
	// Analizador de consultas
	QueryParser queryParser;
	// Consulta
	Query query;
   
   // Constructor
   public Searcher(String indexDirectoryPath) throws IOException{
	   
	  // Abrimos el directorio del índice
      Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));
      
      // Instanciamos el analizador y la consulta
      indexSearcher = new IndexSearcher(indexDirectory);
      queryParser = new QueryParser(Version.LUCENE_36,"contents",new StandardAnalyzer(Version.LUCENE_36));
   }
   
   // **************** METODOS DE LA CLASE SEARCHER ****************

   // Método que realiza la búsqueda con una cadena
   // Retorna un objeto TopDocs
   public TopDocs search(String searchQuery) throws IOException, ParseException{
      query = queryParser.parse(searchQuery);
      return indexSearcher.search(query, 10);
   }
   
   // Método que realiza la búsqueda con un objeto Query
   // Retorna un objeto TopDocs
   public TopDocs search(Query query) throws IOException, ParseException{
      return indexSearcher.search(query, 10);
   }
   
   // Método que retorna un objeto de tipo Document
   public Document getDocument(ScoreDoc scoreDoc) throws CorruptIndexException, IOException{
	  return indexSearcher.doc(scoreDoc.doc);	
   }
   
   // Método que cierra el buscador
   public void close() throws IOException{
      indexSearcher.close();
   }
}