/**
 * Created the 15 janv. 2015 at 08:55:12
 * by bodereau
 * 
 */
package com.naio.canreader.utils;

/**
 * BytesFunction provides functions that convert Integer into twoComplement
 * numbers and a function for add '0' to a binary String
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
	
	public static String fillWithZeroTheBinaryString(String binaryText){
		switch (binaryText.length()) {
		case 0:
			binaryText = '0' + binaryText;
		case 1:
			binaryText = '0' + binaryText;
		case 2:
			binaryText = '0' + binaryText;
		case 3:
			binaryText = '0' + binaryText;
		case 4:
			binaryText = '0' + binaryText;
		case 5:
			binaryText = '0' + binaryText;
		case 6:
			binaryText = '0' + binaryText;
		case 7:
			binaryText = '0' + binaryText;
		default:
			break;
		}
		return binaryText;
	}
	

}
