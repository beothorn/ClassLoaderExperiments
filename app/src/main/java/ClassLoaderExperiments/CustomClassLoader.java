package ClassLoaderExperiments;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class CustomClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String resource = switch (name) {
            case "com.example.Add" -> "/Add.class";
            case "com.example.Printer" -> "/Printer.class";
            default -> throw new IllegalStateException("Unexpected value: " + name);
        };

        byte[] classBytes;
        try (InputStream inputStream = getClass().getResourceAsStream(resource)) {
            if (inputStream == null) {
                throw new ClassNotFoundException("Could not find the class file in resources");
            }
            classBytes = inputStream.readAllBytes();
        } catch (IOException e) {
            throw new ClassNotFoundException("Error reading the class file", e);
        }

        return defineClass(name, classBytes, 0, classBytes.length);
    }
}
