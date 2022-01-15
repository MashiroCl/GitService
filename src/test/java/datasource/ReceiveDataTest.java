package datasource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mashirocl@gmail.com
 * @since 2022/01/12 18:20
 */
class ReceiveDataTest {
    ReceiveData receiveData;
    public ReceiveDataTest(){
        receiveData = new ReceiveData();
    }

    @Test
    public void testCurl() throws IOException {
        String url = "https://github.com/bennidi/mbassador";
        String result = receiveData.Curl(url);
        System.out.println(result);
    }


    @ParameterizedTest
    @CsvSource({"https://api.github.com/repos/bennidi/mbassador/pulls?state=all&sort=updated&direction=desc&per_page=100"})
    void testUseGitApi(String api) throws IOException, InterruptedException {
        ReceiveData.useGitApi(api);
    }
}