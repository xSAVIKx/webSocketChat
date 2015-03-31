package chat.spring.configuration;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationContextProvider implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	private ApplicationContextProvider() {
		//
	}

	public static ApplicationContext getApplicationContext() {
		if (applicationContext == null)
			applicationContext = new AnnotationConfigApplicationContext(
					ApplicationConfig.class);
		return applicationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ApplicationContextProvider.applicationContext = applicationContext;
	}

}
