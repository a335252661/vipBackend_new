package ChangeFile;

import java.io.*;
import java.util.ArrayList;

/**
 * @author 程刘德
 * @version 1.0
 * @Description 将idc文件中包含的云堤记录转移到云堤文件中
 * @date 2020/2/28
 */
public class CldChangeFile {
    private static String dirLocation = "C:\\Users\\Admin\\Desktop\\常用\\IDC-云提-物联网\\2020-05-1开账\\服务器原始文件\\";

    private static String ChangedirLocation = "C:\\Users\\Admin\\Desktop\\常用\\IDC-云提-物联网\\2020-05-1开账\\调整之后文件\\";
    //idc
    private static String IDCFileName = "CRM2BILL.ZD.202005.008.021";
    //云提
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

        System.out.println("原idc文件头部 ： " + strings.get(0));
        idcSize = strings.size() - 2;
        System.out.println("原idc文件记录总数(不包含头尾) ： " + idcSize);

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


        //idc生成修改之后文件
        changeSize = arrContains.size();
        System.out.println("新增了文件记录条数 =" + changeSize);

        arrNotContains.set(0, "STA|" + (arrNotContains.size() - 2));
        System.out.println("修改之后文件头部 =" + arrNotContains.get(0));
        System.out.println("修改之后文件记录总数(不包含头尾) ： " + (arrNotContains.size() - 2));

        cldChangeFile.writeFile(arrNotContains, ChangedirLocation + IDCFileName);


        //将修改的数据放到云堤文件中
        ArrayList<String> yunStrings = cldChangeFile.readFile(dirLocation + YUNFileName);

        Long Amount1 = 0L;
        Long yunAllAmount = 0L;
        for (String str : yunStrings) {
            if (!str.contains("STA") && !str.contains("END")) {
                Amount1 = Long.parseLong(str.split("\\|")[3]);
                yunAllAmount += Amount1;
            }
        }

        System.out.println("原云提文件头部 ： " + yunStrings.get(0));
        System.out.println("原云提文件记录总数(不包含头尾) ： " + (yunStrings.size() - 2));

        //修改
        yunStrings.set(0, "STA|" + (yunStrings.size() - 2 + arrContains.size()));
        yunStrings.remove(yunStrings.size() - 1);
        yunStrings.addAll(arrContains);
        yunStrings.add("END");
        cldChangeFile.writeFile(yunStrings, ChangedirLocation + YUNFileName);


        System.out.println("新增了文件记录条数 =" + arrContains.size());
        System.out.println("新增之后文件头部 =" + yunStrings.get(0));
        System.out.println("修改之后文件记录总数(不包含头尾) ： " + (yunStrings.size() - 2));
        yunSize = yunStrings.size()-2;

//        private static int idcSize=0;
//        private static int yunSize=0;
//        private static int changeSize=0;
        //生成邮件内容
        String data = "IDC 开账检查文件：\n" +
                IDCFileName + "\n" +
                "\tIDC文件原记录数（不含头尾）" + idcSize + ";  \n" +
                "\tIDC文件中存在云提业务数据记录条数 " + changeSize + ";  \n" +
                "\t原IDC文件总金额合计： " + idcAllAmount + "分;  \n" +
                "\t原IDC文件总金额：其中idc金额 : " + idcidcAmount + "分，云堤金额:" + idcContainsYUNAllAmount + "分  。无错误文件\n" +
                "\n" +
                "云堤 开账检查文件：\n" +
                YUNFileName +
                "原始记录数（不含头尾）" + yunSize + "；    金额合计： " + yunAllAmount + "分";

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
