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
    }

    public static void bridgeReceive(String message){
        System.out.println("[Server] Server received '" +message + "'" );
    }
}
