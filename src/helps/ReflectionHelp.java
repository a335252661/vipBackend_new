package helps;
import test.PeopleVo;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 反射的 Utils 函数集合
 * 提供访问私有变量, 获取泛型类型 Class, 提取集合中元素属性等 Utils 函数
 */
public class ReflectionHelp {
    public static void main(String[] args) {

        PeopleVo vo = new PeopleVo();

        ReflectionHelp.setFieldValue(vo , "name3" ,"mmmmmmmm");
        System.out.println(vo);

        Object name3 = ReflectionHelp.getFieldValue(vo, "name3");
        System.out.println(name3.toString());

//        PeopleVo.class = vo.getClass();

        ReflectionHelp.getAllField(new PeopleVo());

        ArrayList list = new ArrayList();
        for(int i=0 ;i<4 ; i++){
            list.add(i+"");
        }
        PeopleVo mm = new PeopleVo();
        ReflectionHelp.setBeanFromList(mm , list);
        System.out.println(mm);


    }

    /**
     *
     * @param object 对象
     * @param list 集合
     */
    public static void setBeanFromList(Object object , List list){
        ArrayList<String> allField = ReflectionHelp.getAllField(object);
        int size = allField.size() > list.size() ? list.size() : allField.size();
        //长度判断
        if(list.isEmpty()){
            return;
        }
        for(int i=0;i<size;i++){
            //第一个属性
           String fieldStr =  allField.get(i);
           //第一个需要设置的值
           Object value =  list.get(i);
            ReflectionHelp.setFieldValue(object , fieldStr , value);
        }
    }


    /**
     *
     * @param ob 对象
     * @return 所有属性字符串
     */
    public static  ArrayList<String> getAllField(Object ob ) {
        ArrayList<String> fieldList = new ArrayList<String>();
        try {
            Field[] fields = ob.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                fieldList.add(field.getName());
                System.out.println(field.getName());
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return fieldList;
    }
    /**
     * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
     *
     * @param object
     * @param fieldName
     * @return
     */
    public static Object getFieldValue(Object object, String fieldName) {
        Field field = getDeclaredField(object, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }

        makeAccessible(field);

        Object result = null;

        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
        }

        return result;
    }

    /**
     * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter
     *
     * @param object
     * @param fieldName
     * @param value
     */
    public static void setFieldValue(Object object, String fieldName, Object value) {
        Field field = getDeclaredField(object, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }

        makeAccessible(field);

        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
        }
    }


    /**
     * 通过反射, 获得定义 Class 时声明的父类的泛型参数的类型
     * 如: public EmployeeDao extends BaseDao<Employee, String>
     *
     * @param clazz
     * @param index
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Class getSuperClassGenricType(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }

        if (!(params[index] instanceof Class)) {
            return Object.class;
        }

        return (Class) params[index];
    }

    /**
     * 通过反射, 获得 Class 定义中声明的父类的泛型参数类型
     * 如: public EmployeeDao extends BaseDao<Employee, String>
     *
     * @param <T>
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getSuperGenericType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredMethod
     *
     * @param object
     * @param methodName
     * @param parameterTypes
     * @return
     */
    public static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {

        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                //Method 不在当前类定义, 继续向上转型
            }
        }

        return null;
    }

    /**
     * 使 filed 变为可访问
     *
     * @param field
     */
    public static void makeAccessible(Field field) {
        if (!Modifier.isPublic(field.getModifiers())) {
            field.setAccessible(true);
        }
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredField
     *
     * @param object
     * @param filedName
     * @return
     */
    public static Field getDeclaredField(Object object, String filedName) {

        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(filedName);
            } catch (NoSuchFieldException e) {
                //Field 不在当前类定义, 继续向上转型
            }
        }
        return null;
    }

    /**
     * 直接调用对象方法, 而忽略修饰符(private, protected)
     *
     * @param object
     * @param methodName
     * @param parameterTypes
     * @param parameters
     * @return
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes,
                                      Object[] parameters) throws InvocationTargetException {

        Method method = getDeclaredMethod(object, methodName, parameterTypes);

        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
        }

        method.setAccessible(true);

        try {
            return method.invoke(object, parameters);
        } catch (IllegalAccessException e) {
        }

        return null;
    }
}
