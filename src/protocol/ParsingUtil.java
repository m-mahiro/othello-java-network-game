package protocol;

import protocol.packet.PacketException;

/**
 * Utility class for common parsing operations used across packet and message classes.
 */
public class ParsingUtil {

	/**
	 * Extracts the body string from a formatted string after a specified number of space-separated header fields.
	 * 
	 * @param inputString The full string to parse
	 * @param headerSize The number of space-separated header fields before the body
	 * @return The body string after the header fields
	 * @throws PacketException if the header format is invalid (not enough fields)
	 * @throws IllegalArgumentException if inputString is null or headerSize is negative
	 */
	public static String extractBody(String inputString, int headerSize) {
		if (inputString == null) {
			throw new IllegalArgumentException("inputString cannot be null");
		}
		if (headerSize < 0) {
			throw new IllegalArgumentException("headerSize cannot be negative");
		}
		
		char[] charArray = inputString.toCharArray();
		int count = 0;
		int bodyIndex = -1;
		
		for (int i = 0; i < charArray.length; i++) {
			if (charArray[i] == ' ') {
				count++;
			}
			if (count == headerSize) {
				bodyIndex = i;
				break;
			}
		}
		
		if (count != headerSize) {
			throw PacketException.invalidHeaderFormat(inputString);
		}
		
		return inputString.substring(bodyIndex + 1);
	}
}
