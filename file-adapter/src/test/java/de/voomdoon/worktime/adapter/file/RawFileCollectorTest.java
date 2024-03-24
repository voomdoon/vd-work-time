package de.voomdoon.worktime.adapter.file;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Collection;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.voomdoon.testing.logging.tests.LoggingCheckingTestBase;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
class RawFileCollectorTest {

	/**
	 * DOCME add JavaDoc for RawFileCollectorTest
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	@Nested
	class FindFilesTest extends LoggingCheckingTestBase {

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_directory() throws Exception {
			logTestStart();

			RawFileCollector collector = new RawFileCollector();

			Collection<Path> actuals = collector.findFiles(Path.of("src/test/resources/raw/directory/single"));

			assertThat(actuals).containsExactly(Path.of("src/test/resources/raw/directory/single/1.txt"));
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_directoryWithSubDirectory() throws Exception {
			logTestStart();

			RawFileCollector collector = new RawFileCollector();

			Collection<Path> actuals = collector.findFiles(Path.of("src/test/resources/raw/directory/subDirectory"));

			assertThat(actuals).containsExactly(Path.of("src/test/resources/raw/directory/subDirectory/single1/1.txt"),
					Path.of("src/test/resources/raw/directory/subDirectory/single2/1.txt"));
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_error_FileNotFoundException() throws Exception {
			logTestStart();

			RawFileCollector collector = new RawFileCollector();

			assertThrows(FileNotFoundException.class,
					() -> collector.findFiles(Path.of("src/test/resources/raw/not-existing.txt")));
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_file() throws Exception {
			logTestStart();

			RawFileCollector collector = new RawFileCollector();

			Collection<Path> actuals = collector.findFiles(Path.of("src/test/resources/raw/1day_1section.txt"));

			assertThat(actuals).containsExactly(Path.of("src/test/resources/raw/1day_1section.txt"));
		}
	}
}
