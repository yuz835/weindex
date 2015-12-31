package ca.weindex.services.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.weindex.common.enums.MessageTypeEnum;
import ca.weindex.common.model.Message;
import ca.weindex.common.model.Shop;
import ca.weindex.common.model.User;
import ca.weindex.dao.ShopDao;
import ca.weindex.services.AdminService;
import ca.weindex.services.MessageService;
import ca.weindex.services.UserService;

@Service
public class AdminServiceImp implements AdminService {
	@Autowired
	private ShopDao shopDao;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private UserService userService;
	
	public boolean verifyShop(int shopId) {
		Shop shop = shopDao.getShop(shopId);
		if (shop != null && !shop.isVerified()) {
			shop.setVerified(true);
			boolean b = shopDao.updateShop(shop);
			if (b) {
				try {
					// send message
					Message msg = new Message();
					User user = userService.getUserById(shop.getUserId());
					msg.setDestId(shop.getUserId());
					msg.setDest(user.getUserName());
					msg.setSourceId(-1);
					msg.setSource("Admin");
					msg.setType(MessageTypeEnum.SHOP_VERIFED);
					msg.setContent("");
					messageService.insertMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return b;
		}
		return false;
	}

	public boolean unverifyShop(int shopId) {
		Shop shop = shopDao.getShop(shopId);
		if (shop != null && shop.isVerified()) {
			shop.setVerified(false);
			boolean b = shopDao.updateShop(shop);
			if (b) {
				try {
					// send message
					Message msg = new Message();
					User user = userService.getUserById(shop.getUserId());
					msg.setDestId(shop.getUserId());
					msg.setDest(user.getUserName());
					msg.setSourceId(-1);
					msg.setSource("Admin");
					msg.setType(MessageTypeEnum.SHOP_UNVERIFED);
					msg.setContent("");
					messageService.insertMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return b;
		}
		return false;
	}

}
