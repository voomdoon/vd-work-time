package de.voomdoon.worktime.adapter.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
public class RawFileCollector {

	/**
	 * DOCME add JavaDoc for method findFiles
	 * 
	 * @param directory
	 * @return
	 * @throws IOException
	 * @since 0.1.0
	 */
	public Collection<Path> findFiles(Path directory) throws IOException {
		Collection<Path> result = new ArrayList<>();

		processFileOrDirectory(directory.toFile(), result);

		return result;
	}

	/**
	 * DOCME add JavaDoc for method processDirectory
	 * 
	 * @param file
	 * @param result
	 * 
	 * @throws IOException
	 * @since 0.1.0
	 */
	private void processDirectory(File file, Collection<Path> result) throws IOException {
		for (File fileOrDirectory : file.listFiles()) {
			processFileOrDirectory(fileOrDirectory, result);
		}
	}

	/**
	 * DOCME add JavaDoc for method processFileOrDirectory
	 * 
	 * @param fileOrDirectory
	 * @param result
	 * 
	 * @throws IOException
	 * @since 0.1.0
	 */
	private void processFileOrDirectory(File fileOrDirectory, Collection<Path> result) throws IOException {
		if (fileOrDirectory.isFile()) {
			result.add(fileOrDirectory.toPath());
		} else if (fileOrDirectory.isDirectory()) {
			processDirectory(fileOrDirectory, result);
		} else {
			throw new FileNotFoundException(fileOrDirectory.toString());
		}
	}
}
