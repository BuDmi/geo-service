import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoServiceImpl;

import java.util.stream.Stream;

public class GeoServiceImplTest {
    private static GeoServiceImpl geoService;

    @BeforeAll
    static void init() {
        geoService = new GeoServiceImpl();
    }

    @ParameterizedTest
    @MethodSource("source")
    public void testHostIp(String ip, Location expected) {
        Location actual = geoService.byIp(ip);

        Assertions.assertEquals(expected.getCity(), actual.getCity());
        Assertions.assertEquals(expected.getCountry(), actual.getCountry());
        Assertions.assertEquals(expected.getStreet(), actual.getStreet());
        Assertions.assertEquals(expected.getBuiling(), actual.getBuiling());
    }

    private static Stream<Arguments> source() {
        return Stream.of(
            Arguments.of("127.0.0.1", new Location(null, null, null, 0)),
            Arguments.of("172.0.32.11", new Location("Moscow", Country.RUSSIA, "Lenina", 15)),
            Arguments.of("96.44.183.149", new Location("New York", Country.USA, " 10th Avenue", 32)),
            Arguments.of("172.0.1.10", new Location("Moscow", Country.RUSSIA, null, 0)),
            Arguments.of("96.0.1.10", new Location("New York", Country.USA, null, 0))
        );
    }

    @Test
    public void testByCoordinates() {
        var expected = RuntimeException.class;
        Assertions.assertThrows(expected, () -> geoService.byCoordinates(0.0, 0.0));
    }
}
