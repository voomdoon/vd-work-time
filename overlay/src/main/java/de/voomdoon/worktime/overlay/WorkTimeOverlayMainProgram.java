package de.voomdoon.worktime.overlay;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.voomdoon.util.cli.MainBase;
import de.voomdoon.util.cli.Program;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since DOCME add inception version number
 */
public class WorkTimeOverlayMainProgram extends MainBase {

	/**
	 * @since DOCME add inception version number
	 */
	private static final Map<String, Class<?>> MAINS;

	static {
		HashMap<String, Class<?>> map = new HashMap<>();
		map.put(null, OverlayProgram.class);
		MAINS = Collections.unmodifiableMap(map);
	}

	/**
	 * DOCME add JavaDoc for method main
	 * 
	 * @param args
	 * @since DOCME add inception version number
	 */
	public static void main(String[] args) {
		Program.run(args);
	}

	/**
	 * DOCME add JavaDoc for constructor WorkTimeOverlayMainProgram
	 * 
	 * @param args
	 * @since DOCME add inception version number
	 */
	protected WorkTimeOverlayMainProgram(String[] args) {
		super(args, MAINS);
	}

	/**
	 * @since DOCME add inception version number
	 */
	@Override
	protected String getName() {
		return "vd-work-time-overlay";
	}
}
