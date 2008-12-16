package org.jens;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

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

// TODO: Auto-generated Javadoc
/**
 * The Class Test.
 */
public class Test {
	
	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Test a = new Test();
		try {
			a.run();
		} catch (RrdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Run.
	 * 
	 * @throws RrdException the rrd exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void run() throws RrdException, IOException {
		RrdDef rrdDef = new RrdDef("jbossstatus.rrd");
		rrdDef.setStartTime(920804400L);
		rrdDef.addDatasource("speed", DsTypes.DT_COUNTER, 600, Double.NaN, Double.NaN);
		rrdDef.addArchive(ConsolFuns.CF_AVERAGE, 0.5, 1, 24);
		rrdDef.addArchive(ConsolFuns.CF_AVERAGE, 0.5, 6, 10);
		RrdDb rrdDb = new RrdDb(rrdDef);
		//rrdDb.close();
		
		
		
		//rrdDb = new RrdDb(rrdDef);
		Sample sample = rrdDb.createSample();
		sample.setAndUpdate("920804700:12345");
		sample.setAndUpdate("920805000:12357");
		sample.setAndUpdate("920805300:12363");
		sample.setAndUpdate("920805600:12363");
		sample.setAndUpdate("920805900:12363");
		sample.setAndUpdate("920806200:12373");
		sample.setAndUpdate("920806500:12383");
		sample.setAndUpdate("920806800:12393");
		sample.setAndUpdate("920807100:12399");
		sample.setAndUpdate("920807400:12405");
		sample.setAndUpdate("920807700:12411");
		sample.setAndUpdate("920808000:12415");
		sample.setAndUpdate("920808300:12420");
		sample.setAndUpdate("920808600:12422");
		sample.setAndUpdate("920808900:12423");
		//rrdDb.close();
		
		//rrdDb = new RrdDb(rrdDef);
		FetchRequest fetchRequest = rrdDb.createFetchRequest(ConsolFuns.CF_AVERAGE, 920804700L, 920808900L);
		FetchData fetchData = fetchRequest.fetchData();
		System.out.println(fetchData.dump());
		
		
		RrdGraphDef graphDef = new RrdGraphDef();
		graphDef.setTimeSpan(920804400L, 920808000L);
		graphDef.datasource("myspeed", "jbossstatus.rrd", "speed", ConsolFuns.CF_AVERAGE);
		graphDef.line("myspeed", new Color(0xFF, 0, 0), null, 2);
		graphDef.setFilename("/tmp/x/speed.gif");
		RrdGraph graph = new RrdGraph(graphDef);
		BufferedImage bi = new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);
		graph.render(bi.getGraphics());
		
		rrdDb.close();
		
		
	}

}
