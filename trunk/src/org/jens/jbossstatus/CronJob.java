package org.jens.jbossstatus;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jrobin.core.ConsolFuns;
import org.jrobin.core.DsTypes;
import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDef;
import org.jrobin.core.RrdException;
import org.jrobin.core.Sample;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CronJob implements Job{
	
	private final Log logger = LogFactory.getLog(CronJob.class);

	public String getFilename() {
		return "/var/tmp/database.jrrd";
	}
	
	public String getDbName(String id) {
		int counter = 0;
		for(int i = 0; i< id.length(); i++) {
			counter = counter + id.charAt(i);
		}
		return "" + counter;
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
				rrdDb = new RrdDb(getFilename());
			} catch (FileNotFoundException e) {
				initRRD();
			}
			Sample sample = rrdDb.createSample();
			long time = Collector.getUnixTimeStamp();
			StringBuilder st = new StringBuilder();
			st.append(time).append(":");
			for(int i=0; i < jdbcList.length; i++) {
				st.append(jcounter[i]);
				if (i<jdbcList.length-1) {
					st.append(":");
				}
			}
			
			if (wsList.length>0) {
				st.append(":");
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
	
	public void initRRD() throws RrdException, IOException, NamingException {
		RrdDef rrdDef = new RrdDef(getFilename());
		rrdDef.setStartTime(Collector.getUnixTimeStamp());
		rrdDef.setStep(Collector.EVERY_SECOND);
		JmxReader reader = new JmxReader(new InitialContext());
		String[] jdbcList = reader.listJdbc();
		String[] wsList = reader.listWebServices();
		for(String it : jdbcList) {
			logger.info("Creating JDBC-Database : " + it + "("+getDbName(it)+")");
			rrdDef.addDatasource(getDbName(it), DsTypes.DT_GAUGE, 600, Double.NaN, Double.NaN);
		}
		for(String it : wsList) {
			logger.info("Creating WS-Database : " + it+ "("+getDbName(it)+")");
			rrdDef.addDatasource(getDbName(it+"_FAULT"), DsTypes.DT_COUNTER, 600, Double.NaN, Double.NaN);
			rrdDef.addDatasource(getDbName(it+"_COUNTER"), DsTypes.DT_COUNTER, 600, Double.NaN, Double.NaN);
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

}
