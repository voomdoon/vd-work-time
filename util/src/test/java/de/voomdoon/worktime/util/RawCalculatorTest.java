package de.voomdoon.worktime.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import de.voomdoon.testing.tests.TestBase;
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
class RawCalculatorTest extends TestBase {

	/**
	 * @since 0.1.0
	 */
	private static final LocalDate DAY1 = LocalDate.of(2024, 3, 1);

	/**
	 * @since 0.1.0
	 */
	private RawCalculator summarizer = new RawCalculator();

	/**
	 * @since 0.1.0
	 */
	@Test
	void test_empty() throws Exception {
		logTestStart();

		RawWork work = new RawWork(List.of());

		int actual = summarizer.getDaySum(work, DAY1, LocalTime.now());

		assertThat(actual).isZero();
	}

	/**
	 * @since 0.1.0
	 */
	@Test
	void test_matchingDay() throws Exception {
		logTestStart();

		RawSection section = new RawSection(LocalTime.of(12, 0), LocalTime.of(13, 0), 1);
		RawDay day = new RawDay(DAY1, List.of(section), Set.of(), Set.of());
		RawWork work = new RawWork(List.of(day));

		int actual = summarizer.getDaySum(work, DAY1, LocalTime.now());

		assertThat(actual).isEqualTo(3600);
	}

	/**
	 * @since 0.1.0
	 */
	@Test
	void test_openEnd() throws Exception {
		logTestStart();

		RawSection section = new RawSection(LocalTime.of(12, 0), null, 1);
		RawDay day = new RawDay(DAY1, List.of(section), Set.of(), Set.of());
		RawWork work = new RawWork(List.of(day));

		int actual = summarizer.getDaySum(work, DAY1, LocalTime.of(13, 0));

		assertThat(actual).isEqualTo(3600);
	}
}
