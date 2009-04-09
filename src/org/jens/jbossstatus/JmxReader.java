package org.jens.jbossstatus;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.naming.Context;
import javax.naming.NamingException;

/**
 * The Class JmxReader.
 */
public class JmxReader {

	/** The server. */
	MBeanServerConnection server = null;

	/**
	 * Instantiates a new jmx reader.
	 *
	 * @param ctx the ctx
	 *
	 * @throws NamingException the naming exception
	 */
	public JmxReader(Context ctx) throws NamingException {
		server = (MBeanServerConnection) ctx.lookup("jmx/invoker/RMIAdaptor");
	}

	/*
	 *
	 * JDBC
	 *
	 */


	/**
	 * List jdbc.
	 *
	 * @return the string[]
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String[] listJdbc() throws IOException {
		Set<ObjectName> names = new TreeSet<ObjectName>(server.queryNames(null, null));
		ArrayList<ObjectName> list = new  ArrayList<ObjectName>(names);
		ArrayList<String> result = new ArrayList<String>();
		for(ObjectName it : list) {
			String line = it.getCanonicalName();
			if (line.startsWith("jboss.jca:") && line.endsWith("service=DataSourceBinding")) {
				line = line.substring(15);
				line = line.substring(0,line.length()-26);
				result.add(line);
			}
		}
		return result.toArray(new String[result.size()]);
	}


	/**
	 * Managed connection pool.
	 *
	 * @param jndi the jndi
	 *
	 * @return the object name
	 *
	 * @throws MalformedObjectNameException the malformed object name exception
	 * @throws NullPointerException the null pointer exception
	 */
	private ObjectName managedConnectionPool(String jndi) throws MalformedObjectNameException {
		return new ObjectName("jboss.jca:name=" + jndi + ",service=ManagedConnectionPool");
	}

	/**
	 * Gets the max connections in use.
	 *
	 * @param jndi the jndi
	 *
	 * @return the max connections in use
	 *
	 * @throws AttributeNotFoundException the attribute not found exception
	 * @throws InstanceNotFoundException the instance not found exception
	 * @throws MalformedObjectNameException the malformed object name exception
	 * @throws MBeanException the m bean exception
	 * @throws ReflectionException the reflection exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Long getJdbcMaxConnectionsInUse(String jndi) throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException {
		return (Long) server.getAttribute(managedConnectionPool(jndi), "MaxConnectionsInUseCount");
	}

	/**
	 * Gets the max size.
	 *
	 * @param server the server
	 * @param jndi the jndi
	 *
	 * @return the max size
	 *
	 * @throws AttributeNotFoundException the attribute not found exception
	 * @throws InstanceNotFoundException the instance not found exception
	 * @throws MalformedObjectNameException the malformed object name exception
	 * @throws MBeanException the m bean exception
	 * @throws ReflectionException the reflection exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Integer getJdbcMaxSize(String jndi) throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException  {
		return (Integer) server.getAttribute(managedConnectionPool(jndi), "MaxSize");
	}

	/**
	 * Gets the connection count.
	 *
	 * @param server the server
	 * @param jndi the jndi
	 *
	 * @return the connection count
	 *
	 * @throws AttributeNotFoundException the attribute not found exception
	 * @throws InstanceNotFoundException the instance not found exception
	 * @throws MalformedObjectNameException the malformed object name exception
	 * @throws MBeanException the m bean exception
	 * @throws ReflectionException the reflection exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Integer getJdbcConnectionCount(String jndi) throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException  {
		return (Integer) server.getAttribute(managedConnectionPool(jndi), "ConnectionCount");
	}

	/**
	 * Gets the available connection count.
	 *
	 * @param server the server
	 * @param jndi the jndi
	 *
	 * @return the available connection count
	 *
	 * @throws AttributeNotFoundException the attribute not found exception
	 * @throws InstanceNotFoundException the instance not found exception
	 * @throws MalformedObjectNameException the malformed object name exception
	 * @throws MBeanException the m bean exception
	 * @throws ReflectionException the reflection exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Long getJdbcAvailableConnectionCount(String jndi) throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException  {
		return (Long) server.getAttribute(managedConnectionPool(jndi), "AvailableConnectionCount");
	}

	/**
	 * Gets the idle timeout minutes.
	 *
	 * @param server the server
	 * @param jndi the jndi
	 *
	 * @return the idle timeout minutes
	 *
	 * @throws AttributeNotFoundException the attribute not found exception
	 * @throws InstanceNotFoundException the instance not found exception
	 * @throws MalformedObjectNameException the malformed object name exception
	 * @throws MBeanException the m bean exception
	 * @throws ReflectionException the reflection exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Long getJdbcIdleTimeoutMinutes(String jndi) throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException  {
		return (Long) server.getAttribute(managedConnectionPool(jndi), "IdleTimeoutMinutes");
	}

	/**
	 * Gets the in use connection count.
	 *
	 * @param server the server
	 * @param jndi the jndi
	 *
	 * @return the in use connection count
	 *
	 * @throws AttributeNotFoundException the attribute not found exception
	 * @throws InstanceNotFoundException the instance not found exception
	 * @throws MalformedObjectNameException the malformed object name exception
	 * @throws MBeanException the m bean exception
	 * @throws ReflectionException the reflection exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Long getJdbcInUseConnectionCount(String jndi) throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException  {
		return (Long) server.getAttribute(managedConnectionPool(jndi), "InUseConnectionCount");
	}

	/**
	 * Gets the min size.
	 *
	 * @param jndi the jndi
	 *
	 * @return the min size
	 *
	 * @throws AttributeNotFoundException the attribute not found exception
	 * @throws InstanceNotFoundException the instance not found exception
	 * @throws MalformedObjectNameException the malformed object name exception
	 * @throws MBeanException the m bean exception
	 * @throws ReflectionException the reflection exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Integer getJdbcMinSize(String jndi) throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException  {
		return (Integer) server.getAttribute(managedConnectionPool(jndi), "MinSize");
	}

	/*
	 *
	 * Transactions
	 *
	 */

	/**
	 * Jboss transaction manager.
	 *
	 * @return the object name
	 *
	 * @throws MalformedObjectNameException the malformed object name exception
	 */
	private ObjectName jbossTransactionManager() throws MalformedObjectNameException {
		return new ObjectName("jboss:service=TransactionManager");
	}

	/**
	 * Gets the transaction timeout.
	 *
	 * @param server the server
	 *
	 * @return the transaction timeout
	 *
	 * @throws AttributeNotFoundException the attribute not found exception
	 * @throws InstanceNotFoundException the instance not found exception
	 * @throws MalformedObjectNameException the malformed object name exception
	 * @throws MBeanException the m bean exception
	 * @throws ReflectionException the reflection exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Integer getTransactionTimeout() throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException  {
		return (Integer) server.getAttribute(jbossTransactionManager(), "TransactionTimeout");
	}

	/**
	 * Gets the transaction count.
	 *
	 * @param server the server
	 *
	 * @return the transaction count
	 *
	 * @throws AttributeNotFoundException the attribute not found exception
	 * @throws InstanceNotFoundException the instance not found exception
	 * @throws MalformedObjectNameException the malformed object name exception
	 * @throws MBeanException the m bean exception
	 * @throws ReflectionException the reflection exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Long getTransactionCount() throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException  {
		return (Long) server.getAttribute(jbossTransactionManager(), "TransactionCount");
	}

	/**
	 * Gets the commit count.
	 *
	 * @param server the server
	 *
	 * @return the commit count
	 *
	 * @throws AttributeNotFoundException the attribute not found exception
	 * @throws InstanceNotFoundException the instance not found exception
	 * @throws MalformedObjectNameException the malformed object name exception
	 * @throws MBeanException the m bean exception
	 * @throws ReflectionException the reflection exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Long getTransactionCommitCount() throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException  {
		return (Long) server.getAttribute(jbossTransactionManager(), "CommitCount");
	}

	/**
	 * Gets the rollback count.
	 *
	 * @param server the server
	 *
	 * @return the rollback count
	 *
	 * @throws AttributeNotFoundException the attribute not found exception
	 * @throws InstanceNotFoundException the instance not found exception
	 * @throws MalformedObjectNameException the malformed object name exception
	 * @throws MBeanException the m bean exception
	 * @throws ReflectionException the reflection exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Long getTransactionRollbackCount() throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException  {
		return (Long) server.getAttribute(jbossTransactionManager(), "RollbackCount");
	}

	/**
	 *
	 * EJB
	 *
	 * ear=FotoBrowser.ear,jar=FotoBrowserEJB.jar,name=FotoBrowserBean,service=EJB3
	 * @throws IOException
	 */

	private ObjectName findEjb(String name) throws IOException {
		Set<ObjectName> names = new TreeSet<ObjectName>(server.queryNames(null, null));
		ArrayList<ObjectName> list = new  ArrayList<ObjectName>(names);
		Pattern p = Pattern.compile("^jboss.j2ee:.*name="+name+".*service=EJB3");
		for(ObjectName it : list) {
			String line = it.getCanonicalName();
			Matcher m = p.matcher(line);
			if (m.find()) {
					return it;
			}
		}
		return null;
	}



	/**
	 * List ejb.
	 *
	 * @return the string[]
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws MalformedObjectNameException the malformed object name exception
	 */
	public String[] listEjb() throws IOException, MalformedObjectNameException {
		Set<ObjectName> names = new TreeSet<ObjectName>(server.queryNames(null, null));
		ArrayList<ObjectName> list = new  ArrayList<ObjectName>(names);
		ArrayList<String> result = new ArrayList<String>();
		Pattern p = Pattern.compile("^jboss.j2ee:.*name=(\\w+).*service=EJB3");
		for(ObjectName it : list) {
			String line = it.getCanonicalName();
			Matcher m = p.matcher(line);
			if (m.find()) {
					String find = m.group(1);
					result.add(find);
			}
		}
		return result.toArray(new String[result.size()]);
	}

	public String[] listEjbLong() throws IOException, MalformedObjectNameException {
		Set<ObjectName> names = new TreeSet<ObjectName>(server.queryNames(null, null));
		ArrayList<ObjectName> list = new  ArrayList<ObjectName>(names);
		ArrayList<String> result = new ArrayList<String>();
		Pattern p = Pattern.compile("^jboss.j2ee:.*name=(\\w+).*service=EJB3");
		for(ObjectName it : list) {
			String line = it.getCanonicalName();
			Matcher m = p.matcher(line);
			if (m.find()) {
					result.add(line);
			}
		}
		return result.toArray(new String[result.size()]);
	}

	public Integer getEjbCreateCount(String name) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException{
		return (Integer) server.getAttribute(findEjb(name), "CreateCount");
	}

	public int getEjbCurrentSize(String ejbName) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		return (Integer) server.getAttribute(findEjb(ejbName), "CurrentSize");
	}

	public int getEjbRemoveCount(String ejbName) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		return (Integer) server.getAttribute(findEjb(ejbName), "RemoveCount");
	}

	public int getEjbMaxSize(String ejbName) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		return (Integer) server.getAttribute(findEjb(ejbName), "MaxSize");
	}

	public int getEjbAvailableCount(String ejbName) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		return (Integer) server.getAttribute(findEjb(ejbName), "AvailableCount");
	}

	/*
	 *
	 *  WebServices
	 *
	 */

	public String[] listWebServices() throws IOException {
		Set<ObjectName> names = new TreeSet<ObjectName>(server.queryNames(null, null));
		ArrayList<ObjectName> list = new  ArrayList<ObjectName>(names);
		ArrayList<String> result = new ArrayList<String>();
		Pattern p = Pattern.compile("^jboss.ws:context=.+,endpoint=([^,]+)$");
		for(ObjectName it : list) {
			String line = it.getCanonicalName();
			Matcher m = p.matcher(line);
			if (m.find()) {
				if (!line.contains("recordProcessor")) {
					result.add(m.group(1));
				}
			}
		}
		return result.toArray(new String[result.size()]);
	}

	private ObjectName findWs(String name) throws IOException {
		Set<ObjectName> names = new TreeSet<ObjectName>(server.queryNames(null, null));
		ArrayList<ObjectName> list = new  ArrayList<ObjectName>(names);
		Pattern p = Pattern.compile("^jboss.ws:context=.+,endpoint="+name+"$");
		for(ObjectName it : list) {
			String line = it.getCanonicalName();
			Matcher m = p.matcher(line);
			if (m.find()) {
					return it;
			}
		}
		return null;
	}

	public Long getWsMinProcessingTime(String wsName) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		return (Long) server.getAttribute(findWs(wsName), "MinProcessingTime");
	}

	public Long getWsFaultCount(String wsName) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		return (Long) server.getAttribute(findWs(wsName), "FaultCount");
	}

	public Long getWsRequestCount(String wsName) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		return (Long) server.getAttribute(findWs(wsName), "RequestCount");
	}

	public Long getWsResponseCount(String wsName) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		return (Long) server.getAttribute(findWs(wsName), "ResponseCount");
	}

	public Date getWsStartTime(String wsName) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		return (Date) server.getAttribute(findWs(wsName), "StartTime");
	}

	public Long getWsTotalProcessingTime(String wsName) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		return (Long) server.getAttribute(findWs(wsName), "TotalProcessingTime");
	}

	public Long getWsAverageProcessingTime(String wsName) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		return (Long) server.getAttribute(findWs(wsName), "AverageProcessingTime");
	}

	public Long getWsMaxProcessingTime(String wsName) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		return (Long) server.getAttribute(findWs(wsName), "MaxProcessingTime");
	}

	private ObjectName getSysInfoObject() throws MalformedObjectNameException, NullPointerException {
		return new ObjectName("jboss.system:type=ServerInfo");
	}

	private ObjectName getJavaInternalMemory() throws MalformedObjectNameException, NullPointerException {
		return new ObjectName(ManagementFactory.MEMORY_MXBEAN_NAME);
	}


	public JBossSysinfo getSysInfo() throws MalformedObjectNameException, NullPointerException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		/*
		Set<ObjectName> names = new TreeSet<ObjectName>(server.queryNames(null, null));
		ArrayList<ObjectName> list = new  ArrayList<ObjectName>(names);
		for(ObjectName it : list) {
			String line = it.getCanonicalName();
			System.out.println(line);
		}
		return null;
*/

		ObjectName obj = getSysInfoObject();
		JBossSysinfo result = new JBossSysinfo();
		result.setActiveThreadCount((Integer)server.getAttribute(obj, "ActiveThreadCount"));
		result.setAvailableProcessors((Integer)server.getAttribute(obj, "AvailableProcessors"));
		result.setOSArch((String)server.getAttribute(obj, "OSArch"));
		result.setMaxMemory((Long)server.getAttribute(obj, "MaxMemory"));
		result.setHostAddress((String)server.getAttribute(obj, "HostAddress"));
		result.setJavaVersion((String)server.getAttribute(obj, "JavaVersion"));
		result.setOSVersion((String)server.getAttribute(obj, "OSVersion"));
		result.setJavaVendor((String)server.getAttribute(obj, "JavaVendor"));
		result.setTotalMemory((Long)server.getAttribute(obj, "TotalMemory"));
		result.setActiveThreadGroupCount((Integer)server.getAttribute(obj, "ActiveThreadGroupCount"));
		result.setOSName((String)server.getAttribute(obj, "OSName"));
		result.setFreeMemory((Long)server.getAttribute(obj, "FreeMemory"));
		result.setHostName((String)server.getAttribute(obj, "HostName"));
		result.setJavaVMVersion((String)server.getAttribute(obj, "JavaVMVersion"));
		result.setJavaVMVendor((String)server.getAttribute(obj, "JavaVMVendor"));
		result.setJavaVMName((String)server.getAttribute(obj, "JavaVMName"));



		return result;
	}

	public String getMemoryUsageAsString(boolean graph) throws MalformedObjectNameException, NullPointerException, InstanceNotFoundException, IOException, IntrospectionException, ReflectionException, MBeanException {
		ObjectName name = getSysInfoObject();
		return (String)server.invoke(name, "listMemoryPools", new Object[] { graph }, new String[] { "boolean" });
//		name = getJavaInternalMemory();
//		result = server.invoke(name, "getHeapMemoryUsage", null, null);
		//debug(result);
/*
		Set<ObjectName> names = new TreeSet<ObjectName>(server.queryNames(null, null));
		ArrayList<ObjectName> list = new  ArrayList<ObjectName>(names);
		for(ObjectName it : list) {
			String line = it.getCanonicalName();
			System.out.println(line);
		}
		*/
	}

	public void debug(Object it) {
		if (it == null) {
			System.out.println("NULL");
			return;
		}
		System.out.println("CLASS: " + it.getClass().getName());
		System.out.println("VALUE:"  + it);
	}

}
