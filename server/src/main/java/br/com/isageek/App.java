package br.com.isageek;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "[Server] Hello World!" );
        String clientProp = System.getProperty("clientProp");
        System.out.println("[Server] clientProp: "+clientProp);
    }

    public static void bridgeReceive(String message){
        System.out.println("[Server] Server received '" +message + "'" );
    }

    public static String injectData(String str) {
        return str + " SERVER INJECTED THIS";
    }
}
