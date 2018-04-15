package de.jofre.hivedata.generator;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

	private final static Logger log = Logger.getLogger(Main.class.getName());

	private static final int DATA_COUNTER = 20000;
	
	private static final String jobs[] = {"Lehrer", "Consultant", "Verk�ufer", "G�rtner", "Sportler", "Finanzberater", "Musiker", "Bestatter",
		"Fahrzeugh�ndler", "B�nker", "Informatiker", "Student", "Pastor", "Polizist", "Krankenpfleger", "Arzt", "Fahrlehrer", "Pirat", "B�rgermeister",
		"Politiker", "Geologe", "Professor", "Wissenschaftler", "�bersetzer", "Kellner", "Schneider", "Koch", "Security", "Schauspieler", "Regisseur",
		"Schuhmacher", "M�ller", "Fleischer"};

	public static void main(String[] args) {

		log.log(Level.INFO, "Schreibe " + DATA_COUNTER + " Eintr�ge...");
		try {
			Random r = new Random();

			Writer out = new BufferedWriter(new OutputStreamWriter(
				    new FileOutputStream("hive_test_data.txt"), "UTF-8"));
			
			//PrintWriter out = new PrintWriter("hive_test_data.txt");
			
			for (int i = 0; i < DATA_COUNTER; i++) {

				out.write(i + "\t");
				
				// Names
				String name = NamesByGender.getRandomName();
				String lastName = NamesByGender.getRandomLastName();
				out.write(name + " " + lastName + "\t");

				// Property
				out.write(jobs[r.nextInt(jobs.length)]);
				
				// Umbruch
				if (i < DATA_COUNTER-1) {
					out.write(System.getProperty("line.separator"));
				}
			}

			out.close();
			log.log(Level.INFO, "Fertig!");

		} catch (Exception e) {
			log.log(Level.SEVERE, "Fehler beim Schreiben!");
			e.printStackTrace();
		}
	}

}
