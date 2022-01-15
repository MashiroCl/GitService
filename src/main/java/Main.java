import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import datasource.ReceiveData;
import pullrequest.Comment;
import pullrequest.PullRequestService;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author mashirocl@gmail.com
 * @since 2022/01/14 15:24
 */
public class Main {
    public static void main(String [] args) throws IOException, InterruptedException {
        PullRequestService pullRequestService = new PullRequestService("https://github.com/mozilla/rhino");
        pullRequestService.write2csv(pullRequestService.getPullRequestList(),"/Users/leichen/Desktop/rhino.csv");
    }
}
