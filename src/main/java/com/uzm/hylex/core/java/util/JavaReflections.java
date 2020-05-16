package com.uzm.hylex.core.java.util;


import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.nms.reflections.Accessors;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class JavaReflections
{

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes)
            throws NoSuchMethodException
    {
        @SuppressWarnings("rawtypes")
        Class[] primitiveTypes = DataType.getPrimitive(parameterTypes);
        Method[] arrayOfMethod;
        int j = (arrayOfMethod = clazz.getMethods()).length;
        for (int i = 0; i < j; i++)
        {
            Method method = arrayOfMethod[i];
            if ((method.getName().equals(methodName)) && (DataType.compare(DataType.getPrimitive(method.getParameterTypes()), primitiveTypes))) {
                return method;
            }
        }
        throw new NoSuchMethodException("There is no such method in this class with the specified name and parameter types");
    }
    @SuppressWarnings("resource")
    public static List<Class<?>> getClasses(String packageName, JavaPlugin plugin) {

        List<Class<?>> classes = new ArrayList<>();
        packageName = packageName.replaceAll("\\." , "/");
        try {

            JarInputStream jarFile = new JarInputStream
                    (new FileInputStream (Accessors.getField(JavaPlugin.class, "file", File.class).get(plugin)));

            while (true) {
                JarEntry jar = jarFile.getNextJarEntry();
                if (jar==null) {
                    break;
                }
                if (jar.getName ().startsWith (packageName) &&  jar.getName ().endsWith (".class") && !jar.getName().contains("$"))  {
                    classes.add (Class.forName(jar.getName().replaceAll("/", "\\.").replace(".class", "")));
                }

            }
        }catch (Exception e) {
            System.err.println("[Hylex] An error occurred while the plugin tried to load classes");
        }
        return classes;
    }
    private static Field getField(Class<?> clazz, String fname) throws Exception {
        Field f;
        try {
            f = clazz.getDeclaredField(fname);
        } catch (Exception e) {
            f = clazz.getField(fname);
        }
        setFieldAccessible(f);
        return f;
    }
    public static Object getObject(Class<?> clazz, Object obj, String fname) throws Exception {
        return getField(clazz, fname).get(obj);
    }

    public static Object getObject(Object obj, String fname) throws Exception {
        return getField(obj.getClass(), fname).get(obj);
    }

    public static Object invokeConstructor(Class<?> clazz, Class<?>[] args, Object... initargs) throws Exception {
        return getConstructor(clazz, args).newInstance(initargs);
    }

    private static Constructor<?> getConstructor(Class<?> clazz, Class<?>... args) throws Exception {
        Constructor<?> c = clazz.getConstructor(args);
        c.setAccessible(true);
        return c;
    }

    public static Object invokeMethod(Class<?> clazz, Object obj, String method) throws Exception {
        return Objects.requireNonNull(getMethod(clazz, method)).invoke(obj);
    }

    public static Object invokeMethod(Class<?> clazz, Object obj, String method, Class<?>[] args, Object... initargs)
      throws Exception {
        return Objects.requireNonNull(getMethod(clazz, method, args)).invoke(obj, initargs);
    }

    public static Object invokeMethod(Class<?> clazz, Object obj, String method, Object... initargs) throws Exception {
        return Objects.requireNonNull(getMethod(clazz, method)).invoke(obj, initargs);
    }

    public static Object invokeMethod(Object obj, String method) throws Exception {
        return Objects.requireNonNull(getMethod(obj.getClass(), method)).invoke(obj);
    }

    public static Object invokeMethod(Object obj, String method, Object[] initargs) throws Exception {
        return Objects.requireNonNull(getMethod(obj.getClass(), method)).invoke(obj, initargs);
    }

    private static void setFieldAccessible(Field f) throws Exception {

        f.setAccessible(true);
        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(f, f.getModifiers() & ~Modifier.FINAL);
    }

    public static void setObject(Class<?> clazz, Object obj, String fname, Object value) throws Exception {
        getField(clazz, fname).set(obj, value);
    }

    public static void setObject(Object obj, String fname, Object value) throws Exception {
        getField(obj.getClass(), fname).set(obj, value);
    }

    public enum DataType
    {
        BYTE(Byte.TYPE, Byte.class),  SHORT(Short.TYPE, Short.class),  INTEGER(Integer.TYPE, Integer.class),  LONG(Long.TYPE, Long.class),  CHARACTER(Character.TYPE, Character.class),  FLOAT(Float.TYPE, Float.class),  DOUBLE(Double.TYPE, Double.class),  BOOLEAN(Boolean.TYPE, Boolean.class);

        private static final Map<Class<?>, DataType> CLASS_MAP;
        private final Class<?> primitive;
        private final Class<?> reference;

        static
        {
            CLASS_MAP = new HashMap<>();
            DataType[] arrayOfDataType;
            int j = (arrayOfDataType = values()).length;
            for (int i = 0; i < j; i++)
            {
                DataType type = arrayOfDataType[i];
                CLASS_MAP.put(type.primitive, type);
                CLASS_MAP.put(type.reference, type);
            }
        }

        DataType(Class<?> primitive, Class<?> reference)
        {
            this.primitive = primitive;
            this.reference = reference;
        }

        public Class<?> getPrimitive()
        {
            return this.primitive;
        }

        public Class<?> getReference()
        {
            return this.reference;
        }

        public static DataType fromClass(Class<?> clazz)
        {
            return CLASS_MAP.get(clazz);
        }

        public static Class<?> getPrimitive(Class<?> clazz)
        {
            DataType type = fromClass(clazz);
            return type == null ? clazz : type.getPrimitive();
        }

        public static Class<?> getReference(Class<?> clazz)
        {
            DataType type = fromClass(clazz);
            return type == null ? clazz : type.getReference();
        }

        public static Class<?>[] getPrimitive(Class<?>[] classes)
        {
            int length = classes == null ? 0 : classes.length;
            @SuppressWarnings("rawtypes")
            Class[] types = new Class[length];
            for (int index = 0; index < length; index++) {
                types[index] = getPrimitive(classes[index]);
            }
            return types;
        }

        public static Class<?>[] getReference(Class<?>[] classes)
        {
            int length = classes == null ? 0 : classes.length;
            @SuppressWarnings("rawtypes")
            Class[] types = new Class[length];
            for (int index = 0; index < length; index++) {
                types[index] = getReference(classes[index]);
            }
            return types;
        }

        public static Class<?>[] getPrimitive(Object[] objects)
        {
            int length = objects == null ? 0 : objects.length;
            @SuppressWarnings("rawtypes")
            Class[] types = new Class[length];
            for (int index = 0; index < length; index++) {
                types[index] = getPrimitive(objects[index].getClass());
            }
            return types;
        }

        public static Class<?>[] getReference(Object[] objects)
        {
            int length = objects == null ? 0 : objects.length;
            @SuppressWarnings("rawtypes")
            Class[] types = new Class[length];
            for (int index = 0; index < length; index++) {
                types[index] = getReference(objects[index].getClass());
            }
            return types;
        }

        public static boolean compare(Class<?>[] primary, Class<?>[] secondary)
        {
            if ((primary == null) || (secondary == null) || (primary.length != secondary.length)) {
                return false;
            }
            for (int index = 0; index < primary.length; index++)
            {
                Class<?> primaryClass = primary[index];
                Class<?> secondaryClass = secondary[index];
                if ((!primaryClass.equals(secondaryClass)) && (!primaryClass.isAssignableFrom(secondaryClass))) {
                    return false;
                }
            }
            return true;
        }
    }
}
