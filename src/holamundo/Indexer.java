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
 private IndexWriter writer;
 public Indexer(String indexDirectoryPath) throws IOException{
	 //este directorio contendrá los índices
	 Directory indexDirectory =
	 FSDirectory.open(new File(indexDirectoryPath));
	 //creando el indexador
	 writer = new IndexWriter(indexDirectory, new StandardAnalyzer(Version.LUCENE_36),true,	 IndexWriter.MaxFieldLength.UNLIMITED);
	 }
	 public void close() throws CorruptIndexException, IOException{
	 writer.close();
	 }
	 private Document getDocument(File file) throws IOException{
	 Document document = new Document();
	 //indexando el contenido de los archivos
	 Field contentField = new Field(LuceneConstants.CONTENTS,
	 new FileReader(file));
	 //indexando nombre del archivo
	 Field fileNameField = new Field(LuceneConstants.FILE_NAME,
	 file.getName(),
	 Field.Store.YES,Field.Index.NOT_ANALYZED);
	 //ruta del archivo de índice
	 Field filePathField = new Field(LuceneConstants.FILE_PATH,
	 file.getCanonicalPath(),
	 Field.Store.YES,Field.Index.NOT_ANALYZED);
	 document.add(contentField);
	 document.add(fileNameField);
	 document.add(filePathField);
	 return document;
	 }
	 private void indexFile(File file) throws IOException{
	 System.out.println("Indexando "+file.getCanonicalPath());
	 Document document = getDocument(file);
	 writer.addDocument(document);
	 }
	 public int createIndex(String dataDirPath, FileFilter filter)
	 throws IOException{
	 //obtener todos los archivos en el directorio de datos
	 File[] files = new File(dataDirPath).listFiles();
	 for (File file : files) {
	 if(!file.isDirectory()
	 && !file.isHidden()
	 && file.exists()
	 && file.canRead()
	 && filter.accept(file)
	 ){
	 indexFile(file);
	 }
	 }
	 return writer.numDocs();
	 }
	}