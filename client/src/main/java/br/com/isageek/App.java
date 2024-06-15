package br.com.isageek;

import org.apache.commons.lang3.StringUtils;

public class App
{
    public static void main( String[] args )
    {
        System.out.println("[Client] Hello World!");
        for (final String arg : args) {
            System.out.println("[Client] arg: "+arg);
        }
        String clientEnv = System.getenv("clientEnv");
        System.out.println("[Client] clientEnv: "+clientEnv);
        String clientProp = System.getProperty("clientProp");
        System.out.println("[Client] clientProp: "+clientProp);
        String fileSeparator = System.getProperty("file.separator");
        System.out.println("[Client] fileSeparator: "+fileSeparator);

        callServer(StringUtils.upperCase("Some data"));
        SomeOtherClass someOtherClass = new SomeOtherClass();
        System.out.println(someOtherClass.getPayload("foo"));
        x();
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
