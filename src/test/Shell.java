package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.StreamGobbler;

/**
 * @author ������
 * @version 1.0
 * @Description TODO
 * @date 2020/9/3
 */
public class Shell {
    private String user; // �û���
    private String passwd; // ��¼����
    private String host; // ����IP
    private int port; // �˿�

    public Shell(String user, String passwd, String host, int port) {
        this.user = user;
        this.passwd = passwd;
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) {
//        Shell shell = new Shell("bgusr01","bgusr01321","10.145.205.45",22);
//        shell.execute("cd /home/bgusr01/vip_backend");
//        shell.execute("nohup sh /home/bgusr01/vip_backend/test/11.sh &");


        Shell shell = new Shell("bgusr01","lc#v58iHH","10.7.95.70",22);
        shell.execute("cd /home/bgusr01/vip_backend/test");
        shell.execute("sh 11.sh");

    }

    public String execute(String cmd) {
        try {
            // ��������
            Connection conn = new Connection ( host, 22 );
            // ��������
            conn.connect ();
            // ��֤�û�����
            conn.authenticateWithPassword ( user, passwd );
            ch.ethz.ssh2.Session session = conn.openSession ();
            //ִ������
            session.execCommand ( cmd );

            InputStream stdout = new StreamGobbler ( session.getStdout () );
            BufferedReader br = new BufferedReader ( new InputStreamReader ( stdout ) );
            StringBuffer buffer = new StringBuffer ();
            String line;

            while ((line = br.readLine ()) != null) {
                buffer.append ( line + "\n" );
            }

            String result = buffer.toString ();
            System.out.println(result);

            session.close ();
            conn.close ();
            //���û���쳣�����ؽ��Ϊ����ִ�н����������쳣������null
            return result;

        } catch (IOException e) {
            e.printStackTrace ();
            return null;
        }
    }
}
