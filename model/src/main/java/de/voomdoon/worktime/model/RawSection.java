package de.voomdoon.worktime.model;

import java.time.LocalTime;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
public record RawSection(LocalTime start, LocalTime end, double factor) {

}
