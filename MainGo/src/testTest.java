import go.Goban;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class testTest {
    @Test
    public void  testGo(){
        Goban g = new Goban(6,"B2 A2 A3 A1");
        assertEquals(2, g.getliberties("A3"));
        assertEquals(1, g.getliberties("A2"));
        assertEquals(1, g.getliberties("A1"));
        assertEquals(3, g.getliberties("B2"));
        g.play("black","B1");
    }
}