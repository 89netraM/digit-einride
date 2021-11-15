import main.java.api.DonkeyCar;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DonkeyCarTest {

    @BeforeAll
    static void init() {
        DonkeyCar.initOpenCV();
    }

    @Test
    public void getInstructionsDataShouldReturnExpectedOutput() {
        String EXPECTED = "{ angle: 0.11, throttle: 7.70 }";
        assertEquals(EXPECTED, DonkeyCar.getInstructionsData(0.11, 7.7));
    }

    @Test
    public void getAddressShouldReturnExpectedOutput() {
        String EXPECTED = "http://donkeycar:8887/video";
        assertEquals(EXPECTED, DonkeyCar.getAddress("donkeycar", 8887, "/video"));
    }

    @Test
    public void getVideoCaptureShouldReturnNotNull() {
        assertNotNull(DonkeyCar.getVideoCapture("donkeycar", 8887, "/video"));
    }

}
