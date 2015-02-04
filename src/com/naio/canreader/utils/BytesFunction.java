/**
 * Created the 15 janv. 2015 at 08:55:12
 * by bodereau
 * 
 */
package com.naio.canreader.utils;

/**
 * BytesFunction provides functions that convert Integer into twoComplement
 * number
 * 
 * @author bodereau
 * 
 * 
 */
public class BytesFunction {

	public static int fromTwoComplement(int value, int bitSize) {
		int shift = Integer.SIZE - bitSize;
		// shift sign into position
		int result = value << shift;
		// Java right shift uses sign extension, but only works on integers or
		// longs
		result = result >> shift;
		return result;
	}

	public static double fromTwoComplement(int valueBytes1, int valueBytes2,
			int bitSize, double factor) {
		int value = valueBytes1 * 256 + valueBytes2;
		int shift = Integer.SIZE - bitSize;
		int result = value << shift;
		result = result >> shift;
		return result / factor;
	}
	
	public static String fillWithZeroTheBinaryString(String text){
		switch (text.length()) {
		case 0:
			text = '0' + text;
		case 1:
			text = '0' + text;
		case 2:
			text = '0' + text;
		case 3:
			text = '0' + text;
		case 4:
			text = '0' + text;
		case 5:
			text = '0' + text;
		case 6:
			text = '0' + text;
		case 7:
			text = '0' + text;
		default:
			break;
		}
		return text;
	}

}
