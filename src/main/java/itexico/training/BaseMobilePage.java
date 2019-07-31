package itexico.training;

import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import itexico.training.utilities.CommonUtilities;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseMobilePage {
	
	public MobileDriver<WebElement> driver;
	
	public AndroidDriver<WebElement> driverAndroid;
	public IOSDriver<WebElement> driverIOS;
    public WebDriverWait wait;
    
    public WebElement ele;
    public List<MobileElement> list;
    
    int attempts = 0;
    
    private DesiredCapabilities caps;
    private AppiumDriverLocalService service;
	private AppiumServiceBuilder builder;
	private File app;
 
	public BaseMobilePage() {}
	
	public BaseMobilePage(AndroidDriver<WebElement> driver) {
		this.driverAndroid = driver;
	}
	
	public BaseMobilePage(IOSDriver<WebElement> driver) {
		this.driverIOS = driver;
	}	
	
	public AndroidDriver<WebElement> getDriverAndroid(){
		if(driverAndroid == null)
			driverAndroid = connectDriverAndroid();
		
		return driverAndroid;
	}
	
	protected WebDriverWait getWait(int secs) {
		
		if(driver != null)
			wait = new WebDriverWait(driver, secs);
		
		return wait;
	}
	
	protected AndroidDriver<WebElement> connectDriverAndroid(){
		try {
			caps = new DesiredCapabilities();
	        caps.setCapability("deviceName", "Galaxy Nexus API 24");
	        caps.setCapability("udid", "emulator-5554"); //DeviceId from "adb devices" command
	        caps.setCapability("platformName", "Android");
	        caps.setCapability("platformVersion", "7.0");
	        caps.setCapability("skipUnlock","true");
	        
	        caps.setCapability("autoGrantPermissions", "true");
	        
	        caps.setCapability("appPackage", "com.isinolsun.app");
	        caps.setCapability("appActivity","com.isinolsun.app.activities.SplashActivity");
	        caps.setCapability("noReset","false");
	        driverAndroid = new AndroidDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"),caps);
	        
	        return driverAndroid;
		}
		catch(MalformedURLException mue) {
			
		}
		return null;
		
	}
	
	protected MobileDriver<WebElement> connectDriver(){
		try {
			String apkName = "İşin Olsun – İş bul veya ilan ver_v2.6.0_apkpure.com.apk";
			app = CommonUtilities.appFile(System.getProperty("user.dir"), apkName);
			
			caps = new DesiredCapabilities();
			caps.setCapability(MobileCapabilityType.DEVICE_NAME, "127.0.0.1:4723");
	    	caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
	    	//capabilities.setCapability(MobileCapabilityType.PLATFORM, "Android");
	    	caps.setCapability(MobileCapabilityType.PLATFORM_VERSION,"7.0");
	    	
	    	//capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Galaxy Nexus API 24");
	    	//capabilities.setCapability("deviceName", "Galaxy Nexus API 24");
	    	
	    	caps.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
	    	caps.setCapability("appPackage", "com.isinolsun.app");
	    	caps.setCapability("appActivity","com.isinolsun.app.activities.SplashActivity");
	    	caps.setCapability("deviceReadyTimeout", 450);
	    	caps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 60000);
	    	caps.setCapability("noReset","false");
	    	caps.setCapability("autoGrantPermissions", "true");
	    	caps.setCapability("skipUnlock","true");
	    	caps.setCapability("automationName","UiAutomator2");
	    	
	    	caps.setCapability("avd", "Galaxy_Nexus_API_24");
	    	caps.setCapability("udid", "emulator-5554"); //DeviceId from "adb devices" command
			
			driver = new AndroidDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"),caps);
			return driver;
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	protected WebElement getWebElement(By by, int secs) { 
		while(attempts < 2) {
			try {
				wait = getWait(secs);
				ele = wait.ignoring(TimeoutException.class, NoSuchElementException.class)
						.until(ExpectedConditions.visibilityOfElementLocated(by));
				
				return ele;				
			}
			catch(StaleElementReferenceException e) {
				
			}
			attempts++;
		}
		return null;
	}
	
	protected List<MobileElement> getListWebElements(By by, int secs){
		MobileDriver driver = getDriverAndroid();
		
		while(attempts < 2) {
			try {
				wait = getWait(secs);
				list = driver.findElements(by);
				
				return list;				
			}
			catch(StaleElementReferenceException e) {
				
			}
			attempts++;
		}
		return null;
	}

	protected boolean isWebElementVisible(WebElement ele) {
		return ele.isDisplayed();
	}
	
	protected void startServer() {
		if(!checkServerInUse(4723)) {
			
			String osName = System.getProperty("os.name").toLowerCase();
	    	String nodePath = null;
	    	String appiumPath = null;
	    	
	    	// Validate OS
	    	if(osName.contains("mac") || osName.contains("darwin")) {
	    		nodePath = "/usr/local/bin/node";
	    		appiumPath = "/usr/local/lib/node_modules/appium/build/lib/main.js";
	    	}
	    	else if(osName.contains("nix") || osName.contains("nux")) {
	    		nodePath = System.getenv("HOME") + "/.linuxbrew/bin/node";
	    		appiumPath = System.getenv("HOME") + "/.linuxbrew/lib/node_modules/appium/build/lib/main.js";
	    	}
	    	else if(osName.contains("windows")) {
	    		
	    	}
	    	
	    	//Create an appium service builder
	    	AppiumServiceBuilder builder = new AppiumServiceBuilder();
	    	builder.usingDriverExecutable(new File(nodePath));
	    	builder.usingPort(4723);
	    	builder.withAppiumJS(new File(appiumPath));
	    	
	    	service = AppiumDriverLocalService.buildService(builder);
	    	service.start();
		}
		else System.out.println("Appium Server already running ...");
			
	}
	
	protected void stopServer() {
		service.stop();
	}
	
	protected boolean checkServerInUse(int port) {
		boolean isServerRunning = false;
		ServerSocket serverSocket;
		
		try {
			serverSocket = new ServerSocket();
			serverSocket.close();
		}
		catch(IOException e) {
			// In case that port is in use
			isServerRunning = true;
		}
		finally {
			serverSocket = null;
		}
		
		return isServerRunning;
	}
	
	protected void closeEmulator() {
		try {
			Runtime.getRuntime().exec(System.getProperty("user.dir") 
					+ "/../../Library/Android/sdk/platform-tools/"
					+ "adb -s emulator-5554 emu kill");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
