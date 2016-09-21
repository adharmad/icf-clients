package icfclient;

import java.io.File;
import java.net.URL;

import org.identityconnectors.common.IOUtil;
import org.identityconnectors.framework.api.APIConfiguration;
import org.identityconnectors.framework.api.ConfigurationProperties;
import org.identityconnectors.framework.api.ConfigurationProperty;
import org.identityconnectors.framework.api.ConnectorInfo;
import org.identityconnectors.framework.api.ConnectorInfoManager;
import org.identityconnectors.framework.api.ConnectorInfoManagerFactory;
import org.identityconnectors.framework.api.ConnectorKey;

public class GetOIDConnectorBundleInfo {

	public static void main(String[] args) throws Exception {

		File bundleDirectory = new File("c:/Users/adharmad/Dropbox/code/github/icf-samples/icf-clients/bundles");
		URL db1Url = IOUtil.makeURL(bundleDirectory,
				"org.identityconnectors.ldap-1.0.6380.jar");

		ConnectorInfoManagerFactory factory = ConnectorInfoManagerFactory
				.getInstance();
		ConnectorInfoManager manager = factory.getLocalManager(db1Url);

		 ConnectorKey key = new ConnectorKey("org.identityconnectors.ldap",
                "1.0.6380",
                "org.identityconnectors.ldap.LdapConnector");
		 ConnectorInfo conInfo = manager.findConnectorInfo(key);
		 
		 APIConfiguration apiConfig = conInfo.createDefaultAPIConfiguration();
		 
		 ConfigurationProperties configProps = apiConfig.getConfigurationProperties();
		 
		 System.out.println("Printing out the required connector properties:");
		 System.out.println("------------------------------------");
		 for (String name : configProps.getPropertyNames()) {
			 
			 ConfigurationProperty prop = configProps.getProperty(name);
			 
			 if (prop.isRequired()) {
				 System.out.println("\tname: " + name);
				 System.out.println("\ttype: " + prop.getType());
				 System.out.println("------------------------------------");
			 }
			 
		 } 		 
		 
		 System.out.println("------------------------------------");
		 System.out.println("Printing out the connector properties:");
		 System.out.println("------------------------------------");
		 for (String name : configProps.getPropertyNames()) {
			 ConfigurationProperty prop = configProps.getProperty(name);
			 
			 System.out.println("\tname: " + name);
			 System.out.println("\ttype: " + prop.getType());
			 System.out.println("\tconfidential: " + prop.isConfidential());
			 System.out.println("\tobjectclasses: " + prop.getObjectClasses().toString());
			 System.out.println("\tattributes: " + prop.getAttributes().toString());
			 System.out.println("\toperations: " + prop.getOperations().toString());
			 System.out.println("------------------------------------");
		 } 
	 
	}

}
