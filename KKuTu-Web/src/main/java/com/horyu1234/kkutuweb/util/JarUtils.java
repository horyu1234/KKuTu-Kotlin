package com.horyu1234.kkutuweb.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by horyu on 2018-03-03
 */
public class JarUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JarUtils.class);

    private JarUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean copyResource(String resourceName, String targetFolder) {
        if (getTargetFile(targetFolder, resourceName).exists()) {
            return false;
        }

        Map<String, InputStream> resourceStreamMap = JarUtils.getResourceStreamMap(resourceName);
        for (Map.Entry<String, InputStream> resourceStreamMapEntry : resourceStreamMap.entrySet()) {
            String fileName = resourceStreamMapEntry.getKey();
            InputStream inputStream = resourceStreamMapEntry.getValue();

            File targetFile = getTargetFile(targetFolder, fileName);
            if (targetFile.exists()) {
                continue;
            }

            copyFile(inputStream, targetFile);
        }

        return true;
    }

    private static void copyFile(InputStream inputStream, File targetFile) {
        if (targetFile.getParentFile() != null && !targetFile.getParentFile().exists() && !targetFile.getParentFile().mkdirs()) {
            LOGGER.warn("대상 위치의 상위 폴더를 생성하는 중 문제가 발생하였습니다.");
        }

        try {
            Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOGGER.error("대상 위치에 스트림을 기록하는 중 오류가 발생하였습니다.", e);
        }
    }

    private static File getTargetFile(String targetFolder, String resourceName) {
        return Paths.get(System.getProperty("app.home"), targetFolder, resourceName).toFile();
    }

    private static boolean isRunFromJarFile(String resourceName) {
        URI jarFileUri;
        try {
            jarFileUri = Objects.requireNonNull(JarUtils.class.getClassLoader().getResource(resourceName)).toURI();
        } catch (URISyntaxException e) {
            LOGGER.error("JAR 파일의 주소를 가져오는 중 오류가 발생하였습니다.", e);
            return false;
        }

        return jarFileUri.getScheme().contains("jar");
    }

    private static File getJarFile() {
        String filePathPrefix = "file:/";
        String filePathSuffix = "!/BOOT-INF";

        String file = JarUtils.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        file = file.substring(file.indexOf(filePathPrefix) + filePathPrefix.length(), file.indexOf(filePathSuffix));

        return new File(file);
    }

    private static Map<String, InputStream> getResourceStreamMap(String resourceName) {
        Map<String, InputStream> resourceInputStreams = new HashMap<>();
        if (JarUtils.isRunFromJarFile(resourceName)) {
            resourceInputStreams.putAll(getResourceStreamMapWhenJarFile(resourceName));
        } else {
            resourceInputStreams.putAll(getResourceStreamMapWhenIDE(resourceName));
        }

        return resourceInputStreams;
    }

    private static Map<String, InputStream> getResourceStreamMapWhenJarFile(String resourceName) {
        Map<String, InputStream> resourceStreamMap = new HashMap<>();

        try (JarFile jar = new JarFile(JarUtils.getJarFile())) {
            Enumeration<JarEntry> jarEntries = jar.entries();
            while (jarEntries.hasMoreElements()) {
                JarEntry jarEntry = jarEntries.nextElement();

                String entryFileName = jarEntry.getName();
                if (entryFileName.startsWith("BOOT-INF/classes/" + resourceName)) {
                    entryFileName = removeParentPath(entryFileName, resourceName);
                    if (!isFile(entryFileName)) {
                        continue;
                    }

                    resourceStreamMap.put(entryFileName, getResourceFileStream(entryFileName));
                }
            }
        } catch (IOException e) {
            LOGGER.error("JAR 파일에서 스트림을 가져오는 중 오류가 발생하였습니다.", e);
        }

        return resourceStreamMap;
    }

    private static boolean isFile(String entryFileName) {
        return !"".equals(FilenameUtils.getExtension(entryFileName));
    }

    private static Map<String, InputStream> getResourceStreamMapWhenIDE(String resourceName) {
        Map<String, InputStream> resourceStreamMap = new HashMap<>();

        URL resourceUrl = Thread.currentThread().getContextClassLoader().getResource(resourceName);
        if (resourceUrl != null) {
            File resource = new File(resourceUrl.getFile());

            List<File> resourceFiles = new ArrayList<>();
            if (resource.isFile()) {
                resourceFiles.add(resource);
            } else if (resource.isDirectory()) {
                resourceFiles.addAll(FileUtils.listFiles(resource, null, true));
            }

            resourceStreamMap.putAll(toResourceStreamMap(resourceName, resourceFiles));
        }

        return resourceStreamMap;
    }

    private static Map<String, InputStream> toResourceStreamMap(String resourceName, List<File> resourceFiles) {
        Map<String, InputStream> resourceStreamMap = new HashMap<>();
        for (File resourceFile : resourceFiles) {
            String fileLocation = resourceFile.getPath();
            String fileName = removeParentPath(fileLocation, resourceName);

            try {
                resourceStreamMap.put(fileName, FileUtils.openInputStream(resourceFile));
            } catch (IOException e) {
                LOGGER.error("파일에서 입력 스트림을 여는 중 오류가 발생하였습니다.", e);
            }
        }

        return resourceStreamMap;
    }

    private static String removeParentPath(String path, String folderName) {
        return path.substring(path.indexOf(folderName), path.length());
    }

    private static InputStream getResourceFileStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }
}
