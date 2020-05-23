import com.image.charts.ImageCharts;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class DownloadChartAsDataUri {
    public static void main(String[] args) throws MalformedURLException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        String chartUrl = new ImageCharts()
                .cht("bvg") // vertical bar chart
                .chs("300x300") // 300px x 300px
                .chd("a:60,40") // 2 data points: 60 and 40
                .toDataURI();

        System.out.println(chartUrl); // "data:image/png;base64,iVBORw0KGgo...
    }
}


