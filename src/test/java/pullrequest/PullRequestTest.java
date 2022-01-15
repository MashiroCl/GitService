package pullrequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mashirocl@gmail.com
 * @since 2022/01/14 14:16
 */
class PullRequestTest {
    PullRequest pullRequest = new PullRequest();
    @ParameterizedTest
    @CsvSource({"https://api.github.com/repos/bennidi/mbassador/issues/167/comments"})
    public void testGetCommentList(String comments_url){
        System.out.println(pullRequest.getCommentList(comments_url).get(1).user);
        assertTrue(pullRequest.getCommentList(comments_url).size()>=0);
    }

}