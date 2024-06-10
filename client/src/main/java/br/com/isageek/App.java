package br.com.isageek;

import org.apache.commons.lang3.StringUtils;

public class App
{
    public static void main( String[] args )
    {
        System.out.println("[Client] Hello World! Client");
        callServer(StringUtils.upperCase("Some data"));
    }

    private static void callServer(String payload) {
        System.out.println("[Client] Call server through bridge FAILED, payload: " + payload);
    }
}
