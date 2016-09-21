package icfclient;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
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
import org.identityconnectors.framework.api.operations.APIOperation;
import org.identityconnectors.framework.common.objects.ObjectClassInfo;
import org.identityconnectors.framework.common.objects.Schema;

public class GetDBAppTablesOperationInfo {

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
		 
		 Schema schema = connector.schema();
		 
		 System.out.println("Printing out the operations and supported objectclasses:");
		 System.out.println("=========================");
		 
		 // print out the operation option info
		 Map<Class<? extends APIOperation>, Set<ObjectClassInfo>> opOcMap = schema.getSupportedObjectClassesByOperation();
		 
		 Iterator<Class<? extends APIOperation>> it = opOcMap.keySet().iterator();
		 
		 while (it.hasNext()) {
			 Object op = it.next();
			 Set<ObjectClassInfo> ocinfoSet = (Set<ObjectClassInfo>)opOcMap.get(op);
			 System.out.print(op + " ==> " );
			 for (ObjectClassInfo oc: ocinfoSet) {
				 System.out.print(oc.getType() + " " );
			 }
			 System.out.println();
		 }
	}


}
