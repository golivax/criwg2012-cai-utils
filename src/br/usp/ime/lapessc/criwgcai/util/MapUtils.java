package br.usp.ime.lapessc.criwgcai.util;

import java.util.List;
import java.util.Map;

import com.google.common.base.Functions;
import com.google.common.collect.Ordering;

public class MapUtils {

	public List<String> getKeysSortedByValue(Map<String, Double> map){
		
		return Ordering.natural().onResultOf(Functions.forMap(map)).
				immutableSortedCopy(map.keySet()); 
	}
	
}
