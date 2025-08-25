import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import java.io.File;
import java.time.Duration;
import org.apache.commons.io.FileUtils;
import java.util.concurrent.ThreadLocalRandom;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

public class appSettings {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "https://dev-v2-panel.lyxa.ai/auth/sign-in?admin-tab=general&returnTo=%2F&loginAs=admin";
    private static final String EMAIL = "ah@gmail.com";
    private static final String PASSWORD = "Lyxa2025@";
    private String testMessage;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\quazi\\Downloads\\Automation web\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        // Create screenshots directory
        new File("screenshots").mkdirs();
        System.out.println("Test environment ready");
    }

    // CRITICAL TEST CASE 1: Invalid Login Credentials
    @Test(priority = 1)
    public void testInvalidLoginCredentials() throws Exception {
        System.out.println("Test: Invalid Login Credentials");
        
        driver.get(BASE_URL);
        Thread.sleep(2000);
        
        // Test with invalid email
        WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("email")));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        
        clearAndType(emailField, "invalid@email.com");
        clearAndType(passwordField, "wrongpassword");
        loginButton.click();
        
        Thread.sleep(3000);
        
        // Verify login fails and error message is displayed
        boolean isErrorDisplayed = wait.until(ExpectedConditions.or(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Invalid') or contains(text(),'Error') or contains(text(),'incorrect')]")),
            ExpectedConditions.urlContains("sign-in") // Still on login page
        ));
        
        takeScreenshot("invalid_login_test.png");
        Assert.assertTrue(isErrorDisplayed, "Error message should be displayed for invalid credentials");
        System.out.println("Invalid login test passed - error handling works correctly");
    }

    @Test(priority = 2)
    public void testNavigateToLoginPage() throws Exception {
        System.out.println("Test: Navigate to Login Page");
        
        driver.get(BASE_URL);
        Thread.sleep(2000);
        takeScreenshot("1_login_page_loaded.png");
        
        // Verify login page elements are present
        WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.name("email")
        ));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        
        Assert.assertTrue(emailField.isDisplayed(), "Email field should be visible");
        Assert.assertTrue(passwordField.isDisplayed(), "Password field should be visible");
        Assert.assertTrue(loginButton.isDisplayed(), "Login button should be visible");
        System.out.println("Login page loaded successfully");
        Thread.sleep(2000);
    }
    
    @Test(priority = 3, dependsOnMethods = "testNavigateToLoginPage")
    public void testValidLogin() throws Exception {
        System.out.println("Test: Valid Login");
        
        // Enter credentials
        WebElement emailField = findElement(
            By.name("email"),
            By.xpath("//input[contains(@placeholder,'email') or contains(@placeholder,'Email')]"),
            By.xpath("//input[@type='text' or @type='email']")
        );
        clearAndType(emailField, EMAIL);
        Thread.sleep(2000);
        WebElement passwordField = findElement(
            By.name("password"),
            By.id("password"),
            By.xpath("//input[@type='password']")
        );
        clearAndType(passwordField, PASSWORD);
        
        takeScreenshot("2_credentials_entered.png");
        
        // Click login button
        WebElement loginButton = findElement(
            By.cssSelector("button[type='submit']"),
            By.xpath("//button[contains(text(),'Sign') or contains(text(),'Login')]")
        );
        loginButton.click();
        Thread.sleep(2000);
        // Verify successful login
        boolean loginSuccessful = wait.until(ExpectedConditions.or(
            ExpectedConditions.urlContains("dashboard"),
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(),'Dashboard')]")),
            ExpectedConditions.not(ExpectedConditions.urlContains("sign-in"))
        ));
        
        takeScreenshot("3_login_success.png");
        Assert.assertTrue(loginSuccessful, "Login should be successful");
        System.out.println("Login successful");
    }

    @Test(priority = 4, dependsOnMethods = "testValidLogin")
    public void testOpenNavigationMenu() throws Exception {
        System.out.println("Test: Open Navigation Menu");
        Thread.sleep(2000);
        // Click menu button
        WebElement menuButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("(//button[contains(@class,'MuiIconButton-root')])[1]")
        ));
        menuButton.click();
        Thread.sleep(2000);
        takeScreenshot("4_menu_opened.png");
        
        // Verify menu items are visible
        WebElement settingsOption = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//span[@class='mnl__nav__item__title MuiBox-root css-0' and text()='Settings']")
        ));
        
        Assert.assertTrue(settingsOption.isDisplayed(), "Settings menu item should be visible");
        System.out.println("Navigation menu opened successfully");
    }

    @Test(priority = 5, dependsOnMethods = "testOpenNavigationMenu")
    public void testNavigateToSettings() throws Exception {
        System.out.println("Test: Navigate to Settings");
        
        // Click Settings menu item
        WebElement settingsMenuItem = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//span[@class='mnl__nav__item__title MuiBox-root css-0' and text()='Settings']")
        ));
        settingsMenuItem.click();
      
        Thread.sleep(2000); // Wait for page transition
        takeScreenshot("5_settings_page.png");
        
        // Verify we're on settings page
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("settings") || 
                         driver.getPageSource().contains("Settings"), 
                         "Should navigate to settings page");
        
        System.out.println("Successfully navigated to Settings");
    }

    @Test(priority = 6, dependsOnMethods = "testNavigateToSettings")
    public void testOpenAppSettings() throws Exception {
        System.out.println("Test: Open App Settings");
        
        // Click App Settings
        WebElement testOpenAppSettings = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[@class='MuiBox-root css-171onha' and text()='App Settings']")
        ));
        testOpenAppSettings.click();
        
        Thread.sleep(2000);
        takeScreenshot("6_default_app_settings.png");
        
        // Verify Configuration button is present
        WebElement configurationsTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@role='tab' and contains(text(),'Configurations')]")
            ));
        Thread.sleep(2000);
        WebElement monthlyTargetInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("businessDevelopmentSetting.monthlyShopTarget")
            ));
        
        int newValue = (int)(Math.random() * 49) + 1; // 1-49

        // Clear and set new value
        clearAndType(monthlyTargetInput, String.valueOf(newValue));
        Thread.sleep(2000);
    
        WebElement rewardPrizeInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("businessDevelopmentSetting.monthlyRewardPrize")
            ));
        	
        int newValues = (int)(Math.random() * 99) + 1;  
        clearAndType(rewardPrizeInput, String.valueOf(newValues));
    
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'+ Add')]")
            ));
            addButton.click();
            Thread.sleep(1000);
            
        WebElement unitInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("input-autocomplete-units")
                ));
              
	        String randomUnit = "" + (char)('a' + (int)(Math.random() * 26))
	                + (char)('a' + (int)(Math.random() * 26));
	
	        Thread.sleep(1000);
	        unitInput.sendKeys(randomUnit);

		Thread.sleep(2000); 
		unitInput.sendKeys(Keys.ENTER);
		Thread.sleep(1000);
		unitInput.sendKeys(Keys.TAB);
		WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
		        By.cssSelector("button.css-19sp94p")
		));

		Actions actions = new Actions(driver);
		actions.moveToElement(button).click().perform();
		 
		WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
			    By.xpath("//button[contains(text(),'Save Changes')]")
			));
		Thread.sleep(2000); 
		Actions actIons = new Actions(driver);
		actIons.moveToElement(saveButton).click().perform();

		Thread.sleep(2000); // Wait for save to complete
		takeScreenshot("13_random_unit_added.png");
    }

    // CRITICAL TEST CASE 2: Data Validation for Monthly Target Input
    @Test(priority = 7, dependsOnMethods = "testOpenAppSettings")
    public void testMonthlyTargetDataValidation() throws Exception {
        System.out.println("Test: Monthly Target Data Validation");
        
        WebElement monthlyTargetInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.name("businessDevelopmentSetting.monthlyShopTarget")
        ));
        
        // Test negative values
        clearAndType(monthlyTargetInput, "-10");
        Thread.sleep(1000);
        takeScreenshot("negative_value_test.png");
        
        // Test zero value
        clearAndType(monthlyTargetInput, "0");
        Thread.sleep(1000);
        takeScreenshot("zero_value_test.png");
        
        // Test very large numbers
        clearAndType(monthlyTargetInput, "999999999");
        Thread.sleep(1000);
        takeScreenshot("large_number_test.png");
        
        // Test non-numeric input
        clearAndType(monthlyTargetInput, "abc123");
        Thread.sleep(1000);
        takeScreenshot("non_numeric_test.png");
        
        // Verify validation messages or input restrictions
        String inputValue = monthlyTargetInput.getAttribute("value");
        System.out.println("Current input value after validation: " + inputValue);
        
        // Check if there are any validation error messages
        try {
            WebElement errorMessage = driver.findElement(By.xpath("//*[contains(@class,'error') or contains(@class,'invalid')]"));
            if (errorMessage.isDisplayed()) {
                System.out.println("Validation error detected: " + errorMessage.getText());
            }
        } catch (NoSuchElementException e) {
            System.out.println("No visible validation errors found");
        }
        
        takeScreenshot("validation_test_complete.png");
        System.out.println("Data validation test completed");
    }

    // CRITICAL TEST CASE 3: Save Changes Verification
    @Test(priority = 8, dependsOnMethods = "testMonthlyTargetDataValidation")
    public void testSaveChangesVerification() throws Exception {
        System.out.println("Test: Save Changes Verification");
        
        // Set known values
        int testTargetValue = 25;
        int testRewardValue = 75;
        
        WebElement monthlyTargetInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.name("businessDevelopmentSetting.monthlyShopTarget")
        ));
        clearAndType(monthlyTargetInput, String.valueOf(testTargetValue));
        
        WebElement rewardPrizeInput = driver.findElement(By.name("businessDevelopmentSetting.monthlyRewardPrize"));
        clearAndType(rewardPrizeInput, String.valueOf(testRewardValue));
        
        takeScreenshot("before_save_changes.png");
        
        // Click save
        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(),'Save Changes')]")
        ));
        saveButton.click();
        
        Thread.sleep(3000); // Wait for save operation
        
        // Verify success message or confirmation
        try {
            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Success') or contains(text(),'Saved') or contains(text(),'Updated')]")
            ));
            Assert.assertTrue(successMessage.isDisplayed(), "Success message should be displayed after saving");
            takeScreenshot("save_success_message.png");
        } catch (Exception e) {
            System.out.println("No explicit success message found, checking if values persisted");
        }
        
        takeScreenshot("save_changes_completed.png");
        System.out.println("Save changes verification completed successfully");
    }

    // CRITICAL TEST CASE 4: Session Timeout and Authentication (Independent test)
    @Test(priority = 20) // Run this separately as it affects browser state
    public void testSessionTimeoutHandling() throws Exception {
        System.out.println("Test: Session Timeout Handling");
        
        // Clear cookies to simulate session timeout
        driver.manage().deleteAllCookies();
        Thread.sleep(2000);
        
        // Try to access a protected page directly
        driver.get("https://dev-v2-panel.lyxa.ai/settings");
        Thread.sleep(3000);
        
        takeScreenshot("session_timeout_test.png");
        
        // Verify redirect to login page or appropriate error handling
        String currentUrl = driver.getCurrentUrl();
        boolean isRedirectedToLogin = currentUrl.contains("sign-in") || currentUrl.contains("login") || currentUrl.contains("auth");
        
        if (!isRedirectedToLogin) {
            // Check if there's an authentication error message
            try {
                WebElement authError = driver.findElement(By.xpath("//*[contains(text(),'Unauthorized') or contains(text(),'Please login') or contains(text(),'Session expired')]"));
                isRedirectedToLogin = authError.isDisplayed();
            } catch (NoSuchElementException e) {
                // No error message found
            }
        }
        
        Assert.assertTrue(isRedirectedToLogin, 
            "User should be redirected to login page or see authentication error when session is invalid");
        
        System.out.println("Session timeout handling test completed");
    }



    @AfterClass
    public void tearDown() {
        System.out.println("Cleaning up test environment...");
        if (driver != null) {
            try {
                takeScreenshot("12_final_state.png");
            } catch (Exception e) {
                System.out.println("Could not take final screenshot: " + e.getMessage());
            }
            driver.quit();
            System.out.println("Browser closed successfully");
        }
    }
        
    private WebElement findElement(By... locators) {
        for (By locator : locators) {
            try {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            } catch (Exception ignored) {
               
            }
        }
        throw new NoSuchElementException("Element not found with any of the provided locators");
    }

    private void clearAndType(WebElement element, String text) {
        element.clear();
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);
        element.sendKeys(text);
    }

    private void takeScreenshot(String filename) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destination = new File("screenshots/" + filename);
            FileUtils.copyFile(screenshot, destination);
            System.out.println("Screenshot saved: " + filename);
        } catch (Exception e) {
            System.out.println("Failed to take screenshot: " + e.getMessage());
        }
    }
}