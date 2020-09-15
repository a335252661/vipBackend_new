package test;

/**
 * @author by cld
 * @date 2020/5/19  20:16
 * @description:
 */
public class PeopleVo {

    private String name;
    private String name2;
    private String name6;
    private String name3;
    private String strr;
    private String ip;
    private int age;

    @Override
    public String toString() {
        return "PeopleVo{" +
                "name='" + name + '\'' +
                ", name2='" + name2 + '\'' +
                ", name6='" + name6 + '\'' +
                ", name3='" + name3 + '\'' +
                ", strr='" + strr + '\'' +
                ", ip='" + ip + '\'' +
                ", age=" + age +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getName6() {
        return name6;
    }

    public void setName6(String name6) {
        this.name6 = name6;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public String getStrr() {
        return strr;
    }

    public void setStrr(String strr) {
        this.strr = strr;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
