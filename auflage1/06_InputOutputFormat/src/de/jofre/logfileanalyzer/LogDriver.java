package de.jofre.logfileanalyzer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import de.jofre.pdfinput.PDFInputFormat;
import de.jofre.xlsoutput.XLSOutputFormat;

public class LogDriver extends Configured implements Tool {
	
	private final static Logger log = Logger.getLogger(LogDriver.class.getName());

	public static void main(String[] args) {
        int res = 1; // Wenn 1 nicht ver�ndert wird, endet der Job nicht korrekt
		try {
			res = ToolRunner.run(new Configuration(), new LogDriver(), args);
		} catch (Exception e) {
			log.log(Level.SEVERE, "Fehler beim Ausf�hren des Jobs!");
			e.printStackTrace();
		}
        System.exit(res);
	}
	
	
    @Override
    public int run(String[] args) {
 
		log.log(Level.INFO, "Starte Map-Reduce-Job 'LogDriver'... ");
        
		// Wenn Configured erweitert wird, kann die bestehende Konfiguration
		// per getConf abgerufen werden.
        Configuration conf = this.getConf();
        Job job = null;
        
		try {
			job = Job.getInstance(conf);
		} catch (IOException e1) {
			log.log(Level.SEVERE, "Fehler bei Instanziierung des Jobs!");
			e1.printStackTrace();
		}
		
		// Hadoop soll ein verf�gbares JAR verwenden, das die Klasse
		// LogDriver enth�lt.
		job.setJarByClass(LogDriver.class);
		
		// Mapper- und Reducer-Klasse werden festgelegt
		job.setMapperClass(LogMapper.class);
		job.setReducerClass(LogReducer.class);
		
		// Ausgabetypen werden festgelegt
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		// Nicht n�tig, da diese mit OutputKeyClass und OutputValueClass �bereinstimmen.
		//job.setMapOutputKeyClass(Text.class);
		//job.setMapOutputValueClass(IntWritable.class);
		
		// Hier setzen wir unsere eigenen Klassen ein
		job.setInputFormatClass(PDFInputFormat.class);
		job.setOutputFormatClass(XLSOutputFormat.class);
		
		// Der Pfad, aus dem Hadoop die Eingabedateien list, wird als erstes Argument
		// beim Starten des JARs �bergeben.
		try {
			FileInputFormat.addInputPath(job, new Path(args[0]));
		} catch (Exception e) {
			log.log(Level.SEVERE, "Fehler beim Setzen des Eingabepfades!");
			e.printStackTrace();
		}
		
		// Der Ausgabeordner wird als zweites Argument �bergeben
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		boolean result = false;

		try {
			// F�hre den Job aus und warte, bis er beendet wurde
			result = job.waitForCompletion(true);
		} catch (Exception e) {
			log.log(Level.SEVERE, "Fehler beim Ausf�hren des Jobs!");
			e.printStackTrace();
		}

		log.log(Level.INFO, "Fertig!");
		return result ? 0 : 1;
    }
}
