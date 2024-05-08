package de.voomdoon.worktime.adapter.file.observer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.voomdoon.testing.logging.tests.LoggingCheckingTestBase;
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
class RawObserverTest extends LoggingCheckingTestBase {

	/**
	 * DOCME add JavaDoc for RawObserverTest
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	@Nested
	class Register extends LoggingCheckingTestBase {

		/**
		 * @since 0.1.0
		 */
		private static final String ANY_FILE = "src/test/resources/raw/1day_1section.txt";
		/**
		 * @since 0.1.0
		 */
		private RawObserver observer;

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_error_NPE() throws Exception {
			logTestStart();

			observer = new RawObserverImpl(ANY_FILE);

			NullPointerException actual = assertThrows(NullPointerException.class,
					() -> observer.register(null, INTERVAL));
			assertThat(actual).hasMessageContaining("listener");
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_result() throws Exception {
			logTestStart();

			observer = new RawObserverImpl(ANY_FILE);

			RawListener listener = new NoOpRawListener() {

				@Override
				public void notifySectionEnded(RawSection section, RawDay day, RawWork work) {
					// nothing to do
				}
			};

			RawWork actual = observer.register(listener, INTERVAL);

			assertThat(actual).extracting(RawWork::days)//
					.asInstanceOf(InstanceOfAssertFactories.LIST).hasSize(1);
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_success() throws Exception {
			logTestStart();

			observer = new RawObserverImpl(ANY_FILE);

			RawListener listener = new NoOpRawListener() {

				@Override
				public void notifySectionEnded(RawSection section, RawDay day, RawWork work) {
					// nothing to do
				}
			};

			assertDoesNotThrow(() -> observer.register(listener, INTERVAL));
		}
	}

	/**
	 * DOCME add JavaDoc for RawObserverTest
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	private abstract static class NoOpRawListener implements RawListener {

		@Override
		public void notifySectionEnded(RawSection section, RawDay day, RawWork work) {
			// nothing to do
		}

		@Override
		public void notifySectionStarted(RawSection section, RawDay day, RawWork work) {
			// nothing to do
		}
	}

	/**
	 * @since 0.1.0
	 */
	private static final boolean APPEND = true;

	/**
	 * [ms]
	 * 
	 * @since 0.1.0
	 */
	private static final int INTERVAL = 1000;

	/**
	 * @since 0.1.0
	 */
	private RawObserverImpl observer;

	/**
	 * @since 0.1.0
	 */
	@Test
	void test_consecutiveChanges() throws Exception {
		logTestStart();

		String fileName = getTempDirectory() + "/file.txt";
		BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
		bw.write("2024-01-01\n");
		bw.write("12:00\t");
		bw.close();

		observer = new RawObserverImpl(fileName);

		List<RawSection> sectionsEnded = new ArrayList<>();
		AtomicReference<RawSection> sectionStartedReference = new AtomicReference<>();

		RawListener listener = new NoOpRawListener() {

			@Override
			public void notifySectionEnded(RawSection section, RawDay day, RawWork work) {
				sectionsEnded.add(section);
			}

			@Override
			public void notifySectionStarted(RawSection section, RawDay day, RawWork work) {
				sectionStartedReference.set(section);
			}
		};

		observer.register(listener, INTERVAL);

		logger.trace("write...");
		bw = new BufferedWriter(new FileWriter(fileName, APPEND));
		bw.write("13:00\n");
		bw.close();
		logger.trace("write done");

		await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
			assumeThat(sectionsEnded).hasSize(1).element(0).extracting(RawSection::end)
					.isEqualTo(LocalTime.parse("13:00"));
		});

		logger.trace("write...");
		bw = new BufferedWriter(new FileWriter(fileName, APPEND));
		bw.write("14:00\t\n");
		bw.close();
		logger.trace("write done");

		await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(sectionStartedReference.get()).extracting(RawSection::start).isEqualTo(LocalTime.parse("14:00"));
		});

		assertThat(sectionsEnded).hasSize(1);
	}

	/**
	 * @since 0.1.0
	 */
	@Test
	void test_listener_notifySectionEnded() throws Exception {
		logTestStart();

		String fileName = getTempDirectory() + "/file.txt";
		BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
		bw.write("2024-01-01\n");
		bw.write("12:00\t");
		bw.close();

		observer = new RawObserverImpl(fileName);

		AtomicReference<RawSection> sectionReference = new AtomicReference<>();

		RawListener listener = new NoOpRawListener() {

			@Override
			public void notifySectionEnded(RawSection section, RawDay day, RawWork work) {
				sectionReference.set(section);
			}
		};

		observer.register(listener, 1000);

		bw = new BufferedWriter(new FileWriter(fileName, APPEND));
		bw.write("13:00\n");
		bw.close();

		await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(sectionReference.get()).extracting(RawSection::end).isEqualTo(LocalTime.parse("13:00"));
		});
	}

	/**
	 * @since 0.1.0
	 */
	@Test
	void test_listener_notifySectionStarted() throws Exception {
		logTestStart();

		String fileName = getTempDirectory() + "/file.txt";
		BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
		bw.write("2024-01-01\n");
		bw.write("12:00\t13:00\n");
		bw.close();

		observer = new RawObserverImpl(fileName);

		AtomicReference<RawSection> sectionReference = new AtomicReference<>();

		RawListener listener = new NoOpRawListener() {

			@Override
			public void notifySectionStarted(RawSection section, RawDay day, RawWork work) {
				sectionReference.set(section);
			}
		};

		observer.register(listener, 1000);

		bw = new BufferedWriter(new FileWriter(fileName, APPEND));
		bw.write("14:00\t");
		bw.close();

		await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(sectionReference.get()).extracting(RawSection::start).isEqualTo(LocalTime.parse("14:00"));
		});
	}
}
