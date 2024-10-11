package demo.wrappers;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.JavascriptExecutor;

public class Wrappers {
    /*
     * Write your selenium wrappers here
     */
    ChromeDriver driver;


    public static boolean clickingOnElement(WebElement element, WebDriver driver){
        if(element.isDisplayed()) {
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].scrollIntoView(true)", element);
                element.click();
                Thread.sleep(2000);
                return false;
            }
            catch (Exception e){
                return false;
            }
        }
        return false;
    }
}
