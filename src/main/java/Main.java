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
//        String comments_url = "https://api.github.com/repos/bennidi/mbassador/issues/167/comments";
//        String command = "curl -H Accept: application/vnd.github.v3+json -H \'Authorization: token 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\' https://api.github.com/repos/bennidi/mbassador/pulls?state=all&sort=updated&direction=desc&per_page=100'";
//        JsonElement jsonElement= JsonParser.parseReader(new InputStreamReader
//                (Runtime.getRuntime().exec(command).getInputStream()));
//        System.out.println(jsonElement.toString());
        PullRequestService pullRequestService = new PullRequestService("https://github.com/mozilla/rhino");
        pullRequestService.write2csv(pullRequestService.getPullRequestList(),"/Users/leichen/Desktop/rhino.csv");
    }
}
