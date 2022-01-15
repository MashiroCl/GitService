package datasource;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Collectors;

/**
 * Get data from github
 * @author mashirocl@gmail.com
 * @since 2022/01/12 18:16
 */
public class ReceiveData {

    /**
     * connect to github according to url
     * @param url
     * @return contents on this url
     * @throws IOException
     */
    public String Curl(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection)new URL(url).openConnection();
        con.setRequestMethod("GET");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String result = bufferedReader.lines().collect(Collectors.joining("\n"));
        bufferedReader.close();
        return result;
    }

    public static Process useGitApi(String ownerAndRepo, String api) throws IOException{
        String gitToken = System.getenv("GIT_SERVICE_IMPLEMENTAION_TOKEN");
        String apiURL = "https://api.github.com/repos/";

        String [] commands = {"curl","-H","Accept:","application/vnd.github.v3+json",
                "Authorization:","token",gitToken,
                "https://api.github.com/repos/bennidi/mbassador/pulls?state=all&sort=updated&direction=desc&per_page=100"};

        StringBuilder sb = new StringBuilder();

        String command = sb.append("curl ").append("-H ").append("\"Accept: application/vnd.github.v3+json\" ")
                .append("-H ").append("\"Authorization: token ").append(gitToken).append("\" ")
                .append(apiURL).append(ownerAndRepo).append(api).toString();
        System.out.println(command);
        return Runtime.getRuntime().exec(command);
    }

    public static HttpResponse useGitApi(String api) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().
                header("Accept","application/vnd.github.v3+json").
                header("Authorization","token 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").
                uri(URI.create(api))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;
        }


}
