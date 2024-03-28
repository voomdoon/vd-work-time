package de.voomdoon.worktime.overlay;

import de.voomdoon.util.cli.Program;
import de.voomdoon.worktime.adapter.file.observer.RawDirectoryObserver;
import de.voomdoon.worktime.adapter.file.observer.RawDirectoryObserverImpl;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
public class OverlayProgram extends Program {

	/**
	 * @param args
	 * @since 0.1.0
	 */
	public static void main(String[] args) {
		Program.run(args);
	}

	/**
	 * @since 0.1.0
	 */
	@Override
	protected void runProgram() throws Exception {
		String input = pollArg("input");
		RawDirectoryObserver observer = new RawDirectoryObserverImpl(input);
		Overlay overlay = new Overlay(observer);
		overlay.start();
	}
}
