import com.image.charts.ImageCharts;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class DownloadChartAsImage {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String chartPath = "/tmp/chart.png";

        new ImageCharts()
                .cht("bvg") // vertical bar chart
                .chs("300x300") // 300px x 300px
                .chd("a:60,40") // 2 data points: 60 and 40
                .toFile(chartPath);

        System.out.println("Image chart written at " + chartPath);
    }
}

