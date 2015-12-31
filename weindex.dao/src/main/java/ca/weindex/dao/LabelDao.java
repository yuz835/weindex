package ca.weindex.dao;

import java.util.List;

import ca.weindex.common.enums.LabelTypeEnum;
import ca.weindex.common.model.Label;

public interface LabelDao {
	public Label getLabelById(int id);

	public List<Label> getLabel();
	public List<Label> getLabelByType(LabelTypeEnum type);
	public List<Label> getLabelByTypeLevelCategory(LabelTypeEnum type, int levelId, int parentCategoryId);
	public List<Label> getLabelByTypeLevel(LabelTypeEnum type, int levelId);


	public boolean insertLabel(Label label);

	public boolean updateLabel(Label label);

	public boolean deleteLabel(int id);
	
	public List<Label> getLabelByIdList(String idList);
	public List<Label> getLabelByIdList(int[] idList);
	public List<Label> getLabelByIdList(List<Integer> idList);
	public List<Label> getLabelListByParentCategoryId(int parentCategoryId);

}
