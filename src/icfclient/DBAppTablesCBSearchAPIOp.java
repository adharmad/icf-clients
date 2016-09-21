package icfclient;

import java.io.File;
import java.net.URL;

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
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.filter.EqualsFilter;
import org.identityconnectors.framework.common.objects.filter.Filter;

public class DBAppTablesCBSearchAPIOp {


	public static void main(String[] args) throws Exception {

		File bundleDirectory = new File("c:/Users/adharmad/Dropbox/code/github/icf-samples/icf-clients/bundles");
		URL db1Url = IOUtil.makeURL(bundleDirectory,
				"org.identityconnectors.databasetable-1.2.2.jar");

		ConnectorInfoManagerFactory factory = ConnectorInfoManagerFactory
				.getInstance();
		ConnectorInfoManager manager = factory.getLocalManager(db1Url);

		 ConnectorKey key = new ConnectorKey("org.identityconnectors.databasetable",
                "1.2.2",
                "org.identityconnectors.databasetable.DatabaseTableConnector");
		 ConnectorInfo conInfo = manager.findConnectorInfo(key);
		 
		 APIConfiguration apiConfig = conInfo.createDefaultAPIConfiguration();
		 
		 ConfigurationProperties configProps = apiConfig.getConfigurationProperties();
		 
		 configProps.setPropertyValue("host", "adc2140567.us.oracle.com");
		 configProps.setPropertyValue("port", "5521");
		 configProps.setPropertyValue("user", "vdec9_oim");
		 configProps.setPropertyValue("password", new GuardedString("welcome1".toCharArray()));
		 configProps.setPropertyValue("database", "oimdb");
		 configProps.setPropertyValue("table", "usr");
		 configProps.setPropertyValue("keyColumn", "usr_key");
		 
		 
		 ConnectorFacade connector = 
			    ConnectorFacadeFactory.getInstance().newInstance(apiConfig);	
		 connector.validate();
		 
		 // perform search
		 ResultsHandler resultsHandler = new ResultsHandler() {
			 public boolean handle(ConnectorObject cobject) {
				 //for (Attribute attribute : cobject.getAttributes()) {
				 //	 System.out.println(attribute.toString());
				 //}
				 
				 String usrLogin = (String)cobject.getAttributeByName("USR_LOGIN").getValue().get(0);
				 String firstName = (String)cobject.getAttributeByName("USR_FIRST_NAME").getValue().get(0);
				 String lastName = (String)cobject.getAttributeByName("USR_LAST_NAME").getValue().get(0);
				 
				 System.out.println(usrLogin + " " + firstName + " " + lastName);
				 return true;
			 }
		 };

		 
		 Filter f = new EqualsFilter(AttributeBuilder.build("USR_STATUS", "Active"));
		 
		 connector.search(ObjectClass.ACCOUNT, f, resultsHandler, null);
		 //connector.search(ObjectClass.ACCOUNT, null, resultsHandler, null);
	 
	}

}
