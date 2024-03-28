package de.voomdoon.worktime.model;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
public enum Flag {

	/**
	 * @since 0.1.0
	 */
	FREE("free"),

	/**
	 * @since 0.1.0
	 */
	HOME_OFFICE("home office"),

	/**
	 * de: Feiertag
	 * 
	 * @since 0.1.0
	 */
	PUBLIC_HOLIDAY("public holiday"),

	/**
	 * @since 0.1.0
	 */
	SICK("sick"),

	/**
	 * @since 0.1.0
	 */
	UNPAID("unpaid"),

	/**
	 * de: Urlaub
	 * 
	 * @since 0.1.0
	 */
	VACATION("vacation")

	;

	/**
	 * @since 0.1.0
	 */
	private String name;

	/**
	 * DOCME add JavaDoc for constructor Flag
	 * 
	 * @param name
	 * @since 0.1.0
	 */
	Flag(String name) {
		this.name = name;
	}

	/**
	 * DOCME add JavaDoc for method getName
	 * 
	 * @return
	 * @since 0.1.0
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            name
	 * @since 0.1.0
	 */
	public void setName(String name) {
		this.name = name;
	}
}
