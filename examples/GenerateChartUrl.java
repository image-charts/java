import com.image.charts.ImageCharts;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class GenerateChartUrl {
    public static void main(String[] args) throws MalformedURLException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        String chartUrl = new ImageCharts()
                .cht("bvg") // vertical bar chart
                .chs("300x300") // 300px x 300px
                .chd("a:60,40") // 2 data points: 60 and 40
                .toURL(); // get the generated URL

        System.out.println(chartUrl); // https://image-charts.com/chart?cht=bvg&chs=300x300&chd=a%3A60%2C40
    }
}
