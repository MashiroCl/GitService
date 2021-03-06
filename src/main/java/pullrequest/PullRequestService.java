package pullrequest;

import com.google.gson.*;
import datasource.ReceiveData;

import java.io.*;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author mashirocl@gmail.com
 * @since 2022/01/12 18:15
 */
public class PullRequestService {
    String repoURL;
    public PullRequestService(String repoURL){
        this.repoURL = repoURL;
    }
    public JsonArray ListPullRequest() throws IOException, InterruptedException {
        String apiURL = "https://api.github.com/repos/";
        String ownerAndRepo = repoURL.split("https://github.com/")[1];
        String requestForPullRequest =  new StringBuilder().append(apiURL).append(ownerAndRepo).append("/pulls?state=all&sort=updated&direction=desc&per_page=100").toString();
        HttpResponse<String> response= ReceiveData.useGitApi(requestForPullRequest);
        JsonElement jsonElement = JsonParser.parseString(response.body());

        JsonArray jsonArray = jsonElement.getAsJsonArray();
        return jsonArray;
    }

    public List<PullRequest> getPullRequestList() throws IOException, InterruptedException {
        List<PullRequest> pullRequestList = new LinkedList<>();
        for(JsonElement jsonElement: ListPullRequest()){
            pullRequestList.add(new PullRequest(jsonElement.getAsJsonObject()));
        }
        return pullRequestList;
    }

    public void write2csv(List<PullRequest> pullRequestList,String output){
        PrintWriter printWriter = null;
        StringBuilder builder = new StringBuilder();
            try{
                printWriter = new PrintWriter(output);
                if(!Files.exists(Paths.get(output))){
                    String columnNamesList = "url, created data, updated data, paticipants";
                    builder.append(columnNamesList+"\n");
                    printWriter.write(builder.toString());
                }
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        for(PullRequest pullRequest:pullRequestList){
            builder = new StringBuilder();
            builder.append(pullRequest.comments_url+", ");
            builder.append(pullRequest.state).append(", ");
            builder.append(pullRequest.login).append(", ").append(pullRequest.created_at).append(", ");
            for(Comment comment:pullRequest.commentList){
                builder.append(comment.getUser()).append(", ").append(comment.getCreated_at()).append(", ");
            }
            if(pullRequest.state==State.merged||pullRequest.state==State.closed){
            builder.append(pullRequest.closedmerged_by).append(", ").append(pullRequest.closedmerged_at).append(", ");
            }

            builder.append("\n");
            printWriter.append(builder.toString());
        }
        printWriter.close();
        System.out.println("Write to CSV "+ output);
    }

}
