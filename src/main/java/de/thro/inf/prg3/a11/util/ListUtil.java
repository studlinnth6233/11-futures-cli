package de.thro.inf.prg3.a11.util;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Peter Kurfer
 * Created on 12/20/17.
 */
public abstract class ListUtil {

	private ListUtil() {
	}

	public static <T> List<T> mergeLists(List<? extends T> list1, List<? extends T> list2) {
		List<T> result = new LinkedList<>(list1);
		result.addAll(list2);
		return result;
	}
}
