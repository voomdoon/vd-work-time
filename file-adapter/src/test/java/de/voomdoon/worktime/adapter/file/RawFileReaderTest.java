package de.voomdoon.worktime.adapter.file;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import de.voomdoon.testing.logging.tests.LoggingCheckingTestBase;
import de.voomdoon.worktime.model.Flag;
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
class RawFileReaderTest {

	/**
	 * DOCME add JavaDoc for RawFileReaderTest
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	@Nested
	class ReadFileTest extends ReadFileTestBase {

		/**
		 * DOCME add JavaDoc for RawFileReaderTest.ReadFileTest
		 *
		 * @author André Schulz
		 *
		 * @since 0.1.0
		 */
		abstract class FlagTestBase extends ReadFileTestBase {

			/**
			 * DOCME add JavaDoc for method getScope
			 * 
			 * @return
			 * @since 0.1.0
			 */
			protected abstract String getScope();

			/**
			 * @since 0.1.0
			 */
			@ParameterizedTest
			@EnumSource(Flag.class)
			void testSingle(Flag flag) throws Exception {
				logTestStart();

				RawWork actual = readFile("flag/single/" + getScope() + "/" + flag.getName() + ".txt");

				assertThat(actual).extracting(RawWork::days).asInstanceOf(InstanceOfAssertFactories.LIST)
						.flatExtracting(getScope() + "DayFlags").containsExactly(flag);
			}
		}

		/**
		 * DOCME add JavaDoc for RawFileReaderTest.ReadFileTest
		 *
		 * @author André Schulz
		 *
		 * @since 0.1.0
		 */
		@Nested
		class FullDayFlagTest extends FlagTestBase {

			/**
			 * @since 0.1.0
			 */
			@Override
			protected String getScope() {
				return "full";
			}

			/**
			 * @since 0.1.0
			 */
			@Test
			void testMultiple() throws Exception {
				logTestStart();

				RawWork actual = readFile("flag/multiple/home office and public holiday.txt");

				assertThat(actual).extracting(RawWork::days).asInstanceOf(InstanceOfAssertFactories.LIST)
						.flatExtracting(getScope() + "DayFlags")
						.containsExactlyInAnyOrder(Flag.HOME_OFFICE, Flag.PUBLIC_HOLIDAY);
			}
		}

		/**
		 * @author André Schulz
		 *
		 * @since 0.1.0
		 */
		@Nested
		class HalfDayFlagTest extends FlagTestBase {

			/**
			 * @since 0.1.0
			 */
			@Override
			protected String getScope() {
				return "half";
			}
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_1day() throws Exception {
			logTestStart();

			RawWork actual = readFile("1day_1section.txt");

			assertThat(actual).extracting(RawWork::days)//
					.asInstanceOf(InstanceOfAssertFactories.LIST).hasSize(1);
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_1day_1section() throws Exception {
			logTestStart();

			RawWork actual = readFile("1day_1section.txt");

			assertThat(actual).extracting(RawWork::days)//
					.asInstanceOf(InstanceOfAssertFactories.LIST).anySatisfy(day -> {
						assertThat((RawDay) day).extracting(RawDay::sections)
								.asInstanceOf(InstanceOfAssertFactories.LIST).hasSize(1);
					});
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_1day_1sectionEnd() throws Exception {
			logTestStart();

			RawWork actual = readFile("1day_1section.txt");

			assertThat(actual).extracting(RawWork::days)//
					.asInstanceOf(InstanceOfAssertFactories.LIST).anySatisfy(day -> {
						assertThat((RawDay) day).extracting(RawDay::sections)
								.asInstanceOf(InstanceOfAssertFactories.LIST).anySatisfy(section -> {
									assertThat((RawSection) section).extracting(RawSection::end)
											.isEqualTo(LocalTime.of(12, 10));
								});
					});
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_1day_1sectionEndMissing() throws Exception {
			logTestStart();

			RawWork actual = readFile("1day_1section_noEnd.txt");

			assertThat(actual).extracting(RawWork::days)//
					.asInstanceOf(InstanceOfAssertFactories.LIST).anySatisfy(day -> {
						assertThat((RawDay) day).extracting(RawDay::sections)
								.asInstanceOf(InstanceOfAssertFactories.LIST).anySatisfy(section -> {
									assertThat((RawSection) section).extracting(RawSection::end).isNull();
								});
					});
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_1day_1sectionFactor() throws Exception {
			logTestStart();

			RawWork actual = readFile("1day_1section.txt");

			assertThat(actual).extracting(RawWork::days)//
					.asInstanceOf(InstanceOfAssertFactories.LIST).anySatisfy(day -> {
						assertThat((RawDay) day).extracting(RawDay::sections)
								.asInstanceOf(InstanceOfAssertFactories.LIST).anySatisfy(section -> {
									assertThat((RawSection) section).extracting(RawSection::factor).isEqualTo(1.0);
								});
					});
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_1day_1sectionStart() throws Exception {
			logTestStart();

			RawWork actual = readFile("1day_1section.txt");

			assertThat(actual).extracting(RawWork::days)//
					.asInstanceOf(InstanceOfAssertFactories.LIST).anySatisfy(day -> {
						assertThat((RawDay) day).extracting(RawDay::sections)
								.asInstanceOf(InstanceOfAssertFactories.LIST).anySatisfy(section -> {
									assertThat((RawSection) section).extracting(RawSection::start)
											.isEqualTo(LocalTime.of(12, 0));
								});
					});
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_1day_date() throws Exception {
			logTestStart();

			RawWork actual = readFile("1day_1section.txt");

			// TODO decide to one version

			assertThat(actual).extracting(RawWork::days)//
					.asInstanceOf(InstanceOfAssertFactories.LIST).anySatisfy(day -> {
						assertThat((RawDay) day).extracting(RawDay::date).isEqualTo(DAY1);
					});

			assertThat(actual).extracting(RawWork::days, InstanceOfAssertFactories.LIST).anySatisfy(day -> {
				assertThat((RawDay) day).extracting(RawDay::date).isEqualTo(DAY1);
			});

			assertThat(actual).extracting(RawWork::days).asInstanceOf(InstanceOfAssertFactories.LIST).extracting("date")
					.containsExactly(DAY1);
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_2days_date() throws Exception {
			logTestStart();

			RawWork actual = readFile("2days_1section.txt");

			assertThat(actual).extracting(RawWork::days)//
					.asInstanceOf(InstanceOfAssertFactories.LIST).satisfiesExactly(day -> {
						assertThat((RawDay) day).extracting(RawDay::date).isEqualTo(DAY1);
					}, day -> {
						assertThat((RawDay) day).extracting(RawDay::date).isEqualTo(DAY2);
					});
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_empty() throws Exception {
			logTestStart();

			RawWork actual = readFile("empty.txt");

			assertThat(actual).extracting(RawWork::days).asInstanceOf(InstanceOfAssertFactories.LIST).isEmpty();
		}
	}

	/**
	 * DOCME add JavaDoc for RawFileReaderTest
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	private abstract class ReadFileTestBase extends LoggingCheckingTestBase {

		/**
		 * @since 0.1.0
		 */
		protected static final LocalDate DAY1 = LocalDate.of(2024, 1, 1);

		/**
		 * @since 0.1.0
		 */
		protected static final LocalDate DAY2 = LocalDate.of(2024, 1, 2);

		/**
		 * @since 0.1.0
		 */
		private RawFileReader reader = new RawFileReader();

		/**
		 * DOCME add JavaDoc for method readFile
		 * 
		 * @param string
		 * @return
		 * @throws IOException
		 * @since 0.1.0
		 */
		protected RawWork readFile(String string) throws IOException {
			Path file = Path.of("src/test/resources/raw/" + string);

			return reader.readFile(file);
		}
	}
}
