package ca.weindex.services.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.weindex.common.enums.LabelTypeEnum;
import ca.weindex.common.model.Blog;
import ca.weindex.common.model.Label;
import ca.weindex.common.model.Offer;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.Shop;
import ca.weindex.dao.BlogDao;
import ca.weindex.dao.LabelDao;
import ca.weindex.dao.OfferDao;
import ca.weindex.dao.ShopDao;
import ca.weindex.services.LabelService;

@Service
public class LabelServiceImp implements LabelService {
	@Autowired
	private LabelDao labelDao;
	
	@Autowired
	private OfferDao offerDao;
	
	@Autowired
	private ShopDao shopDao;
	
	@Autowired
	private BlogDao blogDao;
	
	public Label getLabelById(int id) {
		return labelDao.getLabelById(id);
	}

	public List<Label> getLabel() {
		return labelDao.getLabel();
	}

	public List<Label> getLabelByType(LabelTypeEnum type) {
		return labelDao.getLabelByType(type);
	}


	public List<Label> getLabelByTypeLevelCategory(LabelTypeEnum type, int levelId,  int parentCategoryId) {
		return labelDao.getLabelByTypeLevelCategory(type, levelId, parentCategoryId);
	}

	public List<Label> getLabelByTypeLevel(LabelTypeEnum type, int levelId){
		return labelDao.getLabelByTypeLevel(type, levelId);
	}

	public boolean insertLabel(Label label) {
		return labelDao.insertLabel(label);
	}

	public boolean deleteLabel(int id) {
		return labelDao.deleteLabel(id);
	}

	public boolean setLabelVisible(int id) {
		Label label = labelDao.getLabelById(id);
		if (label != null) {
			label.setVisible(true);
			return labelDao.updateLabel(label);
		}
		return false;
	}

	public boolean setLabelUnvisible(int id) {
		Label label = labelDao.getLabelById(id);
		if (label != null) {
			label.setVisible(false);
			return labelDao.updateLabel(label);
		}
		return false;
	}

	public boolean updateLabelPos(int id, int pos) {
		Label label = labelDao.getLabelById(id);
		if (label != null) {
			label.setPos(pos);
			return labelDao.updateLabel(label);
		}
		return false;
	}
	public boolean updateLabel(Label label) {
		if (label != null) {
			return labelDao.updateLabel(label);
		}
		return false;
	};

	public SearchResult<Offer> getOfferByLabelId(int labelId, Pagination page) {
		return offerDao.getOfferByLabelId(labelId, page);
	}

	public SearchResult<Shop> getShopByLabelId(int labelId, Pagination page) {
		return shopDao.getShopByLabelId(labelId, page);
	}

	public SearchResult<Blog> getBlogByLabelId(int labelId, Pagination page) {
		return blogDao.getBlogByLabelId(labelId, page);
	}


}
