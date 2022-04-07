package pullrequest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import datasource.ReceiveData;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author mashirocl@gmail.com
 * @since 2022/01/13 14:07
 */
public class PullRequest {
    String url;
    State state;
    String login;
    String body;
    String closedmerged_by;
    String created_at;
    String updated_at;
    String comments_url;
    List<Comment> commentList = new LinkedList<>();
    String closedmerged_at;
    /**
     * Contents in body is counted as the 1st comment which is from the pull request proposer(developer)
     * @param jsonObject
     */
    public PullRequest(JsonObject jsonObject1){
        url = jsonObject1.get("url").toString().replaceAll("\"","").trim();
        JsonObject jsonObject = null;
        try{
            HttpResponse<String> httpResponse_pulls = ReceiveData.useGitApi(url);
            JsonElement jsonPullRequest = JsonParser.parseString(httpResponse_pulls .body());
            jsonObject = jsonPullRequest.getAsJsonObject();
        } catch (IOException | InterruptedException e){
            e.printStackTrace();
        }

        state = getState(jsonObject);
        login = jsonObject.get("user").getAsJsonObject().get("login").toString().replaceAll("\"","").trim();
        body = jsonObject.get("body").toString();
        if(state==State.open){
            closedmerged_by = null;
            closedmerged_at = null;
        }else if(state==State.closed){
            //To obtain closed_by, need to access " https://api.github.com/repos/{OWNER}/{REPOSITORY}}/issues/xxx"
            JsonObject jsonObject_issue = null;
            try{
                HttpResponse<String> httpResponse_issue = ReceiveData.useGitApi(jsonObject.get("issue_url")
                        .toString().replaceAll("\"","").trim());
                JsonElement jsonPullRequest_issue = JsonParser.parseString(httpResponse_issue.body());
                jsonObject_issue = jsonPullRequest_issue.getAsJsonObject();
            } catch (IOException | InterruptedException e){
                e.printStackTrace();
            }
//            pull request is not closed anonymously
            if (jsonObject_issue.get("closed_by").equals("null")) {
                closedmerged_by = jsonObject_issue.get("closed_by").getAsJsonObject().get("login").toString().replaceAll("\"", "").trim();
            }
//            if closed anonymously, set the pull request proposer as the closer
            else{
                closedmerged_by = jsonObject_issue.get("user").getAsJsonObject().get("login").toString();
            }
            closedmerged_at = jsonObject_issue.get("closed_at").toString();
        }
        else if(state==State.merged){
            closedmerged_by = jsonObject.get("merged_by").getAsJsonObject().get("login").toString().replaceAll("\"","").trim();
            closedmerged_at = jsonObject.get("merged_at").toString();
        }

        created_at = jsonObject.get("created_at").toString();
        updated_at = jsonObject.get("updated_at").toString();
        comments_url = jsonObject.get("comments_url").toString().replaceAll("\"","");
        commentList = getCommentList(comments_url);
    }
    public PullRequest(){

    }

    public State getState(JsonObject jsonObject){
        String s = jsonObject.get("state").toString().replaceAll("\"","").trim();
        State res = null;
        if(s.equals("open")){
            res= State.open;
        }
        else if(s.equals("closed")){
            if(jsonObject.get("merged").toString().replaceAll("\"","").trim().equals("false")){
                res = State.closed;
            }
            else{
                res = State.merged;
            }
        }
        else{
            res=State.unknown;
        }
        return res;
    }

    public List<Comment> getCommentList(String comments_url) {
        List<Comment> commentList = new LinkedList<>();
        try{
            HttpResponse<String> response = ReceiveData.useGitApi(comments_url);
            JsonElement jsonComments = JsonParser.parseString(response.body());
            for(JsonElement jsonElement:jsonComments.getAsJsonArray()){
                commentList.add(new Comment(jsonElement.getAsJsonObject()));
            }
        }
        catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
        return commentList;
    }
}

