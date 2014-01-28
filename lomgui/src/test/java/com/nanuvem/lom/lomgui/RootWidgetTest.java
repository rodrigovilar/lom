package com.nanuvem.lom.lomgui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

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

		WebElement clientLi = ElementHelper.waitAndFindElementById(driver, "class_Cliente", 10);

		assertNotNull("Client Class not found", clientLi);
		assertEquals("Cliente", clientLi.getText());
	}

}
