package ca.weindex.services.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.weindex.common.model.Message;
import ca.weindex.common.model.UserContactor;
import ca.weindex.dao.MessageDao;
import ca.weindex.services.MessageService;

@Service
public class MessageServiceImp implements MessageService {

	@Autowired
	private MessageDao messageDao;
	
	public boolean insertMessage(Message message) {
		return messageDao.insertMessage(message);
	}

	public List<Message> getMessageByUserId(int sourceId, int destId) {
		List<Message> list = messageDao.getMessageByUserId(sourceId, destId);
		// make all messages which are from destid to source id as readed
		messageDao.readMessageBySourceDest(destId, sourceId);
		return list;
	}

	public List<UserContactor> getUserContactor(int userId) {
		return messageDao.getUserContactor(userId);
	}

	public int getUserUnreadMessageNumber(int userId) {
		return messageDao.getUserUnreadMessageNumber(userId);
	}

	public List<Message> getMessageList(int userId) {
		List<Message> list = messageDao.getMessageByDest(userId);
		if (list != null && !list.isEmpty()) {
			messageDao.readMessageByDest(userId);
		}
		return list;
	}

}
