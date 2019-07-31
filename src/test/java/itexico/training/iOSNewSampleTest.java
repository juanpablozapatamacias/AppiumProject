package itexico.training;


import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.remote.DesiredCapabilities;


import io.appium.java_client.ios.IOSDriver;

import itexico.training.utilities.CommonUtilities;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;



public class iOSNewSampleTest extends BaseMobilePage {
	
    public WebDriverWait wait;
    
    @BeforeSuite
    public void startAppiumSession() {
    	startServer();
    }  
    
    @AfterSuite
    public void stopAppiumSession() {
    	stopServer();
    }
    
    @BeforeClass
    public void setup() throws MalformedURLException {
    	DesiredCapabilities caps = new DesiredCapabilities();
    	caps.setCapability("platformName", "iOS");
    	caps.setCapability("platformVersion", "12.2");
    	caps.setCapability("deviceName", "iPhone X");
    	caps.setCapability("udid", "256222DA-3F17-466B-8E79-A28697C10AC3");
    	
    	caps.setCapability("noReset",true);
    	caps.setCapability("fullReset",false);
    	//caps.setCapability("deviceReadyTimeout", 450);
    	//caps.setCapability("skipUnlock","true");
    	
    	caps.setCapability("bundleId", "com.testingbot.sample");
    	caps.setCapability("app","https://testingbot.com/appium/sample.zip");
    	//caps.setCapability("app", System.getProperty("user.dir") + "/testdata/app/TestingBotSampleApp.app");
    	caps.setCapability("automationName", "XCUITest");
    	
    	caps.setCapability("showXcodeLog", true);
    	caps.setCapability("wdaLocalPort", 8101);
    	driver = new IOSDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"),caps);
    }
    
    @Test(alwaysRun=true)
    public void iconDisplayedTest() {
    	Assert.assertTrue(isWebElementVisible(getWebElement
    			(By.xpath("//XCUIElementTypeImage[contains(@name,'asset')]"),5)), "Element is not present in the app");
    }
    
    @Test(dependsOnMethods="iconDisplayedTest")
    public void basicTesting() {
    	if(isWebElementVisible(getWebElement(By.id("A: inputA + B: inputB = Total: total"),5))) {
    		getWebElement(By.name("inputA"),5).sendKeys("5");
        	CommonUtilities.sleepByNSecs(1);
        	
        	getWebElement(By.name("inputB"),5).sendKeys("3");
        	CommonUtilities.sleepByNSecs(1);
    	}
    }
    
    @AfterClass
    public void teardown() {
    	
    	driver.quit();
    	String[] cmd = {"killall", "Simulator"};
    	
    	CommonUtilities.closeSimulator(cmd);
    	
    }
}
