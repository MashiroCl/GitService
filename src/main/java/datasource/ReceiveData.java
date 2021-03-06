package datasource;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
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


    public static HttpResponse useGitApi(String api) throws IOException, InterruptedException {
        String gitToken = System.getenv("GIT_SERVICE_IMPLEMENTAION_TOKEN");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().
                header("Accept","application/vnd.github.v3+json").
                header("Authorization","token " +gitToken).
                uri(URI.create(api))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
        }


}
