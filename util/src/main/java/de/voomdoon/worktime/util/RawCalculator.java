package de.voomdoon.worktime.util;

import java.time.Duration;
import java.time.LocalDate;

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
public class RawCalculator {

	/**
	 * DOCME add JavaDoc for method getCurrentDaySum
	 * 
	 * @param work
	 * @param date
	 * @return
	 * @since 0.1.0
	 */
	public int getDaySum(RawWork work, LocalDate date) {
		return work.days().stream().filter(d -> d.date().equals(date)).findAny().map(this::sumDay).orElse(0);
	}

	/**
	 * DOCME add JavaDoc for method getDuration
	 * 
	 * @param section
	 * @return
	 * @since 0.1.0
	 */
	private int getDuration(RawSection section) {
		return (int) Duration.between(section.start(), section.end()).toSeconds();
	}

	/**
	 * DOCME add JavaDoc for method sumDay
	 * 
	 * @param day
	 * @return
	 * @since 0.1.0
	 */
	private int sumDay(RawDay day) {
		return day.sections().stream().findAny().map(this::getDuration).get();
	}
}
