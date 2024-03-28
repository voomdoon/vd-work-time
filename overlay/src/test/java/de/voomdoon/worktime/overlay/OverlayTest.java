package de.voomdoon.worktime.overlay;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import de.voomdoon.testing.logging.tests.LoggingCheckingTestBase;
import de.voomdoon.worktime.adapter.file.observer.RawDirectoryObserver;
import de.voomdoon.worktime.adapter.file.observer.RawListener;
import de.voomdoon.worktime.model.RawWork;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
class OverlayTest extends LoggingCheckingTestBase {

	/**
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	private class NoOpRawDirectoryObserver implements RawDirectoryObserver {

		/**
		 * @since 0.1.0
		 */
		@Override
		public RawWork register(RawListener listener) {
			return null;
		}
	}

	/**
	 * @since 0.1.0
	 */
	private Overlay overlay;

	/**
	 * @since 0.1.0
	 */
	@AfterEach
	void afterEach_stopOverlay() {
		if (overlay != null) {
			overlay.stop();
		}
	}

	/**
	 * @since 0.1.0
	 */
	@Test
	void test_file_startStopWorks() throws Exception {
		logTestStart();

		overlay = new Overlay(new NoOpRawDirectoryObserver());

		assertDoesNotThrow(() -> {
			overlay.start();
			overlay.stop();
		});
	}

	/**
	 * @since 0.1.0
	 */
	@Test
	void testStart_registersListener() throws Exception {
		logTestStart();

		AtomicInteger registerCount = new AtomicInteger();

		RawDirectoryObserver observer = new RawDirectoryObserver() {

			@Override
			public RawWork register(RawListener listener) {
				registerCount.incrementAndGet();

				return null;
			}
		};

		overlay = new Overlay(observer);
		overlay.start();

		assertThat(registerCount).hasValue(1);
	}
}
