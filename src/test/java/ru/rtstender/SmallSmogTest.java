package ru.rtstender;


import org.junit.Test;
import org.junit.BeforeClass;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SmallSmogTest {

    public static DataPage dataPage;
    public static WebDriver driver;
    public Double sum;

    @BeforeClass
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", ConfProperties.getProperty("chromedriver"));
        //создание экземпляра драйвера
        driver = new ChromeDriver();
        dataPage = new DataPage(driver);
        //окно разворачивается на полный экран
        driver.manage().window().maximize();
        //задержка на выполнение теста = 10 сек.
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //получение ссылки на страницу входа из файла настроек
        driver.get(ConfProperties.getProperty("page"));
    }

    @Test
    public void getDataTest() {
        //кликаем на коммерческую закупку
        dataPage.clickChcComPurchase();
        //кликаем на "закупка с нормами ФЗ 223"
        dataPage.clickChkPurchase223FZ();
        //устанавливаем начальную цену 0
        dataPage.inputStartPrice("0");
        //вводим начальную дату публикации извещения и подтгиваем ее из конфига
        dataPage.inputPublicationDateFrom(ConfProperties.getProperty("startDateFrom"));
        //вводим конечную дату публикации извещения  и подтгиваем ее из конфига
        dataPage.inputPublicationDateTo(ConfProperties.getProperty("startDateTo"));
        //роизводим поиск
        dataPage.clickFindBtn();
        //перелистнем страницу
        dataPage.clickNextPage();
        //выбираем те элементы у которых есть номер ЕИС
        //String table  = dataPage.getListOfRows();
        //System.out.println(table);
        System.out.print("Количество лотов:");
        System.out.print("Итоговая сумма:");
    }
}
