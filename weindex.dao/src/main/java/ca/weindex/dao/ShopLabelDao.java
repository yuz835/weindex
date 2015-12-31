package ca.weindex.dao;

import java.util.List;

import ca.weindex.common.enums.ShopLabelTypeEnum;
import ca.weindex.common.model.ShopLabel;

public interface ShopLabelDao {
	public ShopLabel getShopLabelById(int id);

	public List<ShopLabel> getShopLabelByShopId(int shopId);
	public List<ShopLabel> getShopLabelByShopIdType(int shopId, ShopLabelTypeEnum type);

	public boolean insertShopLabel(ShopLabel label);

	public boolean updateShopLabel(ShopLabel label);

	public boolean deleteShopLabel(int id);
	
	public List<ShopLabel> getShopLabelByIdList(String idList);
	public List<ShopLabel> getShopLabelByIdList(int[] idList);
	public List<ShopLabel> getShopLabelByIdList(List<Integer> idList);
}
