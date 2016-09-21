package icfclient;

import java.io.File;
import java.net.URL;
import java.util.Set;

import org.identityconnectors.common.IOUtil;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.api.APIConfiguration;
import org.identityconnectors.framework.api.ConfigurationProperties;
import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.api.ConnectorFacadeFactory;
import org.identityconnectors.framework.api.ConnectorInfo;
import org.identityconnectors.framework.api.ConnectorInfoManager;
import org.identityconnectors.framework.api.ConnectorInfoManagerFactory;
import org.identityconnectors.framework.api.ConnectorKey;
import org.identityconnectors.framework.common.objects.AttributeInfo;
import org.identityconnectors.framework.common.objects.ObjectClassInfo;
import org.identityconnectors.framework.common.objects.Schema;

public class GetOIDConnectorObjectClassInfo {


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
		 
		 System.out.println("Printing out the objectclasses:");
		 System.out.println("-------------------------------");

		 configProps.setPropertyValue("host", "<HOST>");
		 configProps.setPropertyValue("port", new Integer(3060));
		 configProps.setPropertyValue("principal", "<PRINCIPAL>");
		 configProps.setPropertyValue("credentials", new GuardedString("<PASSWORD>".toCharArray()));
		 configProps.setPropertyValue("baseContexts", new String[]{"<ROOT_CONTEXT>"});
		 configProps.setPropertyValue("passwordAttribute", "credentials");
		 configProps.setPropertyValue("ssl", Boolean.FALSE);
		 
		 ConnectorFacade connector = 
			    ConnectorFacadeFactory.getInstance().newInstance(apiConfig);	
		 connector.validate();
		 
		 Schema schema = connector.schema();
		 
		 System.out.println("=========================");
		 // print out the schema fields
		 Set<ObjectClassInfo> ociSet = schema.getObjectClassInfo();
		 for (ObjectClassInfo oc: ociSet) {
			 System.out.println(oc.getType());
			 Set<AttributeInfo> aiSet = oc.getAttributeInfo();
			 for (AttributeInfo ai: aiSet) {
				 System.out.println("\t" + ai.getName() + " ==> " + ai.getType());
			 }
		 }		 
		 
	 
	}


}
