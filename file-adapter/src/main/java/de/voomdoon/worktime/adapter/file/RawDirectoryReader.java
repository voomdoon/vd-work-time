package de.voomdoon.worktime.adapter.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.voomdoon.worktime.model.RawDay;
import de.voomdoon.worktime.model.RawWork;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
public class RawDirectoryReader {

	/**
	 * @since 0.1.0
	 */
	private RawFileReader fileReader = new RawFileReader();

	/**
	 * DOCME add JavaDoc for method readDirectory
	 * 
	 * @param directory
	 * @return
	 * @throws IOException
	 * @since 0.1.0
	 */
	public RawWork readDirectory(Path directory) throws IOException {
		List<RawDay> days = new ArrayList<>();

		Collection<Path> files = new RawFileCollector().findFiles(directory);

		for (Path file : files) {
			fileReader.readFile(file).days().forEach(days::add);
		}

		return new RawWork(days);
	}
}
