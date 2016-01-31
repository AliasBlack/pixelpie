package black.alias.pixelpie;

import java.io.PrintWriter;
import processing.core.*;

/**
 * Class to log error messages into a text file.
 * @author Xuanming
 *
 */
public class logger {
	final PrintWriter log;
	final PApplet app;
	
	// Initialize log file.
	public logger(PApplet app) {
		this.app = app;
		this.log = app.createWriter(System.getProperty("user.home") + "/PixelPie/log.txt");
		prepareExitHandler();
	}
	
	// Write console to file.
	private void prepareExitHandler() {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				try {
					log.flush();
					log.close();
					app.stop();
				} catch (Exception e) {
				}
			}
		}));
	}

	// Print to log and console.
	public void printlg(Object obj) {
		PApplet.println(obj);
		log.println(obj);
	}
}
