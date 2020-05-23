import com.image.charts.ImageCharts;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class DownloadChartAsBuffer {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        BufferedImage chartUrl = new ImageCharts()
                .cht("bvg") // vertical bar chart
                .chs("300x300") // 300px x 300px
                .chd("a:60,40") // 2 data points: 60 and 40
                .toBuffer();

        System.out.println(chartUrl); // "BufferedImage@...
    }
}
