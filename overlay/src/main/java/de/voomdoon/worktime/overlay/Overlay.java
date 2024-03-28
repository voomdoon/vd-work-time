package de.voomdoon.worktime.overlay;

import java.io.IOException;
import java.nio.file.Path;

import de.voomdoon.logging.LogManager;
import de.voomdoon.logging.Logger;
import de.voomdoon.worktime.adapter.file.RawDirectoryReader;
import de.voomdoon.worktime.adapter.file.observer.RawObserver;
import de.voomdoon.worktime.adapter.file.observer.RawListener;
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
public class Overlay {

	/**
	 * DOCME add JavaDoc for Overlay
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	private class Listener implements RawListener {

		/**
		 * @since 0.1.0
		 */
		@Override
		public void notifySectionEnded(RawSection section, RawDay day, RawWork work) {
			// TODO implement notifySectionEnded
			throw new UnsupportedOperationException("'notifySectionEnded' not implemented at 'RawListener'!");
		}

		/**
		 * @since 0.1.0
		 */
		@Override
		public void notifySectionStarted(RawSection section, RawDay day, RawWork work) {
			// TODO implement notifySectionStarted
			throw new UnsupportedOperationException("'notifySectionStarted' not implemented at 'RawListener'!");
		}
	}

	/**
	 * @since 0.1.0
	 */
	private String input;

	/**
	 * @since 0.1.0
	 */
	private Listener listener;

	/**
	 * @since 0.1.0
	 */
	private Logger logger = LogManager.getLogger(getClass());

	/**
	 * @since 0.1.0
	 */
	private RawObserver observer;

	/**
	 * @since 0.1.0
	 */
	private RawDirectoryReader reader = new RawDirectoryReader();

	/**
	 * DOCME add JavaDoc for constructor Overlay
	 * 
	 * @param observer
	 * @since 0.1.0
	 */
	public Overlay(RawObserver observer) {
		this.observer = observer;
	}

	/**
	 * DOCME add JavaDoc for method start
	 * 
	 * @since 0.1.0
	 */
	public void start() {
		listener = new Listener();
		RawWork work = observer.register(listener);
		logger.debug("work: " + work);
	}

	/**
	 * DOCME add JavaDoc for method stop
	 * 
	 * @since 0.1.0
	 */
	public void stop() {
		// TODO implement stop
	}

	/**
	 * DOCME add JavaDoc for method read
	 * 
	 * @return
	 * @since 0.1.0
	 */
	private RawWork read() {
		try {
			return reader.readDirectory(Path.of(input));
		} catch (IOException e) {
			// TODO implement error handling
			throw new RuntimeException("Error at 'Overlay': " + e.getMessage(), e);
		}
	}
}
