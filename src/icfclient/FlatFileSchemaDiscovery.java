package icfclient;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.identityconnectors.common.IOUtil;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.api.APIConfiguration;
import org.identityconnectors.framework.api.ConfigurationProperties;
import org.identityconnectors.framework.api.ConfigurationProperty;
import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.api.ConnectorFacadeFactory;
import org.identityconnectors.framework.api.ConnectorInfo;
import org.identityconnectors.framework.api.ConnectorInfoManager;
import org.identityconnectors.framework.api.ConnectorInfoManagerFactory;
import org.identityconnectors.framework.api.ConnectorKey;
import org.identityconnectors.framework.api.operations.APIOperation;
import org.identityconnectors.framework.common.objects.AttributeInfo;
import org.identityconnectors.framework.common.objects.ObjectClassInfo;
import org.identityconnectors.framework.common.objects.OperationOptionInfo;
import org.identityconnectors.framework.common.objects.Schema;

public class FlatFileSchemaDiscovery {


	public static void main(String[] args) throws Exception {

//		File bundleDirectory = new File("C:/Reference-Useful/ICF/icf-samples-master/icf-clients/bundles");
//		URL connBundleUrl = IOUtil.makeURL(bundleDirectory,"icf_csvfile_bundle.jar");

		File bundleDirectory = new File("D:/Dropbox/work/bundles/flatfile/dist");
		URL connBundleUrl = IOUtil.makeURL(bundleDirectory,"org.identityconnectors.flatfile-1.0.1115.jar");
		
		ConnectorInfoManagerFactory factory = ConnectorInfoManagerFactory.getInstance();
		ConnectorInfoManager manager = factory.getLocalManager(connBundleUrl);
		List <ConnectorInfo> connInfoList = manager.getConnectorInfos();
		System.out.println("Number of Connectors defined in the bundle...."+connInfoList.size());
		FlatFileSchemaDiscovery flatFile = new FlatFileSchemaDiscovery();
		for (ConnectorInfo ci : connInfoList)
		{
			flatFile.printGeneralConnectorInfo(ci);
			flatFile.printConfigurationProperties(ci);
			ConnectorFacade connector = flatFile.createConnectorFacade(ci);
			flatFile.printSchema(connector);
			
		}//for
	}//main

	public void printGeneralConnectorInfo(ConnectorInfo ci)
	{
		System.out.println("\n============Printing General Connector Info==================");
		System.out.println("Connector Display Name: " +ci.getConnectorDisplayName());
		
		ConnectorKey cKey = ci.getConnectorKey();
		System.out.println("\nBundle Name: " +cKey.getBundleName());
		System.out.println("Bundle Version: " +cKey.getBundleVersion());
		System.out.println("Connector Name: " +cKey.getConnectorName());
		APIConfiguration apiConfig = ci.createDefaultAPIConfiguration();
		System.out.println("\nIs connection pooling supported ? " +apiConfig.isConnectorPoolingSupported());
	}
	
	public static ConnectorFacade createConnectorFacade(ConnectorInfo ci)
	{
		System.out.println("\n============Creating ConnectorFacade ==================");
		
		
		APIConfiguration apiConfig = ci.createDefaultAPIConfiguration();
		//Set all required properties needed by the connector
		ConfigurationProperties configProps = apiConfig.getConfigurationProperties();
		
	//	configProps.setPropertyValue("csvFileName", "c:/Reference-Useful/ICF/icf-samples-master/icf-csvfile-bundle/accounts.txt");
	//	configProps.setPropertyValue("delimiter", ",");
		
		configProps.setPropertyValue("schemaFile", "D:/Dropbox/work/bundles/flatfile/a.properties");
		
		ConnectorFacade connector =   ConnectorFacadeFactory.getInstance().newInstance(apiConfig);	
		connector.validate();
		
		return connector;
	 	
	}
	public void printConfigurationProperties(ConnectorInfo ci)
	{
		System.out.println("\n==============Configuration Properties defined============= \n");
		APIConfiguration apiConfig = ci.createDefaultAPIConfiguration();
		ConfigurationProperties configProps = apiConfig.getConfigurationProperties();
		
		for (String propName : configProps.getPropertyNames())
		{
			ConfigurationProperty cp = configProps.getProperty(propName);
			System.out.println("Property Name:" + cp.getName());
			System.out.println("Is Confidential:" + cp.isConfidential());
			System.out.println("Is Required:" + cp.isRequired());
			System.out.println("Type:" + cp.getType());
			System.out.println("Value:" + cp.getValue());
			System.out.println("Display Name:" + cp.getDisplayName(cp.getName()));
			System.out.println("Help Message:" + cp.getHelpMessage(cp.getName()));
			System.out.println("Operations for which this property must be specified:" + cp.getOperations());
			System.out.println("ObjectClasses for which this property must be specified:" + cp.getObjectClasses());
			System.out.println("Attributes for which this property must be specified:" + cp.getAttributes());
			System.out.println("----------------------------------------------------------------------------");
		}
	
	}
	
//	public static void printSchema(ConnectorFacade connector)
//	{
//		System.out.println("\n============Printing Schema==================");
//		
//		Schema schema = connector.schema();
//	
//		 System.out.println("Printing out the objectclasses:");		 
//		 System.out.println("=========================");
//		 // print out the schema fields
//		 Set<ObjectClassInfo> ociSet = schema.getObjectClassInfo();
//		 for (ObjectClassInfo oc: ociSet) {
//			 System.out.println(oc.getType());
//			 Set<AttributeInfo> aiSet = oc.getAttributeInfo();
//			 for (AttributeInfo ai: aiSet) {
//				 System.out.println("\t" + ai.getName() + " ==> " + ai.getType());
//			 }
//		 }	
//	}
	public void printSchema(ConnectorFacade connector)
	{
		System.out.println("\n============Printing Schema==================");
	 
		Set<Class< ? extends APIOperation>> ops = connector.getSupportedOperations();
		//This does NOT take into account if the supported operation is for a specific objectclass
		//System.out.println("SupportedOperations on Connector" + ops);
		
		
		Schema schema = connector.schema();

		System.out.println("==================Printing out the objectclasses and their attributes:=====================");		 
		 // print out the schema fields
		Set<ObjectClassInfo> objectClassInfoSet = schema.getObjectClassInfo();
		
		 //For each object class 
		 for (ObjectClassInfo oc: objectClassInfoSet) {
			 //Get Basic object class details
			 System.out.println("object class type : "+oc.getType());
			 System.out.println("object class : is containiner? : "+oc.isContainer());
			 System.out.println("object class : is embedded? : "+oc.isEmbedded());
			 
			 //Attributes of the object class
			 Set<AttributeInfo> aiSet = oc.getAttributeInfo();
			 for (AttributeInfo ai: aiSet) {
				 //getObjectClassName : For attributes that hold an embedded object, returns the name of the object class representing that object.
				 System.out.println("\t" + ai.getName() + " ==> " + ai.getType()+ "\t isCreateable "+ai.isCreateable()+"\t isMultiValued "+ai.isMultiValued()+"\t isReadable "+ai.isReadable()+"\t isRequired "+ai.isRequired()+"\t isReturnedByDefault "+ai.isReturnedByDefault()+"\t isUpdateble "+ai.isUpdateable()+"\t objectClassName "+ai.getObjectClassName());
			 }
		 }	
		 
		 //Supported operations for each of  the object class in the schema
		 
		 System.out.println("==================Printing Supported operations for the objectclasses:=====================");		 
		 Map<Class<? extends APIOperation>, Set<ObjectClassInfo>> opObClassSetMap = schema.getSupportedObjectClassesByOperation();
		 
		 Iterator<Class<? extends APIOperation>> it = opObClassSetMap.keySet().iterator();
		 while (it.hasNext()) {
			 Object op = it.next();
			 Set<ObjectClassInfo> ocinfoSet = (Set<ObjectClassInfo>)opObClassSetMap.get(op);
			 //operation name
			 System.out.print(op + " ==> " );
			 
			 //object class set
			 for (ObjectClassInfo oc: ocinfoSet) {
				 System.out.print(oc.getType() + " " );
			 }
			 System.out.println();
		 }
		 
		 testReverse(schema);
		 
		 System.out.println("==================Printing Supported Options for the objectclasses:=====================");		 
		 
			Set<OperationOptionInfo> ooinfoList =schema.getOperationOptionInfo();
			
			System.out.println("Number of OperationOptionInfo listed in the connector "+ooinfoList.size());
		 Map<Class<? extends APIOperation>, Set<OperationOptionInfo>> opOptionSetMap = schema.getSupportedOptionsByOperation();
		 
		 Iterator<Class<? extends APIOperation>> it1 = opOptionSetMap.keySet().iterator();
		 while (it1.hasNext()) {
			 Object op1 = it1.next();
			 Set<OperationOptionInfo> opOptionInfoSet = (Set<OperationOptionInfo>)opOptionSetMap.get(op1);
			 //operation name
			 System.out.print(op1 + " ==> " );
			 
			 //OperationOptionInfo
			 for (OperationOptionInfo oc: opOptionInfoSet) {
				 System.out.print("type=> "+oc.getType() + " name => " +oc.getName());
			 }
			 System.out.println();
		 }
	}

	public void testReverse(Schema schema)
	{
		Map <String,Set<String>> ocToOperationMap = new HashMap<String,Set<String>>(); 
		
		 System.out.println("==================Printing Supported REVERSE for the objectclasses:=====================");		 
		 Map<Class<? extends APIOperation>, Set<ObjectClassInfo>> opObClassSetMap = schema.getSupportedObjectClassesByOperation();
		 for (Class<? extends APIOperation> operation : opObClassSetMap.keySet())
		 {
			 Set<ObjectClassInfo> ocinfoSet = (Set<ObjectClassInfo>)opObClassSetMap.get(operation);

			 for (ObjectClassInfo oc: ocinfoSet) {
				 Set<String> operationSet;
				if( !ocToOperationMap.containsKey(oc.getType()))
					operationSet = new LinkedHashSet<String>();					 
				else
					operationSet = ocToOperationMap.get(oc.getType());
				
				operationSet.add(operation.getName());
				ocToOperationMap.put(oc.getType(),operationSet);

			 }
		 }
	//printing end value
		 System.out.println("=================REVERSED==================");
		 for (String objectClassName: ocToOperationMap.keySet())
		 {
			 
			 System.out.println(objectClassName + "==>");
			 
			 Set<String> operations = (Set<String>)ocToOperationMap.get(objectClassName);
			 for(String operationName : operations)
			 {
				 System.out.println(operationName);
			 }
			 
		 }
		 
	}
	

}