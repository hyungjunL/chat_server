package study.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

//@ConfigurationProperties(prefix="mybatis")
@Data
@Slf4j
public class MybatisSetting {

	private Resource configLocation;
	private String mapperLocations;
	private String driverClassName;
	private String username;
	private String password;
	private String url;
	private boolean testWhileIdle;
	private long timeBetweenEvictionRunsMillis;
	private String validationQuery;
	private int maxTotal;
	private int maxIdle;

	public String getPassword() {
		
		String str = "";
		
		try {
			StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
			encryptor.setPassword("MY_STUDY");
			str = encryptor.decrypt(this.password);
		}catch(Exception e) {
			log.error(e.getMessage(), e);
		}

		return str;
	}
}