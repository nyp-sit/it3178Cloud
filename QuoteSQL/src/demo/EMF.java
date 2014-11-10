package demo;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.google.appengine.api.utils.SystemProperty;

public final class EMF {
	private static final Map<String, String> properties = null;
	private static EntityManagerFactory emfInstance = null;

	private EMF() {
	}

	public static EntityManagerFactory get() {
		if (emfInstance == null) {
			Map<String, String> properties = new HashMap();
			if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
				System.out.println("Production server!!");
				properties.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.GoogleDriver");//
				properties.put("javax.persistence.jdbc.url","jdbc:google:mysql://x5-gentle-oxygen-h:it3178quote/test");
//				properties.put("javax.persistence.jdbc.url","jdbc:google:mysql://it3178quote:data/test");
//				properties.put("javax.persistence.jdbc.url","jdbc:google:mysql://173.194.250.168:3306/test");
				properties.put("javax.persistence.jdbc.user", "root");
//				properties.put("javax.persistence.jdbc.password", "it3178quote");
			} else {
				System.out.println("Local server!!");
				properties.put("javax.persistence.jdbc.driver","com.mysql.jdbc.Driver");
				properties.put("javax.persistence.jdbc.url","jdbc:mysql://127.0.0.1:3306/test");
				properties.put("javax.persistence.jdbc.user", "root");
				properties.put("javax.persistence.jdbc.password", "admin");
			}
			emfInstance = Persistence.createEntityManagerFactory("transactions-optional", properties);
		}
		return emfInstance;
	}
	
}