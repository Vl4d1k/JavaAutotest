package ru.rtstender;

import org.junit.Test;
import org.junit.BeforeClass;

import java.io.FileInputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.IOException;
import java.util.logging.*;

public class SmallSmogTest {
    public static Logger logger;
    public static DataPage dataPage;
    public static WebDriver driver;

    //имя атрибутов
    public static String attrNameForEAS = "BaseMainContent_MainContent_jqgTrade_OosNumber";
    public static String attrNamePrice = "BaseMainContent_MainContent_jqgTrade_StartPrice";
    public static String attrNameCanceled = "BaseMainContent_MainContent_jqgTrade_LotStateString";
    //строка с тестом заказа, если он отменен
    public static String canceled = "Отменена";
    //сумма всеъ закупок с номер ЕИС
    public static Double sumOfAllItem = new Double(0);
    //сумма отменненых закупок
    public static Double sumOfCanceledItem = new Double(0);
    //количество закупок
    public static int lots = new Integer(0);

    @BeforeClass
    public static void setup() {
        logger = Logger.getLogger("MyLog");

        try {
            FileHandler fh = new FileHandler("./log/log.txt");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //установка web-driver
        System.setProperty("webdriver.chrome.driver", ConfProperties.getProperty("chromedriver"));

        driver = new ChromeDriver();
        dataPage = new DataPage(driver);

        //окно разворачивается на полный экран
        driver.manage().window().maximize();
        //задержка на выполнение теста = 10 сек.
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //получение ссылки на страницу из файла настроек
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

        WebDriverWait wait = new WebDriverWait(driver, 10);
        //существует ли ИАС номер
        boolean isExistEAS = false;
        //цена текущей закупки
        double price = 0;
        String priceString = new String();
        //класс, когда кнопка заблокирована
        String nextBtnDisabledClass = new String("ui-pg-button ui-corner-all ui-state-disabled");

        while (!nextBtnDisabledClass.equals(dataPage.getNextPageBtnClass())) {
            //задержка для загрузки данных
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sp_1_BaseMainContent_MainContent_jqgTrade_pager")));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("ui-paging-info")));
            //не получилось сделать подругому
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //берем данные с таблицы
            WebElement table = driver.findElement(By.id("BaseMainContent_MainContent_jqgTrade"));
            //получаем элементы tr
            List < WebElement > allRows = table.findElements(By.tagName("tr"));
            // Получаем колонки
            for (WebElement row: allRows) {
                List < WebElement > cells = row.findElements(By.tagName("td"));
                isExistEAS = false;
                for (WebElement cell: cells) {

                    //выбираем те элементы у которых есть номер ЕИС
                    if (!isExistEAS && attrNameForEAS.equals(cell.getAttribute("aria-describedby")))
                        if (cell.getText().length() != 0) {
                            isExistEAS = true;
                            lots++;
                        }

                    if (isExistEAS && attrNamePrice.equals(cell.getAttribute("aria-describedby"))) {
                        priceString = cell.getText().replaceAll("[^0-9?!\\,]", "").replace(",", ".");
                        price = Double.parseDouble(priceString);
                        sumOfAllItem += price;
                        logger.info("Стоимость закупки: " + String.valueOf(price));
                    }

                    if (isExistEAS && canceled.equals(cell.getText())) {
                        sumOfCanceledItem += price;
                        logger.info("Закупка отменена.");
                    }
                }
                logger.info("+------------------------------------------------------+");
            }
            
            dataPage.clickNextPage();
        }

        double result = sumOfAllItem - sumOfCanceledItem;

        logger.info("Итоговая сумма: " + String.valueOf(result));
        logger.info("Количество лотов: " + String.valueOf(lots));
    }
}