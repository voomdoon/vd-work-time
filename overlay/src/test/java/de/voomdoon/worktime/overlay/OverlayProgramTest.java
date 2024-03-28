package de.voomdoon.worktime.overlay;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
class OverlayProgramTest {

	/**
	 * DOCME add JavaDoc for OverlayProgramTest
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	@Nested
	class MainTest extends LoggingCheckingTestBase {

		/**
		 * DOCME add JavaDoc for method test
		 * 
		 * @since 0.1.0
		 */
		@Test
		void test_start_file() throws Exception {
			logTestStart();

			assertDoesNotThrow(
					() -> OverlayProgram.main(new String[] { "src/test/resources/raw/1day_1section_done.txt" }));
		}
	}
}
