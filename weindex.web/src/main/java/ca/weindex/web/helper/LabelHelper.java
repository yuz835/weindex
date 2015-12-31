package ca.weindex.web.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.weindex.common.model.Label;

public class LabelHelper {
	public static Map<String, Label> getLabelMap(List<? extends Label> list) {
		 Map<String, Label> map = new HashMap<String, Label>();
		 for (Label l : list) {
			 map.put(Integer.toString(l.getId()), l);
		 }
		 return map;
	}
	
	public static List<Label> convertLabel(String label, List<? extends Label> list) {
		if (label == null || list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		Map<String, Label> labelMap = getLabelMap(list);
		String[] ls = label.split(",");
		List<Label> myLabels = new ArrayList<Label>();
		for (String l : ls) {
			Label lab = labelMap.get(l);
			if (lab != null) {
				myLabels.add(lab);
			}
		}
		return myLabels;
	}
}
