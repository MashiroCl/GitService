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
                printWriter = new PrintWriter(new File(output));
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
            builder.append(pullRequest.comments_url+",");
            builder.append(pullRequest.created_at+",");
            builder.append(pullRequest.updated_at+",");
            for(String developer:pullRequest.developerCommentsCount.keySet()){
                builder.append(developer+",");
                builder.append(pullRequest.developerCommentsCount.get(developer).toString());
                builder.append(",");
            }
            builder.append("\n");
            printWriter.append(builder.toString());
        }
        printWriter.close();
        System.out.println("Write to CSV "+ output);
    }


}
