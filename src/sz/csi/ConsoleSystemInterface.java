package sz.csi;

import sz.util.Position;

public interface ConsoleSystemInterface {
	
	/**
	 * Prints a character on the console
	 * @param x
	 * @param y
	 * @param what The character to be printed
	 * @param color The color, one of the ConsoleSystemInterface constants
	 */
	public void print(int x, int y, char what, int color);

	/**
	 * Same as print but must check for validity of the coordinates
	 * @param x
	 * @param y
	 * @param what The character to be printed
	 * @param color The color, one of the ConsoleSystemInterface constants
	 */
	public void safeprint(int x, int y, char what, int color);

	/**
	 * Prints a String on the console
	 * @param x
	 * @param y
	 * @param what The string to be printed
	 * @param color The color, one of the ConsoleSystemInterface constants
	 */
	public void print(int x, int y, String what, int color);

	/**
	 * Prints a String on the console with the default color
	 * @param x
	 * @param y
	 * @param what The String to be printed
	 */
	public void print(int x, int y, String what);

	/**
	 * Checks what character is at a given position
	 * @param x
	 * @param y
	 * @return The character at the x,y position
	 */
	public char peekChar(int x, int y);

	/**
	 * Checks what color is at a given position
	 * @param x
	 * @param y
	 * @return The color at the x,y position
	 */
	public int peekColor(int x, int y);

	/**
	 * Waits until a key is pressed and returns it 
	 * @return The key that was pressed
	 */
	public CharKey inkey();

	/**
	 * Locates the input caret on a given position
	 * @param x
	 * @param y
	 */
	public void locateCaret(int x, int y);

	/**
	 * Reads a string from the keyboard
	 * @return The String that was read after pressing enter
	 */
	public String input();

	/**
	 * Reads a string from the keyboard with a maximum length
	 * @return The String that was read after pressing enter
	 */
	public String input(int length);

	/**
	 * Checks if the position is valid
	 * @param e
	 * @return true if the position is valid
	 */
	public boolean isInsideBounds(Position e);

	/**
	 * Clears the screen
	 */
	public void cls();

	/**
	 * Refreshes the screen, printing all characters that were buffered
	 * 
	 * Some implementations may instead write directly to the console
	 */
	public void refresh();

	/**
	 * Refreshes the screen, printing all characters that were buffered, and interrupts the Thread
	 * 
	 * Some implementations may instead write directly to the console
	 */
	public void refresh(Thread t);

	/**
	 * Makes the screen flash with a given color
	 * @param color
	 */
	public void flash(int color);

	/**
	 * Sets whether or not a buffer will be used 
	 * @param value
	 */
	public void setAutoRefresh(boolean value);

	/**
	 * Waits for the user to press a key
	 * @param keyCode
	 */
	public void waitKey(int keyCode);

	/**
	 * Saves the screen contents to a backup buffer
	 *
	 */
	public void saveBuffer();
	/**
	 * Restores the contents of the backup buffer to screen
	 *
	 */
	public void restore();

}