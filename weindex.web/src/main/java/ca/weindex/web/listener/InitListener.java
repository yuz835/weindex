package ca.weindex.web.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import ca.weindex.web.helper.WeiboHelper;

@Component
public class InitListener implements ApplicationListener<ContextRefreshedEvent> {
	@Autowired
	private WeiboHelper weiboHelper;
	
	public void onApplicationEvent(ContextRefreshedEvent event) {
		weiboHelper.init();
	}

}
