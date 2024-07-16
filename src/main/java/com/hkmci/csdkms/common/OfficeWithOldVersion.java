package com.hkmci.csdkms.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Value;

public class OfficeWithOldVersion {
	
//	@Value("${com.hkmci.csdkms.location}")
	@Value("${app.officeToPDFAddress}")
	private String toolsAddress;
	
	@SuppressWarnings("finally")
	public Integer runCMD(String inputname, String outputname) {
		  
		//toolsAddress = "D:\\csdkms\\backend";
		toolsAddress = " d:\\csdkms\\backend";
		
		ProcessBuilder processBuilder = new ProcessBuilder();
		String cmd = "d:\\csdkms\\backend\\o2pdf.exe " + inputname + " " + outputname ;

		System.out.println("Command is:  " + toolsAddress +  " && " + cmd);
		
		// -- Linux --

		// Run a shell command
		//processBuilder.command("bash", "-c", "ls /home/mkyong/");

		// Run a shell script
		//processBuilder.command("path/to/hello.sh");

		// -- Windows --

		// Run a command
		processBuilder.command("cmd.exe", "/c", "cd" + toolsAddress +  " && " + cmd);
		//String tcmd = "cd" + toolsAddress +  "&& " + cmd;
		
		//processBuilder.command("cmd.exe", "/c ",tcmd);
		//processBuilder.command("cmd.exe", "/c ",cmd);
		System.out.println("See the Process Builder Command : "+processBuilder.command());
		// Run a bat file
		//processBuilder.command("C:\\Users\\mkyong\\hello.bat");

		try {
			System.out.println("In try -- O 2 P ");
			Process process = processBuilder.start();
			
			System.out.println("After Process Build start "+ process.getOutputStream()+" or "+ process);
			StringBuilder output = new StringBuilder();
			
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
				System.out.println("Line 22 ＝ "+line);
			}

			int exitVal = process.waitFor();
			System.out.println("exit Val " + exitVal );
			System.out.println("Output = " + output);
//			if (exitVal == 0) {
//		
//				System.out.println(output);
////				System.exit(0);
//				return 1;
//			} else {
//				//abnormal...\
//				
//				return 0;
//			}
			
			
			if( reader.readLine() == null) {
				return 1;
			}else {
				System.out.println("You get an error ");
				return 0;
			}
			
			
			

		} catch (IOException e) {
			System.out.println("can;t 02p 1 = "+e);
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("can;t 02p 2 = "+e);
			e.printStackTrace();
		}
		finally {
		
			File file = new File(outputname);
			System.out.println("File is Directory = "+ file.isDirectory());
			if(file.isDirectory()){//Detect whether file is exist.
				
				return 1;
			}else {
				
				return 1;
			}
		}
	}

}
