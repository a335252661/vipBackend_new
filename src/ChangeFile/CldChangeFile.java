package ChangeFile;

import java.io.*;
import java.util.ArrayList;

/**
 * @author ������
 * @version 1.0
 * @Description ��idc�ļ��а������Ƶ̼�¼ת�Ƶ��Ƶ��ļ���
 * @date 2020/2/28
 */
public class CldChangeFile {
    private static String dirLocation = "C:\\Users\\Admin\\Desktop\\����\\IDC-����-������\\2020-05-1����\\������ԭʼ�ļ�\\";

    private static String ChangedirLocation = "C:\\Users\\Admin\\Desktop\\����\\IDC-����-������\\2020-05-1����\\����֮���ļ�\\";
    //idc
    private static String IDCFileName = "CRM2BILL.ZD.202005.008.021";
    //����
    private static String YUNFileName = "CRM2BILL.ZD.202005.007.021";


    private static int idcSize = 0;
    private static int yunSize = 0;
    private static int changeSize = 0;
//    private static  int  idcHend

    public static void main(String[] args) {
        CldChangeFile cldChangeFile = new CldChangeFile();
        ArrayList<String> strings = cldChangeFile.readFile(dirLocation + IDCFileName);

        ArrayList<String> arrContains = new ArrayList<String>();
        ArrayList<String> arrNotContains = new ArrayList<String>();

        System.out.println("ԭidc�ļ�ͷ�� �� " + strings.get(0));
        idcSize = strings.size() - 2;
        System.out.println("ԭidc�ļ���¼����(������ͷβ) �� " + idcSize);

        Long idcAllAmount = 0L;
        Long idcContainsYUNAllAmount = 0L;
        Long idcidcAmount = 0L;

        Long amount = 0L;
        for (String str : strings) {
            if (!str.contains("STA") && !str.contains("END")) {
                amount = Long.parseLong(str.split("\\|")[3]);
                idcAllAmount += amount;
            }

            if (str.contains("13411525") ||
                    str.contains("13410985") ||
                    str.contains("13412685") ||
                    str.contains("13412686") ||
                    str.contains("13413465")) {  //OTN

                arrContains.add(str);

                idcContainsYUNAllAmount += amount;
            } else {
                arrNotContains.add(str);

                if (!str.contains("STA") && !str.contains("END")) {
                    idcidcAmount += amount;
                }


            }

        }


        //idc�����޸�֮���ļ�
        changeSize = arrContains.size();
        System.out.println("�������ļ���¼���� =" + changeSize);

        arrNotContains.set(0, "STA|" + (arrNotContains.size() - 2));
        System.out.println("�޸�֮���ļ�ͷ�� =" + arrNotContains.get(0));
        System.out.println("�޸�֮���ļ���¼����(������ͷβ) �� " + (arrNotContains.size() - 2));

        cldChangeFile.writeFile(arrNotContains, ChangedirLocation + IDCFileName);


        //���޸ĵ����ݷŵ��Ƶ��ļ���
        ArrayList<String> yunStrings = cldChangeFile.readFile(dirLocation + YUNFileName);

        Long Amount1 = 0L;
        Long yunAllAmount = 0L;
        for (String str : yunStrings) {
            if (!str.contains("STA") && !str.contains("END")) {
                Amount1 = Long.parseLong(str.split("\\|")[3]);
                yunAllAmount += Amount1;
            }
        }

        System.out.println("ԭ�����ļ�ͷ�� �� " + yunStrings.get(0));
        System.out.println("ԭ�����ļ���¼����(������ͷβ) �� " + (yunStrings.size() - 2));

        //�޸�
        yunStrings.set(0, "STA|" + (yunStrings.size() - 2 + arrContains.size()));
        yunStrings.remove(yunStrings.size() - 1);
        yunStrings.addAll(arrContains);
        yunStrings.add("END");
        cldChangeFile.writeFile(yunStrings, ChangedirLocation + YUNFileName);


        System.out.println("�������ļ���¼���� =" + arrContains.size());
        System.out.println("����֮���ļ�ͷ�� =" + yunStrings.get(0));
        System.out.println("�޸�֮���ļ���¼����(������ͷβ) �� " + (yunStrings.size() - 2));
        yunSize = yunStrings.size()-2;

//        private static int idcSize=0;
//        private static int yunSize=0;
//        private static int changeSize=0;
        //�����ʼ�����
        String data = "IDC ���˼���ļ���\n" +
                IDCFileName + "\n" +
                "\tIDC�ļ�ԭ��¼��������ͷβ��" + idcSize + ";  \n" +
                "\tIDC�ļ��д�������ҵ�����ݼ�¼���� " + changeSize + ";  \n" +
                "\tԭIDC�ļ��ܽ��ϼƣ� " + idcAllAmount + "��;  \n" +
                "\tԭIDC�ļ��ܽ�����idc��� : " + idcidcAmount + "�֣��Ƶ̽��:" + idcContainsYUNAllAmount + "��  ���޴����ļ�\n" +
                "\n" +
                "�Ƶ� ���˼���ļ���\n" +
                YUNFileName +
                "ԭʼ��¼��������ͷβ��" + yunSize + "��    ���ϼƣ� " + yunAllAmount + "��";

        System.out.println(data);

    }


    public ArrayList<String> readFile(String pathname) {
        ArrayList<String> list = new ArrayList<String>();
        try {
            FileReader reader = new FileReader(pathname);
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void writeFile(ArrayList<String> arr, String str) {
        ArrayList<String> list = new ArrayList<String>();
        try {
            OutputStream os = new FileOutputStream(str);
            PrintWriter pw = new PrintWriter(os);
            for (String strs : arr) {
                pw.println(strs);
            }
            try {
                pw.close();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
