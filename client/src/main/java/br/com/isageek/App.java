package br.com.isageek;

import org.apache.commons.lang3.StringUtils;

import java.io.PrintStream;

public class App
{
    public static int main( String[] args )
    {
        System.out.println("[Client] Hello World!");
        if (args.length == 0) {
            System.out.println("[Client] NO ARGS");
        }
        for (final String arg : args) {
            System.out.println("[Client] arg: "+arg);
        }
        String languageFromEnvVar = System.getenv("LANGUAGE");
        PrintStream out = System.out;
        out.println("[Client] LANGUAGE: "+languageFromEnvVar);
        String langFromEnvVar = System.getenv("LANG");
        System.out.println("[Client] LANG: "+langFromEnvVar);
        String clientProp = System.getProperty("clientProp");
        System.out.println("[Client] clientProp: "+clientProp);
        String fileSeparator = System.getProperty("file.separator");
        System.out.println("[Client] fileSeparator: "+fileSeparator);

        callServer(StringUtils.upperCase("Some data"));
        SomeOtherClass someOtherClass = new SomeOtherClass();
        System.out.println(someOtherClass.getPayload("foo"));
        x();
        return 42;
    }

    private static void callServer(String payload) {
        System.out.println("[Client] FAILED!!! Call server , payload: " + payload);
    }

    private static void x() {
        System.out.println("[Client] FAILED!!! XXXXXXXX");
    }

    private static void y() {
        System.out.println("[Client] YYYYYYYY");
    }
}
