package com.suttori;

import java.io.*;
import java.util.Properties;

public class ProjectProperties {

    private static String fileName = "app.properties";
    private static Properties props = setProperties();

    public static String getProperty(String property){
        return props.getProperty(property);
    }

    public static Properties getEmailProperties(){
        Properties props = new Properties();
        props.put("mail.smtp.host", getProperty("mail.host"));
        props.put("mail.smtp.port", getProperty("mail.port"));
        props.put("mail.smtp.auth", "true");
        props.put("mail.transport.protocol", getProperty("mail.protocol"));
        props.put("mail.smtp.starttls.enable", "true");
        return props;
    }

    private static Properties setProperties(){
        Properties props = new Properties();
        try (InputStream resource = ProjectProperties.class.getClassLoader().getResourceAsStream(fileName)) {
            props.load(resource);
        } catch (IOException exception) {
            System.out.println("smth wrong with property file");
            exception.printStackTrace();
        }
        return props;
    }
}
