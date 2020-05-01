package com.uzm.hylex.core.java.util;


import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.nms.reflections.Accessors;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
