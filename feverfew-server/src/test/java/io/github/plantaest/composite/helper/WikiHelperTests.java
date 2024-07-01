package io.github.plantaest.composite.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WikiHelperTests {

    @Test
    public void action_api_uri_of_enwiki() {
        String serverName = "en.wikipedia.org";
        String expectedUri = "https://en.wikipedia.org/w/api.php";
        String actualUri = WikiHelper.createActionApiUri(serverName);
        assertEquals(expectedUri, actualUri);
    }

    @Test
    public void action_api_uri_of_viwiki() {
        String serverName = "vi.wikipedia.org";
        String expectedUri = "https://vi.wikipedia.org/w/api.php";
        String actualUri = WikiHelper.createActionApiUri(serverName);
        assertEquals(expectedUri, actualUri);
    }

}
