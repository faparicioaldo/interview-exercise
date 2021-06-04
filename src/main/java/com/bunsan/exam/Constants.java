package com.bunsan.exam;

import java.util.HashMap;
import java.util.Map;

public class Constants {
	public static final Map<String, String> MAP_SEGMENTS_BINARY = new HashMap<>();
	static {
		MAP_SEGMENTS_BINARY.put("1111110", "0000");
		MAP_SEGMENTS_BINARY.put("0110000", "0001");
		MAP_SEGMENTS_BINARY.put("1101101", "0010");
		MAP_SEGMENTS_BINARY.put("1111001", "0011");
		MAP_SEGMENTS_BINARY.put("0110011", "0100");
		MAP_SEGMENTS_BINARY.put("1011011", "0101");
		MAP_SEGMENTS_BINARY.put("1011111", "0110");
		MAP_SEGMENTS_BINARY.put("1110000", "0111");
		MAP_SEGMENTS_BINARY.put("1111111", "1000");
		MAP_SEGMENTS_BINARY.put("1111011", "1001");
	}
}
