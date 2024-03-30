package de.voomdoon.worktime.adapter.file.observer;

import de.voomdoon.worktime.model.RawWork;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
public interface RawObserver {

	/**
	 * @since 0.1.0
	 * @deprecated XXX temporary solution until observer is polling itself
	 */
	@Deprecated
	public void run();

	/**
	 * DOCME add JavaDoc for method register
	 * 
	 * @param listener
	 * @since 0.1.0
	 */
	RawWork register(RawListener listener);
}