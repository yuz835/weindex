package ca.weindex.dao.imp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.weindex.common.enums.ShopLabelTypeEnum;
import ca.weindex.common.model.ShopLabel;
import ca.weindex.dao.ShopLabelDao;

public class ShopLabelDaoImp extends SqlMapClientDaoSupport implements ShopLabelDao {

	public ShopLabel getShopLabelById(int id) {
		ShopLabel label = (ShopLabel) getSqlMapClientTemplate().queryForObject("getShopLabelById", id);
		return label;
	}

	public List<ShopLabel> getShopLabelByShopId(int shopId) {
		List<?> list = getSqlMapClientTemplate().queryForList("getShopLabelByShopId", shopId);
		if (list != null && !list.isEmpty()) {
			List<ShopLabel> result = new ArrayList<ShopLabel>();
			for (Object o : list) {
				result.add((ShopLabel) o);
			}
			return result;
		}
		return Collections.emptyList();
	}

	public List<ShopLabel> getShopLabelByShopIdType(int shopId, ShopLabelTypeEnum type) {
		Map<String, Integer> query = new HashMap<String, Integer>();
		query.put("shopId", shopId);
		query.put("type", type.getType());
		List<?> list = getSqlMapClientTemplate().queryForList("getShopLabelByShopIdType", query);
		if (list != null && !list.isEmpty()) {
			List<ShopLabel> result = new ArrayList<ShopLabel>();
			for (Object o : list) {
				result.add((ShopLabel) o);
			}
			return result;
		}
		return Collections.emptyList();
	}

	public boolean insertShopLabel(ShopLabel label) {
		int i = getSqlMapClientTemplate().update("insertShopLabel", label);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean updateShopLabel(ShopLabel label) {
		int i = getSqlMapClientTemplate().update("updateShopLabel", label);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteShopLabel(int id) {
		int i = getSqlMapClientTemplate().delete("deleteShopLabel", id);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public List<ShopLabel> getShopLabelByIdList(String idList) {
		String[] ids = idList.split(",");
		return getShopLabelByIdList(ids);
//		List<?> list = getSqlMapClientTemplate().queryForList("getShopLabelByIdList", idList);
//		if (list != null && !list.isEmpty()) {
//			List<ShopLabel> result = new ArrayList<ShopLabel>();
//			for (Object o : list) {
//				result.add((ShopLabel) o);
//			}
//			return result;
//		}
//		return Collections.emptyList();
	}

	private List<ShopLabel> getShopLabelByIdList(String[] idList) {
		if (idList != null && idList.length > 0) {
			List<?> list = getSqlMapClientTemplate().queryForList("getShopLabelByIdList", idList);
			if (list != null && !list.isEmpty()) {
				List<ShopLabel> result = new ArrayList<ShopLabel>();
				for (Object o : list) {
					result.add((ShopLabel) o);
				}
				return result;
			}
		}
		return Collections.emptyList();
	}

	public List<ShopLabel> getShopLabelByIdList(int[] idList) {
		if (idList != null) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < idList.length; i++) {
				if (i > 0) {
					sb.append(",");
				}
				sb.append(idList[i]);
			}
			return getShopLabelByIdList(sb.toString());
		}
		return Collections.emptyList();
	}

	public List<ShopLabel> getShopLabelByIdList(List<Integer> idList) {
		if (idList != null && !idList.isEmpty()) {

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < idList.size(); i++) {
				if (i > 0) {
					sb.append(",");
				}
				sb.append(idList.get(i));
			}
			return getShopLabelByIdList(sb.toString());
		}
		return Collections.emptyList();
	}

}
