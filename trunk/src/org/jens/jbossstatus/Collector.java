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
public class Collector extends HttpServlet implements Servlet, Job {
	private final Log logger = LogFactory.getLog(Collector.class);
	private static final long serialVersionUID = 1L;
	
	private static final String FILENAME="jbossstatus.jrrd";
	private static final int EVERY_SECOND=60*5;
	
	private static Long getUnixTimeStamp() {
		return new Date().getTime()/ 1000L;
	}
    
    @Override
    public void init() {
    	try {
	        File test = new File(FILENAME);
	        if (!test.exists()) {
	        	initRRD();
	        }
        
			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
			Scheduler scheduler = schedFact.getScheduler();

			JobDetail testJob = scheduler.getJobDetail("gatherData", "defaultGroup");
			if (testJob != null) {
				scheduler.deleteJob("gatherData", "defaultGroup");
			}
			JobDetail jobDetail = new JobDetail("gatherData",
					"defaultGroup",Collector.class);
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

	private void initRRD() throws RrdException, IOException, NamingException {
		RrdDef rrdDef = new RrdDef(FILENAME);
		rrdDef.setStartTime(getUnixTimeStamp());
		rrdDef.setStep(EVERY_SECOND);
		JmxReader reader = new JmxReader(new InitialContext());
		String[] jdbcList = reader.listJdbc();
		String[] wsList = reader.listWebServices();
		for(String it : jdbcList) {
			logger.info("Creating JDBC-Database : " + it);
			rrdDef.addDatasource(it, DsTypes.DT_GAUGE, 600, Double.NaN, Double.NaN);
		}
		for(String it : wsList) {
			logger.info("Creating WS-Database : " + it);
			rrdDef.addDatasource(it+"_FAULT", DsTypes.DT_COUNTER, 600, Double.NaN, Double.NaN);
			rrdDef.addDatasource(it+"_COUNT", DsTypes.DT_COUNTER, 600, Double.NaN, Double.NaN);
		}
		//rrdDef.addDatasource("ws_sitaraService", DsTypes.DT_COUNTER, 600, Double.NaN, Double.NaN);
		rrdDef.addArchive(ConsolFuns.CF_AVERAGE, 0.5, 1, 600);
		rrdDef.addArchive(ConsolFuns.CF_AVERAGE, 0.5, 6, 700);
		rrdDef.addArchive(ConsolFuns.CF_AVERAGE, 0.5, 24, 775);
		rrdDef.addArchive(ConsolFuns.CF_AVERAGE, 0.5, 288, 797);
		rrdDef.addArchive(ConsolFuns.CF_MAX, 0.5, 1, 600);
		rrdDef.addArchive(ConsolFuns.CF_MAX, 0.5, 6, 700);
		rrdDef.addArchive(ConsolFuns.CF_MAX, 0.5, 24, 775);
		rrdDef.addArchive(ConsolFuns.CF_MAX, 0.5, 288, 797);


		
		
		RrdDb rrdDb = new RrdDb(rrdDef);
		rrdDb.close();
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
		graphDef.setTimeSpan(beginTime, time);
		graphDef.datasource(db, FILENAME, db, ConsolFuns.CF_AVERAGE);
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

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		try {
			Context ctx = new InitialContext();
			JmxReader reader = new JmxReader(ctx);
			String[] jdbcList = reader.listJdbc();
			String[] wsList = reader.listWebServices();
			long[] jcounter = new long[jdbcList.length];
			long[] w1counter = new long[wsList.length];
			long[] w2counter = new long[wsList.length];
			int counter=0;
			for(String it : jdbcList) {
				jcounter[counter++] = reader.getJdbcConnectionCount(it);
			}
			counter=0;
			for(String it : wsList) {
				w1counter[counter] = reader.getWsFaultCount(it);
				w2counter[counter++] = reader.getWsRequestCount(it);
			}
			RrdDb rrdDb = null;
			try {
				rrdDb = new RrdDb(FILENAME);
			} catch (FileNotFoundException e) {
				initRRD();
			}
			Sample sample = rrdDb.createSample();
			long time = getUnixTimeStamp();
			StringBuilder st = new StringBuilder();
			st.append(time).append(":");
			for(int i=0; i < jdbcList.length; i++) {
				st.append(jcounter[i]).append(":");
			}
			
			for(int i = 0; i<wsList.length; i++ ) {
				st.append(w1counter[i]).append(":");
				st.append(w2counter[i]);
				if (i<wsList.length-1) {
					st.append(":");
				}
			}
			logger.info(st.toString());
			//sample.setAndUpdate(time +":" + count1+":"+count2+":" + count3);
			sample.setAndUpdate(st.toString());
			rrdDb.close();
			
		} catch (NamingException e) {
			throw new JobExecutionException(e);
		} catch (IOException e) {
			throw new JobExecutionException(e);
		} catch (RrdException e) {
			throw new JobExecutionException(e);
		} catch (AttributeNotFoundException e) {
			throw new JobExecutionException(e);
		} catch (InstanceNotFoundException e) {
			throw new JobExecutionException(e);
		} catch (MalformedObjectNameException e) {
			throw new JobExecutionException(e);
		} catch (MBeanException e) {
			throw new JobExecutionException(e);
		} catch (ReflectionException e) {
			throw new JobExecutionException(e);
		}
		
	}

}
