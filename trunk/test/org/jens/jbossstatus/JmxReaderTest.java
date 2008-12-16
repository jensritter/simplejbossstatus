package org.jens.jbossstatus;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Date;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.jens.Shorthand.JNDI;
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
	
	public static final String jndi="jdbc/foto";

	
	@Before
	public void init() throws NamingException {
		ctx = JNDI.jbossContext("matrix.jens.org");
		reader = new JmxReader(ctx);
	}

	@Test
	public void testJmxReader() throws NamingException {
		JmxReader test = new JmxReader(ctx);
		assertNotNull(test);
	}

	@Test
	public void testGetMaxConnectionsInUse() throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException {
		assertTrue(reader.getJdbcMaxConnectionsInUse(jndi) >= 0);
	}

	@Test
	public void testGetMaxSize() throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException {
		assertEquals(MAXSIZE,reader.getJdbcMaxSize(jndi));
	}

	@Test
	public void testGetConnectionCount() throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException {
		assertTrue(reader.getJdbcConnectionCount(jndi) >= CONNECTIONCOUNT);
	}

	@Test
	public void testGetAvailableConnectionCount() throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException {
		assertEquals(AVAILABLECONNECTIONCOUNT,reader.getJdbcAvailableConnectionCount(jndi));
	}

	@Test
	public void testGetIdleTimeoutMinutes() throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException {
		assertEquals(IDLETIMEOUTMINUTES,reader.getJdbcIdleTimeoutMinutes(jndi));
	}

	@Test
	public void testGetInUseConnectionCount() throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException {
		assertEquals(INUSECONNECTIONCOUNT,reader.getJdbcInUseConnectionCount(jndi));
	}

	@Test
	public void testGetMinSize() throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException {
		assertEquals(MINSIZE,reader.getJdbcMinSize(jndi));
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
	public void listEjbTest() throws MalformedObjectNameException, NullPointerException, IOException {
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
