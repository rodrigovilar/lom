package com.nanuvem.lom.lomgui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RootWidgetTest {

	private WebDriver driver;
	 
	@Before
	public void setUp() {
		driver = new FirefoxDriver();
	}
 
	@After
	public void tearDown() {
		driver.close();
	}
 
	@Test
	public void mockScenario() {
		driver.get("http://localhost:8080/lomgui/");
		
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
            	try {
            		driver.findElement(By.id("class_Cliente"));
            		return true;
            	} catch (Exception e) {
            		return false;
            	}
            }
        });
		
		WebElement clientLi = driver.findElement(By.id("class_Cliente"));
		
		assertNotNull("Client Class not found", clientLi);
		assertEquals("Cliente", clientLi.getText());
		
	}

}
