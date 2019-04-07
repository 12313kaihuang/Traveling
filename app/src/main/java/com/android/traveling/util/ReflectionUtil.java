package com.android.traveling.util;

import java.lang.reflect.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by HY
 * 2019/3/31 15:26
 * <p>
 * 反射相关工具包
 */
@SuppressWarnings("unused")
public class ReflectionUtil {

    /**
     * 打印一个类的所有构造方法
     *
     * @param c class
     */
    public static void printConstructors(Class c) {
        //返回包含Constructor对象的数组，其中包含了Class对象所描述的类的所有构造器
        Constructor[] constructors = c.getDeclaredConstructors();

        //遍历构造器（构造方法）
        for (Constructor constructor : constructors) {
            //获取构造方法名
            String name = constructor.getName();
            System.out.print("  ");
            //获取modifiers中位设置的修饰符的字符串表示（public private)
            String modifies = Modifier.toString(constructor.getModifiers());
            if (modifies.length() > 0) {
                System.out.print(modifies + " ");
            }
            System.out.print(name + "(");

            //获取参数类型的Class对象数组
            Class[] paramTypes = constructor.getParameterTypes();
            for (int i = 0; i < paramTypes.length; i++) {
                if (i > 0) {
                    System.out.print(", ");
                }
                System.out.print(paramTypes[i].getName());
            }
            System.out.println(");");
        }
    }


    /**
     * 打印一个类的所有方法
     *
     * @param c class
     */
    public static void printMethods(Class c) {
        //返回这个类或接口的全部方法，但不包括由超类继承的方法。
        Method[] methods = c.getDeclaredMethods();

        //遍历方法
        for (Method method : methods) {
            Class returnType = method.getReturnType();  //方法返回类型
            String name = method.getName();  //方法名

            System.out.print("  ");
            //获取modifiers中位设置的修饰符的字符串表示（public private)
            String modifies = Modifier.toString(method.getModifiers());
            if (modifies.length() > 0) {
                System.out.print(modifies + " ");
            }
            System.out.print(returnType.getName() + " " + name + "(");

            //获取参数类型的Class对象数组
            Class[] paramTypes = method.getParameterTypes();
            for (int i = 0; i < paramTypes.length; i++) {
                if (i > 0) {
                    System.out.print(", ");
                }
                System.out.print(paramTypes[i].getName());
            }
            System.out.println(");");
        }
    }


    /**
     * 打印一个类的所有属性
     *
     * @param c class
     */
    public static void printFields(Class c) {
        Field[] fields = c.getDeclaredFields();

        for (Field field : fields) {
            Class<?> type = field.getType();
            String name = field.getName();
            System.out.print("  ");
            //获取modifiers中位设置的修饰符的字符串表示（public private)
            String modifies = Modifier.toString(field.getModifiers());
            if (modifies.length() > 0) {
                System.out.print(modifies + " ");
            }
            System.out.println(type.getName() + " " + name + ";");
        }
    }

    //内部类 用于实现toString方法
    public static class ObjectAnalyzer {

        //为了避免循环引用而导致的无限递归
        private ArrayList<Object> visited = new ArrayList<>();

        /**
         * 将任意对象toString
         *
         * @param object object
         * @return String
         */
        @SuppressWarnings("WeakerAccess")
        public String toString(Object object) {
            if (object == null) return "null";
            if (visited.contains(object)) return "...";

            visited.add(object);
            Class<?> c1 = object.getClass();
            if (c1 == String.class) return (String) object;
            //是否为数组
            if (c1.isArray()) {
                StringBuilder r = new StringBuilder(c1.getComponentType() + "[]{");
                for (int i = 0; i < Array.getLength(object); i++) {
                    if (i > 0) {
                        r.append(",");
                    }
                    Object val = Array.get(object, i);
                    //Class.isPrimitive 判断是否为原始类型（boolean char byte short int long float double )
                    if (c1.getComponentType().isPrimitive()) {
                        r.append(val);
                    } else {
                        r.append(toString(val));
                    }
                }
                return r.toString() + "}";
            }

            StringBuilder r = new StringBuilder(c1.getName());

            do {
                r.append("[");
                //获取所有的域（属性）
                Field[] fields = c1.getDeclaredFields();
                //设置对象数组可访问 true表示屏蔽Java语言的访问检查，使得私有属性也可被查询和设置
                AccessibleObject.setAccessible(fields, true);

                for (Field field : fields) {
                    if (!Modifier.isStatic(field.getModifiers())) {
                        if (!r.toString().endsWith("[")) r.append(",");
                        r.append(field.getName()).append("=");
                        try {
                            Class<?> type = field.getType();
                            Object val = field.get(object);
                            //是否是原始类型
                            if (type.isPrimitive()) {
                                r.append(val);
                            } else {
                                r.append(toString(val));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                r.append("]");
                c1 = c1.getSuperclass();
            } while (c1 != null);
            return r.toString();
        }
    }

    /**
     * 日期转换类
     */
    private static final SimpleDateFormat defaultSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    /**
     * 将对象属性值转换成键值对
     *
     * @param object object
     * @return Map
     */
    public static Map<String, String> toMap(Object object) {
        return toMap(object, defaultSimpleDateFormat);
    }

    /**
     * 将对象属性值转换成键值对
     *
     * @param object     object
     * @param dateFormat dateFormat
     * @return Map
     */
    @SuppressWarnings("WeakerAccess")
    public static Map<String, String> toMap(Object object, SimpleDateFormat dateFormat) {
        HashMap<String, String> map = new HashMap<>();

        Class c = object.getClass();
        Field[] fields = c.getDeclaredFields();
        //设置对象数组可访问 true表示屏蔽Java语言的访问检查，使得私有属性也可被查询和设置
        AccessibleObject.setAccessible(fields, true);

        for (Field field : fields) {
            //是否是静态的域
            if (!Modifier.isStatic(field.getModifiers())) {
                String name = field.getName();
                String value;
                try {
                    //属性值
                    Object val = field.get(object);
                    //获取该属性的类型
                    Class<?> valClass = field.getType();
                    //                    System.out.println("val.class = " + field.getType().getName());
                    if (valClass == String.class) {
                        value = (String) val;
                    } else if (valClass == Date.class) {
                        value = dateFormat.format(val);
                    } else if (valClass.isPrimitive()) {
                        value = "" + val;
                    } else {
                        //                        System.out.println("非普通类型" + val);
                        value = val.toString();
                    }
                    map.put(name, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }
}
