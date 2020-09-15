//package utils;
//
//import org.apache.poi.ss.formula.functions.T;
//import test.testVo;
//
//import java.lang.reflect.Field;
//
///**
// * @author 程刘德
// * @version 1.0
// * @Description TODO
// * @date 2020/5/18
// */
//public class ReflactHelp {
//    public static void main(String[] args) {
//        testVo cat = new testVo();
//        Field name = cat.getClass().getDeclaredField("name");
//        name.setAccessible(true);
//        name.set(cat, "波斯猫");
//        System.out.println(cat.name);
//
//    }
//
//
//    public void fun(Class<T> co, String Attributes,Object value ) {
//
//        try {
//            Field name = co.getClass().getDeclaredField(Attributes);
//            name.setAccessible(true);
//            name.set(cat, "波斯猫");
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } finally {
//        }
//
//    }
//
//}
