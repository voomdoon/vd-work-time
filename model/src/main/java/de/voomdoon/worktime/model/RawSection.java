package de.voomdoon.worktime.model;

import java.time.LocalTime;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since DOCME add inception version number
 */
public record RawSection(LocalTime start, LocalTime end, double factor) {

}
