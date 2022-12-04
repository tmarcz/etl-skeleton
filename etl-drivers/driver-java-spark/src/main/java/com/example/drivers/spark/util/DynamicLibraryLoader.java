package com.example.drivers.spark.util;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

public class DynamicLibraryLoader {

    public void load() {
        try {
            File file = new File("c:\\myjar.jar");

            URL url = file.toURI().toURL();
            URL[] urls = new URL[]{url};

            ClassLoader cl = new URLClassLoader(urls);
            Class cls = cl.loadClass("com.mypackage.myclass");

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void unload() {

    }
}
