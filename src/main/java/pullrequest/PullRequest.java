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
    String state;
    String login;
    String body;
    String created_at;
    String updated_at;
    String comments_url;
    Map<String,Integer> developerCommentsCount;
    List<Comment> commentList = new LinkedList<>();

    /**
     * Contents in body is counted as the 1st comment which is from the pull request proposer(developer)
     * @param jsonObject
     */
    public PullRequest(JsonObject jsonObject){
        url = jsonObject.get("url").toString();
        state = jsonObject.get("state").toString();
        login = jsonObject.get("user").getAsJsonObject().get("login").toString().replaceAll("\"","").trim();
        body = jsonObject.get("body").toString();
        created_at = jsonObject.get("created_at").toString();
        updated_at = jsonObject.get("updated_at").toString();
        comments_url = jsonObject.get("comments_url").toString().replaceAll("\"","");
        developerCommentsCount = new HashMap<>();
        developerCommentsCount.put(login,1);
        if(comments_url!=null) {
            for (Comment comment : getCommentList(comments_url)) {
                developerCommentsCount.put(comment.getUser(), developerCommentsCount.getOrDefault(comment.getUser(), 0) + 1);
            }
        }

    }
    public PullRequest(){

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

