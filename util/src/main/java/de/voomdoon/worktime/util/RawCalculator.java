package de.voomdoon.worktime.util;

import java.time.Duration;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

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
	 * @param now
	 * @return
	 * @since 0.1.0
	 */
	public int getDaySum(RawWork work, LocalDate date, LocalTime now) {
		return work.days().stream().filter(d -> d.date().equals(date)).findAny().map(day -> sumDay(day, now)).orElse(0);
	}

	/**
	 * @param work
	 * @param date current date
	 * @param now current time
	 * @return seconds worked from Monday through {@code date}
	 * @since 0.1.0
	 */
	public int getWeekSum(RawWork work, LocalDate date, LocalTime now) {
		LocalDate monday = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

		return work.days().stream().filter(day -> !day.date().isBefore(monday) && !day.date().isAfter(date))
				.map(day -> sumDay(day, now)).reduce(0, Integer::sum);
	}

	/**
	 * DOCME add JavaDoc for method getDuration
	 * 
	 * @param section
	 * @param now
	 * @return
	 * @since 0.1.0
	 */
	private int getDuration(RawSection section, LocalTime now) {
		return (int) Duration.between(section.start(), getEnd(section, now)).toSeconds();
	}

	/**
	 * DOCME add JavaDoc for method getEnd
	 * 
	 * @param section
	 * @param now
	 * @return
	 * @since 0.1.0
	 */
	private LocalTime getEnd(RawSection section, LocalTime now) {
		if (section.end() != null) {
			return section.end();
		} else {
			return now;
		}
	}

	/**
	 * DOCME add JavaDoc for method sumDay
	 * 
	 * @param day
	 * @param now
	 * @return
	 * @since 0.1.0
	 */
	private int sumDay(RawDay day, LocalTime now) {
		return day.sections().stream().map(section -> getDuration(section, now)).reduce(0, Integer::sum);
	}
}
