package ru.safronov;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Класс запуска тестов Cucumber
 */
@CucumberOptions(
    //если имеются нереализоваанные шаги, тесты будут падать
    strict = true,
    //декорирование вывода в консоли
    monochrome = true,
    //используемые плагины для форматирования вывода, отчетов
    plugin = {},
    //местонахождение пакета с фича-файлами
    features = "src/test/features",
    //пакет с реализацией шагов
    glue = {"stepdefs", "hooks"},
    //Указатель тегов для запуска тестов. Префиксы ~/not исключают тест из списка запускаемых тестов, например ~@fail или not в зависимости от версиии cucumber;
    tags = "not @excluded"
)

@RunWith(Cucumber.class)
public class CucumberRunnerTest {

}
