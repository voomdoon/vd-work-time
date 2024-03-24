package de.voomdoon.worktime.adapter.file;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.voomdoon.testing.logging.tests.LoggingCheckingTestBase;
import de.voomdoon.worktime.model.RawWork;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
class RawDirectoryReaderTest {

	/**
	 * DOCME add JavaDoc for RawFileReaderTest
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	@Nested
	class ReadDirectoryTest extends LoggingCheckingTestBase {

		/**
		 * @since 0.1.0
		 */
		private RawDirectoryReader reader = new RawDirectoryReader();

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_file() throws Exception {
			logTestStart();

			RawWork actual = readDirectory("single/1.txt");

			assertThat(actual).extracting(RawWork::days)//
					.asInstanceOf(InstanceOfAssertFactories.LIST).hasSize(1);
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_single() throws Exception {
			logTestStart();

			RawWork actual = readDirectory("single");

			assertThat(actual).extracting(RawWork::days)//
					.asInstanceOf(InstanceOfAssertFactories.LIST).hasSize(1);
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_subDirectory() throws Exception {
			logTestStart();

			RawWork actual = readDirectory("subDirectory");

			assertThat(actual).extracting(RawWork::days)//
					.asInstanceOf(InstanceOfAssertFactories.LIST).hasSize(2);
		}

		/**
		 * DOCME add JavaDoc for method readDirectory
		 * 
		 * @param string
		 * @return
		 * @throws IOException
		 * @since 0.1.0
		 */
		private RawWork readDirectory(String string) throws IOException {
			Path file = Path.of("src/test/resources/raw/directory/" + string);

			return reader.readDirectory(file);
		}
	}
}
