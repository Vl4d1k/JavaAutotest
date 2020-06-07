package ru.rtstender;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class DataPage {

    public WebDriver driver;

    public DataPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public String getItems() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@class, 'account__name_hasAccentLetter')]")));
        //String item = userMenu.getText();
        return "1";
    }

    //метод для ввода начальной цены
    public void inputStartPrice(String price) {
        startPriceField.sendKeys(price);
    }

    //метод для нажатия кнопки поиска
    public void clickFindBtn() {
        findItemsBtn.click();
    }

    //метод для ввода начальной даты публикации извещения
    public void inputPublicationDateFrom(String date) {
        publicationDateFrom.sendKeys(date);
    }

    //метод для вводв конечной даты публикации извещения
    public void inputPublicationDateTo(String date) {
        publicationDateTo.sendKeys(date);
    }

    //метод для клика по чебоксу "Комерческая закупка"
    public void clickChcComPurchase() {
        chkComPurchase.click();
    }

    //метод для клика по чебоксу "ФЗ 233"
    public void clickChkPurchase223FZ() {
        chkPurchase223FZ.click();
    }

    //метод для перелистывания страницы
    public void clickNextPage() {
        nextPageBtn.click();
    }

    public String getNextPageBtnClass() { return nextWrappedPageBtn.getAttribute("class"); }


    //локатор для номера ЕИС
    @FindBy(xpath = "//*[contains(@id, 'BaseMainContent_MainContent_chkPurchaseType_1')]")
    private WebElement numberEIS;

    //локатор для чекбокса "Комерческая закупка"
    @FindBy(xpath = "//*[contains(@id, 'BaseMainContent_MainContent_chkPurchaseType_1')]")
    private WebElement chkComPurchase;

    //локатор для чекбокса "Закупка в соответствии с нормами 223-ФЗ"
    @FindBy(xpath = "//*[contains(@id, 'BaseMainContent_MainContent_chkPurchaseType_0')]")
    private WebElement chkPurchase223FZ;

    //локатор input'a "Начальная цена"
    @FindBy(xpath = "//*[contains(@id, 'BaseMainContent_MainContent_txtStartPrice_txtRangeFrom')]")
    private WebElement startPriceField;

    //локатор input'a "Дата публикации извещения" №1
    @FindBy(xpath = "//*[contains(@id, 'BaseMainContent_MainContent_txtPublicationDate_txtDateFrom')]")
    private WebElement publicationDateFrom;

    //локатор input'a "Дата публикации извещения" №2
    @FindBy(xpath = "//*[contains(@id, 'BaseMainContent_MainContent_txtPublicationDate_txtDateTo')]")
    private WebElement publicationDateTo;

    //локатор для ссылки на следующую страницу
    @FindBy(xpath = "//span[@class='ui-icon ui-icon-seek-next']")
    private WebElement nextPageBtn;

    //локатор для ссылки на следующую страницу
    @FindBy(xpath = "//td[@id='next_t_BaseMainContent_MainContent_jqgTrade_toppager']")
    private WebElement nextWrappedPageBtn;

    //локатор для кнопки поиска
    @FindBy(xpath = "//*[contains(@id, 'BaseMainContent_MainContent_btnSearch')]")
    private WebElement findItemsBtn;

}
