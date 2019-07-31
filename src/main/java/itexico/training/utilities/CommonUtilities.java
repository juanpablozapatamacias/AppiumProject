package itexico.training.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommonUtilities {

	public static File appFile(String path, String appName) {
		File classpathRoot = new File(path);
    	File appDir = new File(classpathRoot, "//testdata//apk");
    	File app = new File(appDir,appName);
    	return app;
	}
	
	public static void sleepByNSecs(int secs) {
		try {
			Thread.sleep(secs * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static void closeSimulator(String [] cmd) {
		try {
    		
        	Runtime run = Runtime.getRuntime();
        	Process pr = run.exec(cmd);
			int tim = pr.waitFor();
			BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line = "";
			
			while ((line=buf.readLine())!=null) {
				System.out.println(line);
			}
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
