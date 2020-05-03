import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.ArrayList;
import java.util.List;

public class ResultPipeline implements Pipeline {

    public static Logger logger = Logger.getLogger(ResultPipeline.class);

    public static int commitCount = 0;

    public static ArrayList commitList = new ArrayList();

    public void process(ResultItems resultItems, Task task) {

        logger.info(" resultpipeline ==> ");

        List<CommentDo> CommentDOS = resultItems.get("comment");
        if (CollectionUtils.isNotEmpty(CommentDOS)) {
            for (CommentDo commentdo : CommentDOS) {
                logger.info(" user ==> " + commentdo.getUser());
                logger.info(" comment ==> " + commentdo.getCommentText());
                logger.info(" star ==> " + commentdo.getStar());
                commitCount = commitCount + 1;
                commitList.add(commentdo.getCommentText());
            }
        }

    }

    public static int getCommitCount() {
        return commitCount;
    }

    public static ArrayList getCommitList() {
        return commitList;
    }



}
