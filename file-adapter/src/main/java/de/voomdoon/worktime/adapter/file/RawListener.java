package de.voomdoon.worktime.adapter.file;

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
public interface RawListener {

	/**
	 * DOCME add JavaDoc for method sectionEnded
	 * 
	 * @param section
	 * @param day
	 * @param work
	 * @since 0.1.0
	 */
	void notifySectionEnded(RawSection section, RawDay day, RawWork work);

	/**
	 * DOCME add JavaDoc for method sectionEnded
	 * 
	 * @param section
	 * @param day
	 * @param work
	 * @since 0.1.0
	 */
	void notifySectionStarted(RawSection section, RawDay day, RawWork work);
}
