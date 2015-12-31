package ca.weindex.dao;

import java.util.List;


import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.WeiboAction;

public interface WeiboActionDao {

	public boolean insertWeiboAction(WeiboAction action); 
	public boolean checkWeiboAction(WeiboAction action);
	public List<WeiboAction> getNewActionList(WeiboAction action); 
	public boolean setActionComplete(int id); 


}
