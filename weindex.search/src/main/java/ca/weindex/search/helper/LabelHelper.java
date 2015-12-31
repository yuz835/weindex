package ca.weindex.search.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.weindex.common.model.Label;
import ca.weindex.dao.LabelDao;

@Component
public class LabelHelper {
	@Autowired
	private LabelDao labelDao;

	public Field getLabelField(String labelStr) {
		if (labelStr == null || labelStr.length() == 0) {
			return null;
		}
		List<Label> allLabel = labelDao.getLabel();
		if (allLabel == null || allLabel.isEmpty()) {
			return null;
		}
		Map<String, Label> map = new HashMap<String, Label>();
		for (Label l : allLabel) {
			map.put(Integer.toString(l.getId()), l);
		}
		String[] labelIds = labelStr.split(",");
		StringBuilder sb = new StringBuilder();
		for (String id : labelIds) {
			Label l = map.get(id);
			if (l != null) {
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(l.getCnName());
				sb.append(", ");
				sb.append(l.getEnName());
			}
		}
		if (sb.length() > 0) {
			Field field = new Field("label", sb.toString(), Field.Store.YES, Field.Index.ANALYZED);
			return field;
		} else {
			return null;
		}
	}

}
