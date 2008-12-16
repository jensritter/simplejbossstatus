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
 * Servlet implementation class EjbUsageGraph
 */
public class EjbUsageGraph extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EjbUsageGraph() {
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

		
		JFreeChart chart = getCreateCount(reader,ejb);
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

	private JFreeChart getCreateCount(JmxReader reader, String ejb) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		int used = reader.getEjbCurrentSize(ejb);
		data.addValue(used,"used","");
		data.addValue(reader.getEjbMaxSize(ejb) - used,"Free","");
		
		JFreeChart chart = ChartFactory.createStackedBarChart("","", "", data, PlotOrientation.HORIZONTAL, false, false, false);
		return chart;
	}

}
