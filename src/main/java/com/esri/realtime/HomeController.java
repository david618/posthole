package com.esri.realtime;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receiver")
public class HomeController {

	@Value("${data.folder}")
	private String POST_HOLE_FOLDER;

	BufferedWriter writer = null;
	String filename = "";
    String fieldSeparator = System.getProperty("file.separator");
    String lineSeparator = System.getProperty("line.separator");

	private void openFile(String filename) {
		try {
			writer = new BufferedWriter(new FileWriter(filename));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static Long messageCounter = 0L;
	
	@GetMapping("/count")
	public Long count() {		
		return messageCounter;		
	}
	

	@PostMapping("")
	public String postBody(@RequestBody String body) {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH");
		LocalDateTime now = LocalDateTime.now();
		String currenthour = dtf.format(now);
		if (writer == null) {
			filename = currenthour;
		    openFile(POST_HOLE_FOLDER + fieldSeparator + filename);
		} else {
			if (!filename.equalsIgnoreCase(currenthour)) {
				try {
					writer.close();
				} catch (Exception e) {
					// ok to ignore
				}
				filename = currenthour;
			    openFile(POST_HOLE_FOLDER + fieldSeparator + filename);
			}
		}
		try {
			writer.write(body + lineSeparator);
			writer.flush();
			messageCounter += 1;
			if (messageCounter == Long.MAX_VALUE) {
				System.out.println("Resetting Message Counter to 0");
				messageCounter = 0L;
			}
					
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

		return "ok";

	}

}
