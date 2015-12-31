package ca.weindex.dao.imp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.weindex.common.model.Message;
import ca.weindex.common.model.UserContactor;
import ca.weindex.dao.MessageDao;

public class MessageDaoImp extends SqlMapClientDaoSupport implements MessageDao {

	public boolean insertMessage(Message message) {
		int i = getSqlMapClientTemplate().update("insertMessage", message);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public List<Message> getMessageByDest(int destId) {
		List<?> list = getSqlMapClientTemplate().queryForList("getMessageByDestId", destId);
		if (list != null && !list.isEmpty()) {
			List<Message> result = new ArrayList<Message>();
			for (Object o : list) {
				result.add((Message) o);
			}
			return result;
		}
		return Collections.emptyList();
	}

	public List<Message> getMessageByUserId(int sourceId, int destId) {
		Message message = new Message();
		message.setSourceId(sourceId);
		message.setDestId(destId);
		List<?> list = getSqlMapClientTemplate().queryForList("getMessageBySourceIdOrDestId", message);
		if (list != null && !list.isEmpty()) {
			List<Message> result = new ArrayList<Message>();
			for (Object o : list) {
				result.add((Message) o);
			}
			return result;
		}
		return Collections.emptyList();
	}

	public List<UserContactor> getUserContactor(int userId) {
		List<?> list = getSqlMapClientTemplate().queryForList("getContactorByUser", userId);
		if (list != null && !list.isEmpty()) {
			List<UserContactor> result = new ArrayList<UserContactor>();
			for (Object o : list) {
				result.add((UserContactor) o);
			}
			return result;
		}
		return Collections.emptyList();
	}

	public void readMessageBySourceDest(int sourceId, int destId) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("sourceId", sourceId);
		map.put("destId", destId);
		getSqlMapClientTemplate().update("readMessageBySourceDest", map);		
	}

	public void readMessageByDest(int destId) {
		getSqlMapClientTemplate().update("readMessageByDest", destId);		
	}

	public int getUserUnreadMessageNumber(int userId) {
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("getUserUnreadMessageNumber", userId);
		return count;
	}

}
