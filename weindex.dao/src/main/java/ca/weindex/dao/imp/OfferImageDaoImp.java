package ca.weindex.dao.imp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.weindex.common.model.OfferImage;
import ca.weindex.dao.OfferImageDao;

public class OfferImageDaoImp extends SqlMapClientDaoSupport implements
		OfferImageDao {

	public OfferImage getOfferImageById(int id) {
		OfferImage result = (OfferImage) getSqlMapClientTemplate()
				.queryForObject("getOfferImage", id);
		return result;
	}

	public List<OfferImage> getOfferImageByOfferId(int offerId) {
		List<?> list = getSqlMapClientTemplate().queryForList(
				"getOfferImageByOfferId", offerId);
		if (list != null && !list.isEmpty()) {
			List<OfferImage> result = new ArrayList<OfferImage>();
			for (Object o : list) {
				result.add((OfferImage) o);
			}
			return result;
		}
		return Collections.emptyList();
	}

	public OfferImage getOfferImageByNameType(String name, String type) {
		OfferImage image = new OfferImage();
		image.setName(name);
		image.setType(type);
		OfferImage result = (OfferImage) getSqlMapClientTemplate()
				.queryForObject("getOfferImageByNameType", image);
		return result;
	}

	public boolean insertOfferImage(OfferImage image) {
		int i = getSqlMapClientTemplate().update("insertOfferImage", image);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean updateOfferImage(OfferImage image) {
		int i = getSqlMapClientTemplate().update("updateOfferImage", image);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteOfferImage(int id) {
		int i = getSqlMapClientTemplate().delete("deleteOfferImage", id);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean updateOfferImageLogo(OfferImage image) {
		int i = getSqlMapClientTemplate().update("updateOfferImageLogo", image);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

}
