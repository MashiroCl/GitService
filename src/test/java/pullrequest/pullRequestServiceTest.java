package pullrequest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mashirocl@gmail.com
 * @since 2022/01/12 20:49
 */
class PullRequestServiceTest {
    PullRequestService pullRequestService = new PullRequestService("https://github.com/mozilla/rhino");

    @Test
    public void testListPullRequest() throws IOException, InterruptedException {
        JsonArray jsonArray = pullRequestService.ListPullRequest();
        System.out.println(jsonArray.size());
        System.out.println(jsonArray.get(0).getAsJsonObject().get("user").getAsJsonObject().toString());
//        System.out.println(jsonObject.toString());
    }

}