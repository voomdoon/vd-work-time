package de.voomdoon.worktime.adapter.file.observer;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.voomdoon.logging.LogManager;
import de.voomdoon.logging.Logger;
import de.voomdoon.worktime.adapter.file.RawDirectoryReader;
import de.voomdoon.worktime.adapter.file.RawFileCollector;
import de.voomdoon.worktime.model.RawDay;
import de.voomdoon.worktime.model.RawSection;
import de.voomdoon.worktime.model.RawWork;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
public class RawObserverImpl implements RawObserver {

	/**
	 * @since 0.1.0
	 */
	private Map<Path, RawWork> fileWorks;

	/**
	 * @since 0.1.0
	 */
	private RawWork globalWork;

	/**
	 * @since 0.1.0
	 */
	private Path input;

	/**
	 * @since 0.1.0
	 */
	private RawListener listener;

	/**
	 * @since 0.1.0
	 */
	private Logger logger = LogManager.getLogger(getClass());

	/**
	 * @since 0.1.0
	 */
	private RawDirectoryReader reader = new RawDirectoryReader();

	/**
	 * @since 0.1.0
	 */
	private Map<WatchKey, Path> watchKeys;

	/**
	 * @since 0.1.0
	 */
	private WatchService watchService;

	/**
	 * DOCME add JavaDoc for constructor RawObserver
	 * 
	 * @param input
	 * @throws IOException
	 * @since 0.1.0
	 */
	public RawObserverImpl(String input) throws IOException {
		this.input = Path.of(input);

		fileWorks = new HashMap<>();

		watchService = FileSystems.getDefault().newWatchService();
		Collection<Path> files = new RawFileCollector().findFiles(this.input);

		List<Path> directories = files.stream().map(Path::getParent).distinct().toList();
		watchKeys = new HashMap<>();

		for (Path directory : directories) {
			logger.debug("register for " + directory);
			WatchKey watchKey = directory.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
			logger.trace("watchKey: " + watchKey);
			watchKeys.put(watchKey, directory);

			for (File file : directory.toFile().listFiles()) {
				if (file.isFile()) {
					fileWorks.put(file.toPath(), reader.readDirectory(file.toPath()));
				}
			}
		}

		logger.debug("fileWorks: " + fileWorks);
	}

	/**
	 * @since 0.1.0
	 */
	@Override
	public RawWork register(RawListener listener) {
		Objects.requireNonNull(listener, "listener");
		this.listener = listener;
		// TODO implement register

		try {
			globalWork = reader.readDirectory(input);// TOTO rework
		} catch (IOException e) {
			// TODO implement error handling
			throw new RuntimeException("Error at 'register': " + e.getMessage(), e);
		}

		return globalWork;
	}

	/**
	 * DOCME add JavaDoc for method run
	 * 
	 * @since 0.1.0
	 */
	void run() {
		WatchKey watchKey;

		while ((watchKey = watchService.poll()) != null) {
			logger.debug("watchKey: " + watchKey);
			List<WatchEvent<?>> events = watchKey.pollEvents();
			Path directory = watchKeys.get(watchKey);
			logger.debug("directory: " + directory);
			logger.debug("events: " + events.stream().map(e -> e.context()).toList());
			List<Path> files = events.stream().map(e -> Path.of(directory.toString(), e.context().toString())).toList();
			logger.debug("files: " + files);

			for (Path file : files) {
				processFile(file);
			}
		}

		// TODO implement run
	}

	/**
	 * DOCME add JavaDoc for method process
	 * 
	 * @param file
	 * @param currentDay
	 * @param newDay
	 * @param newWork
	 * @since 0.1.0
	 */
	private void processDay(Path file, RawDay currentDay, RawDay newDay, RawWork newWork) {
		int iSection = 0;

		while (iSection < currentDay.sections().size()) {
			RawSection currSection = currentDay.sections().get(iSection);
			RawSection newSection = newDay.sections().get(iSection);
			logger.debug("currSection: " + currSection);
			logger.debug("newSection:  " + newSection);

			if (!newSection.equals(currSection)) {
				processSection(file, currSection, newSection, newDay, newWork);
			}

			iSection++;
		}

		if (newDay.sections().size() > currentDay.sections().size()) {
			RawSection newSection = newDay.sections().get(iSection);
			logger.debug("newSection:  " + newSection);
			publishSectionStarted(newDay, newWork, newSection);
		}
	}

	/**
	 * DOCME add JavaDoc for method processFile
	 * 
	 * @param file
	 * @since 0.1.0
	 */
	private void processFile(Path file) {
		logger.debug("processFile: " + file);
		RawWork currentWork = fileWorks.get(file);
		logger.debug("currWork: " + currentWork);
		RawWork newWork;
		try {
			newWork = reader.readDirectory(file);
		} catch (IOException e) {
			// TODO implement error handling
			throw new RuntimeException("Error at 'processFile': " + e.getMessage(), e);
		}
		logger.debug("newWork:     " + newWork);

		processFileWork(file, currentWork, newWork);
		// TODO implement processFile
	}

	/**
	 * DOCME add JavaDoc for method processFile
	 * 
	 * @param file
	 * @param currentWork
	 * @param newWork
	 * @since 0.1.0
	 */
	private void processFileWork(Path file, RawWork currentWork, RawWork newWork) {
		int iDay = 0;

		while (iDay < currentWork.days().size()) {
			RawDay currDay = currentWork.days().get(iDay);
			RawDay newDay = newWork.days().get(iDay);
			logger.debug("currDay: " + currDay);
			logger.debug("newDay:  " + newDay);

			processDay(file, currDay, newDay, newWork);

			iDay++;
		}
		// TODO implement processFile
	}

	/**
	 * DOCME add JavaDoc for method process
	 * 
	 * @param file
	 * @param currSection
	 * @param newSection
	 * @param newDay
	 * @param newWork
	 * @since 0.1.0
	 */
	private void processSection(Path file, RawSection currSection, RawSection newSection, RawDay newDay,
			RawWork newWork) {
		if (currSection.end() == null || newSection.end() == null) {
			publishSectionEnded(file, currSection, newSection, newDay, newWork);
		}
	}

	/**
	 * DOCME add JavaDoc for method publishSectionEnded
	 * 
	 * @param file
	 * @param currSection
	 * @param newSection
	 * @param newDay
	 * @param newWork
	 * @since 0.1.0
	 */
	private void publishSectionEnded(Path file, RawSection currSection, RawSection newSection, RawDay newDay,
			RawWork newWork) {
		listener.notifySectionEnded(newSection, newDay, newWork);
		// TODO implement publishSectionEnded
	}

	/**
	 * DOCME add JavaDoc for method publishSectionStarted
	 * 
	 * @param newDay
	 * @param newWork
	 * @param newSection
	 * @since 0.1.0
	 */
	private void publishSectionStarted(RawDay newDay, RawWork newWork, RawSection newSection) {
		listener.notifySectionStarted(newSection, newDay, newWork);
	}
}
