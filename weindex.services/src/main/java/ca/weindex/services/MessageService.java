package ca.weindex.services;

import java.util.List;

import ca.weindex.common.model.Message;
import ca.weindex.common.model.UserContactor;

public interface MessageService {
	public boolean insertMessage(Message message);

	public List<Message> getMessageList(int userId);

	public List<Message> getMessageByUserId(int sourceId, int destId);

	public List<UserContactor> getUserContactor(int userId);
	
	public int getUserUnreadMessageNumber(int userId);

}
