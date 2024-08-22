import com.me.test.test_framework.junit5.annotations.TestCaseID;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdditionTest {

    @Order(1)
    @Test
    @TestCaseID("123")
    public void testAddition1() {
        // This method no longer takes parameters.
        assertEquals(1, 1);
    }

    @Order(2)
    @TestCaseID("124")  // Ensure unique TestCaseID
    @ParameterizedTest
    @CsvSource({
            "1, 1, 2",
            "2, 3, 5",
            "3, 7, 10",
            "-1, 5, 4",
            "0, 0, 0"
    })
    public void testAddition(int a, int b, int expectedResult) {
        assertEquals(expectedResult, add(a, b));
    }

    int add(int a, int b) {
        return a + b;
    }
}
