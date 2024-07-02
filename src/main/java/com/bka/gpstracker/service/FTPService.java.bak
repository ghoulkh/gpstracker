package com.bka.gpstracker.service;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Log4j2
public class FTPService {
    @Autowired
    private FTPClient ftpClient;
    @Value("${ftp.dir.local}")
    private String localDirectory;
    @Value("${ftp.dir.remote}")
    private String remoteDirectory;

    @Scheduled(initialDelay = 10, fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    public void jobPullFile() {
        List<String> pathFilesRemote = getAllPathFileFromRemote();
        List<String> pathFileLocal = getAllPathFileFromLocal();
        pathFilesRemote.removeAll(pathFileLocal);
        pathFilesRemote.forEach(path -> {
            storeToLocal(path);
            log.info("stored file {}", path);
        });
    }


    public byte[] getImageFromPath(String path) {
        String userDirectory = System.getProperty("user.dir");
        StringBuilder hexBuilder = new StringBuilder();
        try (BufferedReader buffer = new BufferedReader(
                new FileReader(userDirectory + localDirectory + "/" + path))) {

            String str;

            // Condition check via buffer.readLine() method
            // holding true upto that the while loop runs
            while ((str = buffer.readLine()) != null) {

                hexBuilder.append(str);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return hexToByte(hexBuilder.toString());
    }



    private byte[] hexToByte(String hex) {
        byte[] ans = new byte[hex.length() / 2];
        for (int i = 0; i < ans.length; i++) {
            int index = i * 2;

            int val = Integer.parseInt(hex.substring(index, index + 2), 16);
            ans[i] = (byte)val;
        }
        return ans;
    }




    public List<String> getAllPathFileFromRemote() {
        try {
            FTPFile[] ftpFiles = ftpClient.listFiles(this.remoteDirectory);
            List<String> result = new ArrayList<>();
            for (int i = 0; i < ftpFiles.length; i ++ ) {
                String imagesDirectoryName = ftpFiles[i].getName();
                String imagesDirectoryPath = this.remoteDirectory + "/"+ imagesDirectoryName;
                FTPFile[] ftpImageFiles = ftpClient.listFiles(imagesDirectoryPath);
                for (int x = 0; x < ftpImageFiles.length; x ++) {
                    result.add(imagesDirectoryName + "/" + ftpImageFiles[x].getName());
                }
            }
            return result;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<String> getAllPathFile() {
        return getAllPathFileFromLocal();
    }

    public void storeToLocal(String path) {
        String userDirectory = System.getProperty("user.dir");
        String imagesDirectory = userDirectory + this.localDirectory;
        makeImageDirectory(imagesDirectory);
        String directory = path.substring(0, path.indexOf("/"));

        makeImageDirectory(imagesDirectory + "/" + directory);
        String filePath = imagesDirectory + "/" +path;
        File file = makeImageFile(filePath);

        try {
            FileOutputStream out = new FileOutputStream(file.getPath());
            ftpClient.retrieveFile(this.remoteDirectory + "/" + path, out);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private List<String> getAllPathFileFromLocal() {
        String userDirectory = System.getProperty("user.dir");
        String imagesDirectory = userDirectory + this.localDirectory;
        File file = new File(imagesDirectory);
        File[] files = file.listFiles();
//        if (files == null) return new ArrayList<>();
        List<String> result = new ArrayList<>();
        for (int i = 0; i < files.length; i ++) {
            String imagesDir = files[i].getName();
            String imagesDirPath = imagesDirectory + "/" + imagesDir;
            File file1 = new File(imagesDirPath);
            File[] files1 = file1.listFiles();
            SortedMap<Long, String> stringSortedMap = new TreeMap<Long, String>(Collections.reverseOrder());
            for (int x = 0; x < files1.length; x ++) {
                stringSortedMap.put(Long.valueOf(files1[x].getName()
                        .replace("_", "")
                        .replace(".txt", "")), files1[x].getName());
            }

            for (SortedMap.Entry<Long, String> entry : stringSortedMap.entrySet()) {
                result.add(imagesDir + "/" + entry.getValue());
            }
        }
        return result;
    }



    private void makeImageDirectory(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    private File makeImageFile(String path) {
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return file;
    }

}
