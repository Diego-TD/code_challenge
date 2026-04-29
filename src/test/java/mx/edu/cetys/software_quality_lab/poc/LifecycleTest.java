package mx.edu.cetys.software_quality_lab.poc;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@DisplayName("Lifecycle and Display Name annotations")
public class LifecycleTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LifecycleTest.class);

    @BeforeAll
    static void beforeAll() {
        LOGGER.info("Before All Tests");
    }

    @BeforeEach
    void beforeEach() {
        LOGGER.info("Before Each Test");
    }

    @AfterEach
    void afterEach() {
        LOGGER.info("After Each Test");
    }

    @Test
    @DisplayName("Test 1 🤔 ╰(*°▽°*)╯")
    void test1(){
        LOGGER.info("This is a test 1");
    }

    @Test
    @DisplayName("Test 2 👌👌👌")
    void test2(){
        LOGGER.info("This is a test 2");
    }

    @AfterAll
    static void afterAll() {
        LOGGER.info("After All Tests");
    }
}
