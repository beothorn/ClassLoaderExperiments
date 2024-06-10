package br.com.isageek;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class App
{
    public static void main( String[] args )
    {
        System.out.println("[Client] Hello World! Client");
        callServer("Some data");
    }

    private static void callServer(String payload) {
        System.out.println("[Client] Call server through bridge FAILED, payload: " + payload);
    }
}
