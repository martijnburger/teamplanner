package nl.paston.teamplanner.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PageCountTest {

    @ParameterizedTest
    @CsvSource({
            "0, 20, 0",
            "1, 20, 1",
            "4, 2, 2",
            "5, 2, 3",
            "20, 20, 1",
            "21, 20, 2",
    })
    void pageCount(long count, int pageSize, int expected) {
        assertEquals(expected, AbstractRest.pageCount(count, pageSize));
    }

}
