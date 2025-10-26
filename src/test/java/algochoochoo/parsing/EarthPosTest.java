package algochoochoo.parsing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EarthPosTest {
    @Test
    void testLatitude() {
        EarthPos pos1 = new EarthPos("42.0" , "0.0");
        EarthPos pos2 = new EarthPos("-1.0" , "0.0");
        assertEquals(42.0, pos1.latitude());
        assertEquals(-1.0, pos2.latitude());
    }

    @Test
    void testLongitude() {
        EarthPos pos1 = new EarthPos("0.0" , "-2.0");
        EarthPos pos2 = new EarthPos("0.0" , "67.0");
        assertEquals(-2.0, pos1.longitude());
        assertEquals(67.0, pos2.longitude());
    }

    @Test
    void testToString() {
        EarthPos pos = new EarthPos("6.0" , "7.0");
        assertEquals("Pos(6.0, 7.0)", pos.toString());
    }

    @Test
    void testWrongPos() {
        assertThrows(IllegalArgumentException.class, 
        () -> new EarthPos("xxx", "0.0"));
    }
}