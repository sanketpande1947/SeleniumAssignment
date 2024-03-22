package assignment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DynamicTable {

	public static void main(String[] args) {
		
		// Initialize ChromeOptions to disable notifications and set window size
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("window-size=1200,800");

        // Initialize WebDriver
        WebDriver driver = new ChromeDriver(options);
        
        // Maximize the browser window
        driver.manage().window().maximize();
        		
		//Open the URL
		driver.get("https://testpages.herokuapp.com/styled/tag/dynamic-table.html");
		
		// Click on Table Data button
		WebElement tableDataButton = driver.findElement(By.xpath("//details/summary[text()='Table Data']"));
        tableDataButton.click();
		
		//Clear previous table data
		driver.findElement(By.xpath("//textarea[@id='jsondata']")).clear();
		
        // Insert data from JSON file into input text box
        WebElement inputTextBox = driver.findElement(By.xpath("//textarea[@id='jsondata']"));
        try {
            String jsonData = new String(Files.readAllBytes(Paths.get("C:\\Users\\sankepan\\Emp.json")));
            inputTextBox.sendKeys(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Click on refresh button
        WebElement refreshButton = driver.findElement(By.xpath("//button[@id='refreshtable']"));
        refreshButton.click();

        // Wait for the table to populate
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[@id='dynamictable']")));

        // Get data from the table
        List<WebElement> tableRows = driver.findElements(By.xpath("//table[@id='dynamictable']/tbody/tr"));

        // Assert the data from JSON file with the data in the table
        // Assuming the data in the table is same as in the JSON file
        try {
            String jsonData = new String(Files.readAllBytes(Paths.get("C:\\Users\\sankepan\\Emp.json")));
            String[] expectedData = jsonData.split(", ");
            for (int i = 0; i < tableRows.size(); i++) {
                String rowData = tableRows.get(i).getText();
                assert rowData.contains(expectedData[i]) : "Data mismatch at row " + (i + 1);
            }
            System.out.println("Given data has been correctly populated in the UI table.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //using thread.sleep just for assignment demonstration purpose that data has been populated. 
        // ***However, it's not a recommended practice for production-grade test automation due to its unreliability and inflexibility.***
        
        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        // Close the browser
        driver.quit();
        
	}

}
