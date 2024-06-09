package br.com.isageek.bridge;

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
        serverLoader = loadAndRun(
                "server-1.0-SNAPSHOT-jar-with-dependencies.jar",
                "br.com.isageek.App"
        );
        clientLoader = loadAndRun(
                "client-1.0-SNAPSHOT-jar-with-dependencies.jar",
                "br.com.isageek.App"
        );
    }

    private static URLClassLoader loadAndRun(final String jarFileName, final String mainClassQualifiedName) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        File tempJarFile = extractResourceToTempFile(jarFileName);

        // Load the JAR using URLClassLoader
        URL jarUrl = tempJarFile.toURI().toURL();
        try(URLClassLoader classLoader = new URLClassLoader(new URL[] { jarUrl })){
            // Load the main class from the JAR
            Class<?> mainClass = classLoader.loadClass(mainClassQualifiedName);

            Method mainMethod = mainClass.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) new String[] {});
            return classLoader;
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

    public static void callBridge(String payload){
        System.out.println("[Bridge] Received from client: " + payload);

        try {
            Class<?> mainClass = serverLoader.loadClass("br.com.isageek.App");
            Method mainMethod = mainClass.getMethod("bridgeReceive", String.class);
            mainMethod.invoke(null, payload);
        } catch (NoSuchMethodException | ClassNotFoundException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
