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
	 * DOCME add JavaDoc for method main
	 * 
	 * @param args
	 * @since 0.1.0
	 */
	public static void main(String[] args) {
		Program.run(args);
	}

	/**
	 * @since 0.1.0
	 */
	private String input;

	/**
	 * DOCME add JavaDoc for constructor OverlayProgram
	 * 
	 * @param args
	 * @since 0.1.0
	 */
	protected OverlayProgram(String[] args) {
		super(args);

		input = args[0];
	}

	/**
	 * @since 0.1.0
	 */
	@Override
	protected void runProgram() throws Exception {
		RawDirectoryObserver observer = new RawDirectoryObserverImpl(input);
		Overlay overlay = new Overlay(observer);
		overlay.start();
	}
}
