package br.com.isageek;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "[Server] Hello World!" );
        if (args.length == 0) {
            System.out.println("[Server] NO ARGS");
        }
        for (final String arg : args) {
            System.out.println("[Server] arg: "+arg);
        }
        String clientProp = System.getProperty("clientProp");
        System.out.println("[Server] clientProp: "+clientProp);
        String languageFromEnvVar = System.getenv("LANGUAGE");
        String langFromEnvVar = System.getenv("LANG");
        System.out.println("[Server] LANG: "+langFromEnvVar);
        System.out.println("[Server] LANGUAGE: "+languageFromEnvVar);
    }

    public static void bridgeReceive(String message){
        System.out.println("[Server] Server received '" +message + "'" );
    }

    public static String injectData(String str) {
        return str + " SERVER INJECTED THIS";
    }
}
