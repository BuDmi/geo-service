import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MessageSenderTest {

    private static GeoService geoService;
    private static LocalizationService localizationService;
    @BeforeAll
    static void init() {
        geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp(Mockito.startsWith("172.")))
            .thenReturn(new Location("Moscow", Country.RUSSIA, null, 0));
        Mockito.when(geoService.byIp(Mockito.startsWith("96.")))
            .thenReturn(new Location("New York", Country.USA, null, 0));
        Mockito.when(geoService.byIp(Mockito.startsWith("127.")))
            .thenReturn(new Location(null, null, null, 0));
        localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Mockito.any(Country.class))).thenReturn("Welcome");
        Mockito.when(localizationService.locale(Country.RUSSIA)).thenReturn("Добро пожаловать");
    }

    @ParameterizedTest
    @MethodSource("source")
    public void testMessage(String ip, String message) {
        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        Map<String, String> map = new HashMap<>();
        map.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);

        Assertions.assertEquals(message, messageSender.send(map));
    }

    private static Stream<Arguments> source() {
        return Stream.of(
            Arguments.of("172.0.32.11", "Добро пожаловать"),
            Arguments.of("172.44.543.1", "Добро пожаловать"),
            Arguments.of("96.44.183.149", "Welcome"),
            Arguments.of("96.2.0.1", "Welcome"),
            Arguments.of("", "Welcome")
        );
    }
}
