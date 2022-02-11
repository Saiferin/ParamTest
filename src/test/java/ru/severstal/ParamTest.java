package ru.severstal;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.By.partialLinkText;

public class ParamTest {

    @BeforeEach
    void beforeEach() {
        open("https://github.com/");
    }

    @AfterEach
    void tearDown() {
        System.out.println("@AfterEach - executed after each test method.");
    }

    @AfterAll
    static void done() {
        System.out.println("@AfterAll - executed after all test methods.");
    }

    static Stream<Arguments> methodSource() {
        return Stream.of(
                Arguments.of("JUnit5", "#2826"),
                Arguments.of("TestNg", "#2728")
        );
    }

    @ValueSource(strings = {"JUnit5", "TestNg"})
    @ParameterizedTest(name = "Поиск в Github по ключевому слову {0}")
    public void commonSearchTest(String textSearch) {
        $("[data-test-selector='nav-search-input']").setValue(textSearch).pressEnter();
        $(".repo-list").shouldHave(Condition.text(textSearch));
    }

    @CsvSource({
            "JUnit5, #2826",
            "TestNg, #2728"
    })

    @ParameterizedTest(name = "Поиск в Github по ключевому слову {0} и по искомому issue {1}")
    public void commonSearchTest(String textSearch, String issues) {
        $("[data-test-selector='nav-search-input']").setValue(textSearch).pressEnter();
        $(".repo-list").shouldHave(Condition.text(textSearch));
        $("ul.repo-list li").$("a").click();
        $(partialLinkText("Issues")).click();
        $(withText(issues)).should(Condition.visible);
    }

    @MethodSource("methodSource")
    @ParameterizedTest(name = "Поиск в Github по ключевым параметрам {0} - {1}")
    public void commonSearchTest2(String textSearch, String issues) {
        $("[data-test-selector='nav-search-input']").setValue(textSearch).pressEnter();
        $(".repo-list").shouldHave(Condition.text(textSearch));
        $("ul.repo-list li").$("a").click();
        $(partialLinkText("Issues")).click();
        $(withText(issues)).should(Condition.visible);
    }

    enum Direction {
        JUnit5, TestNg
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void testWithEnumSource(Direction textSearch) {
        $("[data-test-selector='nav-search-input']").setValue(String.valueOf(textSearch)).pressEnter();
        $(".repo-list").shouldHave(Condition.text(String.valueOf(textSearch)));
    }
}
