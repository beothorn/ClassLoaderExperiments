package br.com.isageek.bridge;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.Advice.AllArguments;
import net.bytebuddy.asm.Advice.OnMethodEnter;
import net.bytebuddy.asm.Advice.Origin;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.matcher.ElementMatchers;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;

public class App {

    static URLClassLoader serverLoader;
    static URLClassLoader clientLoader;

    public static void main( String[] args ) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        try {
            // Install the ByteBuddy agent
            ByteBuddyAgent.install();
        } catch (IllegalStateException e) {
            System.err.println("ByteBuddy agent installation failed: " + e.getMessage());
            return;
        }

        File serverJar = extractResourceToTempFile("server-1.0-SNAPSHOT-jar-with-dependencies.jar");

        // Load the JAR using URLClassLoader
        URL serverJarUrl = serverJar.toURI().toURL();
        try(URLClassLoader serverClassLoader = new URLClassLoader(new URL[] {serverJarUrl})){
            // Load the main class from the JAR
            Class<?> mainClass1 = serverClassLoader.loadClass("br.com.isageek.App");

            Method mainMethod1 = mainClass1.getMethod("main", String[].class);
            mainMethod1.invoke(null, (Object) new String[] {});
            serverLoader = serverClassLoader;
        }


        File clientJar = extractResourceToTempFile("client-1.0-SNAPSHOT-jar-with-dependencies.jar");

        // Load the JAR using URLClassLoader
        URL clientJarUrl = clientJar.toURI().toURL();
        try(URLClassLoader clientClassLoader = new URLClassLoader(new URL[] { clientJarUrl })){

            Class<?> targetClass = clientClassLoader.loadClass("br.com.isageek.App");

            // Modify the class
            new ByteBuddy()
                    .redefine(targetClass)
                    .visit(Advice.to(MethodInterceptor.class).on(ElementMatchers.named("callServer")))
                    .make()
                    .load(targetClass.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

            // Load the main class from the JAR
            Class<?> mainClass = clientClassLoader.loadClass("br.com.isageek.App");

            Method mainMethod = mainClass.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) new String[] {});
            clientLoader = clientClassLoader;
        }
    }

    private static File extractResourceToTempFile(String resourceName) throws IOException {
        InputStream resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
        if (resourceStream == null) {
            throw new FileNotFoundException("Resource not found: " + resourceName);
        }

        File tempFile = Files.createTempFile("temp-", ".jar").toFile();
        tempFile.deleteOnExit();

        try (OutputStream out = Files.newOutputStream(tempFile.toPath())) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = resourceStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } finally {
            resourceStream.close();
        }
        return tempFile;
    }

    public static void dispatch(String payload) {
        System.out.println("[Bridge] Will dispatch payload to client: '" + payload + "'");
        try {
            Class<?> mainClass = serverLoader.loadClass("br.com.isageek.App");
            Method mainMethod = mainClass.getMethod("bridgeReceive", String.class);
            mainMethod.invoke(null, payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Define the advice class
    class MethodInterceptor {
        @OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
        static boolean enter(
                @Origin Method method,
                @AllArguments Object[] allArguments
        ) {
            System.out.println("[Bridge] Entered method");
            try {
                Class<?> mainClass = Thread.currentThread().getContextClassLoader().loadClass("br.com.isageek.bridge.App");
                Method mainMethod = mainClass.getMethod("dispatch", String.class);
                mainMethod.invoke(null, allArguments[0]);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return true; // Indicate to skip the original method execution
        }
    }
}
