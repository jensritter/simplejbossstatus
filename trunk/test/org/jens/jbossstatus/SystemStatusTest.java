package org.jens.jbossstatus;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Properties;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;

public class SystemStatusTest {

	private static final String HOSTNAME="matrix";
	private static final int PORT=1099;
	private InitialContext ctx;
	private JmxReader reader;

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
	public void testSimple() {
		try {
			JBossSysinfo result = reader.getSysInfo();
			assertNotNull(result);

			assertTrue(result.getActiveThreadCount()>1);
			assertEquals(2,result.getAvailableProcessors());
			assertEquals("amd64",result.getOSArch());
			assertEquals(954466304L,result.getMaxMemory());
			assertEquals("192.168.100.1",result.getHostAddress());
			assertTrue(result.getJavaVersion().startsWith("1.6"));
			assertTrue(result.getOSVersion().startsWith("2.6"));
			assertEquals("Sun Microsystems Inc.",result.getJavaVendor());
			assertEquals(465895424L,result.getTotalMemory());
			assertEquals(10,result.getActiveThreadGroupCount());
			assertEquals("Linux",result.getOSName());
			assertTrue(result.getFreeMemory()> 10000);
			assertEquals("matrix.jens.org",result.getHostName());
			assertNotNull(result.getJavaVMVersion());
			assertEquals("Sun Microsystems Inc.",result.getJavaVMVendor());
			assertEquals("Java HotSpot(TM) 64-Bit Server VM",result.getJavaVMName());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AttributeNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstanceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MBeanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReflectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testMemory() throws MalformedObjectNameException, InstanceNotFoundException, NullPointerException, IOException, IntrospectionException, ReflectionException, MBeanException {
		reader.getMemoryUsageAsString(true);
	}
}
