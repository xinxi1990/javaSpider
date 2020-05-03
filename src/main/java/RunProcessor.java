import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import org.apache.log4j.Logger;
import us.codecraft.webmagic.selector.Html;

import javax.net.ssl.SSLContext;
import java.util.ArrayList;
import java.util.List;


public class RunProcessor implements PageProcessor {

    public int maxCount = 2;

    public int spiderCount = 0;
    public int commitCount = 0;

    Logger logger = Logger.getLogger(RunProcessor.class);


    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    public void process(Page page) {

        logger.info("当前爬虫URL ==> " + page.getUrl() + " ==> " + spiderCount);
        spiderCount = spiderCount + 1;

        List<CommentDo> CommentDTOS = new ArrayList<CommentDo>();

        List<String> commentList = page.getHtml().xpath("//div[@class=\"comment\"]").all();
        for (String comment : commentList) {
            Html commentHtml = Html.create(comment);

            // 用户名
            String user = commentHtml.xpath("//h3/span[2]/a/text()").get();
            // 分数
            String star = commentHtml.xpath("//h3/span[2]/span[2]/@class").get();
            // 评分时间
            String date_time = commentHtml.xpath("//h3/span[2]/span[3]/@title").get();
            String date = commentHtml.xpath("//h3/span[2]/span[3]").get();

            String commentText = commentHtml.xpath("//p/span/text()").get();
            CommentDo commentdo = new CommentDo();
            commentdo.setUser(user);
            commentdo.setCommentText(commentText);
            commentdo.setStar(getMovStar(star));
            CommentDTOS.add(commentdo);
        }

        //page.addTargetRequests(page.getHtml().links().regex("(https://movie.douban\\.com/\\w+/\\w+)").all());
        //logger.info("爬虫搜索==> ");

        page.putField("comment", CommentDTOS);

        // 获取下一页的连接
        String nextPageUrl = page.getHtml().xpath("//div[@id='paginator']/a[@class='next']").links().get();
        logger.info("下一页地址 ==> " + nextPageUrl);
        if (StringUtils.isNotBlank(nextPageUrl)) {
            // 将下一页地址存入 page 这样才会继续爬取
            page.addTargetRequest(nextPageUrl);
        }

    }


    /***
     *
     * @return
     */
    public Site getSite() {
        site.setUserAgent("User-Agent\": 'Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0)");
        site.addHeader("Cookie", Config.cookie);
        return site;
    }

    public String getMovStar(String start){
        try {
            int movStart = Integer.valueOf(start.replace("allstar","").replace(" rating","") ) / 10;
            return String.valueOf(movStart);
        }catch (Exception e){
            return "0";
        }

    }



}


