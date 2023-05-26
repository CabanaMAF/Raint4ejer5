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
	private IndexWriter escritor; 
  
  	// Constructor
 	public Indexer(String rutaDirectorioIndice) throws IOException{
 		
		 // Directorio que contendrá los índices
		 Directory directorioIndice = FSDirectory.open(new File(rutaDirectorioIndice));
		 
		 // Creamos el indexador o escritor de índice
		 escritor = new IndexWriter(directorioIndice, new StandardAnalyzer(Version.LUCENE_36),true,IndexWriter.MaxFieldLength.UNLIMITED);
	 }
 	
 	// **************** METODOS DE LA CLASE INDEXER ****************
 	
 	// Método para cerrar el indexador
	public void close() throws CorruptIndexException, IOException{
		escritor.close();
	}
	
	// Método que recibe un archivo y retorna un documento
	private Document getDocument(File archivo) throws IOException{
		
		// Creamos un nuevo documento
		Document documento = new Document();
		
		// Indexamos el contenido del archivo
		Field contenido = new Field("contents",new FileReader(archivo));
		// Indexamos el nombre del archivo
		Field nombre = new Field("filename",archivo.getName(),Field.Store.YES,Field.Index.NOT_ANALYZED);
		// Indexamos la ruta del archivo
		Field ruta = new Field("filepath",archivo.getCanonicalPath(),Field.Store.YES,Field.Index.NOT_ANALYZED);
		
		// Añadimos los atributos obtenidos al documento creado
		documento.add(contenido);
		documento.add(nombre);
		documento.add(ruta);
		
		// Retornamos el documento
		return document;
	 }
	
	// Método que obtiene un documento de un archivo
	// y lo añade al indexador (Lo indexa)
	private void indexFile(File archivo) throws IOException{
		System.out.println("Indexando "+archivo.getCanonicalPath());
		Document document = getDocument(archivo);
		escritor.addDocument(document);
	}
	
	// Método que crea el índice.
	// Recibe una ruta y un filtro de archivos .txt
	public int createIndex(String rutaDirectorioDatos, FileFilter filtro) throws IOException{
		
		// Obtenemos todos los archivos en la ruta indicada
		File[] archivos = new File(rutaDirectorioDatos).listFiles();
		
		/* Para cada archivo nos aseguramos de que:
		   - No es un directorio
		   - No está oculto
		   - Existe
		   - Se puede leer
		   - Es un archivo .txt   */
		for (File archivo : archivos) {
			 if(!archivo.isDirectory() && !archivo.isHidden() && archivo.exists() && archivo.canRead() && archivo.accept(file)){
				 indexFile(file); // Lo indexa con el método anterior
			 }
		}
		// Retornamos el numero de documentos indexados
		return escritor.numDocs(); 
	}
}