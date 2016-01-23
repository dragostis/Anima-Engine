package org.anima.engine.scripting;

import android.content.res.AssetManager;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class MRubyState {
    public long pointer;
    private List<Class> classes;
    private Context context;
    private String currentPath;
    private RequiredFiles requiredFiles;
    private Cache<String, String> cache;

    public MRubyState(String rootPath, AssetManager assetManager) {
        pointer = getStatePointer();
        classes = new ArrayList<Class>();
        context = new AssetsContext(assetManager);
        requiredFiles = new RequiredFiles();
        cache = new Cache<String, String>(256);
    }

    public void loadClass(Class aClass) {
        loadClass(aClass, ParameterLoad.NONE);
    }

    public void loadClass(Class aClass, ParameterLoad parameterLoad) {
        classes.add(aClass);

        loadClassToState(pointer, getJavaClassName(aClass), getRubyClassName(aClass));

        switch (parameterLoad) {
            case NONE:
                break;
            case CHILDREN:
                for (Method method : filterObjectMethods(aClass.getMethods())) {
                    for (Class parameter : method.getParameterTypes()) loadClass(parameter, ParameterLoad.NONE);

                    loadClass(method.getReturnType(), ParameterLoad.NONE);
                }

                for (Constructor constructor : aClass.getConstructors()) {
                    for (Class parameter : constructor.getExceptionTypes()) loadClass(parameter, ParameterLoad.NONE);
                }

                break;
            case RECURSIVE:
                for (Method method : filterObjectMethods(aClass.getMethods())) {
                    for (Class parameter : method.getParameterTypes()) loadClass(parameter, ParameterLoad.RECURSIVE);

                    loadClass(method.getReturnType(), ParameterLoad.RECURSIVE);
                }

                for (Constructor constructor : aClass.getConstructors()) {
                    for (Class parameter : constructor.getExceptionTypes()) loadClass(parameter, ParameterLoad.RECURSIVE);
                }

                break;
        }
    }

    public void loadMethods() {
        Method[] publicMethods;
        Constructor[] constructors;

        for (Class aClass : classes) {
            publicMethods = filterObjectMethods(aClass.getMethods());
            constructors = aClass.getConstructors();

            int length = publicMethods.length + constructors.length;

            String[] javaNames = new String[length];
            String[] rubyNames = new String[length];
            String[] javaSignatures = new String[length];
            boolean[] isStatic = new boolean[length];

            for (int i = 0; i < constructors.length; i++) {
                javaNames[i] = "<init>";
                rubyNames[i] = "construct";
                javaSignatures[i] = getConstructorSignature(constructors[i]);
                isStatic[i] = false;
            }

            for (int i = 0; i < publicMethods.length; i++) {
                int j = i + constructors.length;

                javaNames[j] = publicMethods[i].getName();
                rubyNames[j] = getRubyMethodName(javaNames[j]);
                javaSignatures[j] = getSignature(publicMethods[i]);
                isStatic[j] = Modifier.isStatic(publicMethods[i].getModifiers());
            }

            loadClassMethodsToState(pointer, getJavaClassName(aClass), getRubyClassName(aClass), javaNames, rubyNames,
                    javaSignatures, isStatic);
        }
    }

    public void close() {
        close(pointer);
    }

    private String getJavaClassName(Class aClass) {
        return aClass.getName().replaceAll("\\.", "/");
    }

    private String getRubyClassName(Class aClass) {
        return aClass.getName().replaceAll("(\\w+\\.)*", "");
    }

    private String getRubyMethodName(String javaName) {
        if (javaName.matches("^get.+")) javaName = javaName.substring(3);
        if (javaName.matches("^set.+")) javaName = javaName.substring(3) + '=';

        javaName = Character.toLowerCase(javaName.charAt(0)) + javaName.substring(1);

        javaName = javaName.replaceAll("add", "+");
        javaName = javaName.replaceAll("subtract", "-");
        javaName = javaName.replaceAll("multiply", "*");
        javaName = javaName.replaceAll("divide", "/");

        return javaName.replaceAll("([A-Z])", "_$1").toLowerCase();
    }

    private String getConstructorSignature(Constructor constructor) {
        String signature = "(";

        for (Class aClass : constructor.getParameterTypes()) {
            signature += getClassTag(aClass);
        }

        signature += ")";

        return signature + "V";
    }

    private String getSignature(Method method) {
        String signature = "(";

        for (Class aClass : method.getParameterTypes()) {
            signature += getClassTag(aClass);
        }

        signature += ")";

        return signature + getClassTag(method.getReturnType());
    }

    private String getClassTag(Class aClass) {
        String tag = aClass.getCanonicalName();

        if (tag.endsWith("[]")) {
            tag = "[" + tag.substring(0, tag.length() - 2);
        }

        if (tag.startsWith("[") && !tag.startsWith("[L")) {
            tag = "[L" + tag.substring(1);
        } else if (!tag.startsWith("L")) {
            tag = "L" + tag;
        }

        if (!tag.endsWith(";")) {
            tag += ";";
        }

        tag = tag.replaceAll("L((java\\.lang\\.Boolean)|(boolean));", "Z");
        tag = tag.replaceAll("L((java\\.lang\\.Byte)|(byte));", "B");
        tag = tag.replaceAll("L((java\\.lang\\.Character)|(char));", "C");
        tag = tag.replaceAll("L((java\\.lang\\.Short)|(short));", "S");
        tag = tag.replaceAll("L((java\\.lang\\.Integer)|(int));", "I");
        tag = tag.replaceAll("L((java\\.lang\\.Long)|(long));", "J");
        tag = tag.replaceAll("L((java\\.lang\\.Float)|(float));", "F");
        tag = tag.replaceAll("L((java\\.lang\\.Double)|(double));", "D");

        tag = tag.replaceAll("Lvoid;", "V");

        return tag.replaceAll("\\.", "/");
    }

    private Method[] filterObjectMethods(Method[] methods) {
        List<Method> newMethods = new ArrayList<Method>();
        List<Method> toBeRemoved = new ArrayList<Method>();

        Collections.addAll(toBeRemoved, Object.class.getMethods());

        for (Method method : methods) {
            boolean toRemove = false;

            for (Method remove : toBeRemoved) {
                if (method.getName().equals(remove.getName())) toRemove = true;
            }

            if (!toRemove) newMethods.add(method);
        }

        Object[] objects = newMethods.toArray();
        Method[] result = new Method[objects.length];

        for (int i = 0; i < objects.length; i++) result[i] = (Method) objects[i];

        return result;
    }

    public void executeString(String mrubyString, String fileName) {
        loadString(pointer, mrubyString, fileName);
    }

    public void executeStream(InputStream mrubyStream, String fileName) throws IOException {
        String cached = cache.get(fileName);

        if (cached != null) {
            loadString(pointer, cached, fileName);
        } else {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(mrubyStream));
            StringBuilder stringBuilder = new StringBuilder();

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }

            String string = stringBuilder.toString();

            cache.put(fileName, string);
            loadString(pointer, string, fileName);
        }
    }

    private boolean require(String path) throws IOException {
        InputStream mrubyStream = context.get(path);

        if (requiredFiles.contains(currentPath, path)) {
            return false;
        } else {
            String oldPath = currentPath;

            currentPath = path;
            executeStream(mrubyStream, path);
            currentPath = oldPath;

            return true;
        }
    }

    public static enum ParameterLoad {
        NONE,
        CHILDREN,
        RECURSIVE
    }

    private static class RequiredFiles {
        private HashMap<String, HashSet<String>> required = new HashMap<String, HashSet<String>>();

        public boolean contains(String required, String path) {
            HashSet<String> paths = this.required.get(required);

            if (paths == null) {
                HashSet<String> set = new HashSet<String>();

                set.add(path);

                this.required.put(required, set);

                return false;
            } else {
                if (paths.contains(path)) {
                    return true;
                } else {
                    paths.add(path);

                    return false;
                }
            }
        }
    }

    private static class Cache<K, V> extends LinkedHashMap<K, V> {
        private int size;

        public Cache(int size) {
            super(16, 0.75f, true);

            this.size = size;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() >= size;
        }
    }

    private native long getStatePointer();

    private native void loadClassToState(long pointer, String javaName, String rubyName);

    private native void loadClassMethodsToState(long pointer, String className, String rubyClassName,
                                                String[] javaNames, String[] rubyNames, String[] javaSignatures,
                                                boolean[] isStatic);

    private native void loadString(long pointer, String mrubyString, String fileName);

    private native void close(long pointer);
}
