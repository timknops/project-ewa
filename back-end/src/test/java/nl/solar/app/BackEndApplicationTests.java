package nl.solar.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BackEndApplicationTests {

    @Autowired
    BackEndApplication backEndApplication = null;

    @Test
    void contextLoads() {
        assertNotNull(backEndApplication);
    }

    @Test
    public void jUnit5HasBeenConfiguredForTesting() {
        assertThrows(RuntimeException.class, () -> { int a = 0; int b = 1 / a; });
    }

}
