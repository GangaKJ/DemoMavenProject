package mavenTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import utilityfile.Utility_XLSReader;

public class DataDriven {
	
	    WebDriver driver;
	    Utility_XLSReader excel;
	    String sheetName = "PatientData";

	    @BeforeClass
	    public void setup() {
	        driver = new ChromeDriver();
	        driver.manage().window().maximize();
	        excel = new Utility_XLSReader("D:\\Entri_seleniumbinaries\\DataDriven_ExcelSheets\\PatientDetails.xlsx"); // your Excel file path
	    }

	    @Test
	    public void fillPatientRegistrationForm() throws InterruptedException {
	    	
	        int rowCount = excel.getRowCount(sheetName);

	        for (int i = 2; i <= rowCount; i++) {
	            driver.get("https://demo.wpeverest.com/user-registration/patient-registration-form/");

	            String firstName = excel.getCellData(sheetName, "FirstName", i);
	            String lastName = excel.getCellData(sheetName, "LastName", i);
	            String email = excel.getCellData(sheetName, "useremail", i);
	            String password = excel.getCellData(sheetName, "userpassword", i);
	            String gender = excel.getCellData(sheetName, "Gender", i);
	            String phone = excel.getCellData(sheetName, "Phone", i);

	            // Fill form fields
	            driver.findElement(By.name("first_name")).sendKeys(firstName);
	            driver.findElement(By.name("last_name")).sendKeys(lastName);
	            driver.findElement(By.name("user_email")).sendKeys(email);
	            driver.findElement(By.name("user_pass")).sendKeys(password);

	            // Handle Gender (Radio Button)
	            if (gender.equalsIgnoreCase("male")) 
	            {
	                driver.findElement(By.id("radio_1665633911_Male")).click();
	            } 
	            else if (gender.equalsIgnoreCase("female")) 
	            {
	                driver.findElement(By.id("radio_1665633911_Female")).click();
	            } 
	            else 
	            {
	                driver.findElement(By.id("radio_1665633911_Other")).click();
	            }

	            driver.findElement(By.name("phone_1665633959")).sendKeys(phone);

	           // complete the remaining fields...............

	        }
	    }

	    @AfterClass
	    public void tearDown() {
	        driver.quit();
	    }
	}
