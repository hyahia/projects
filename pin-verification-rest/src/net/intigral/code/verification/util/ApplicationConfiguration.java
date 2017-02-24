package net.intigral.code.verification.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfiguration {

	private Properties props;
	private static ApplicationConfiguration instance;
	
	public static ApplicationConfiguration getInstance(){
		if(instance == null){
			instance = new ApplicationConfiguration();
		}
		
		return instance;
	}
	private ApplicationConfiguration() {

		props = new Properties();
		InputStream input = null;

		try {
			String filename = "config.properties";
			input = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
			props.load(input);
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Properties getAppProperties() {
		if (props != null) {
			return props;
		} else
			return new Properties();
	}

	public String getPropertryValue(String key) {
		if (props != null)
			return props.getProperty(key);
		else
			return "";
	}


}