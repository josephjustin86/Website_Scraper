package demo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Level;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases {
    ChromeDriver driver;

    /*
     * TODO: Write your tests here with testng @Test annotation. 
     * Follow `testCase01` `testCase02`... format or what is provided in instructions
     */
    
    @Test(enabled = true)
    public void testCase01()
    throws InterruptedException {

        System.out.println("Start Test case: testCase01");
        driver.get("https://www.scrapethissite.com/pages/");
        Assert.assertTrue(driver.getCurrentUrl().equals("https://www.scrapethissite.com/pages/"), "Url not verified");
        System.out.println("Url verified: https://www.scrapethissite.com/pages/");

        WebElement hockeyElement = driver.findElement(By.xpath("//a[contains(text(), 'Hockey Teams')]"));
        Wrappers.clickingOnElement(hockeyElement, driver);
        
        ArrayList<HashMap<String, Object>> dataList = new ArrayList<>();

        WebElement clickOnPage = driver.findElement(By.xpath("(//ul[@class='pagination']/li/a)[1]"));

        Wrappers.clickingOnElement(clickOnPage, driver);

        for (int page = 1; page <= 4; page++) {
            List<WebElement> rows = driver.findElements(By.xpath("//tr[@class='team']"));
            for (WebElement row : rows) {
                String teamName = row.findElement(By.xpath("./td[@class='name']")).getText();
                int year = Integer.parseInt(row.findElement(By.xpath("./td[@class='year']")).getText());
                double winPercentage = Double.parseDouble(row.findElement(By.xpath("./td[contains(@class, 'pct')]")).getText());

                long epoch = System.currentTimeMillis() / 1000;

                String epochTime = String.valueOf(epoch);

                if (winPercentage < 0.4) {
                    HashMap<String, Object> dataMap = new HashMap<>();
                    dataMap.put("eposhTime", epochTime);
                    dataMap.put("teamName", teamName);
                    dataMap.put("year", year);
                    dataMap.put("winPercentage", winPercentage);

                    dataList.add(dataMap);
                }
            }

            if (page < 4) {        
                WebElement nextPageElement = driver.findElement(By.xpath("//a[@aria-label='Next']"));
                nextPageElement.click();
                Thread.sleep(5000);
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String useDir = System.getProperty("user.dir");
            File jsonFile = new File(useDir+"/src/test/resources/hockey-team-data.json");
            objectMapper.writeValue(jsonFile, dataList);
            System.out.println("JSON data written to: " + jsonFile.getAbsolutePath());
            Assert.assertTrue(jsonFile.length() != 0);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("End Test case: testCase01");

    }

    @Test(enabled = true)
    public void testCase02()
    throws InterruptedException {

        System.out.println("Start Test case: testCase02");
        driver.get("https://www.scrapethissite.com/pages/");
        WebElement oscarWiningFilms = driver.findElement(By.xpath("//a[contains(text(), 'Oscar Winning Films')]"));
        Wrappers.clickingOnElement(oscarWiningFilms, driver);

        Utilities.scrape("2015", driver);
        Utilities.scrape("2014", driver);
        Utilities.scrape("2013", driver);
        Utilities.scrape("2012", driver);
        Utilities.scrape("2011", driver);
        Utilities.scrape("2010", driver);

        System.out.println("Start Test case: testCase02");
    }
     
    /*
     * Do not change the provided methods unless necessary, they will help in automation and assessment
     */
    @BeforeTest
    public void startBrowser()
    {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log"); 

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
    }

    @AfterTest
    public void endTest()
    {
        driver.close();
        driver.quit();

    }
}