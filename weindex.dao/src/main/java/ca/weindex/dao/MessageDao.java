package ca.weindex.dao;

import java.util.List;

import ca.weindex.common.model.Message;
import ca.weindex.common.model.UserContactor;

public interface MessageDao {
	public boolean insertMessage(Message message);
	
	public List<Message> getMessageByDest(int destId);
	
	public List<Message> getMessageByUserId(int sourceId, int destId);
	
	public List<UserContactor> getUserContactor(int userId);
	
	public void readMessageBySourceDest(int sourceId, int destId);

	public void readMessageByDest(int destId);
	
	public int getUserUnreadMessageNumber(int userId);
}
