package de.voomdoon.worktime.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since DOCME add inception version number
 */
public record RawDay(LocalDate date, List<RawSection> sections, Set<Flag> fullDayFlags, Set<Flag> halfDayFlags) {

	/**
	 * DOCME add JavaDoc for constructor RawDay
	 * 
	 * @param date
	 * @param sections
	 * @since DOCME add inception version number
	 * @deprecated
	 */
	@Deprecated
	public RawDay(LocalDate date, List<RawSection> sections) {
		this(date, sections, Set.of(), Set.of());
	}
}
