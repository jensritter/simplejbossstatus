package org.jens.jbossstatus;

import java.io.IOException;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

// TODO: Auto-generated Javadoc
/**
 * Servlet implementation class JdbcUsageGraph.
 */
public class Graph extends HttpServlet {
	private final Log logger = LogFactory.getLog(Graph.class);
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new graph.
     */
    public Graph() {
        super();
    }

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Context ctx = new InitialContext();
			JmxReader reader = new JmxReader(ctx);
			
			String jdbc = request.getParameter("jdbc");
			String ejb = request.getParameter("ejb");
			String ws = request.getParameter("ws");
			
			CronJob job = new CronJob();
			
			JFreeChart chart = null;
			if (jdbc != null && !jdbc.equals("")) {
				 chart = getConnectionCountFromSingleJdbcSmall(reader,job.getDbName(jdbc));	
			}
			if (ejb != null && !ejb.equals("")) {
				chart = getCreateCount(reader,job.getDbName(ejb));
			}
			if (ws != null && !ws.equals("")) {
				chart = getFailureCount(reader,job.getDbName(ws));
			}
			response.setContentType("image/png");
			ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 600, 50);
		} catch (NamingException e) {
			logger.error(e.getMessage(),e);
		} catch (AttributeNotFoundException e) {
			logger.error(e.getMessage(),e);
		} catch (InstanceNotFoundException e) {
			logger.error(e.getMessage(),e);
		} catch (MalformedObjectNameException e) {
			logger.error(e.getMessage(),e);
		} catch (MBeanException e) {
			logger.error(e.getMessage(),e);
		} catch (ReflectionException e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * Gets the connection count from single jdbc.
	 * 
	 * @param reader the reader
	 * @param jdbc the jdbc
	 * 
	 * @return the connection count from single jdbc
	 * 
	 * @throws AttributeNotFoundException the attribute not found exception
	 * @throws InstanceNotFoundException the instance not found exception
	 * @throws MalformedObjectNameException the malformed object name exception
	 * @throws MBeanException the m bean exception
	 * @throws ReflectionException the reflection exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static JFreeChart getConnectionCountFromSingleJdbc(JmxReader reader, String jdbc) throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException {
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		data.addValue(reader.getJdbcMaxConnectionsInUse(jdbc),"used","ConnectionCount");
		data.addValue(reader.getJdbcAvailableConnectionCount(jdbc),"Free","ConnectionCount");

		
		JFreeChart chart = ChartFactory.createStackedBarChart(jdbc,"", "", data, PlotOrientation.HORIZONTAL, false, false, false);
		
		return chart;
	}
	
	/**
	 * Gets the connection count from single jdbc small.
	 * 
	 * @param reader the reader
	 * @param jdbc the jdbc
	 * 
	 * @return the connection count from single jdbc small
	 * 
	 * @throws AttributeNotFoundException the attribute not found exception
	 * @throws InstanceNotFoundException the instance not found exception
	 * @throws MalformedObjectNameException the malformed object name exception
	 * @throws MBeanException the m bean exception
	 * @throws ReflectionException the reflection exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private JFreeChart getConnectionCountFromSingleJdbcSmall(JmxReader reader, String jdbc) throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException {
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		int used = reader.getJdbcConnectionCount(jdbc);
		data.addValue(used,"used","");
		data.addValue(reader.getJdbcAvailableConnectionCount(jdbc) - used,"Free","");
		
		
		
		JFreeChart chart = ChartFactory.createStackedBarChart("","", "", data, PlotOrientation.HORIZONTAL, false, false, false);
		return chart;
	}
	
	/**
	 * Gets the connection count from all jdbc.
	 * 
	 * @param reader the reader
	 * 
	 * @return the connection count from all jdbc
	 * 
	 * @throws AttributeNotFoundException the attribute not found exception
	 * @throws InstanceNotFoundException the instance not found exception
	 * @throws MalformedObjectNameException the malformed object name exception
	 * @throws MBeanException the m bean exception
	 * @throws ReflectionException the reflection exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static JFreeChart getConnectionCountFromAllJdbc(JmxReader reader) throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException {
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		String[] lst = reader.listJdbc();
		for(String jdbc: lst) {
			data.addValue(reader.getJdbcInUseConnectionCount(jdbc),"used",jdbc);
			data.addValue(reader.getJdbcAvailableConnectionCount(jdbc),"Free",jdbc);
		}
		
		JFreeChart chart = ChartFactory.createStackedBarChart("All Connections","", "", data, PlotOrientation.HORIZONTAL, false, false, false);
		return chart;
	}
	
	

	/**
	 * Gets the creates the count.
	 * 
	 * @param reader the reader
	 * @param ejb the ejb
	 * 
	 * @return the creates the count
	 * 
	 * @throws AttributeNotFoundException the attribute not found exception
	 * @throws InstanceNotFoundException the instance not found exception
	 * @throws MBeanException the m bean exception
	 * @throws ReflectionException the reflection exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private JFreeChart getCreateCount(JmxReader reader, String ejb) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		int used = reader.getEjbCurrentSize(ejb);
		data.addValue(used,"used","");
		data.addValue(reader.getEjbMaxSize(ejb) - used,"Free","");
		
		JFreeChart chart = ChartFactory.createStackedBarChart("","", "", data, PlotOrientation.HORIZONTAL, false, false, false);
		return chart;
	}
	
	/**
	 * Gets the failure count.
	 * 
	 * @param reader the reader
	 * @param wsName the ws name
	 * 
	 * @return the failure count
	 * 
	 * @throws AttributeNotFoundException the attribute not found exception
	 * @throws InstanceNotFoundException the instance not found exception
	 * @throws MBeanException the m bean exception
	 * @throws ReflectionException the reflection exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private JFreeChart getFailureCount(JmxReader reader, String wsName) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		Long failure = reader.getWsFaultCount(wsName);
		data.addValue(failure,"Failure","");
		data.addValue(reader.getWsRequestCount(wsName) - failure,"Processed","");
		
		JFreeChart chart = ChartFactory.createStackedBarChart("","", "", data, PlotOrientation.HORIZONTAL, false, false, false);
		return chart;
	}
	
	
}
