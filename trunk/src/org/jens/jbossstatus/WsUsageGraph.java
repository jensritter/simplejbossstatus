package org.jens.jbossstatus;

import java.io.IOException;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
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
 * Servlet implementation class WsUsageGraph
 */
public class WsUsageGraph extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WsUsageGraph() {
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
			String ejb = request.getParameter("ejb");

			
			JFreeChart chart = getFailureCount(reader,ejb);
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
			} catch (MBeanException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ReflectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private JFreeChart getFailureCount(JmxReader reader, String wsName) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		Long failure = reader.getWsFaultCount(wsName);
		data.addValue(failure,"Failure","");
		data.addValue(reader.getWsRequestCount(wsName) - failure,"Processed","");
		
		JFreeChart chart = ChartFactory.createStackedBarChart("","", "", data, PlotOrientation.HORIZONTAL, false, false, false);
		return chart;
	}

}
