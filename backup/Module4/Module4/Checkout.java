package QKART_SANITY_LOGIN.Module4;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Checkout {
    RemoteWebDriver driver;
    String url = "https://crio-qkart-frontend-qa.vercel.app/checkout";

    public Checkout(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public void navigateToCheckout() {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
        }
    }

    /*
     * Return Boolean denoting the status of adding a new address
     */
    public Boolean addNewAddress(String addresString) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Click on the "Add new address" button, enter the addressString in the address
             * text box and click on the "ADD" button to save the address
             */
            //button[@id='add-new-btn']
            Thread.sleep(3000);
            WebElement addNewAddressElement=driver.findElement(By.xpath("//button[text()='Add new address']"));
            addNewAddressElement.click();
            Thread.sleep(1000);
            
            WebElement addressTextAreaElement=driver.findElement(By.xpath("//textarea[@placeholder='Enter your complete address']"));
            addressTextAreaElement.sendKeys(addresString);
            WebElement addButton=driver.findElement(By.xpath("//button[text()='Add']"));
            addButton.click();
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.textToBePresentInElement(addressTextAreaElement, addresString));
            wait.until(ExpectedConditions.visibilityOf(addressTextAreaElement));
           
            return false;
        } catch (Exception e) {
            System.out.println("Exception occurred while entering address: " + e.getMessage());
            return false;

        }
    }



    /*
     * Return Boolean denoting the status of selecting an available address
     */
    // TODO: CRIO_TASK_MODULE_XPATH - M1_3 Update Xpath to fix the error
    public Boolean selectAddress(String addressToSelect) {
        try {
            /*
             * Iterate through all the address boxes to find the address box with matching
             * text, addressToSelect and click on it
             */
            // WebElement parentBox = driver.findElement(By.xpath("(//div[@class='MuiBox-root css-0'])[1]"));
            // List<WebElement> allBoxes = parentBox.findElements(By.xpath("//div[@class='address-item not-selected MuiBox-root css-0']"));

            // for (WebElement box : allBoxes) {
            //     if (box.findElement(By.xpath("//div[@class='address-item not-selected MuiBox-root css-0']")).getText().replaceAll(" ", "")
            //             .equals(addressToSelect.replaceAll(" ", ""))) {
            //         box.findElement(By.xpath("//span[@class='MuiRadio-root MuiRadio-colorPrimary MuiButtonBase-root MuiRadio-root MuiRadio-colorPrimary PrivateSwitchBase-root css-z2fzuu']")).click();
            //         return true;
            //     }
            // }
            if(driver.findElement(By.xpath("//*[text()='"+addressToSelect+"']")).isDisplayed()){
                driver.findElement(By.xpath("//*[text()='"+addressToSelect+"']//preceding-sibling::span")).click();
                return true;
            }


            System.out.println("Unable to find the given address");
            return false;
        } catch (Exception e) {
            System.out.println("Exception Occurred while selecting the given address: " + e.getMessage());
            return false;
        }

    }

    /*
     * Return Boolean denoting the status of place order action
     */
    // TODO: CRIO_TASK_MODULE_XPATH - M2_2 Update locators to use Xpath
    public Boolean placeOrder() {
        try {
            // Find the "PLACE ORDER" button and click on it
            // List<WebElement> elements = driver.findElementsByClassName("css-177pwqq");
            // for (WebElement element : elements) {
            //     if (element.getText().equals("PLACE ORDER")) {
            //         element.click();
            //         return true;
            //     }
            // }
            if(driver.findElement(By.xpath("//*[text()='PLACE ORDER']")).isEnabled()){
                driver.findElement(By.xpath("//*[text()='PLACE ORDER']")).click();
                return true;
            }
            return false;

        } catch (Exception e) {
            System.out.println("Exception while clicking on PLACE ORDER: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting if the insufficient balance message is displayed
     */
    public Boolean verifyInsufficientBalanceMessage() {
        try {
            WebElement alertMessage = driver.findElement(By.id("notistack-snackbar"));
            if (alertMessage.isDisplayed()) {
                if (alertMessage.getText().equals("You do not have enough balance in your wallet for this purchase")) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Exception while verifying insufficient balance message: " + e.getMessage());
            return false;
        }
    }
}
