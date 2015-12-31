package ca.weindex.services;

import java.util.List;

import ca.weindex.common.enums.LabelTypeEnum;
import ca.weindex.common.model.Blog;
import ca.weindex.common.model.Label;
import ca.weindex.common.model.Offer;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.Shop;

public interface LabelService {
	public Label getLabelById(int id);

	public List<Label> getLabel();
	public List<Label> getLabelByType(LabelTypeEnum type);


	public List<Label> getLabelByTypeLevelCategory(LabelTypeEnum type, int levelId,  int parentCategoryId);
	public List<Label> getLabelByTypeLevel(LabelTypeEnum type, int levelId);


	public boolean insertLabel(Label label);

	public boolean deleteLabel(int id);
	
	public boolean setLabelVisible(int id);
	
	public boolean setLabelUnvisible(int id);

	public boolean updateLabelPos(int id, int pos);

	public boolean updateLabel(Label label);

	
	public SearchResult<Offer> getOfferByLabelId(int labelId, Pagination page);
	public SearchResult<Shop> getShopByLabelId(int labelId, Pagination page);
	public SearchResult<Blog> getBlogByLabelId(int labelId, Pagination page);
	
}
