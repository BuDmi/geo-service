import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.i18n.LocalizationServiceImpl;

import java.util.stream.Stream;

public class LocalizationServiceImplTest {

    private static LocalizationServiceImpl localizationService;

    @BeforeAll
    static void init() {
        localizationService = new LocalizationServiceImpl();
    }

    @ParameterizedTest
    @MethodSource("source")
    public void testLocaleText(String text, Country country) {
        Assertions.assertEquals(text, localizationService.locale(country));
    }

    private static Stream<Arguments> source() {
        return Stream.of(
            Arguments.of("Добро пожаловать", Country.RUSSIA),
            Arguments.of("Welcome", Country.USA),
            Arguments.of("Welcome", Country.BRAZIL),
            Arguments.of("Welcome", Country.GERMANY)
        );
    }
}
