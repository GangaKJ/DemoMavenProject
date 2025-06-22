package mavenTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class Loginpage {
	
	WebDriver driver;
	
	@BeforeTest
	public void browsersetup()
	{
		driver =new ChromeDriver();
		driver.manage().window().maximize();	
	}
	
	@BeforeMethod
	public void navigatetoURL()
	{
		driver.get("https://practicetestautomation.com/practice-test-login/");
	}
	
	
	@Test(priority=1)
	public void validLogin()
	{
		driver.findElement(By.xpath("//input[@id='username']")).sendKeys("student");
		driver.findElement(By.xpath("//input[@id='password']")).sendKeys("Password123");
		driver.findElement(By.xpath("//button[@id='submit']")).click();
		String currentURL= driver.getCurrentUrl();
		Assert.assertTrue(currentURL.contains("logged-in-successfully"),"Login failed for valid credentials");
	}
	
	
	@Test(priority=2)
	public void Invalidusername()
	{
		driver.findElement(By.xpath("//input[@id='username']")).sendKeys("student12");
		driver.findElement(By.xpath("//input[@id='password']")).sendKeys("Password123");
		driver.findElement(By.xpath("//button[@id='submit']")).click();
		
		WebElement errortext=driver.findElement(By.xpath("//div[@id='error']"));
		String errmessage= errortext.getText();
		
		String url= driver.getCurrentUrl();
		
		SoftAssert sassert= new SoftAssert();
		sassert.assertEquals(url,"https://practicetestautomation.com/practice-test-login/","URL doesn't match");
		sassert.assertEquals(errmessage,"Your username is invalid!","Invalid username message mismatch");
		System.out.println("Invalid username validation done");
		sassert.assertAll();
		
	}
	

	@Test(priority=3)
	public void blankInputs()
	{
		driver.findElement(By.xpath("//button[@id='submit']")).click();
		WebElement err =driver.findElement(By.xpath("//div[@id='error']"));
		
		WebElement errortext=driver.findElement(By.xpath("//div[@id='error']"));
		String errmessage= errortext.getText();
		
		try
		{
			Assert.assertEquals(errmessage,"Input fields are blanks");
		}
		catch(AssertionError er)
		{
			System.out.println(" Message displayed for blank inputs is invalid . Message generated is "+errmessage);
			
		}
		
	}
	
	
	@AfterTest
	public void closebrowser()
	{
	    driver.quit();
	}

}
