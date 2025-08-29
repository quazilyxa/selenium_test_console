import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import java.io.File;
import java.util.List;
import java.util.Random;
import java.time.Duration;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.interactions.Actions;

public class brandNutrationCancellation {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "https://test-v2-panel.lyxa.ai/auth/sign-in?returnTo=%2F&loginAs=admin";
    private static final String EMAIL = "nour@gmail.com";
    private static final String PASSWORD = "Nour1234@";
    private String randomBrandName;
    private String randomNutritionName;
    private String randomReasonName;
    private String courierReasonName;
    private String randomCourierReasonName;
    private String randomShopReasonName;
    private String randomResolveChatReasonName;
    private String randomPlusReasonName;
    Random random = new Random();


    @BeforeClass
    public void setUp() {
        System.out.println("Starting browser setup...");
        
        try {
            System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\quazi\\Downloads\\Automation web\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
            
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--incognito");
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-popup-blocking");
            options.addArguments("--disable-web-security");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            
            System.out.println("Creating ChromeDriver instance...");
            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            
            
            new File("screenshots").mkdirs();
            System.out.println("Test environment ready - Browser should be visible now");
            
        } catch (Exception e) {
            System.err.println("Failed to setup browser: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Browser setup failed", e);
        }
    }

    @Test(priority = 1)
    public void testNavigateToLoginPage() throws Exception {
        System.out.println("Test: Navigate to Login Page");
        
        try {
            driver.get(BASE_URL);
            Thread.sleep(2000);
            takeScreenshot("1_login_page_loaded.png");
            
            
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
            
        } catch (Exception e) {
            System.err.println("Error in testNavigateToLoginPage: " + e.getMessage());
            takeScreenshot("error_login_page.png");
            throw e;
        }
    }
    
    @Test(priority = 2, dependsOnMethods = "testNavigateToLoginPage")
    public void testValidLogin() throws Exception {
        System.out.println("Test: Valid Login");
        
        try {
           
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
            
            
            WebElement loginButton = findElement(
                By.cssSelector("button[type='submit']"),
                By.xpath("//button[contains(text(),'Sign') or contains(text(),'Login')]")
            );
            loginButton.click();
            Thread.sleep(2000);
            
            
            boolean loginSuccessful = wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("dashboard"),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(),'Dashboard')]")),
                ExpectedConditions.not(ExpectedConditions.urlContains("sign-in"))
            ));
            
            takeScreenshot("3_login_success.png");
            Assert.assertTrue(loginSuccessful, "Login should be successful");
            System.out.println("Login successful");
            
        } catch (Exception e) {
            System.err.println("Error in testValidLogin: " + e.getMessage());
            takeScreenshot("error_login.png");
            throw e;
        }
    }

    @Test(priority = 3, dependsOnMethods = "testValidLogin")
    public void testOpenNavigationMenu() throws Exception {
        System.out.println("Test: Open Navigation Menu");
        
        try {
            Thread.sleep(2000);
            
            WebElement menuButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//button[contains(@class,'MuiIconButton-root')])[1]")
            ));
            menuButton.click();
            Thread.sleep(2000);
            takeScreenshot("4_menu_opened.png");
            
            
            WebElement settingsOption = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[@class='mnl__nav__item__title MuiBox-root css-0' and text()='Settings']")
            ));
            
            Assert.assertTrue(settingsOption.isDisplayed(), "Settings menu item should be visible");
            System.out.println("Navigation menu opened successfully");
            
        } catch (Exception e) {
            System.err.println("Error in testOpenNavigationMenu: " + e.getMessage());
            takeScreenshot("error_menu.png");
            throw e;
        }
    }
//check
    @Test(priority = 4, dependsOnMethods = "testOpenNavigationMenu")
    public void testNavigateToSettings() throws Exception {
        System.out.println("Test: Navigate to Settings");
        
        try {
            
            WebElement settingsMenuItem = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@class='mnl__nav__item__title MuiBox-root css-0' and text()='Settings']")
            ));
            settingsMenuItem.click();
          
            Thread.sleep(2000); // Wait for page transition
            takeScreenshot("5_settings_page.png");
            
            
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("settings") || 
                             driver.getPageSource().contains("Settings"), 
                             "Should navigate to settings page");
            
            System.out.println("Successfully navigated to Settings");
            
        } catch (Exception e) {
            System.err.println("Error in testNavigateToSettings: " + e.getMessage());
            takeScreenshot("error_settings.png");
            throw e;
        }
    }

    @Test(priority = 5, dependsOnMethods = "testNavigateToSettings")
    public void testOpenAppSettings() throws Exception {
        System.out.println("Test: Open and Add Brand");
        
        try {
            
            WebElement testOpenAppSettings = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='MuiBox-root css-171onha' and text()='Brands']")
            ));
            testOpenAppSettings.click();
            
            Thread.sleep(2000);
            takeScreenshot("6_default_brand.png");
            
            
            WebElement addBrand = wait.until(ExpectedConditions.elementToBeClickable(
            		By.xpath("//button[normalize-space()='Add Brand']")
                ));
            addBrand.click();
            Thread.sleep(2000);
            System.out.println("Add brand form opened successfully");
            
            int length = 3 + (int)(Math.random() * 5); // 3 to 7
            StringBuilder brandNameBuilder = new StringBuilder();

            for (int i = 0; i < length; i++) {
                char randomChar = (char) ('A' + (int)(Math.random() * 26)); // Uppercase A-Z
                brandNameBuilder.append(randomChar);
            }
            

            randomBrandName = brandNameBuilder.toString(); 

            
            String randomDescription = "Automated description " + System.currentTimeMillis();

            
            WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.name("name")
            ));
            nameInput.sendKeys(randomBrandName);
            Thread.sleep(2000);
            
            WebElement descriptionTextarea = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.name("description")
            ));
            descriptionTextarea.sendKeys(randomDescription);
            System.out.println("Generated Brand Name: " + randomBrandName);
            System.out.println("Generated Description: " + randomDescription);
            Thread.sleep(2000);
            
            WebElement addBrandButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit' and normalize-space()='Add Brand']")
            ));
            addBrandButton.click();

            System.out.println("Clicked on Add Brand button successfully" + randomBrandName);
            Thread.sleep(2000);   
            
        } catch (Exception e) {
            System.err.println("Error in testOpenAppSettings: " + e.getMessage());
            takeScreenshot("error_app_settings.png");
            throw e;
        }
    }

    @Test(priority = 6, dependsOnMethods = "testOpenAppSettings")
    public void testSearchAndDeleteBrand() throws Exception {
        System.out.println("Test: Search and Delete Brand" + randomBrandName);
        
        try {
            WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.name("searchValue")));
            searchInput.clear();
            searchInput.sendKeys(randomBrandName);
            Thread.sleep(2000);

            System.out.println("Searched brand: " + randomBrandName);
          JavascriptExecutor js = (JavascriptExecutor) driver;
            Thread.sleep(2000);
            WebElement menuButton = wait.until(ExpectedConditions.elementToBeClickable(
            	    By.xpath("//tr[td[contains(text(),'" + randomBrandName + "')]]//button[contains(@class,'MuiIconButton-root')]")
            	));
            	menuButton.click();


      
            WebElement deleteOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//li[normalize-space()='Delete']")
            ));
            deleteOption.click();
            Thread.sleep(1000);

   
            WebElement confirmDeleteButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='button' and normalize-space()='Delete']")
            ));
            confirmDeleteButton.click();
            Thread.sleep(2000);

            System.out.println("Brand deleted successfully: " + randomBrandName);
            
            Thread.sleep(2000);
           

        } catch (Exception e) {
            System.err.println("Error in testSearchAndDeleteBrand: " + e.getMessage());
            takeScreenshot("error_search_delete_brand.png");
            throw e;
        }
        Thread.sleep(2000);
    }
    
    
    
    @Test(priority = 7, dependsOnMethods = "testNavigateToSettings")
    public void testOpenNutrition() throws Exception {
        System.out.println("Test: Open and Add Nutrition");
        
        try {
            
            WebElement testOpenNutrition = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='MuiBox-root css-171onha' and text()='Nutrition']")
            ));
            testOpenNutrition.click();
            
            Thread.sleep(2000);
            takeScreenshot("7_default_nutrition.png");
            
            
            WebElement addNutrition = wait.until(ExpectedConditions.elementToBeClickable(
            		By.xpath("//button[normalize-space()='Add Nutrition']")
                ));
            addNutrition.click();
            Thread.sleep(2000);
            System.out.println("Add nutrition form opened successfully");
            
            int length = 3 + (int)(Math.random() * 5); // 3 to 7
            StringBuilder nutritionNameBuilder = new StringBuilder();

            for (int i = 0; i < length; i++) {
                char randomChar = (char) ('A' + (int)(Math.random() * 26)); // Uppercase A-Z
                nutritionNameBuilder.append(randomChar);
            }

            randomNutritionName = nutritionNameBuilder.toString();     
            WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.name("name")
            ));
            nameInput.sendKeys(randomNutritionName);
            Thread.sleep(2000);
           
            
            WebElement addBrandButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit' and normalize-space()='Add Nutrition']")
            ));
            addBrandButton.click();

            System.out.println("Clicked on Add Nutrition button successfully" + randomNutritionName);
            Thread.sleep(2000);   
            
        } catch (Exception e) {
            System.err.println("Error in testOpenAppSettings: " + e.getMessage());
            takeScreenshot("error_app_settings.png");
            throw e;
        }
    }
    
    
    @Test(priority = 8, dependsOnMethods = "testOpenAppSettings")
    public void testSearchAndDisableNutrition() throws Exception {
        System.out.println("Test: Search and Delete Nutrition" + randomNutritionName);
        
        try {
        	WebElement searchInput = wait.until(ExpectedConditions
        		    .visibilityOfElementLocated(By.xpath("//input[@name='searchValue' and not(@disabled)]")));
            searchInput.clear();
            searchInput.sendKeys(randomNutritionName);
            Thread.sleep(2000);

            System.out.println("Searched brand: " + randomNutritionName);
          JavascriptExecutor js = (JavascriptExecutor) driver;
            Thread.sleep(2000);
            WebElement toggle = wait.until(ExpectedConditions.elementToBeClickable(
            	    By.xpath("//tr[td[contains(text(),'" + randomNutritionName + "')]]//span[contains(@class,'MuiSwitch-switchBase')]")
            	));
            	toggle.click();
            Thread.sleep(2000);
            
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
            	    By.xpath("//input[@id='input-autocomplete-status']/following-sibling::div//button[contains(@class,'MuiAutocomplete-popupIndicator')]")
            	));
            	dropdown.click();
            	Thread.sleep(1000);
            	WebElement inactiveOption = wait.until(ExpectedConditions.elementToBeClickable(
            		    By.xpath("//li[text()='Inactive']")
            		));
            		inactiveOption.click();
            		Thread.sleep(2000);
                    WebElement menuButton = wait.until(ExpectedConditions.elementToBeClickable(
                    	    By.xpath("//tr[td[contains(text(),'" + randomNutritionName + "')]]//button[contains(@class,'MuiIconButton-root')]")
                    	));
                    	menuButton.click();

                    	Thread.sleep(1000);
              
                    WebElement deleteOption = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//li[normalize-space()='Delete']")
                    ));
                    deleteOption.click();
                    Thread.sleep(1000);

           
                    WebElement confirmDeleteButton = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[@type='button' and normalize-space()='Delete']")
                    ));
                    confirmDeleteButton.click();
                    Thread.sleep(2000);	
            
            

        } catch (Exception e) {
            System.err.println("Error in testSearchAndDeleteBrand: " + e.getMessage());
            takeScreenshot("error_search_delete_brand.png");
            throw e;
        }
        Thread.sleep(2000);
    }
    
    
    @Test(priority = 9, dependsOnMethods = "testNavigateToSettings")
    public void testOpenCancellation() throws Exception {
        System.out.println("Test: Open and Add Cancellation");
        
        try {
            WebElement testOpenCancel = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='MuiBox-root css-171onha' and text()='Cancellation Reasons']")
            ));
            testOpenCancel.click();
            
            Thread.sleep(2000);
            takeScreenshot("9_default_cancellation.png");
            Thread.sleep(2000);
            
            WebElement userTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='User']"))
            );
            userTab.click();
            
            WebElement addReason = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Add']")
            ));
            addReason.click();
            Thread.sleep(2000);
            System.out.println("Add reason form opened successfully");
            
            String[] words = {"System", "Update", "Delay", "Request", "Change", "Order", "Cancel", "Network", "Issue", "Process"};
            Random rand = new Random();

         
            int wordCount = 3 + rand.nextInt(2); // 3 or 4 words
            StringBuilder reasonNameBuilder = new StringBuilder();

            for (int i = 0; i < wordCount; i++) {
                reasonNameBuilder.append(words[rand.nextInt(words.length)]);
                if (i < wordCount - 1) {
                    reasonNameBuilder.append(" ");
                }
            }

            this.randomReasonName = reasonNameBuilder.toString();
            
            WebElement reasonInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("reason")
            ));
            reasonInput.sendKeys(randomReasonName);
            Thread.sleep(2000);
           
            WebElement addReasonButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit' and normalize-space()='Add']")
            ));
            addReasonButton.click();

            System.out.println("Clicked on Add reason button successfully: " + randomReasonName);
            Thread.sleep(2000);   
            
        } catch (Exception e) {
            System.err.println("Error in testOpenCancellation: " + e.getMessage());
            takeScreenshot("error_cancellation.png");
            throw e;
        }
    }

    @Test(priority = 10, dependsOnMethods = "testOpenCancellation")
    public void testSearchAndDisableReason() throws Exception {
        if (randomReasonName == null || randomReasonName.trim().isEmpty()) {
            throw new IllegalStateException("randomReasonName is null or empty. Previous test may have failed.");
        }
        
        System.out.println("Test: Search, Update and Delete Status of Reason: " + randomReasonName);
        
        try {
            
            WebElement searchBox = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//input[@name='searchValue' and @placeholder='Search...']"))
            );

            searchBox.click();
            Thread.sleep(500);

            searchBox.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            searchBox.sendKeys(randomReasonName);
            Thread.sleep(1000);
            searchBox.sendKeys(Keys.ENTER);
            Thread.sleep(2000);

            takeScreenshot("10_search_results.png");
            
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,200);");  // scrolls down 200 pixels
            Thread.sleep(500);
            
            searchBox.sendKeys(Keys.TAB); // or use Keys.TAB
            Thread.sleep(500);
            
            WebElement tableHeader = wait.until(ExpectedConditions.elementToBeClickable(
            	    By.xpath("//th[contains(text(),'Name')]")
            	));
            	tableHeader.click();
            	Thread.sleep(500);
            	
            	WebElement menuButton = wait.until(ExpectedConditions.elementToBeClickable(
            		    By.xpath("//tr[td//div[text()='" + randomReasonName + "']]//button[contains(@class,'MuiIconButton-root')]")
            		));
            		menuButton.click();
            		
            		
            		WebElement editOption = wait.until(ExpectedConditions.elementToBeClickable(
            			    By.xpath("//li[normalize-space()='Edit']")
            			));
            			editOption.click();
            			Thread.sleep(2000);
            			System.out.println("Edit option clicked successfully");

            			takeScreenshot("10_edit_form_opened.png");

            		
            			WebElement statusDropdown = wait.until(ExpectedConditions.elementToBeClickable(
            			    By.xpath("//div[@role='combobox' and contains(@class,'MuiSelect-select') and text()='Active']")
            			));
            			statusDropdown.click();
            			Thread.sleep(1000);
            			System.out.println("Status dropdown clicked");

            		
            			WebElement inactiveOption = wait.until(ExpectedConditions.elementToBeClickable(
            			    By.xpath("//li[@role='option' and @data-value='inactive' and text()='Inactive']")
            			));
            			inactiveOption.click();
            			Thread.sleep(1000);
            			System.out.println("Changed status to Inactive");

            			takeScreenshot("10_status_changed_to_inactive.png");

            		
            			WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
            			    By.xpath("//button[@type='submit' or contains(text(),'Save') or contains(text(),'Update')]")
            			));
            			saveButton.click();
            			Thread.sleep(2000);
            			System.out.println("Changes saved successfully");
            		
            
            WebElement dropdownButton = wait.until(ExpectedConditions.elementToBeClickable(
            				    By.xpath("//button[contains(@class,'MuiAutocomplete-popupIndicator')]")
            				));
            				dropdownButton.click();
            				Thread.sleep(1000);
            				System.out.println("Autocomplete dropdown opened");
            				
            WebElement inactiveOptions = wait.until(ExpectedConditions.elementToBeClickable(
            					    By.xpath("//li[@role='option' and text()='Inactive']")
            					));
            					inactiveOptions.click();
            					Thread.sleep(1000);
            					System.out.println("Selected 'Inactive' from dropdown");
           
            		
          WebElement menuButtons = wait.until(ExpectedConditions.elementToBeClickable(
            	            		    By.xpath("//tr[td//div[text()='" + randomReasonName + "']]//button[contains(@class,'MuiIconButton-root')]")
            	            		));
            	            		menuButtons.click();		
            		
            	            		Thread.sleep(1000);
            
            WebElement deleteOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[normalize-space()='Delete']")
            ));
            deleteOption.click();
            Thread.sleep(1000);

            WebElement confirmDeleteButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='button' and normalize-space()='Delete']")
            ));
            confirmDeleteButton.click();
            Thread.sleep(2000);
            
            System.out.println("Reason deleted successfully: " + randomReasonName);
            takeScreenshot("10_reason_deleted.png");
            
            JavascriptExecutor js2 = (JavascriptExecutor) driver;
            js2.executeScript("window.scrollBy(0,-200);"); 
            Thread.sleep(500);

        } catch (Exception e) {
            System.err.println("Error in testSearchAndDisableReason: " + e.getMessage());
            takeScreenshot("error_search_delete_reason.png");
            throw e;
        }
    }
   
    
    @Test(priority = 11)
    public void testCourierCancellationReasonFlow() throws Exception {
        System.out.println("Test: Add, Search, Update, and Delete Courier Cancellation Reason");

        try {
        	
          WebElement testOpenCancel = wait.until(ExpectedConditions.elementToBeClickable(
          By.xpath("//div[@class='MuiBox-root css-171onha' and text()='Cancellation Reasons']")
      ));
      testOpenCancel.click();
      
      Thread.sleep(2000);
      takeScreenshot("9_default_cancellation.png");
      Thread.sleep(2000);
        
            WebElement courierTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Courier']")   
            ));
            courierTab.click();
            Thread.sleep(1000);
            takeScreenshot("20_courier_tab_opened.png");

  
            WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Add']")  
            ));
            addButton.click();
            Thread.sleep(1000);
            takeScreenshot("20_add_form_opened.png");
            
            
            String[] subjects = {
                    "Customer", "Courier", "Restaurant", "System", "Payment gateway"
                };

                String[] actions = {
                    "requested cancellation", "faced an issue", "could not complete the order",
                    "reported a problem", "encountered an error", "declined the request"
                };

                String[] contexts = {
                    "due to incorrect address", "because of payment failure", 
                    "as the item was unavailable", "due to unexpected delay", 
                    "because the shop was closed", "due to weather conditions"
                };

                String reason = subjects[random.nextInt(subjects.length)] + " " +
                                actions[random.nextInt(actions.length)] + " " +
                                contexts[random.nextInt(contexts.length)];

                reason += " (Ref-" + (1000 + random.nextInt(9000)) + ")";


                WebElement reasonInput = driver.findElement(By.name("reason"));
                reasonInput.clear();
                reasonInput.sendKeys(reason);

                randomCourierReasonName = reason;
                System.out.println("Entered reason: " + reason);
            
              WebElement addReasonButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[@type='submit' and normalize-space()='Add']")
          ));
          addReasonButton.click();

          System.out.println("Clicked on Add reason button successfully: " + randomCourierReasonName);
          Thread.sleep(2000);      


        } catch (Exception e) {
            System.err.println("Error in testCourierCancellationReasonFlow: " + e.getMessage());
            takeScreenshot("error_courier_reason.png");
            throw e;
        }
    }

    
    @Test(priority = 12, dependsOnMethods = "testCourierCancellationReasonFlow")
    public void testSearchUpdateDeleteCourierReason() throws Exception {
        if (randomCourierReasonName == null || randomCourierReasonName.trim().isEmpty()) {
            throw new IllegalStateException("randomCourierReasonName is null or empty. Previous test may have failed.");
        }
        
        System.out.println("Test: Search, Update and Delete Courier Reason: " + randomCourierReasonName);
        
        try {
            WebElement searchBox = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//input[@name='searchValue' and @placeholder='Search...']"))
            );

            searchBox.click();
            Thread.sleep(500);

            // Clear search box and enter the reason name
            searchBox.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            searchBox.sendKeys(randomCourierReasonName);
            Thread.sleep(1000);
            searchBox.sendKeys(Keys.ENTER);
            Thread.sleep(2000);

            takeScreenshot("12_search_results.png");
            
            // Scroll down a bit
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,200);");
            Thread.sleep(500);
            
            // Tab out of search box
            searchBox.sendKeys(Keys.TAB);
            Thread.sleep(500);
            
            // Click on table header to sort (optional)
            WebElement tableHeader = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//th[contains(text(),'Name')]")
            ));
            tableHeader.click();
            Thread.sleep(500);
            
            // EDIT THE REASON - Click menu button for the specific reason
            WebElement menuButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//tr[td//div[text()='" + randomCourierReasonName + "']]//button[contains(@class,'MuiIconButton-root')]")
            ));
            menuButton.click();
            
            // Click Edit option
            WebElement editOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[normalize-space()='Edit']")
            ));
            editOption.click();
            Thread.sleep(2000);
            System.out.println("Edit option clicked successfully");

            takeScreenshot("12_edit_form_opened.png");

            // UPDATE STATUS TO INACTIVE
            WebElement statusDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='combobox' and contains(@class,'MuiSelect-select') and text()='Active']")
            ));
            statusDropdown.click();
            Thread.sleep(1000);
            System.out.println("Status dropdown clicked");

            // Select Inactive option
            WebElement inactiveOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='option' and @data-value='inactive' and text()='Inactive']")
            ));
            inactiveOption.click();
            Thread.sleep(1000);
            System.out.println("Changed status to Inactive");

            takeScreenshot("12_status_changed_to_inactive.png");

            // SAVE CHANGES
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit' or contains(text(),'Save') or contains(text(),'Update')]")
            ));
            saveButton.click();
            Thread.sleep(2000);
            System.out.println("Changes saved successfully");

            // CHANGE STATUS FILTER TO SHOW INACTIVE ITEMS
            WebElement dropdownButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class,'MuiAutocomplete-popupIndicator')]")
            ));
            dropdownButton.click();
            Thread.sleep(1000);
            System.out.println("Autocomplete dropdown opened");
            
            WebElement inactiveFilterOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='option' and text()='Inactive']")
            ));
            inactiveFilterOption.click();
            Thread.sleep(1000);
            System.out.println("Selected 'Inactive' from dropdown filter");

            // DELETE THE REASON
            WebElement menuButtonForDelete = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//tr[td//div[text()='" + randomCourierReasonName + "']]//button[contains(@class,'MuiIconButton-root')]")
            ));
            menuButtonForDelete.click();		

            Thread.sleep(1000);

            // Click Delete option
            WebElement deleteOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[normalize-space()='Delete']")
            ));
            deleteOption.click();
            Thread.sleep(1000);

            // Confirm deletion
            WebElement confirmDeleteButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='button' and normalize-space()='Delete']")
            ));
            confirmDeleteButton.click();
            Thread.sleep(2000);
            
            System.out.println("Courier reason deleted successfully: " + randomCourierReasonName);
            takeScreenshot("12_courier_reason_deleted.png");
            
            // Scroll back up
            JavascriptExecutor js2 = (JavascriptExecutor) driver;
            js2.executeScript("window.scrollBy(0,-200);"); 
            Thread.sleep(500);

        } catch (Exception e) {
            System.err.println("Error in testSearchUpdateDeleteCourierReason: " + e.getMessage());
            takeScreenshot("error_search_update_delete_courier_reason.png");
            throw e;
        }
    }
    
    
    
    @Test(priority = 13)
    public void testShopCancellationReasonShop() throws Exception {
        System.out.println("Test: Add, Search, Update, and Delete Shop Cancellation Reason");

        try {
        	
          WebElement testOpenCancel = wait.until(ExpectedConditions.elementToBeClickable(
          By.xpath("//div[@class='MuiBox-root css-171onha' and text()='Cancellation Reasons']")
      ));
      testOpenCancel.click();
      
      Thread.sleep(2000);
      takeScreenshot("9_default_cancellation.png");
      Thread.sleep(2000);
        
            WebElement shopTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Shop']")   
            ));
            shopTab.click();
            Thread.sleep(1000);
            takeScreenshot("21_shop_tab_opened.png");

  
            WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Add']")  
            ));
            addButton.click();
            Thread.sleep(1000);
            takeScreenshot("21_add_form_opened.png");
            
            
            String[] subjects = {
                    "Customer", "Shop", "Restaurant", "System", "Payment gateway"
                };

                String[] actions = {
                    "requested cancellation", "faced an issue", "could not complete the order",
                    "reported a problem", "encountered an error", "declined the request"
                };

                String[] contexts = {
                    "due to incorrect address", "because of payment failure", 
                    "as the item was unavailable", "due to unexpected delay", 
                    "because the shop was closed", "due to weather conditions"
                };

                String reason = subjects[random.nextInt(subjects.length)] + " " +
                                actions[random.nextInt(actions.length)] + " " +
                                contexts[random.nextInt(contexts.length)];

                reason += " (Ref-" + (1000 + random.nextInt(9000)) + ")";


                WebElement reasonInput = driver.findElement(By.name("reason"));
                reasonInput.clear();
                reasonInput.sendKeys(reason);

                randomShopReasonName = reason;
                System.out.println("Entered reason: " + reason);
            
              WebElement addReasonButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[@type='submit' and normalize-space()='Add']")
          ));
          addReasonButton.click();

          System.out.println("Clicked on Add reason button successfully: " + randomShopReasonName);
          Thread.sleep(2000);      


        } catch (Exception e) {
            System.err.println("Error in testCourierCancellationReasonFlow: " + e.getMessage());
            takeScreenshot("error_courier_reason.png");
            throw e;
        }
    }

    
    @Test(priority = 14, dependsOnMethods = "testShopCancellationReasonShop")
    public void testSearchUpdateDeleteShopReasons() throws Exception {
        if (randomShopReasonName == null || randomShopReasonName.trim().isEmpty()) {
            throw new IllegalStateException("randomCourierReasonName is null or empty. Previous test may have failed.");
        }
        
        System.out.println("Test: Search, Update and Delete Courier Reason: " + randomShopReasonName);
        
        try {
            WebElement searchBox = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//input[@name='searchValue' and @placeholder='Search...']"))
            );
            searchBox.click();
            Thread.sleep(500);

     
            searchBox.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            searchBox.sendKeys(randomShopReasonName);
            Thread.sleep(1000);
            searchBox.sendKeys(Keys.ENTER);
            Thread.sleep(2000);

            takeScreenshot("12_search_results.png");
            
            
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,200);");
            Thread.sleep(500);
            
          
            searchBox.sendKeys(Keys.TAB);
            Thread.sleep(500);
            
          
            WebElement tableHeader = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//th[contains(text(),'Name')]")
            ));
            tableHeader.click();
            Thread.sleep(500);
            
   
            WebElement menuButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//tr[td//div[text()='" + randomShopReasonName + "']]//button[contains(@class,'MuiIconButton-root')]")
            ));
            menuButton.click();
            
      
            WebElement editOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[normalize-space()='Edit']")
            ));
            editOption.click();
            Thread.sleep(2000);
            System.out.println("Edit option clicked successfully");

            takeScreenshot("12_edit_form_opened.png");

     
            WebElement statusDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='combobox' and contains(@class,'MuiSelect-select') and text()='Active']")
            ));
            statusDropdown.click();
            Thread.sleep(1000);
            System.out.println("Status dropdown clicked");

 
            WebElement inactiveOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='option' and @data-value='inactive' and text()='Inactive']")
            ));
            inactiveOption.click();
            Thread.sleep(1000);
            System.out.println("Changed status to Inactive");

            takeScreenshot("12_status_changed_to_inactive.png");

 
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit' or contains(text(),'Save') or contains(text(),'Update')]")
            ));
            saveButton.click();
            Thread.sleep(2000);
            System.out.println("Changes saved successfully");

    
            WebElement dropdownButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class,'MuiAutocomplete-popupIndicator')]")
            ));
            dropdownButton.click();
            Thread.sleep(1000);
            System.out.println("Autocomplete dropdown opened");
            
            WebElement inactiveFilterOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='option' and text()='Inactive']")
            ));
            inactiveFilterOption.click();
            Thread.sleep(1000);
            System.out.println("Selected 'Inactive' from dropdown filter");

 
            WebElement menuButtonForDelete = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//tr[td//div[text()='" + randomShopReasonName + "']]//button[contains(@class,'MuiIconButton-root')]")
            ));
            menuButtonForDelete.click();		

            Thread.sleep(1000);


            WebElement deleteOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[normalize-space()='Delete']")
            ));
            deleteOption.click();
            Thread.sleep(1000);

            WebElement confirmDeleteButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='button' and normalize-space()='Delete']")
            ));
            confirmDeleteButton.click();
            Thread.sleep(2000);
            
            System.out.println("Shop reason deleted successfully: " + randomShopReasonName);
            takeScreenshot("12_courier_reason_deleted.png");
            
 
            JavascriptExecutor js2 = (JavascriptExecutor) driver;
            js2.executeScript("window.scrollBy(0,-200);"); 
            Thread.sleep(500);

        } catch (Exception e) {
            System.err.println("Error in testSearchUpdateDeleteCourierReason: " + e.getMessage());
            takeScreenshot("error_search_update_delete_courier_reason.png");
            throw e;
        }
    }
    
    
    
    
    @Test(priority = 15)
    public void testResolveCancellationReasonShop() throws Exception {
        System.out.println("Test: Add, Search, Update, and Delete Resolve Chat Cancellation Reason");

        try {
        	
          WebElement testOpenCancel = wait.until(ExpectedConditions.elementToBeClickable(
          By.xpath("//div[@class='MuiBox-root css-171onha' and text()='Cancellation Reasons']")
      ));
      testOpenCancel.click();
      
      Thread.sleep(2000);
      takeScreenshot("9_default_cancellation.png");
      Thread.sleep(2000);
        
            WebElement resolveTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Resolve Chat']")   
            ));
            resolveTab.click();
            Thread.sleep(1000);
            takeScreenshot("21_resolve_tab_opened.png");

  
            WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Add']")  
            ));
            addButton.click();
            Thread.sleep(1000);
            takeScreenshot("21_add_form_opened.png");
            
            
            String[] subjects = {
                    "Customer", "Chat", "Restaurant", "System", "Payment gateway"
                };

                String[] actions = {
                    "requested cancellation", "faced an issue", "could not complete the order",
                    "reported a problem", "encountered an error", "declined the request"
                };

                String[] contexts = {
                    "due to incorrect address", "because of payment failure", 
                    "as the item was unavailable", "due to unexpected delay", 
                    "because the shop was closed", "due to weather conditions"
                };

                String reason = subjects[random.nextInt(subjects.length)] + " " +
                                actions[random.nextInt(actions.length)] + " " +
                                contexts[random.nextInt(contexts.length)];

                reason += " (Ref-" + (1000 + random.nextInt(9000)) + ")";


                WebElement reasonInput = driver.findElement(By.name("reason"));
                reasonInput.clear();
                reasonInput.sendKeys(reason);

                randomResolveChatReasonName = reason;
                System.out.println("Entered reason: " + reason);
            
              WebElement addReasonButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[@type='submit' and normalize-space()='Add']")
          ));
          addReasonButton.click();
          Thread.sleep(2000);  
          System.out.println("Clicked on Add reason button successfully: " + randomResolveChatReasonName);
          Thread.sleep(2000);      


        } catch (Exception e) {
            System.err.println("Error in testCourierCancellationReasonFlow: " + e.getMessage());
            takeScreenshot("error_courier_reason.png");
            throw e;
        }
    }

    
    @Test(priority = 16, dependsOnMethods = "testResolveCancellationReasonShop")
    public void testSearchUpdateDeleteResolveReasons() throws Exception {
        if (randomResolveChatReasonName == null || randomResolveChatReasonName.trim().isEmpty()) {
            throw new IllegalStateException("randomCourierReasonName is null or empty. Previous test may have failed.");
        }
        
        System.out.println("Test: Search, Update and Delete Courier Reason: " + randomResolveChatReasonName);
        
        try {
            WebElement searchBox = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//input[@name='searchValue' and @placeholder='Search...']"))
            );

            searchBox.click();
            Thread.sleep(500);

     
            searchBox.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            searchBox.sendKeys(randomResolveChatReasonName);
            Thread.sleep(1000);
            searchBox.sendKeys(Keys.ENTER);
            Thread.sleep(2000);

            takeScreenshot("12_search_results.png");
            
            
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,200);");
            Thread.sleep(500);
            
          
            searchBox.sendKeys(Keys.TAB);
            Thread.sleep(500);
            
          
            WebElement tableHeader = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//th[contains(text(),'Name')]")
            ));
            tableHeader.click();
            Thread.sleep(500);
            
   
            WebElement menuButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//tr[td//div[text()='" + randomResolveChatReasonName + "']]//button[contains(@class,'MuiIconButton-root')]")
            ));
            menuButton.click();
            
      
            WebElement editOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[normalize-space()='Edit']")
            ));
            editOption.click();
            Thread.sleep(2000);
            System.out.println("Edit option clicked successfully");

            takeScreenshot("12_edit_form_opened.png");

     
            WebElement statusDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='combobox' and contains(@class,'MuiSelect-select') and text()='Active']")
            ));
            statusDropdown.click();
            Thread.sleep(1000);
            System.out.println("Status dropdown clicked");

 
            WebElement inactiveOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='option' and @data-value='inactive' and text()='Inactive']")
            ));
            inactiveOption.click();
            Thread.sleep(1000);
            System.out.println("Changed status to Inactive");

            takeScreenshot("12_status_changed_to_inactive.png");

 
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit' or contains(text(),'Save') or contains(text(),'Update')]")
            ));
            saveButton.click();
            Thread.sleep(2000);
            System.out.println("Changes saved successfully");

    
            WebElement dropdownButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class,'MuiAutocomplete-popupIndicator')]")
            ));
            dropdownButton.click();
            Thread.sleep(1000);
            System.out.println("Autocomplete dropdown opened");
            
            WebElement inactiveFilterOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='option' and text()='Inactive']")
            ));
            inactiveFilterOption.click();
            Thread.sleep(1000);
            System.out.println("Selected 'Inactive' from dropdown filter");

 
            WebElement menuButtonForDelete = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//tr[td//div[text()='" + randomResolveChatReasonName + "']]//button[contains(@class,'MuiIconButton-root')]")
            ));
            menuButtonForDelete.click();		

            Thread.sleep(1000);


            WebElement deleteOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[normalize-space()='Delete']")
            ));
            deleteOption.click();
            Thread.sleep(1000);

            WebElement confirmDeleteButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='button' and normalize-space()='Delete']")
            ));
            confirmDeleteButton.click();
            Thread.sleep(2000);
            
            System.out.println("Shop reason deleted successfully: " + randomResolveChatReasonName);
            takeScreenshot("12_courier_reason_deleted.png");
            
 
            JavascriptExecutor js2 = (JavascriptExecutor) driver;
            js2.executeScript("window.scrollBy(0,-200);"); 
            Thread.sleep(500);

        } catch (Exception e) {
            System.err.println("Error in testSearchUpdateDeleteCourierReason: " + e.getMessage());
            takeScreenshot("error_search_update_delete_courier_reason.png");
            throw e;
        }
    }
    
    
    
    
    @Test(priority = 17)
    public void testLyxaPlusCancellationReason() throws Exception {
        System.out.println("Test: Add, Search, Update, and Delete Resolve Chat Cancellation Reason");

        try {
        	
          WebElement testOpenCancel = wait.until(ExpectedConditions.elementToBeClickable(
          By.xpath("//div[@class='MuiBox-root css-171onha' and text()='Cancellation Reasons']")
      ));
      testOpenCancel.click();
      
      Thread.sleep(2000);
      takeScreenshot("9_default_cancellation.png");
      Thread.sleep(2000);
        
            WebElement resolveTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Lyxa Plus Subscription']")   
            ));
            resolveTab.click();
            Thread.sleep(1000);
            takeScreenshot("21_resolve_tab_opened.png");

  
            WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Add']")  
            ));
            addButton.click();
            Thread.sleep(1000);
            takeScreenshot("21_add_form_opened.png");
            
            
            String[] subjects = {
                    "Customer", "Chat", "Restaurant", "System", "Payment gateway"
                };

                String[] actions = {
                    "requested cancellation", "faced an issue", "could not complete the order",
                    "reported a problem", "encountered an error", "declined the request"
                };

                String[] contexts = {
                    "due to incorrect address", "because of payment failure", 
                    "as the item was unavailable", "due to unexpected delay", 
                    "because the shop was closed", "due to weather conditions"
                };

                String reason = subjects[random.nextInt(subjects.length)] + " " +
                                actions[random.nextInt(actions.length)] + " " +
                                contexts[random.nextInt(contexts.length)];

                reason += " (Ref-" + (1000 + random.nextInt(9000)) + ")";


                WebElement reasonInput = driver.findElement(By.name("reason"));
                reasonInput.clear();
                reasonInput.sendKeys(reason);

                randomPlusReasonName = reason;
                System.out.println("Entered reason: " + reason);
            
              WebElement addReasonButton = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//button[@type='submit' and normalize-space()='Add']")
          ));
          addReasonButton.click();
          Thread.sleep(2000);  
          System.out.println("Clicked on Add reason button successfully: " + randomPlusReasonName);
          Thread.sleep(2000);      


        } catch (Exception e) {
            System.err.println("Error in testCourierCancellationReasonFlow: " + e.getMessage());
            takeScreenshot("error_courier_reason.png");
            throw e;
        }
    }

    
    @Test(priority = 18, dependsOnMethods = "testLyxaPlusCancellationReason")
    public void testSearchUpdateDeletePlusReasons() throws Exception {
        if (randomPlusReasonName == null || randomPlusReasonName.trim().isEmpty()) {
            throw new IllegalStateException("randomCourierReasonName is null or empty. Previous test may have failed.");
        }
        
        System.out.println("Test: Search, Update and Delete Courier Reason: " + randomPlusReasonName);
        
        try {
            WebElement searchBox = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//input[@name='searchValue' and @placeholder='Search...']"))
            );

            searchBox.click();
            Thread.sleep(500);

     
            searchBox.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            searchBox.sendKeys(randomPlusReasonName);
            Thread.sleep(1000);
            searchBox.sendKeys(Keys.ENTER);
            Thread.sleep(2000);

            takeScreenshot("12_search_results.png");
            
            
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,200);");
            Thread.sleep(500);
            
          
            searchBox.sendKeys(Keys.TAB);
            Thread.sleep(500);
            
          
            WebElement tableHeader = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//th[contains(text(),'Name')]")
            ));
            tableHeader.click();
            Thread.sleep(500);
            
   
            WebElement menuButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//tr[td//div[text()='" + randomPlusReasonName + "']]//button[contains(@class,'MuiIconButton-root')]")
            ));
            menuButton.click();
            
      
            WebElement editOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[normalize-space()='Edit']")
            ));
            editOption.click();
            Thread.sleep(2000);
            System.out.println("Edit option clicked successfully");

            takeScreenshot("12_edit_form_opened.png");

     
            WebElement statusDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='combobox' and contains(@class,'MuiSelect-select') and text()='Active']")
            ));
            statusDropdown.click();
            Thread.sleep(1000);
            System.out.println("Status dropdown clicked");

 
            WebElement inactiveOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='option' and @data-value='inactive' and text()='Inactive']")
            ));
            inactiveOption.click();
            Thread.sleep(1000);
            System.out.println("Changed status to Inactive");

            takeScreenshot("12_status_changed_to_inactive.png");

 
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit' or contains(text(),'Save') or contains(text(),'Update')]")
            ));
            saveButton.click();
            Thread.sleep(2000);
            System.out.println("Changes saved successfully");

    
            WebElement dropdownButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class,'MuiAutocomplete-popupIndicator')]")
            ));
            dropdownButton.click();
            Thread.sleep(1000);
            System.out.println("Autocomplete dropdown opened");
            
            WebElement inactiveFilterOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='option' and text()='Inactive']")
            ));
            inactiveFilterOption.click();
            Thread.sleep(1000);
            System.out.println("Selected 'Inactive' from dropdown filter");

 
            WebElement menuButtonForDelete = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//tr[td//div[text()='" + randomPlusReasonName + "']]//button[contains(@class,'MuiIconButton-root')]")
            ));
            menuButtonForDelete.click();		

            Thread.sleep(1000);


            WebElement deleteOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[normalize-space()='Delete']")
            ));
            deleteOption.click();
            Thread.sleep(1000);

            WebElement confirmDeleteButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='button' and normalize-space()='Delete']")
            ));
            confirmDeleteButton.click();
            Thread.sleep(2000);
            
            System.out.println("Shop reason deleted successfully: " + randomPlusReasonName);
            takeScreenshot("12_courier_reason_deleted.png");
            
 
            JavascriptExecutor js2 = (JavascriptExecutor) driver;
            js2.executeScript("window.scrollBy(0,-200);"); 
            Thread.sleep(500);

        } catch (Exception e) {
            System.err.println("Error in testSearchUpdateDeleteCourierReason: " + e.getMessage());
            takeScreenshot("error_search_update_delete_courier_reason.png");
            throw e;
        }
    }
     
    
    @AfterClass
    public void tearDown() {
        System.out.println("Cleaning up test environment...");
        if (driver != null) {
            try {
                takeScreenshot("final_state.png");
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