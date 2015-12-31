package ca.weindex.dao.imp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.weindex.common.enums.LabelTypeEnum;
import ca.weindex.common.model.Label;
import ca.weindex.dao.LabelDao;

public class LabelDaoImp extends SqlMapClientDaoSupport implements LabelDao {

	public Label getLabelById(int id) {
		Label label = (Label) getSqlMapClientTemplate().queryForObject("getLabelById", id);
		return label;
	}

	public List<Label> getLabel() {
		List<?> list = getSqlMapClientTemplate().queryForList("getLabel");
		if (list != null && !list.isEmpty()) {
			List<Label> result = new ArrayList<Label>();
			for (Object o : list) {
				result.add((Label) o);
			}
			return result;
		}
		return Collections.emptyList();
	}

	public List<Label> getLabelByType(LabelTypeEnum type) {
		List<?> list = getSqlMapClientTemplate().queryForList("getLabelByType", type.getType());
		if (list != null && !list.isEmpty()) {
			List<Label> result = new ArrayList<Label>();
			for (Object o : list) {
				result.add((Label) o);
			}
			return result;
		}
		return Collections.emptyList();
	}
	

	
	public List<Label> getLabelByTypeLevelCategory(LabelTypeEnum type, int levelId, int parentCategoryId) {
		Label request = new Label();
		request.setLevelId(levelId);
		request.setType(type.getType());
		request.setParentCategoryId(parentCategoryId);
		List<?> list = getSqlMapClientTemplate().queryForList("getLabelByTypeLevelCategory", request);
		if (list != null && !list.isEmpty()) {
			List<Label> result = new ArrayList<Label>();
			for (Object o : list) {
				result.add((Label) o);
			}
			return result;
		}
		return Collections.emptyList();
	}



	public List<Label> getLabelByTypeLevel(LabelTypeEnum type, int levelId) {
		Label request = new Label();
		request.setLevelId(levelId);
		request.setType(type.getType());
		List<?> list = getSqlMapClientTemplate().queryForList("getLabelByTypeLevel", request);
		if (list != null && !list.isEmpty()) {
			List<Label> result = new ArrayList<Label>();
			for (Object o : list) {
				result.add((Label) o);
			}
			return result;
		}
		return Collections.emptyList();
	}

	public boolean insertLabel(Label label) {
		int i = getSqlMapClientTemplate().update("insertLabel", label);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean updateLabel(Label label) {
		int i = getSqlMapClientTemplate().update("updateLabel", label);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteLabel(int id) {
		int i = getSqlMapClientTemplate().delete("deleteLabel", id);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public List<Label> getLabelByIdList(String idList) {
		String[] ids = idList.split(",");
		return getLabelByIdList(ids);
//		List<?> list = getSqlMapClientTemplate().queryForList("getLabelByIdList", idList);
//		if (list != null && !list.isEmpty()) {
//			List<Label> result = new ArrayList<Label>();
//			for (Object o : list) {
//				result.add((Label) o);
//			}
//			return result;
//		}
//		return Collections.emptyList();
	}

	private List<Label> getLabelByIdList(String[] idList) {
		if (idList != null && idList.length > 0) {
			List<?> list = getSqlMapClientTemplate().queryForList("getLabelByIdList", idList);
			if (list != null && !list.isEmpty()) {
				List<Label> result = new ArrayList<Label>();
				for (Object o : list) {
					result.add((Label) o);
				}
				return result;
			}
		}
		return Collections.emptyList();
	}

	public List<Label> getLabelByIdList(int[] idList) {
		if (idList != null) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < idList.length; i++) {
				if (i > 0) {
					sb.append(",");
				}
				sb.append(idList[i]);
			}
			return getLabelByIdList(sb.toString());
		}
		return Collections.emptyList();
	}

	public List<Label> getLabelByIdList(List<Integer> idList) {
		if (idList != null && !idList.isEmpty()) {

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < idList.size(); i++) {
				if (i > 0) {
					sb.append(",");
				}
				sb.append(idList.get(i));
			}
			return getLabelByIdList(sb.toString());
		}
		return Collections.emptyList();
	}

	public List<Label> getLabelListByParentCategoryId(int parentCategoryId) {
		List<?> list = getSqlMapClientTemplate().queryForList("getLabelByParentCategoryId", parentCategoryId);
		if (list != null && !list.isEmpty()) {
			List<Label> result = new ArrayList<Label>();
			for (Object o : list) {
				result.add((Label) o);
			}
			return result;
		}
		return Collections.emptyList();
	};


}
