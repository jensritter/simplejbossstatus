package org.jens.jbossstatus;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Properties;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;

public class JmxReaderTest {
	
	private static final String WS_NAME = "FotoBrowser-FotoBrowserEJB";
	private static final String EJB_NAME = "FotoBrowserBean";
	private static final int MAXSIZE = 20;
	private static final int CONNECTIONCOUNT = 0;
	private static final long AVAILABLECONNECTIONCOUNT = 20L;
	private static final long IDLETIMEOUTMINUTES = 1;
	private static final long INUSECONNECTIONCOUNT = 0;
	private static final int MINSIZE = 0;
	private static final int TRANSACTIONTIMEOUT = 300;
	private static final long TRANSACTIONCOUNT = 0;
	private static final long COMMITCOUNT = 0;
	private static final long ROLLBACKCOUNT = 0;
	Context ctx = null;
	JmxReader reader = null;
	
	public static final String JNDI_JDBC="jdbc/foto";
	public static final String HOSTNAME="localhost";
	public static final int PORT=1099;

	
	@Before
	public void init() throws NamingException {
		Properties env = new Properties();
		env.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		env.setProperty("java.naming.provider.url", HOSTNAME + ":"+PORT);
		env.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
		ctx = new InitialContext(env);
		reader = new JmxReader(ctx);
	}

	@Test
	public void testJmxReader() throws NamingException {
		JmxReader test = new JmxReader(ctx);
		assertNotNull(test);
	}

	@Test
	public void testGetMaxConnectionsInUse() throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException {
		assertTrue(reader.getJdbcMaxConnectionsInUse(JNDI_JDBC) >= 0);
	}

	@Test
	public void testGetMaxSize() throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException {
		assertEquals(MAXSIZE,reader.getJdbcMaxSize(JNDI_JDBC));
	}

	@Test
	public void testGetConnectionCount() throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException {
		assertTrue(reader.getJdbcConnectionCount(JNDI_JDBC) >= CONNECTIONCOUNT);
	}

	@Test
	public void testGetAvailableConnectionCount() throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException {
		assertEquals(AVAILABLECONNECTIONCOUNT,reader.getJdbcAvailableConnectionCount(JNDI_JDBC));
	}

	@Test
	public void testGetIdleTimeoutMinutes() throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException {
		assertEquals(IDLETIMEOUTMINUTES,reader.getJdbcIdleTimeoutMinutes(JNDI_JDBC));
	}

	@Test
	public void testGetInUseConnectionCount() throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException {
		assertEquals(INUSECONNECTIONCOUNT,reader.getJdbcInUseConnectionCount(JNDI_JDBC));
	}

	@Test
	public void testGetMinSize() throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException {
		assertEquals(MINSIZE,reader.getJdbcMinSize(JNDI_JDBC));
	}

	@Test
	public void testGetTransactionTimeout() throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException {
		assertEquals(TRANSACTIONTIMEOUT,reader.getTransactionTimeout());
	}

	@Test
	public void testGetTransactionCount() throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException {
		assertEquals(TRANSACTIONCOUNT,reader.getTransactionCount());
	}

	@Test
	public void testGetCommitCount() throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException {
		assertEquals(COMMITCOUNT,reader.getTransactionCommitCount());
	}

	@Test
	public void testGetRollbackCount() throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException {
		assertEquals(ROLLBACKCOUNT,reader.getTransactionRollbackCount());
	}
	
	@Test
	public void listEjbTest() throws MalformedObjectNameException, IOException {
		String[] lst = reader.listEjb();
		boolean find = false;
		for(String it : lst) {
			if (it.equals(EJB_NAME)) {
				find = true;
			}
		}
		assertTrue(find);
	}
	
	@Test
	public void listEjbLongTest() throws MalformedObjectNameException, IOException {
		String[] lst = reader.listEjbLong();
		boolean find = false;
		for(String it : lst) {
			if (it.equals("jboss.j2ee:ear=FotoBrowser.ear,jar=FotoBrowserEJB.jar,name=FotoBrowserBean,service=EJB3")) {
				find = true;
			}
		}
		assertTrue(find);
	}
	
	@Test
	public void listJdbcTest() throws IOException {
		String[] list = reader.listJdbc();
		boolean findit = false;
		for(String it : list) {
			if (it.equals("jdbc/foto")) {
				findit = true;
			}
		}
		assertTrue(findit);
	}
	
	@Test
	public void getEjbCreateCountTest() throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		assertTrue(reader.getEjbCreateCount(EJB_NAME) >= 0);
	}
	
	@Test
	public void getEjbCurrentSizeTest() throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		assertTrue(reader.getEjbCurrentSize(EJB_NAME) >= 0);
	}
	
	@Test
	public void getEjbRemoveCountTest() throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		assertTrue(reader.getEjbRemoveCount(EJB_NAME) >= 0);
	}
	
	@Test
	public void getEjbMaxSizeTest() throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		assertTrue(reader.getEjbMaxSize(EJB_NAME) >= 0);
	}
	
	@Test
	public void getEjbAvailableCountTest() throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		assertTrue(reader.getEjbAvailableCount(EJB_NAME) >= 0);
	}
	
	@Test
	public void listWebservicesTest() throws IOException {
		String[] lst = reader.listWebServices();
		boolean find = false;
		for(String it : lst) {
			if (it.equals(WS_NAME)) {
				find = true;
			}
		}
		assertTrue(find);
	}
	
	@Test
	public void getWsMinProcessingTimetest() throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		assertTrue(reader.getWsMinProcessingTime(WS_NAME) >= 0);
	}
	
	@Test
	public void getWsFaulCountTest() throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		assertTrue(reader.getWsFaultCount(WS_NAME) >= 0);
	}
	
	@Test
	public void getWsRequestCountTest() throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		assertTrue(reader.getWsRequestCount(WS_NAME) >= 0);
	}
	
	@Test
	public void getWsResponseCount() throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		assertTrue(reader.getWsResponseCount(WS_NAME) >= 0);
	}
	
	@Test
	public void getWsStartTime() throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		assertEquals(java.util.Date.class,reader.getWsStartTime(WS_NAME).getClass());
		assertNotNull(reader.getWsStartTime(WS_NAME));
	}
	
	@Test
	public void getWsTotalProcessingTimeTest() throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		assertTrue(reader.getWsTotalProcessingTime(WS_NAME) >= 0);
	}
	
	@Test
	public void getWsAverageProcessingTime() throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		assertTrue(reader.getWsAverageProcessingTime(WS_NAME) >= 0);
	}
	
	@Test
	public void getWsMaxProcessingTime() throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		assertTrue(reader.getWsMaxProcessingTime(WS_NAME) >= 0);
	}
	
	

}
