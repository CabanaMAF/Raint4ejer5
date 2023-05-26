package holamundo;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
	
	// Atributos 
	// Declaramos el Indexador
	private IndexWriter writer; 
  
  	// Constructor
 	public Indexer(String indexDirectoryPath) throws IOException{
 		
		 // Directorio que contendrá los índices
		 Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));
		 
		 // Creamos el indexador o escritor de índice
		 writer = new IndexWriter(indexDirectory, new StandardAnalyzer(Version.LUCENE_36),true,IndexWriter.MaxFieldLength.UNLIMITED);
	 }
 	
 	// **************** METODOS DE LA CLASE INDEXER ****************
 	
 	// Método para cerrar el indexador
	public void close() throws CorruptIndexException, IOException{
		 writer.close();
	}
	
	// Método que recibe un archivo y retorna un documento
	private Document getDocument(File file) throws IOException{
		
		// Creamos un nuevo documento
		Document document = new Document();
		
		// Indexamos el contenido del archivo
		Field contentField = new Field("contents",new FileReader(file));
		// Indexamos el nombre del archivo
		Field fileNameField = new Field("filename",file.getName(),Field.Store.YES,Field.Index.NOT_ANALYZED);
		// Indexamos la ruta del archivo
		Field filePathField = new Field("filepath",file.getCanonicalPath(),Field.Store.YES,Field.Index.NOT_ANALYZED);
		
		// Añadimos los atributos obtenidos al documento creado
		document.add(contentField);
		document.add(fileNameField);
		document.add(filePathField);
		
		// Retornamos el documento
		return document;
	 }
	
	// Método que obtiene un documento de un archivo
	// y lo añade al indexador (Lo indexa)
	private void indexFile(File file) throws IOException{
		System.out.println("Indexando "+file.getCanonicalPath());
		Document document = getDocument(file);
		writer.addDocument(document);
	}
	
	// Método que crea el índice.
	// Recibe una ruta y un filtro de archivos .txt
	public int createIndex(String dataDirPath, FileFilter filter) throws IOException{
		
		// Obtenemos todos los archivos en la ruta indicada
		File[] files = new File(dataDirPath).listFiles();
		
		/* Para cada archivo nos aseguramos de que:
		   - No es un directorio
		   - No está oculto
		   - Existe
		   - Se puede leer
		   - Es un archivo .txt   */
		for (File file : files) {
			 if(!file.isDirectory() && !file.isHidden() && file.exists() && file.canRead() && filter.accept(file)){
				 indexFile(file); // Lo indexa con el método anterior
			 }
		}
		// Retornamos el numero de documentos indexados
		return writer.numDocs(); 
	}
}