package helps;
//package com.gvbrain.appApi.Utils;
import java.util.Random;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2022/2/10
 */
public class NameHelps {




    private static String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static String ALPHABET_NUMBER = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static String FIRST_NAME="赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻柏水窦章云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳酆鲍史唐费廉岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅皮卞齐康伍余元卜顾孟平黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计伏成戴谈宋茅庞熊纪舒屈项祝董梁杜阮蓝闵席季麻强贾路娄危江童颜郭梅盛林刁钟徐邱骆高夏蔡田樊胡凌霍虞万支柯咎管卢莫经房裘缪干解应宗宣丁贲邓郁单杭洪包诸左石崔吉钮龚程嵇邢滑裴陆荣翁荀羊於惠甄魏加封芮羿储靳汲邴糜松井段富巫乌焦巴弓牧隗山谷车侯宓蓬全郗班仰秋仲伊宫宁仇栾暴甘钭厉戎祖武符刘姜詹束龙叶幸司韶郜黎蓟薄印宿白怀蒲台从鄂索咸籍赖卓蔺屠蒙池乔阴郁胥能苍双闻莘党翟谭贡劳逄姬申扶堵冉宰郦雍却璩桑桂濮牛寿通边扈燕冀郏浦尚农温别庄晏柴瞿阎充慕连茹习宦艾鱼容向古易慎戈廖庚终暨居衡步都耿满弘匡国文寇广禄阙东殴殳沃利蔚越夔隆师巩厍聂晁勾敖融冷訾辛阚那简饶空曾毋沙乜养鞠须丰巢关蒯相查后江红游竺权逯盖益桓公万俟司马上官欧阳夏侯诸葛闻人东方赫连皇甫尉迟公羊澹台公冶宗政濮阳淳于仲孙太叔申屠公孙乐正轩辕令狐钟离闾丘长孙慕容鲜于宇文司徒司空亓官司寇仉督子车颛孙端木巫马公西漆雕乐正壤驷公良拓拔夹谷宰父谷粱晋楚阎法汝鄢涂钦段干百里东郭南门呼延归海羊舌微生岳帅缑亢况后有琴梁丘左丘东门西门商牟佘佴伯赏南宫墨哈谯笪年爱阳佟第五言福百家姓续";
    private static String GIRL="秀娟英华慧巧美娜静淑惠珠翠雅芝玉萍红娥玲芬芳燕彩春菊兰凤洁梅琳素云莲真环雪荣爱妹霞香月莺媛艳瑞凡佳嘉琼勤珍贞莉桂娣叶璧璐娅琦晶妍茜秋珊莎锦黛青倩婷姣婉娴瑾颖露瑶怡婵雁蓓纨仪荷丹蓉眉君琴蕊薇菁梦岚苑婕馨瑗琰韵融园艺咏卿聪澜纯毓悦昭冰爽琬茗羽希宁欣飘育滢馥筠柔竹霭凝晓欢霄枫芸菲寒伊亚宜可姬舒影荔枝思丽 ";
    private static String BOY="伟刚勇毅俊峰强军平保东文辉力明永健世广志义兴良海山仁波宁贵福生龙元全国胜学祥才发武新利清飞彬富顺信子杰涛昌成康星光天达安岩中茂进林有坚和彪博诚先敬震振壮会思群豪心邦承乐绍功松善厚庆磊民友裕河哲江超浩亮政谦亨奇固之轮翰朗伯宏言若鸣朋斌梁栋维启克伦翔旭鹏泽晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风航弘";
    private static String[] EMAIL_SUFFIX="@gmail.com,@yahoo.com,@msn.com,@hotmail.com,@aol.com,@ask.com,@live.com,@qq.com,@0355.net,@163.com,@163.net,@263.net,@3721.net,@yeah.net,@googlemail.com,@126.com,@sina.com,@sohu.com,@yahoo.com.cn".split(",");
    private static String[] TEL_FIRST = ("133, 153, 177, 180,181, 189, 134, 135, 136, 137, 138, 139, 150, 151, 152, 157, 158, 159" +
            "178, 182, 183, 184, 187, 188, 130, 131, 132, 155, 156, 176, 185, 186" +
            "145, 147, 170").split(",");

    static Random random = new Random();

    /**
     * 得到从start到end之间的随机数
     * @param start 随机数区间最大值
     * @param end 随机数区间最小值
     * @return
     */
    public static int getNum(int start,int end) {
        return (int)(Math.random()*(end-start+1)+start);
    }

    /**
     * 获取区间段内的随机年龄
     * @param start
     * @param end
     * @return
     */
    public static int getRandomAge(int start,int end){
        return getNum(start,end);
    }

    /**
     * 获取随机的英文字母组合，大小写随机
     * @param num 组合中需要的英文字母个数
     * @return
     */
    public String getRandomAlphabet(Integer num){
        String randomAlphabet = "";
        char[] c = ALPHABET.toCharArray();
        for( int i = 0; i < num; i ++) {
            randomAlphabet +=  c[random.nextInt(c.length)];
        }
        return randomAlphabet;
    }

    /**
     * 获得随机姓名
     * @return
     */
    public static String getRandomName(){
        char firstName = FIRST_NAME.toCharArray()[random.nextInt(FIRST_NAME.toCharArray().length)];
        String lastName;
        int sex = random.nextInt(2);
        if (sex == 0){
            lastName = getGirlName();
        }else {
            lastName = getBOYlName();
        }
        String randomName = firstName + lastName;
        return randomName;
    }

    private static String getGirlName(){
        String lastName = "";
        int num = getNum(1,2);//随机获取2字姓名和3字姓名
        if (num == 1){
            lastName = String.valueOf(GIRL.charAt(random.nextInt(GIRL.toCharArray().length)));
        }else if (num == 2){
            for (int i = 0; i < 2; i++){
                lastName += GIRL.charAt(random.nextInt(GIRL.toCharArray().length));
            }
        }
        return lastName;
    }

    private static String getBOYlName(){
        String lastName = "";
        int num = getNum(1,2);//随机获取2字姓名和3字姓名
        if (num == 1){
            lastName = String.valueOf(BOY.charAt(random.nextInt(BOY.toCharArray().length)));
        }else if (num == 2){
            for (int i = 0; i < 2; i++){
                lastName += BOY.charAt(random.nextInt(BOY.toCharArray().length));
            }
        }
        return lastName;
    }

    /**
     * 获取随机的手机号码
     * @return
     */
    public String getRandomPhoneNumber(){
        //获取随机的号码开头
        String firstNum = TEL_FIRST[random.nextInt(TEL_FIRST.length)];
        //获取剩余8位的号码随机数
        String remainingNum = String.valueOf(getNum(0,99999999)+100000000).substring(1);
        return firstNum + remainingNum;
    }

    /**
     * 获取随机的email，前缀后缀分别获取后组合成随机email
     * @param num 自定义前缀的位数
     * @return
     */
    public String getRandomEmail(Integer num){
        //获取随机的email前缀,自定义前缀长度
        String firstPart = "";
        char[] c = ALPHABET_NUMBER.toCharArray();
        for (int i = 0; i < num; i++){
            firstPart += c[random.nextInt(c.length)];
        }
        //获取随机email后缀
        String secondPart = EMAIL_SUFFIX[random.nextInt(EMAIL_SUFFIX.length)];
        return firstPart + secondPart;
    }

    /**
     * 根据不同年份区间和月份，获得随机日期，以特定格式返回
     * @return
     */
    public static String getRandomBirthDate(){
        //先随机获取年份,取1915-1971区间
        String year = String.valueOf(getNum(1915,1971));
        //再随机获取月份
        String month = String.valueOf(getNum(1,12) + 100).substring(1);
        String day;
        //判断月份，获取随机日期
        if (month.equals("02")){
            day = String.valueOf(getNum(1,28) + 100).substring(1);
        }else if (month.equals("04") || month.equals("06") || month.equals("09") || month.equals("11")){
            day = String.valueOf(getNum(1,30) + 100).substring(1);
        }else {
            day = String.valueOf(getNum(1,31) + 100).substring(1);
        }
        String BirthDate = year + "-" + month + "-" + day;
        return BirthDate;
    }


    public static void main(String[] args) {
        String randomName = NameHelps.getRandomName();
        System.out.println(randomName);
    }
}

