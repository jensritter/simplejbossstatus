package org.jens.jbossstatus;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jrobin.core.RrdException;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;

/**
 * Servlet implementation class Collector.
 */
public class RrdBarGrapher extends HttpServlet implements Servlet {

	/** The logger. */
	private final Log logger = LogFactory.getLog(RrdBarGrapher.class);

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() {
		try {
			// Init Quartz-Cron-Job
			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
			Scheduler scheduler = schedFact.getScheduler();

			JobDetail testJob = scheduler.getJobDetail("gatherData", "defaultGroup");
			if (testJob != null) {
				scheduler.deleteJob("gatherData", "defaultGroup");
			}
			JobDetail jobDetail = new JobDetail("gatherData",
					"defaultGroup",CronJob.class);
			//Trigger trigger = TriggerUtils.makeMinutelyTrigger(5);
			Trigger trigger = TriggerUtils.makeSecondlyTrigger(CronJob.EVERY_SECOND);
			trigger.setStartTime(new Date());
			trigger.setName("gatherData");
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			logger.fatal("Fehler",e);
		}
	}


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			if (request.getParameterMap().containsKey("debug")) {
				CronJob job = new CronJob();
				PrintWriter out = response.getWriter();
				out.write(job.dump());
				out.close();
				return;
			}
			response.setContentType("image/png");
			CronJob job = new CronJob();
			job.graph(request.getParameter("db"),Integer.parseInt(request.getParameter("scale")),response.getOutputStream());
		} catch (IOException e) {
			logger.fatal(e.getMessage(),e);
		} catch (RrdException e) {
			logger.fatal(e.getMessage(),e);
		}	
	}


}
