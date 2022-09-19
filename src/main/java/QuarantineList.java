import Objects.QuarantineItem;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class QuarantineList {

    public List<QuarantineItem> buildQuarantineList() throws IOException {

        final ObjectMapper mapper = new ObjectMapper();

        return Arrays.asList(mapper.readValue(Paths.get("inclusive.json").toFile(), QuarantineItem[].class));

    }
}
