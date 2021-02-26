package vip;

import org.apache.commons.lang.ArrayUtils;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/5/20
 */
public class AllEntrances {
    public static void main(String[] args) {
        if(ArrayUtils.contains(args, "0")){
            //跑十个线程，存数据进入cld_temp_data_last
            CreateDateStep0_LAST.main(null);
            System.out.println("存数据进入cld_temp_data_last结束");
        }
        if(ArrayUtils.contains(args, "1")){
            //跑十个线程，存数据进入cld_temp_data
            CreateDateStep1_OCS.main(null);
            System.out.println("存数据进入cld_temp_data_ocs");
        }
        if(ArrayUtils.contains(args, "2")){
            //跑十个线程，存数据进入cld_temp_data_ocs , 并且将数据存入存数据进入cld_temp_data
            CreateDateStep2_ADD.main(null);
            System.out.println("数据汇总到cld_temp_data_new");

        }
        if(ArrayUtils.contains(args, "2.5")){
            //跑十个线程，存数据进入cld_temp_data_ocs , 并且将数据存入存数据进入cld_temp_data
            CreateDateStep2_5_MEAL.main(null);
            System.out.println("CreateDateStep2_5_MEAL");

        }
        if(ArrayUtils.contains(args, "3")){
            //数据准备，生成最终表cld_temp_data_all
            CreateDateStep3_ALL.main(null);
            System.out.println("生成最终表cld_temp_data_all");

        }
        if(ArrayUtils.contains(args, "4")){
            //读表cld_temp_data_all ， 生成文件
            CreateDateStep4_FILE.main(null);
            System.out.println("读表cld_temp_data_all ， 生成文件结束");

        }
        if(ArrayUtils.contains(args, "5")){
            //将文件移动到 采集目录自动转码
            CreateDateStep5_MOVE.main(null);
            System.out.println("将文件移动到 采集目录结束");

        }

    }
}
