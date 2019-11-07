import org.junit.Test;

import static org.junit.Assert.*;

public class DianMian2Test {

    @Test
    public void findLowestUnassigned() {
        assertEquals(DianMian2.findLowestUnassigned(new int[] {}), 1);
        assertEquals(DianMian2.findLowestUnassigned(new int[] {1}), 2);
        assertEquals(DianMian2.findLowestUnassigned(new int[] {5,3,1}), 2);
        assertEquals(DianMian2.findLowestUnassigned(new int[] {5,2,3,1,4}), 6);

    }
}