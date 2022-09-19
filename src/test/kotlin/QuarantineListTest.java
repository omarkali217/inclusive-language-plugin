import Objects.QuarantineItem;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class QuarantineListTest extends TestCase {

    QuarantineList list = new QuarantineList();

    //not working yet.
    @Test
    public void testListBuild() throws IOException {
        List<QuarantineItem> mylist = list.buildQuarantineList();

        assertTrue(true);
        mylist.forEach(System.out::println);

    }
}