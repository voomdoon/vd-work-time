package de.voomdoon.worktime.adapter.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;

import de.voomdoon.logging.LogManager;
import de.voomdoon.logging.Logger;
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
public class RawFileReader {

	/**
	 * DOCME add JavaDoc for RawFileReader
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	private class Data {

		/**
		 * @since 0.1.0
		 */
		private LocalDate date = null;

		/**
		 * @since 0.1.0
		 */
		private List<RawDay> days = new ArrayList<>();

		/**
		 * @since 0.1.0
		 */
		private Set<Flag> fullDayFlags = new HashSet<>();

		/**
		 * @since 0.1.0
		 */
		private Set<Flag> halfDayFlags = new HashSet<>();

		/**
		 * @since 0.1.0
		 */
		private List<RawSection> sections = new ArrayList<>();

		/**
		 * DOCME add JavaDoc for method closeDay
		 * 
		 * @since 0.1.0
		 */
		public void closeDay() {
			if (date != null) {
				days.add(new RawDay(date, sections, fullDayFlags, halfDayFlags));
				sections = new ArrayList<>();
			}
		}
	}

	/**
	 * @since 0.1.0
	 */
	private static final Pattern DATE_PATTERN = Pattern.compile("\\d{4}\\-\\d{2}\\-\\d{2}");

	/**
	 * @since 0.1.0
	 */
	private static final Pattern TIMES_PATTERN = Pattern
			.compile("\\d{2}:\\d{2}\\t(\\d{2}:\\d{2}(\\t@\\d{1,3}%(\\t.*)?)?)?");

	/**
	 * @since 0.1.0
	 */
	private Logger logger = LogManager.getLogger(getClass());

	/**
	 * DOCME add JavaDoc for method readFile
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @since 0.1.0
	 */
	public RawWork readFile(Path file) throws IOException {
		Queue<String> lines = new LinkedList<>(Files.readAllLines(file));

		if (lines.isEmpty()) {
			return new RawWork(List.of());
		}

		Data data = new Data();

		while (!lines.isEmpty()) {
			processNextLine(lines, data);
		}

		data.closeDay();

		return new RawWork(data.days);
	}

	/**
	 * 
	 * DOCME add JavaDoc for method isNextLineDate
	 * 
	 * @param lines
	 * @return
	 * @since 0.1.0
	 */
	private boolean isNextLineADate(Queue<String> lines) {
		return DATE_PATTERN.matcher(lines.peek()).matches();
	}

	/**
	 * DOCME add JavaDoc for method isNextLineFlag
	 * 
	 * @param lines
	 * @return
	 * @since 0.1.0
	 */
	private boolean isNextLineAFlag(Queue<String> lines) {
		return lines.peek().startsWith("• ");
	}

	/**
	 * DOCME add JavaDoc for method isNextLineTime
	 * 
	 * @param lines
	 * @return
	 * @since 0.1.0
	 */
	private boolean isNextLineATime(Queue<String> lines) {
		return TIMES_PATTERN.matcher(lines.peek()).matches();
	}

	/**
	 * DOCME add JavaDoc for method isNextLineIgnore
	 * 
	 * @param lines
	 * @return
	 * @since 0.1.0
	 */
	private boolean isNextLineToIgnore(Queue<String> lines) {
		return lines.peek().isBlank() || lines.peek().startsWith("─");
	}

	/**
	 * DOCME add JavaDoc for method parseSection
	 * 
	 * @param line
	 * @param sections
	 * @since 0.1.0
	 */
	private void parseSection(String line, List<RawSection> sections) {
		String[] split = line.split("\t");
		LocalTime start = LocalTime.parse(split[0]);
		LocalTime end = split.length == 1 ? null : LocalTime.parse(split[1]);
		double factor = 1;

		sections.add(new RawSection(start, end, factor));
	}

	/**
	 * DOCME add JavaDoc for method processFlags
	 * 
	 * @param lines
	 * @param fullDayFlags
	 * @param halfDayFlags
	 * @since 0.1.0
	 */
	private void processFlags(Queue<String> lines, Set<Flag> fullDayFlags, Set<Flag> halfDayFlags) {
		String line = lines.poll();

		for (Flag flag : Flag.values()) {
			if (line.contains(flag.getName())) {
				if (line.contains("1/2")) {
					halfDayFlags.add(flag);
				} else {
					fullDayFlags.add(flag);
				}
			}
		}
	}

	/**
	 * DOCME add JavaDoc for method processNextLine
	 * 
	 * @param lines
	 * @param data
	 * @since 0.1.0
	 */
	private void processNextLine(Queue<String> lines, Data data) {
		if (isNextLineToIgnore(lines)) {
			lines.poll();
		} else if (isNextLineADate(lines)) {
			data.closeDay();
			data.date = LocalDate.parse(lines.poll());
		} else if (isNextLineATime(lines)) {
			parseSection(lines.poll(), data.sections);
		} else if (isNextLineAFlag(lines)) {
			processFlags(lines, data.fullDayFlags, data.halfDayFlags);
		} else {
			logger.warn("unexpected line: '" + lines.poll() + "'");
		}
	}
}
