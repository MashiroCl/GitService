package pullrequest;

import com.google.gson.JsonObject;

/**
 * @author mashirocl@gmail.com
 * @since 2022/01/14 11:21
 */
public class Comment {
    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    String url;
    String user;
    String created_at;
    String updated_at;

    public Comment(JsonObject jsonObject){
        url = jsonObject.get("url").toString();
        user = jsonObject.get("user").getAsJsonObject().get("login").toString().replaceAll("\"","").trim();
        created_at = jsonObject.get("created_at").toString();
        updated_at = jsonObject.get("updated_at").toString();
    }

}
