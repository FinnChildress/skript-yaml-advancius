/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package me.sashie.skriptyaml.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * String utilities.
 */
public final class StringUtil {

	private StringUtil() {
	}

	public static String replaceTabs(String text) {
		if (text.contains("\t"))
			return text.replaceAll("\t", "    ");
		return text;
	}

	public static String checkSeparator(String check) {
		if (check.contains("/"))
			return check.replaceAll("/", Matcher.quoteReplacement(File.separator));
		return check;
	}

	public static String checkLastSeparator(String check) {
		if (check.contains("/")) {
			if (!check.endsWith("/"))
				return check + "/";
		} else if (check.contains("\\")) {
			if (!check.endsWith("\\"))
				return check + "\\";
		} else if (!check.contains("/") || !check.contains("\\")) {
			return check + Matcher.quoteReplacement(File.separator);
		}
		return check;
	}

	public static String removeFirst(String remove, String from) {
		if (from.startsWith(remove))
			return from.replace(remove, "");
		return from;
	}

	public static String stripLastSeparator(String check) {
		if (check.endsWith("/") || check.endsWith("\\")) {
			if (check.length() > 0 ) {
			    int endIndex = check.lastIndexOf(File.separator);
			    if (endIndex != -1)
			    	return check.substring(0, endIndex - 1);
			}
		}
		return check;
	}

	public static String stripAfterLastSeparator(String directory) {
		if (directory != null && directory.length() > 0 ) {
		    int endIndex = directory.lastIndexOf(File.separator);
		    if (endIndex != -1)
		    	return directory.substring(0, endIndex);
		}
		return directory;
	}

	public static String addLastNodeSeperator(String node) {
		if (node.equals("") || node.isEmpty())
			return node;
		return node + ".";
	}

	public static String stripBeforeLastNode(String node) {
		if (node != null && node.length() > 0 ) {
		    int endIndex = node.lastIndexOf(".");
		    if (endIndex != -1)
		    	return node.substring(endIndex);
		}
		return node;
	}

	public static String checkRoot(String check) {
		Path root = Paths.get("").normalize().toAbsolutePath().getRoot();
		//String root = new File("").getAbsoluteFile().getAbsolutePath();
		if (root != null) {
			for (File r : File.listRoots()) {
				if (!check.toLowerCase().startsWith(r.getPath().toLowerCase()))
					continue;
				else
					return check;
			}
			return root + check;
		}
		return File.separator + check;
	}

	public static String stripExtention(String strip) {
		int pos = strip.lastIndexOf(".");
		if (pos > 0)
			return strip.substring(0, pos);
		return strip;
	}

	public static String translateColorCodes(String textToTranslate) {
		char[] b = textToTranslate.toCharArray();

		for (int i = 0; i < b.length - 1; ++i) {
			if (b[i] == '§' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
				b[i] = '&';
				b[i + 1] = Character.toLowerCase(b[i + 1]);
			}
		}

		return new String(b);
	}

	public static Object parseString(Object delta, boolean parse) {
		if (delta == null) 
			return null;
		if (!parse && String.class.isAssignableFrom(delta.getClass())) {
			String s = StringUtil.translateColorCodes(((String) delta));
			if (s.matches("true|false|yes|no|on|off")) {
				return s.matches("true|yes|on");
			} else if (s.matches("(-)?\\d+")) {
				try {
					return Long.parseLong(s);
				} catch (NumberFormatException ex) {
//TODO force people to use 'without string checks' syntax or add conversion
//					return new BigInteger(s);
				}
				
			} else if (s.matches("(-)?\\d+(\\.\\d+)")) {
				return Double.parseDouble(s);
			} else {
				return s;
			}
		}
		return delta;
	}

	/**
	 * Trim a string if it is longer than a certain length.
	 * 
	 * @param str
	 *            the string
	 * @param len
	 *            the length to trim to
	 * @return a new string
	 */
	public static String trimLength(String str, int len) {
		if (str.length() > len)
			return str.substring(0, len);
		return str;
	}

	/**
	 * Join an array of strings into a string.
	 * 
	 * @param str
	 *            the string array
	 * @param delimiter
	 *            the delimiter
	 * @param initialIndex
	 *            the initial index to start from
	 * @return a new string
	 */
	public static String joinString(String[] str, String delimiter, int initialIndex) {
		if (str.length == 0) {
			return "";
		}
		StringBuilder buffer = new StringBuilder(str[initialIndex]);
		for (int i = initialIndex + 1; i < str.length; ++i) {
			buffer.append(delimiter).append(str[i]);
		}
		return buffer.toString();
	}

	/**
	 * Join an array of strings into a string.
	 * 
	 * @param str
	 *            the string array
	 * @param delimiter
	 *            the delimiter
	 * @param initialIndex
	 *            the initial index to start form
	 * @param quote
	 *            the character to put around each entry
	 * @return a new string
	 */
	public static String joinQuotedString(String[] str, String delimiter, int initialIndex, String quote) {
		if (str.length == 0) {
			return "";
		}
		StringBuilder buffer = new StringBuilder();
		buffer.append(quote);
		buffer.append(str[initialIndex]);
		buffer.append(quote);
		for (int i = initialIndex + 1; i < str.length; ++i) {
			buffer.append(delimiter).append(quote).append(str[i]).append(quote);
		}
		return buffer.toString();
	}

	/**
	 * Join an array of strings into a string.
	 * 
	 * @param str
	 *            the string array
	 * @param delimiter
	 *            the delimiter
	 * @return a new string
	 */
	public static String joinString(String[] str, String delimiter) {
		return joinString(str, delimiter, 0);
	}

	/**
	 * Join an array of strings into a string.
	 * 
	 * @param str
	 *            an array of objects
	 * @param delimiter
	 *            the delimiter
	 * @param initialIndex
	 *            the initial index to start form
	 * @return a new string
	 */
	public static String joinString(Object[] str, String delimiter, int initialIndex) {
		if (str.length == 0) {
			return "";
		}
		StringBuilder buffer = new StringBuilder(str[initialIndex].toString());
		for (int i = initialIndex + 1; i < str.length; ++i) {
			buffer.append(delimiter).append(str[i]);
		}
		return buffer.toString();
	}

	/**
	 * Join an array of strings into a string.
	 * 
	 * @param str
	 *            a list of integers
	 * @param delimiter
	 *            the delimiter
	 * @param initialIndex
	 *            the initial index to start form
	 * @return a new string
	 */
	public static String joinString(int[] str, String delimiter, int initialIndex) {
		if (str.length == 0) {
			return "";
		}
		StringBuilder buffer = new StringBuilder(Integer.toString(str[initialIndex]));
		for (int i = initialIndex + 1; i < str.length; ++i) {
			buffer.append(delimiter).append(str[i]);
		}
		return buffer.toString();
	}

	/**
	 * Join an list of strings into a string.
	 *
	 * @param str
	 *            a list of strings
	 * @param delimiter
	 *            the delimiter
	 * @param initialIndex
	 *            the initial index to start form
	 * @return a new string
	 */
	public static String joinString(Collection<?> str, String delimiter, int initialIndex) {
		if (str.isEmpty()) {
			return "";
		}
		StringBuilder buffer = new StringBuilder();
		int i = 0;
		for (Object o : str) {
			if (i >= initialIndex) {
				if (i > 0) {
					buffer.append(delimiter);
				}

				buffer.append(o);
			}
			++i;
		}
		return buffer.toString();
	}

	/**
	 * <p>
	 * Find the Levenshtein distance between two Strings.
	 * </p>
	 *
	 * <p>
	 * This is the number of changes needed to change one String into another, where
	 * each change is a single character modification (deletion, insertion or
	 * substitution).
	 * </p>
	 *
	 * <p>
	 * The previous implementation of the Levenshtein distance algorithm was from
	 * <a href=
	 * "http://www.merriampark.com/ld.htm">http://www.merriampark.com/ld.htm</a>
	 * </p>
	 *
	 * <p>
	 * Chas Emerick has written an implementation in Java, which avoids an
	 * OutOfMemoryError which can occur when my Java implementation is used with
	 * very large strings.<br>
	 * This implementation of the Levenshtein distance algorithm is from <a href=
	 * "http://www.merriampark.com/ldjava.htm">http://www.merriampark.com/ldjava.htm</a>
	 * </p>
	 *
	 * <pre>
	 * StringUtil.getLevenshteinDistance(null, *)             = IllegalArgumentException
	 * StringUtil.getLevenshteinDistance(*, null)             = IllegalArgumentException
	 * StringUtil.getLevenshteinDistance("","")               = 0
	 * StringUtil.getLevenshteinDistance("","a")              = 1
	 * StringUtil.getLevenshteinDistance("aaapppp", "")       = 7
	 * StringUtil.getLevenshteinDistance("frog", "fog")       = 1
	 * StringUtil.getLevenshteinDistance("fly", "ant")        = 3
	 * StringUtil.getLevenshteinDistance("elephant", "hippo") = 7
	 * StringUtil.getLevenshteinDistance("hippo", "elephant") = 7
	 * StringUtil.getLevenshteinDistance("hippo", "zzzzzzzz") = 8
	 * StringUtil.getLevenshteinDistance("hello", "hallo")    = 1
	 * </pre>
	 *
	 * @param s
	 *            the first String, must not be null
	 * @param t
	 *            the second String, must not be null
	 * @return result distance
	 * @throws IllegalArgumentException
	 *             if either String input {@code null}
	 */
	public static int getLevenshteinDistance(String s, String t) {
		if (s == null || t == null) {
			throw new IllegalArgumentException("Strings must not be null");
		}

		/*
		 * The difference between this impl. and the previous is that, rather than
		 * creating and retaining a matrix of size s.length()+1 by t.length()+1, we
		 * maintain two single-dimensional arrays of length s.length()+1. The first, d,
		 * is the 'current working' distance array that maintains the newest distance
		 * cost counts as we iterate through the characters of String s. Each time we
		 * increment the index of String t we are comparing, d is copied to p, the
		 * second int[]. Doing so allows us to retain the previous cost counts as
		 * required by the algorithm (taking the minimum of the cost count to the left,
		 * up one, and diagonally up and to the left of the current cost count being
		 * calculated). (Note that the arrays aren't really copied anymore, just
		 * switched...this is clearly much better than cloning an array or doing a
		 * System.arraycopy() each time through the outer loop.)
		 * 
		 * Effectively, the difference between the two implementations is this one does
		 * not cause an out of memory condition when calculating the LD over two very
		 * large strings.
		 */

		int n = s.length(); // length of s
		int m = t.length(); // length of t

		if (n == 0) {
			return m;
		} else if (m == 0) {
			return n;
		}

		int[] p = new int[n + 1]; // 'previous' cost array, horizontally
		int[] d = new int[n + 1]; // cost array, horizontally
		int[] _d; // placeholder to assist in swapping p and d

		// indexes into strings s and t
		int i; // iterates through s
		int j; // iterates through t

		char tj; // jth character of t

		int cost; // cost

		for (i = 0; i <= n; ++i) {
			p[i] = i;
		}

		for (j = 1; j <= m; ++j) {
			tj = t.charAt(j - 1);
			d[0] = j;

			for (i = 1; i <= n; ++i) {
				cost = s.charAt(i - 1) == tj ? 0 : 1;
				// minimum of cell to the left+1, to the top+1, diagonally left
				// and up +cost
				d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
			}

			// copy current distance counts to 'previous row' distance counts
			_d = p;
			p = d;
			d = _d;
		}

		// our last action in the above loop was to switch d and p, so p now
		// actually has the most recent cost counts
		return p[n];
	}

	public static <T extends Enum<?>> T lookup(Map<String, T> lookup, String name, boolean fuzzy) {
		String testName = name.replaceAll("[ _]", "").toLowerCase();

		T type = lookup.get(testName);
		if (type != null) {
			return type;
		}

		if (!fuzzy) {
			return null;
		}

		int minDist = -1;

		for (Map.Entry<String, T> entry : lookup.entrySet()) {
			final String key = entry.getKey();
			if (key.charAt(0) != testName.charAt(0)) {
				continue;
			}

			int dist = getLevenshteinDistance(key, testName);

			if ((dist < minDist || minDist == -1) && dist < 2) {
				minDist = dist;
				type = entry.getValue();
			}
		}

		return type;
	}
}
