package nene.util;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class FileUtil {

	public List<String> fileList(String directory) throws Exception {

		// String directory = "C:\\Users\\72495\\.nenes\\workspace";

		List<String> fileNames = new ArrayList<>();
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directory))) {
			for (Path path : directoryStream) {
				fileNames.add(path.getFileName().toString());
				//System.out.println(path.toString());
				//System.out.println(path.getFileName());
			}
		} catch (IOException ex) {
		}

		return fileNames;

	}

}
