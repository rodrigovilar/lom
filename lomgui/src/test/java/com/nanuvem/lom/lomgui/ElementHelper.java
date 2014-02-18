package com.nanuvem.lom.lomgui;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ElementHelper {

	public static WebElement waitAndFindElementById(WebDriver driver, final String elementId, int timeout) {
		(new WebDriverWait(driver, timeout)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				try {
					driver.findElement(By.id(elementId));
					return true;
				} catch (Exception e) {
					return false;
				}
			}
		});
	
		return driver.findElement(By.id(elementId));
	}

}
