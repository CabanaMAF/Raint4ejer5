package holamundo;
import java.io.File;
import java.io.FileFilter;

// Método utilizado para filtrar los archivos .txt
public class TextFileFilter implements FileFilter {
	@Override
	public boolean accept(File pathname) {
		return pathname.getName().toLowerCase().endsWith(".txt");
	}
}