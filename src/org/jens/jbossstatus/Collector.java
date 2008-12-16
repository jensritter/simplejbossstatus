package org.jens.jbossstatus;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Random;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.spi.LoggerFactory;
import org.jboss.annotation.ejb.cache.simple.PersistenceManager;
import org.jnp.interfaces.NamingContext;
import org.jrobin.core.ConsolFuns;
import org.jrobin.core.DsTypes;
import org.jrobin.core.FetchData;
import org.jrobin.core.FetchRequest;
import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDef;
import org.jrobin.core.RrdException;
import org.jrobin.core.Sample;
import org.jrobin.graph.RrdGraph;
import org.jrobin.graph.RrdGraphDef;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;

/**
 * Servlet implementation class Collector
 */
public class Collector extends HttpServlet implements Servlet {
	private final Log logger = LogFactory.getLog(Collector.class);
	private static final long serialVersionUID = 1L;
	
	public static final int EVERY_SECOND=60*5;
	//public static final int EVERY_SECOND=1;
	
	public static Long getUnixTimeStamp() {
		return new Date().getTime()/ 1000L;
	}
	
    
    @Override
    public void init() {
    	try {
    		CronJob job = new CronJob();
	        File test = new File(job.getFilename());
	        if (!test.exists()) {
	        	job.initRRD(); 
	        }
        
			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
			Scheduler scheduler = schedFact.getScheduler();

			JobDetail testJob = scheduler.getJobDetail("gatherData", "defaultGroup");
			if (testJob != null) {
				scheduler.deleteJob("gatherData", "defaultGroup");
			}
			JobDetail jobDetail = new JobDetail("gatherData",
					"defaultGroup",CronJob.class);
			//Trigger trigger = TriggerUtils.makeMinutelyTrigger(5);
			Trigger trigger = TriggerUtils.makeSecondlyTrigger(EVERY_SECOND);
			trigger.setStartTime(new Date());
			trigger.setName("gatherData");
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			logger.fatal("Fehler",e);
		} catch (RrdException e) {
			logger.fatal("Fehler",e);
		} catch (IOException e) {
			logger.fatal("Fehler",e);
		} catch (NamingException e) {
			logger.fatal("Fehler",e);
		}
    }

	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		try {
			
			RrdDb rrdDb = new RrdDb(filename);
			long ende = getUnixTimeStamp();
			long begin = ende - 100; 
			FetchRequest fetchRequest = rrdDb.createFetchRequest(ConsolFuns.CF_AVERAGE, begin,ende);
			FetchData fetchData = fetchRequest.fetchData();
			PrintWriter out = response.getWriter();
			out.write(fetchData.dump());
			out.close();
		} catch (IOException e) {
			logger.fatal(e.getMessage(),e);
		} catch (RrdException e) {
			logger.fatal(e.getMessage(),e);
		}
		*/
		
		response.setContentType("image/png");
		try {
			graph(request.getParameter("db"),Integer.parseInt(request.getParameter("scale")),response.getOutputStream());
		} catch (RrdException e) {
			logger.fatal(e.getMessage(),e);
		}
	}
	
	private void graph(String db,int scale, OutputStream out) throws IOException, RrdException {
		logger.debug("SCALE: " + scale);
		if (scale == 0) {
			scale = 10*60;
		}
		
		RrdGraphDef graphDef = new RrdGraphDef();
		long time = getUnixTimeStamp();
		long beginTime = time - scale*60;
		CronJob job = new CronJob();
		graphDef.setTimeSpan(beginTime, time);
		graphDef.datasource(db,job.getFilename(), db, ConsolFuns.CF_AVERAGE);
		graphDef.line(db, new Color(0xFF, 0, 0), null, 2);
		graphDef.setFilename("-"); // inMemory !!
		graphDef.setImageFormat("PNG");
		graphDef.setTitle(db);
		//graphDef.setFilename("/tmp/x/speed.gif");
		graphDef.setWidth(400);
		graphDef.setHeight(100);
		RrdGraph graph = new RrdGraph(graphDef);
		BufferedImage bi = new BufferedImage(480,162,BufferedImage.TYPE_INT_RGB);
		graph.render(bi.getGraphics());
		javax.imageio.ImageIO.write(bi,"png",out);
	}
}
