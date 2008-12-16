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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Servlet implementation class JdbcUsageGraph
 */
public class JdbcUsageGraph extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JdbcUsageGraph() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Context ctx = new InitialContext();
			JmxReader reader = new JmxReader(ctx);
			String jdbc = request.getParameter("jdbc");
			if (jdbc == null ) {
				jdbc="DefaultDS";
			}
			
			
			JFreeChart chart = getConnectionCountFromSingleJdbcSmall(reader,jdbc);
			//JFreeChart chart = getConnectionCountFromAllJdbc(reader);
			response.setContentType("image/png");

			ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 600, 50);
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (AttributeNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstanceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedObjectNameException e) {
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
	
	private JFreeChart getConnectionCountFromSingleJdbc(JmxReader reader, String jdbc) throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException {
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		data.addValue(reader.getJdbcMaxConnectionsInUse(jdbc),"used","ConnectionCount");
		data.addValue(reader.getJdbcAvailableConnectionCount(jdbc),"Free","ConnectionCount");

		
		JFreeChart chart = ChartFactory.createStackedBarChart(jdbc,"", "", data, PlotOrientation.HORIZONTAL, false, false, false);
		
		return chart;
	}
	
	private JFreeChart getConnectionCountFromSingleJdbcSmall(JmxReader reader, String jdbc) throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException {
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		int used = reader.getJdbcConnectionCount(jdbc);
		data.addValue(used,"used","");
		data.addValue(reader.getJdbcAvailableConnectionCount(jdbc) - used,"Free","");
		
		
		
		JFreeChart chart = ChartFactory.createStackedBarChart("","", "", data, PlotOrientation.HORIZONTAL, false, false, false);
		return chart;
	}
	
	private JFreeChart getConnectionCountFromAllJdbc(JmxReader reader) throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, IOException {
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		String[] lst = reader.listJdbc();
		for(String jdbc: lst) {
			data.addValue(reader.getJdbcInUseConnectionCount(jdbc),"used",jdbc);
			data.addValue(reader.getJdbcAvailableConnectionCount(jdbc),"Free",jdbc);
		}
		
		JFreeChart chart = ChartFactory.createStackedBarChart("All Connections","", "", data, PlotOrientation.HORIZONTAL, false, false, false);
		return chart;
	}
	
	
}
