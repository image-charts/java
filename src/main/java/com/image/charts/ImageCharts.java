package com.image.charts;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.io.UnsupportedEncodingException;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ImageCharts {
    private static final String DEFAULT_ENCODING = "UTF-8";

    private String secret;
    private Integer timeout = 5000;
    private String host = "image-charts.com";
    private String protocol = "https";
    private Integer port = 443;
    private String pathname = "/chart";
    private Map<String, Object> query = new LinkedHashMap<String, Object>();

    /**
     * Free usage
     **/
    public ImageCharts() {
        this(null, null, null, null, null);
    }

    /**
    * Enterprise & Enterprise+
    * @param secret  (Enterprise and Enterprise+ subscription only) SECRET_KEY. Default : null
    */
    public ImageCharts(String secret) {
        this(secret, null);
    }

    /**
    * Enterprise & Enterprise+
    * @param secret  (Enterprise and Enterprise+ subscription only) SECRET_KEY. Default : null
    * @param timeout  Request timeout (in millisecond) when calling toBuffer() or toDataURI(). Default if null : 5000
    */
    public ImageCharts(String secret, Integer timeout) {
        this(null, null, null, null, secret, timeout);
    }

    /**
    * On-premise
    * @param protocol  (On-Premise subscription only) custom protocol. Default if null : "https"
    * @param host  (Enterprise, Enterprise+ and On-Premise subscription only) custom domain. Default if null : "image-charts.com"
    * @param port  (On-Premise subscription only) custom port. Default if null "443"
    * @param pathname  (On-Premise subscription only) custom pathname. Default if null "/chart"
    * @param secret  (Enterprise and Enterprise+ subscription only) SECRET_KEY. Default : null
    */
    public ImageCharts(String protocol, String host, Integer port, String pathname, String secret) {
        this(protocol, host, port, pathname, secret, null);
    }

    /**
    * On-premise
    * @param protocol  (On-Premise subscription only) custom protocol. Default if null : "https"
    * @param host  (Enterprise, Enterprise+ and On-Premise subscription only) custom domain. Default if null : "image-charts.com"
    * @param port  (On-Premise subscription only) custom port. Default if null "443"
    * @param pathname  (On-Premise subscription only) custom pathname. Default if null "/chart"
    * @param secret  (Enterprise and Enterprise+ subscription only) SECRET_KEY. Default : null
    * @param timeout  Request timeout (in millisecond) when calling toBuffer() or toDataURI(). Default if null : 5000
    */
    public ImageCharts(String protocol, String host, Integer port, String pathname, String secret, Integer timeout) {
        this.secret = secret;
        if (timeout != null) this.timeout = timeout;
        if (host != null) this.host = host;
        if (protocol != null) this.protocol = protocol;
        if (port != null) this.port = port;
        if (pathname != null) this.pathname = pathname;
    }

    private ImageCharts clone(String key, Object value) {
        this.query.put(key, value);
        return this;
    }

    
    /**
    * bvg= grouped bar chart, bvs= stacked bar chart, lc=line chart, ls=sparklines, p=pie chart. gv=graph viz
	*         Three-dimensional pie chart (p3) will be rendered in 2D, concentric pie chart are not supported.
	*         [Optional, line charts only] You can add :nda after the chart type in line charts to hide the default axes.
    *
    * @param cht - Chart type. 
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/reference/chart-type/">cht</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().cht("bvg");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().cht("p");}
    */
    public ImageCharts cht(String cht) {
        return this.clone("cht", cht);
    }
    
    /**
    * chart data
    *
    * @param chd - chart data. 
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/reference/data-format/">chd</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chd("a:-100,200.5,75.55,110");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chd("t:10,20,30|15,25,35");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chd("s:BTb19_,Mn5tzb");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chd("e:BaPoqM2s,-A__RMD6");}
    */
    public ImageCharts chd(String chd) {
        return this.clone("chd", chd);
    }
    
    /**
    * You can configure some charts to scale automatically to fit their data with chds=a. The chart will be scaled so that the largest value is at the top of the chart and the smallest (or zero, if all values are greater than zero) will be at the bottom. Otherwise the &#34;&amp;lg;series_1_min&amp;gt;,&amp;lg;series_1_max&amp;gt;,...,&amp;lg;series_n_min&amp;gt;,&amp;lg;series_n_max&amp;gt;&#34; format set one or more minimum and maximum permitted values for each data series, separated by commas. You must supply both a max and a min. If you supply fewer pairs than there are data series, the last pair is applied to all remaining data series. Note that this does not change the axis range; to change the axis range, you must set the chxr parameter. Valid values range from (+/-)9.999e(+/-)199. You can specify values in either standard or E notation.
    *
    * @param chds - data format with custom scaling. 
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/reference/data-format/#text-format-with-custom-scaling">chds</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chds("-80,140");}
    */
    public ImageCharts chds(String chds) {
        return this.clone("chds", chds);
    }
    
    /**
    * How to encode the data in the QR code. &#39;UTF-8&#39; is the default and only supported value. Contact our team if you wish to have support for Shift_JIS and/or ISO-8859-1.
    *
    * @param choe - QRCode data encoding. 
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/qr-codes/#data-encoding">choe</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().choe("UTF-8");}
    */
    public ImageCharts choe(String choe) {
        return this.clone("choe", choe);
    }
    
    /**
    * QRCode error correction level and optional margin
    *
    * @param chld - QRCode error correction level and optional margin. Default : "L|4"
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/qr-codes/#error-correction-level-and-margin">chld</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chld("L|4");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chld("M|10");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chld("Q|5");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chld("H|18");}
    */
    public ImageCharts chld(String chld) {
        return this.clone("chld", chld);
    }
    
    /**
    * You can specify the range of values that appear on each axis independently, using the chxr parameter. Note that this does not change the scale of the chart elements (use chds for that), only the scale of the axis labels.
    *
    * @param chxr - Axis data-range. 
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/reference/chart-axis/#axis-range">chxr</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chxr("0,0,200");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chxr("0,10,50,5");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chxr("0,0,500|1,0,200");}
    */
    public ImageCharts chxr(String chxr) {
        return this.clone("chxr", chxr);
    }
    
    /**
    * Some clients like Flowdock/Facebook messenger and so on, needs an URL to ends with a valid image extension file to display the image, use this parameter at the end your URL to support them. Valid values are &#34;.png&#34; and &#34;.gif&#34;
    *
    * @param chof - Output fake format. Default : ".png"
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/reference/output-format/">chof</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chof(".png");}
    */
    public ImageCharts chof(String chof) {
        return this.clone("chof", chof);
    }
    
    /**
    * Maximum chart size for all charts except maps is 998,001 pixels total (Google Image Charts was limited to 300,000), and maximum width or length is 999 pixels.
    *
    * @param chs - Chart size (&lt;width&gt;x&lt;height&gt;). 
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/reference/chart-size/">chs</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chs("400x400");}
    */
    public ImageCharts chs(String chs) {
        return this.clone("chs", chs);
    }
    
    /**
    * Format: &amp;lt;data_series_1_label&amp;gt;|...|&amp;lt;data_series_n_label&amp;gt;. The text for the legend entries. Each label applies to the corresponding series in the chd array. Use a + mark for a space. If you do not specify this parameter, the chart will not get a legend. There is no way to specify a line break in a label. The legend will typically expand to hold your legend text, and the chart area will shrink to accommodate the legend.
    *
    * @param chdl - Text for each series, to display in the legend. 
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/reference/legend-text-and-style/">chdl</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chdl("NASDAQ|FTSE100|DOW");}
    */
    public ImageCharts chdl(String chdl) {
        return this.clone("chdl", chdl);
    }
    
    /**
    * Specifies the color and font size of the legend text. &lt;color&gt;,&lt;size&gt;
    *
    * @param chdls - Chart legend text and style. Default : "000000"
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/reference/legend-text-and-style/">chdls</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chdls("9e9e9e,17");}
    */
    public ImageCharts chdls(String chdls) {
        return this.clone("chdls", chdls);
    }
    
    /**
    * Solid or dotted grid lines
    *
    * @param chg - Solid or dotted grid lines. 
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/reference/grid-lines/">chg</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chg("1,1");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chg("0,1,1,5");}
    */
    public ImageCharts chg(String chg) {
        return this.clone("chg", chg);
    }
    
    /**
    * You can specify the colors of a specific series using the chco parameter.
	*       Format should be &amp;lt;series_2&amp;gt;,...,&amp;lt;series_m&amp;gt;, with each color in RRGGBB format hexadecimal number.
	*       The exact syntax and meaning can vary by chart type; see your specific chart type for details.
	*       Each entry in this string is an RRGGBB[AA] format hexadecimal number.
	*       If there are more series or elements in the chart than colors specified in your string, the API typically cycles through element colors from the start of that series (for elements) or for series colors from the start of the series list.
	*       Again, see individual chart documentation for details.
    *
    * @param chco - series colors. Default : "F56991,FF9F80,FFC48C,D1F2A5,EFFAB4"
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/bar-charts/#examples">chco</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chco("FFC48C");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chco("FF0000,00FF00,0000FF");}
    */
    public ImageCharts chco(String chco) {
        return this.clone("chco", chco);
    }
    
    /**
    * chart title
    *
    * @param chtt - chart title. 
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/reference/chart-title/">chtt</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chtt("My beautiful chart");}
    */
    public ImageCharts chtt(String chtt) {
        return this.clone("chtt", chtt);
    }
    
    /**
    * Format should be &#34;&lt;color&gt;,&lt;font_size&gt;[,&lt;opt_alignment&gt;,&lt;opt_font_family&gt;,&lt;opt_font_style&gt;]&#34;, opt_alignement is not supported
    *
    * @param chts - chart title colors and font size. 
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/reference/chart-title/">chts</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chts("00FF00,17");}
    */
    public ImageCharts chts(String chts) {
        return this.clone("chts", chts);
    }
    
    /**
    * Specify which axes you want (from: &#34;x&#34;, &#34;y&#34;, &#34;t&#34; and &#34;r&#34;). You can use several of them, separated by a coma; for example: &#34;x,x,y,r&#34;. Order is important.
    *
    * @param chxt - Display values on your axis lines or change which axes are shown. 
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/reference/chart-axis/#visible-axes">chxt</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chxt("y");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chxt("x,y");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chxt("x,x,y");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chxt("x,y,t,r,t");}
    */
    public ImageCharts chxt(String chxt) {
        return this.clone("chxt", chxt);
    }
    
    /**
    * Specify one parameter set for each axis that you want to label. Format &#34;&lt;axis_index&gt;:|&lt;label_1&gt;|...|&lt;label_n&gt;|...|&lt;axis_index&gt;:|&lt;label_1&gt;|...|&lt;label_n&gt;&#34;. Separate multiple sets of labels using the pipe character ( | ).
    *
    * @param chxl - Custom string axis labels on any axis. 
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/reference/chart-axis/#custom-axis-labels">chxl</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chxl("0:|Jan|July|Jan");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chxl("0:|Jan|July|Jan|1|10|20|30");}
    */
    public ImageCharts chxl(String chxl) {
        return this.clone("chxl", chxl);
    }
    
    /**
    * You can specify the range of values that appear on each axis independently, using the chxr parameter. Note that this does not change the scale of the chart elements (use chds for that), only the scale of the axis labels.
    *
    * @param chxs - Font size, color for axis labels, both custom labels and default label values. 
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/reference/chart-axis/#axis-label-styles">chxs</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chxs("1,0000DD");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chxs("1N*cUSD*Mil,FF0000");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chxs("1N*cEUR*,FF0000");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chxs("2,0000DD,13,0,t");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chxs("0N*p*per-month,0000FF");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chxs("0N*e*,000000|1N*cUSD*Mil,FF0000|2N*2sz*,0000FF");}
    */
    public ImageCharts chxs(String chxs) {
        return this.clone("chxs", chxs);
    }
    
    /**
    * 
	* format should be either:
	*   - line fills (fill the area below a data line with a solid color): chm=&lt;b_or_B&gt;,&lt;color&gt;,&lt;start_line_index&gt;,&lt;end_line_index&gt;,&lt;0&gt; |...| &lt;b_or_B&gt;,&lt;color&gt;,&lt;start_line_index&gt;,&lt;end_line_index&gt;,&lt;0&gt;
	*   - line marker (add a line that traces data in your chart): chm=D,&lt;color&gt;,&lt;series_index&gt;,&lt;which_points&gt;,&lt;width&gt;,&lt;opt_z_order&gt;
	*   - Text and Data Value Markers: chm=N&lt;formatting_string&gt;,&lt;color&gt;,&lt;series_index&gt;,&lt;which_points&gt;,&lt;width&gt;,&lt;opt_z_order&gt;,&lt;font_family&gt;,&lt;font_style&gt;
	*     
    *
    * @param chm - compound charts and line fills. 
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/reference/compound-charts/">chm</a>
    * <p/>
    * Examples :

    */
    public ImageCharts chm(String chm) {
        return this.clone("chm", chm);
    }
    
    /**
    * line thickness and solid/dashed style
    *
    * @param chls - line thickness and solid/dashed style. 
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/line-charts/#line-styles">chls</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chls("10");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chls("3,6,3|5");}
    */
    public ImageCharts chls(String chls) {
        return this.clone("chls", chls);
    }
    
    /**
    * If specified it will override &#34;chdl&#34; values
    *
    * @param chl - bar, pie slice, doughnut slice and polar slice chart labels. 
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/reference/chart-label/">chl</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chl("label1|label2");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chl("multi
	* line
	* label1|label2");}
    */
    public ImageCharts chl(String chl) {
        return this.clone("chl", chl);
    }
    
    /**
    * chart margins
    *
    * @param chma - chart margins. 
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/reference/chart-margin/">chma</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chma("30,30,30,30");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chma("40,20");}
    */
    public ImageCharts chma(String chma) {
        return this.clone("chma", chma);
    }
    
    /**
    * Position of the legend and order of the legend entries
    *
    * @param chdlp - Position of the legend and order of the legend entries. Default : "r"
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/reference/legend-text-and-style/">chdlp</a>
    * <p/>
    * Examples :

    */
    public ImageCharts chdlp(String chdlp) {
        return this.clone("chdlp", chdlp);
    }
    
    /**
    * Background Fills
    *
    * @param chf - Background Fills. Default : "bg,s,FFFFFF"
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/reference/background-fill/">chf</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chf("b0,lg,0,f44336,0.3,03a9f4,0.8");}
    */
    public ImageCharts chf(String chf) {
        return this.clone("chf", chf);
    }
    
    /**
    * gif configuration
    *
    * @param chan - gif configuration. 
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/reference/animation/">chan</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chan("1200");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chan("1300|easeInOutSine");}
    */
    public ImageCharts chan(String chan) {
        return this.clone("chan", chan);
    }
    
    /**
    * doughnut chart inside label
    *
    * @param chli - doughnut chart inside label. 
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/pie-charts/#inside-label">chli</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chli("95K€");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().chli("45%");}
    */
    public ImageCharts chli(String chli) {
        return this.clone("chli", chli);
    }
    
    /**
    * image-charts enterprise `account_id`
    *
    * @param icac - image-charts enterprise `account_id`. 
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/enterprise/">icac</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().icac("accountId");}
    */
    public ImageCharts icac(String icac) {
        return this.clone("icac", icac);
    }
    
    /**
    * HMAC-SHA256 signature required to activate paid features
    *
    * @param ichm - HMAC-SHA256 signature required to activate paid features. 
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/enterprise/">ichm</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().ichm("0785cf22a0381c2e0239e27c126de4181f501d117c2c81745611e9db928b0376");}
    */
    public ImageCharts ichm(String ichm) {
        return this.clone("ichm", ichm);
    }
    
    /**
    * How to use icff to define font family as Google Font : https://developers.google.com/fonts/docs/css2
    *
    * @param icff - Default font family for all text from Google Fonts. Use same syntax as Google Font CSS API. 
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/reference/chart-font/">icff</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().icff("Abel");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().icff("Akronim");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().icff("Alfa Slab One");}
    */
    public ImageCharts icff(String icff) {
        return this.clone("icff", icff);
    }
    
    /**
    * Default font style for all text
    *
    * @param icfs - Default font style for all text. 
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/reference/chart-font/">icfs</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().icfs("normal");}
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().icfs("italic");}
    */
    public ImageCharts icfs(String icfs) {
        return this.clone("icfs", icfs);
    }
    
    /**
    * localization (ISO 639-1)
    *
    * @param iclocale - localization (ISO 639-1). 
    * @return {ImageCharts}
    * @see <a href="">iclocale</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().iclocale("en");}
    */
    public ImageCharts iclocale(String iclocale) {
        return this.clone("iclocale", iclocale);
    }
    
    /**
    * Retina is a marketing term coined by Apple that refers to devices and monitors that have a resolution and pixel density so high — roughly 300 or more pixels per inch – that a person is unable to discern the individual pixels at a normal viewing distance.
	*           In order to generate beautiful charts for these Retina displays, Image-Charts supports a retina mode that can be activated through the icretina=1 parameter
    *
    * @param icretina - retina mode. 
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/reference/retina/">icretina</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().icretina("1");}
    */
    public ImageCharts icretina(String icretina) {
        return this.clone("icretina", icretina);
    }
    
    /**
    * Background color for QR Codes
    *
    * @param icqrb - Background color for QR Codes. Default : "FFFFFF"
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/qr-codes/#background-color">icqrb</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().icqrb("FFFFFF");}
    */
    public ImageCharts icqrb(String icqrb) {
        return this.clone("icqrb", icqrb);
    }
    
    /**
    * Foreground color for QR Codes
    *
    * @param icqrf - Foreground color for QR Codes. Default : "000000"
    * @return {ImageCharts}
    * @see <a href="https://documentation.image-charts.com/qr-codes/#foreground-color">icqrf</a>
    * <p/>
    * Examples :
    * {@code ImageCharts.Builder chart = new ImageCharts.Builder().icqrf("000000");}
    */
    public ImageCharts icqrf(String icqrf) {
        return this.clone("icqrf", icqrf);
    }
    

    /**
     * Get the full Image-Charts API url (signed and encoded if necessary)
     *
     * @return {String} full generated url
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public String toURL() throws MalformedURLException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        StringBuilder queryParams = new StringBuilder();
        final Iterator<Map.Entry<String, Object>> it = this.query.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry<String, Object> entry = it.next();
            final String key = entry.getKey();
            queryParams.append(key);
            queryParams.append('=');

            final Object value = entry.getValue();
            final String valueAsString = value != null ? URLEncoder.encode(value.toString(), DEFAULT_ENCODING) : "";
            queryParams.append(valueAsString);

            if (it.hasNext()) {
                queryParams.append('&');
            }
        }

        if (query.containsKey("icac") && this.secret != null && this.secret.length() > 0) {
            queryParams.append("&ichm=" + sign(secret, queryParams.toString()));
        }

        URL url = new URL(this.protocol, this.host, this.port, this.pathname + "?" + queryParams.toString());


        return url.toString();
    }

    /**
     * Do a request to Image-Charts API with current configuration and yield a BufferedImage
     *
     * @return {BufferedImage}
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    public BufferedImage toBuffer() throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        URL url = new URL(this.toURL());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(this.timeout);
        String userAccount = this.query.containsKey("icac") ? " (" + this.query.get("icac") + ")" : "";
        connection.setRequestProperty("User-Agent", "java-image-charts/1.0.1" + userAccount);
        int status = connection.getResponseCode();

        if (status >= 200 && status < 300) {
            return ImageIO.read(connection.getInputStream());
        }

        String validationMessage = connection.getHeaderField("x-ic-error-validation");
        String validationCode = connection.getHeaderField("x-ic-error-code");
        String message = "";

        if (validationMessage != null && !validationMessage.isEmpty()) {
            JSONArray json = new JSONArray(new JSONTokener(validationMessage));
            JSONArray messageArray = new JSONArray();
            for (Object x : json) {
                messageArray.put(((JSONObject) x).getString("message"));
            }
            message = messageArray.join("\n");
        }

        message = !message.isEmpty() ? message : validationCode;

        throw new ImageChartsException(message);
    }

    private String getFileFormat(){
        return this.query.containsKey("chan") ? "gif" : "png";
    }

    /**
     * Do a request to Image-Charts API with current configuration and writes the content inside a file
     * @param file_path
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    public void toFile(String file_path) throws IOException, InvalidKeyException, NoSuchAlgorithmException{
        ImageIO.write(this.toBuffer(), this.getFileFormat(), new File(file_path));
    }

    /**
     * Do a request to Image-Charts API with current configuration and yield a promise of a base64 encoded [data URI](https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/Data_URIs)
     *
     * @return {String} base64 data URI wrapped inside a promise
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public String toDataURI() throws IOException, NoSuchAlgorithmException, InvalidKeyException {

        BufferedImage image = toBuffer();
        String formatName = this.getFileFormat();
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "png", os);

        return "data:" + "image/" + formatName + ";base64," + new BASE64Encoder().encode(os.toByteArray());
    }

    private static String sign(String key, String data) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        return bytesToHex(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
