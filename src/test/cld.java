package test;

import helps.DateTimeHelp;
import helps.FileHelp;
import helps.TxtWriterHelp;
import helps.WeChatHelp;
import org.apache.commons.lang.ArrayUtils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * @author ������
 * @version 1.0
 * @Description TODO
 * @date 2020/4/16
 */
public class cld {
    public static void main(String[] args) throws Exception {
        String result = execCmd("java -version", null);
        System.out.println(result);
    }






    /**
     * ִ��ϵͳ����, ����ִ�н��
     *
     * @param cmd ��Ҫִ�е�����
     * @param dir ִ��������ӽ��̵Ĺ���Ŀ¼, null ��ʾ�͵�ǰ�����̹���Ŀ¼��ͬ
     */
    public static String execCmd(String cmd, File dir) throws Exception {
        StringBuilder result = new StringBuilder();

        Process process = null;
        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;

        try {
            // ִ������, ����һ���ӽ��̶����������ӽ�����ִ�У�
            process = Runtime.getRuntime().exec(cmd, null, dir);

            // ��������, �ȴ�����ִ����ɣ��ɹ��᷵��0��
            process.waitFor();

            // ��ȡ����ִ�н��, ���������: ��������� �� ����������PS: �ӽ��̵�������������̵����룩
            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));

            // ��ȡ���
            String line = null;
            while ((line = bufrIn.readLine()) != null) {
                result.append(line).append('\n');
            }
            while ((line = bufrError.readLine()) != null) {
                result.append(line).append('\n');
            }

        } finally {
            closeStream(bufrIn);
            closeStream(bufrError);

            // �����ӽ���
            if (process != null) {
                process.destroy();
            }
        }

        // ����ִ�н��
        return result.toString();
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                // nothing
            }
        }
    }
}
