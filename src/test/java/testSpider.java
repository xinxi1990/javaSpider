import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.PixelBoundryBackground;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.tokenizers.ChineseWordTokenizer;
import com.kennycason.kumo.palette.ColorPalette;
import org.apache.commons.io.FileUtils;
import us.codecraft.webmagic.Spider;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class testSpider {


    public static String filePath = "/Users/xinxi/Documents/sndd/javaSpider/";

    public static void main(String[] args) throws IOException {

        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
        String startURL = "https://movie.douban.com/subject/30488569/comments?limit=20&sort=new_score&status=P";

        Spider.create(new RunProcessor())
                .addUrl(startURL)
                .addPipeline(new ResultPipeline())
                //开启5个线程抓取
                .thread(1)
                //启动爬虫
                .run();

        System.out.println(ResultPipeline.getCommitCount());

        worldCloudTest(ResultPipeline.getCommitList());

    }


    /***
     * 生成词云
     * @param getCommitList
     * @throws IOException
     */
    public static void worldCloudTest(ArrayList getCommitList) throws IOException {

        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        // 设置词频
        frequencyAnalyzer.setWordFrequenciesToReturn(600);
        frequencyAnalyzer.setMinWordLength(2);
        // 中文分词
        frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());
        // 过滤一些预期助词等
        List stopWorlds = FileUtils.readLines(new File( filePath + "stopworld/cn_stopwords.txt"));
        // 过滤词
        frequencyAnalyzer.setStopWords(stopWorlds);

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getCommitList);

        final Dimension dimension = new Dimension(500, 312);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        // 词语背景图
        wordCloud.setBackground(new PixelBoundryBackground(filePath +"pic/whale_small.png"));
        // 设置颜色
        wordCloud.setColorPalette(new ColorPalette(new Color(0xC73939), new Color(0x10E71C), new Color(0x0984D5), new Color(0x73CB0E), new Color(0x40D3F1), new Color(0xFFC97915, true)));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile(filePath + "worldcloud/test.png");
    }

}
