package br.com.isageek;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class App
{
    public static void main( String[] args )
    {
        System.out.println("[Client] Hello World! Client");
        try {
            Class<?> bridgeClass = Thread.currentThread().getContextClassLoader().loadClass("br.com.isageek.bridge.App");
            Method mainMethod = bridgeClass.getMethod("callBridge", String.class);
            mainMethod.invoke(null, "Payload");
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            System.out.println("Ops" + e.getMessage());
        }
    }
}
