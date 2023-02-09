package com.suttori;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;

public class ProjectProperties {

    private static Logger logger = Logger.getLogger(ProjectProperties.class);
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
            logger.info("error set properties");
            exception.printStackTrace();
        }
        return props;
    }
}
