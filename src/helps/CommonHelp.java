package helps;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.net.util.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.*;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author by cld
 * @date 2020/5/20  21:44
 * @description:
 */
public class CommonHelp {




    /**
     * 在c盘文件夹中生成二维码
     * @param content
     * @return
//     */
//    public static Boolean makeQrCode(String content){
//        String location = "C:\\QrCode";
//        File file = new File(location);
//        if(!file.exists()){
//            file.mkdir();
//        }
//        return makeQrCode(content,location);
//    }
//
//    /**
//     * 指定生成二维码位置
//     * @param content
//     * @param location
//     * @return
//     */
//    public static Boolean makeQrCode(String content,String location ){
//        int width=300;
//        int height=300;
//        return makeQrCode(width,height,content,location);
//    }

    /**
     * 指定生成二维码位置以及大小
     * @param width
     * @param height
     * @param content
     * @param location
     * @return
     */
//    public static Boolean makeQrCode(int width,int height,String content,String location ){
//        //二维码格式
//        String format = "jpg";
//        //定义二维码参数
//        HashMap hints = new HashMap();
//        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//        hints.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.M);
//        hints.put(EncodeHintType.MARGIN,2);
//        //生成二维码
//        try {
//            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
//            Path file = new File(location+"\\img."+format).toPath();
//            MatrixToImageWriter.writeToPath(bitMatrix, format, file);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }


    /**
     * 从大集合中取等量数据
     *
     * @param list 大集合
     * @param num  每次取的个数 从大集合中取数，每次取100个
     * @return
     */
    public static List<List> subList(List list, int num) {
        List<List> returnList = new ArrayList();
        for (int i = 0; i < list.size(); i = i + num) {
            //最后一次截取集合
            if (i + num > list.size()) {
                num = list.size() - i;
            }
            List newList = list.subList(i, i + num);
            returnList.add(newList);
        }
        return returnList;
    }

    /**
     * @param list
     * @param num  分成 多少个集合
     *             <p>
     *             1000
     *             分成60个集合    ,每个集合16.66 个数
     * @return
     */
    public static List<List> subList2(List list, int num) {

        Integer total = list.size();
        BigDecimal mm = new BigDecimal(total).divide(new BigDecimal(num), 0);
        List<List> lists = subList(list, mm.intValue());
        return lists;
    }


    /**
     * MD5加密()
     * @param res
     * @return
     */
    public static String encodeMD5(String res) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(res.getBytes("utf-8"));
            final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
            StringBuilder ret = new StringBuilder(bytes.length * 2);
            for (int i = 0; i < bytes.length; i++) {
                ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
                ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
            }
            return ret.toString();
        }catch (Exception e){
            return null;
        }
    }

    /**
     * base64编码
     * @param res
     * @return
     */
    public static String encode(String res) {
        return new String(Base64.encodeBase64(res.getBytes()));
    }


    /**
     * 流转 字节数组
     * @param is
     * @return
     * @throws IOException
     */
    public static byte[] InputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        byte[] buffer=new byte[1024];
        int ch;
        while ((ch = is.read(buffer)) != -1) {
            bytestream.write(buffer,0,ch);
        }
        byte data[] = bytestream.toByteArray();
        bytestream.close();
        return data;
    }

    /**
     * 格式化json
     * @param jsonStr
     * @return
     */
    public  String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }

    /**
     * 添加space
     * @param sb
     * @param indent
     */
    private  void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }


    /**
     * 压缩
     * @param str
     * @return
     * @throws IOException
     */
    public static String newcompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return "";
        }

        byte[] tArray;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        try {
            gzip.write(str.getBytes("UTF-8"));
            gzip.flush();
        } catch (Exception e){

        }finally {
            gzip.close();
        }

        tArray = out.toByteArray();
        out.close();

        BASE64Encoder tBase64Encoder = new BASE64Encoder();
        return tBase64Encoder.encode(tArray);
    }


    /**
     * 解压
     * @param str
     * @return
     * @throws IOException
     */
    public static String newuncompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return "";
        }

        BASE64Decoder tBase64Decoder = new BASE64Decoder();
        byte[] t = tBase64Decoder.decodeBuffer(str);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(t);
        GZIPInputStream gunzip = new GZIPInputStream(in);
        try {
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        }finally{
            gunzip.close();
        }
        in.close();
        out.close();
        return out.toString("UTF-8");
    }
//
//    public static void download(String path, HttpServletResponse response) {
//        try {
//            // path是指欲下载的文件的路径。
//            File file = new File(path);
//            // 取得文件名。
//            String filename = file.getName();
//            // 以流的形式下载文件。
//            InputStream fis = new BufferedInputStream(new FileInputStream(path));
//            byte[] buffer = new byte[fis.available()];
//            fis.read(buffer);
//            fis.close();
//            // 清空response
//            response.reset();
//            // 设置response的Header
//            response.addHeader("Content-Disposition", "attachment;filename="
//                    + new String(filename.getBytes()));
//            response.addHeader("Content-Length", "" + file.length());
//            OutputStream toClient = new BufferedOutputStream(
//                    response.getOutputStream());
//            response.setContentType("application/vnd.ms-excel;charset=gb2312");
//            toClient.write(buffer);
//            toClient.flush();
//            toClient.close();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }

    public static void downloadFile(String path, HttpServletResponse response) throws Exception{
        File file = new File(path);
        String filename = file.getName();
        InputStream fin = null;
        ServletOutputStream out = null;
        try {
            fin = new FileInputStream(file);
            out = response.getOutputStream();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));

            byte[] buffer = new byte[1024];
            int bytesToRead = -1;
            // 通过循环将读入的Word文件的内容输出到浏览器中
            while((bytesToRead = fin.read(buffer)) != -1) {
                out.write(buffer, 0, bytesToRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fin != null) fin.close();
            if(out != null) out.close();

        }
    }
//
//
    /**
     * base64加密
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(String key) {
        return (new BASE64Encoder()).encodeBuffer(key.getBytes());
    }


    /**
     * base64解密
     * @param key
     * @return
     */
    public static String decryptBASE64(String key){
        String dekey="";
        try {
            dekey = new String(new BASE64Decoder().decodeBuffer(key));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dekey;
    }


    /**
     * 全屏截图
     * @param location
     * @param fileName
     * @param imageFormat
     */
    public static void screenshot(String location ,String fileName, String imageFormat){
        //获取屏幕分辨率
        Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
        try {
            //拷贝屏幕到一个BufferedImage对象screenshot
            BufferedImage screenshot=(new Robot()).createScreenCapture(
                    new Rectangle(0,0,(int)d.getWidth(),(int)d.getHeight()));
//            serialNum++;
            //根据文件前缀变量和文件格式变量，自动生成文件名
            String name=location+"\\"+fileName+"."+imageFormat;
            System.out.println(name);
            File f=new File(name);
            System.out.println("Save File-"+name);
            //将screenshot对象写入图像文件
            ImageIO.write(screenshot, imageFormat, f);
            System.out.println("..Finished");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

//    public void jsonToOracleCreateSQL(String jsonStrr) {
//        JSONObject jsonObject = JSONObject.parseObject(jsonStrr);
//        Iterator<String> keys = jsonObject.keySet().iterator();// jsonObject.keys();
//        while (keys.hasNext()) {
//            String key = keys.next();
//            stringBuffer.append(key.toString()).append("|");
//            if (jsonObject.get(key) instanceof JSONObject) {
//                JSONObject innerObject = (JSONObject) jsonObject.get(key);
//                stringBuffer.append(getAllKey(innerObject));
//            } else if (jsonObject.get(key) instanceof JSONArray) {
//                JSONArray innerObject = (JSONArray) jsonObject.get(key);
//                stringBuffer.append(getAllKey(innerObject));
//            }
//        }
//    }


    public static  void getAllKey(String jsonStrr) {

//        JSONObject jsonObject = JSONObject.parseObject(jsonStrr);

        LinkedHashMap<String, Object> jsonMap = JSON.parseObject(jsonStrr, new TypeReference<LinkedHashMap<String, Object>>() {
        });

        for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());

        }

//        Iterator<String> keys = jsonObject.keySet().iterator();// jsonObject.keys();
//        while (keys.hasNext()) {
//            String key = keys.next();
//            System.out.println(key);
//            if (jsonObject.get(key) instanceof JSONObject) {
//                JSONObject innerObject = (JSONObject) jsonObject.get(key);
//            } else if (jsonObject.get(key) instanceof JSONArray) {
//                JSONArray innerObject = (JSONArray) jsonObject.get(key);
////                stringBuffer.append(getAllKey(innerObject));
//            }
//        }

    }

    public static void main(String[] args) {

        List<String> list = new ArrayList<String>() {
            {
                add("a");
                add("b");
            }};

            String jsonStrr = "{\n" +
                "    \"bankAccount\": \"\",\n" +
                "    \"buyerAddress\": \"\",\n" +
                "    \"buyerPhone\": \"\",\n" +
                "    \"buyerTaxPayerId\": \"\",\n" +
                "    \"invoiceHeader\": \"\",\n" +
                "    \"invoiceId\": \"\",\n" +
                "    \"issueId\": \"\",\n" +
                "    \"lanId\": \"8310100\",\n" +
                "    \"amounts\": \"39800\",\n" +
                "    \"type\": \"1000\",\n" +
                "    \"object\": [\n" +
                "        {\n" +
                "            \"id\": \"189113334035\",\n" +
                "            \"productId\": \"111333\",\n" +
                "            \"billMonth\": \"202007\",\n" +
                "            \"amount\": \"19900\",\n" +
                "            \"invoiceId\": \"\",\n" +
                "            \"status\": \"2\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        CommonHelp.getAllKey(jsonStrr);
    }


    /**
     * 包含两个方法的HelloWorld接口
     */
    interface HelloWorld {
        public void greet();
        public void greetSomeone(String someone);
    }






}
