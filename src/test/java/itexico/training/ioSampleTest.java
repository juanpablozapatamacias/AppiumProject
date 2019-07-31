package itexico.training;

import org.openqa.selenium.By;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

 
public class ioSampleTest extends BaseMobilePage{
 
    @BeforeSuite
    public void startAppiumSession() {
    	startServer();
    }
    
    @AfterSuite
    public void stopAppiumSession() {
    	stopServer();
    }
    
    @BeforeClass
    public void setup (){
    	driver = connectDriver();
    }
 
 
    @Test
    public void basicTest () {
        //Click and pass Splash
    	getWebElement(By.id("com.isinolsun.app:id/animation_view"),10).click();
    	
        //Click I am searching a job
    	getWebElement(By.id("com.isinolsun.app:id/bluecollar_type_button"),10).click();
 
    	isWebElementVisible(getWebElement(By.id("com.isinolsun.app:id/suggested_position_list"),10));
    }
 
    @AfterClass
    public void teardown(){
    	driver.quit();
    	closeEmulator();
    }
}