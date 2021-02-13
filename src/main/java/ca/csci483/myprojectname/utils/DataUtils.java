/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.csci483.myprojectname.utils;

import ca.csci483.myprojectname.model.User;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultStreamedContent;

/**
 *
 * @author osoufan
 */
public class DataUtils {

    //cannot redirect from confirm dialog
    // this one will for sure do the job
    public static void doRedirect(String url) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T findBean(String beanName) {
        FacesContext context = FacesContext.getCurrentInstance();
        return (T) context.getApplication().evaluateExpressionGet(context, "#{" + beanName + "}", Object.class);
    }

    public static String getDomainURL(String myurl) {
        try {
            URL url = new URL(myurl);
            return url.getProtocol() + "://" + url.getAuthority();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //return all filenames of the given folder used by loading all R scripts
    //abs true return full path, false just file name
    public static ArrayList<String> getFileNames(String folderPath, boolean absPath) {
        File folder = new File(folderPath);
        File files[] = folder.listFiles();
        ArrayList<String> fileNames = new ArrayList();
        if (absPath) {
            for (int i = 0; i < files.length; i++) {
                fileNames.add(folderPath + File.separator + files[i].getName());
            }
        } else {
            for (int i = 0; i < files.length; i++) {
                fileNames.add(files[i].getName());
            }
        }
        return fileNames;
    }

    //Note: for Class uploadedFile
    public static String getJustFileName(String uploadedFileName) {
        int index = uploadedFileName.lastIndexOf('/');
        String justFileName;
        if (index >= 0) {
            justFileName = uploadedFileName.substring(index + 1);
        } else {
            // Try backslash
            index = uploadedFileName.lastIndexOf('\\');
            if (index >= 0) {
                justFileName = uploadedFileName.substring(index + 1);
            } else { // No forward or back slashes
                justFileName = uploadedFileName;
            }
        }
        return justFileName;
    }

    public static void copyFile(File in, File out) {
        try {
            FileInputStream fis = new FileInputStream(in);
            FileOutputStream fos = new FileOutputStream(out);
            copyInputStream(fis, fos);
        } catch (IOException e) {
        }
    }

    private static void copyInputStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) >= 0) {
            out.write(buffer, 0, len);
        }
        in.close();
        out.close();
    }

    private static final int BUFFER_SIZE = 4096;

    private static void extractFile(ZipInputStream in, File outdir, String name) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(outdir, name)));
        int count = -1;
        while ((count = in.read(buffer)) != -1) {
            out.write(buffer, 0, count);
        }
        out.close();
    }

    private static void mkdirs(File outdir, String path) {
        File d = new File(outdir, path);
        if (!d.exists()) {
            d.mkdirs();
        }
    }

    private static String dirpart(String name) {
        int s = name.lastIndexOf(File.separatorChar);
        return s == -1 ? null : name.substring(0, s);
    }

    /**
     * *
     * Extract zipfile to outdir with complete directory structure
     *
     * @param zipfile Input .zip file
     * @param outdir Output directory
     */
    public static void extract(String zipfileName, String outdirName) {
        File zipfile = new File(zipfileName);
        File outdir = new File(outdirName);
        try {
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipfile));
            ZipEntry entry;
            String name, dir;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                if (entry.isDirectory()) {
                    mkdirs(outdir, name);
                    continue;
                }
                /* this part is necessary because file entry can come before
         * directory entry where is file located
         * i.e.:
         *   /foo/foo.txt
         *   /foo/
                 */
                dir = dirpart(name);
                if (dir != null) {
                    mkdirs(outdir, dir);
                }

                extractFile(zin, outdir, name);
            }
            zin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createZipFile(File[] files, String path) {
        // Create a buffer for reading the files
        byte[] buf = new byte[18024];

        try {
            // Create the ZIP file
            String outFilename = path + File.separator + "Download.zip";
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));

            // Compress the files
            for (int i = 0; i < files.length; i++) {
                FileInputStream in = new FileInputStream(files[i]);
                // Add ZIP entry to output stream.
                out.putNextEntry(new ZipEntry(files[i].getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (IOException e) {
        }
    }

    // clear string spaces and punctuations
    public static String cleanString(String s) {
        s = s.replaceAll("^\\s+", ""); //remove leading space
        s = s.replaceAll("\\s+$", ""); //remove trailing space
        s = s.replaceAll("(\\r|\\n)", "");
        s = s.replaceAll("^\"", ""); //remove leading quote
        s = s.replaceAll("\"$", ""); //remove trailing quote
        s = s.replaceAll("[^a-zA-Z0-9)]$", ""); //remove last one if not character/number/) (i.e. punctuation)
        return s;
    }

    public static void deleteFile(User job, String filename) {
        File f1 = new File(job.getHomeDir() + "/" + filename);
        if (f1.exists()) {
            boolean sucess = f1.delete();
            if (!sucess) {
                System.out.println("=== Delete file - " + filename + " failed.");
            }
        }
    }

    //a utility function to remove the old user folders
    //called everytime a new user folder is created, default 1 day
    public static void deleteFilesOlderThanNdays(String dirWay, int n) {
        System.out.println(dirWay);
        File directory = new File(dirWay);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1 * n);
        long purgeTime = cal.getTimeInMillis();

        if (directory.exists()) {
            File[] listFiles = directory.listFiles();
            for (File listFile : listFiles) {
                if (listFile.getName().startsWith("guest")) {
                    if (listFile.lastModified() < purgeTime) {
                        if (!deleteDir(listFile.getAbsolutePath())) {
                            System.err.println("Unable to delete file: " + listFile);
                        }
                    }
                }
            }
        }
    }

    //use Unix command to remvoe (non-empty) folder
    public static boolean deleteDir(String fdPath) {
        //first make sure they are removable 
        ArrayList command = new ArrayList(4);
        command.add("chmod");
        command.add("-R");
        command.add("777");
        command.add(fdPath);
        boolean res = myExec((String[]) command.toArray(new String[1]));

        if (res) {
            command = new ArrayList(3);
            command.add("rm");
            command.add("-r");
            command.add(fdPath);
            return myExec((String[]) command.toArray(new String[1]));
        } else {
            return false;
        }
    }

    /**
     * Tries to exec the command, waits for it to finsih, logs errors if exit
     * status is nonzero, and returns true if exit status is 0 (success).
     *
     * @param command Description of the Parameter
     * @return Description of the Return Value
     */
    private static boolean myExec(String[] command) {
        Process proc;
        try {
            //System.out.println("Trying to execute command " + Arrays.asList(command));
            proc = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            System.out.println("IOException while trying to execute " + command);
            return false;
        }
        //System.out.println("Got process object, waiting to return.");
        int exitStatus;
        while (true) {
            try {
                exitStatus = proc.waitFor();
                break;
            } catch (java.lang.InterruptedException e) {
                System.out.println("Interrupted: Ignoring and waiting");
            }
        }
        if (exitStatus != 0) {
            System.out.println("Error executing command: " + exitStatus);
        }
        return (exitStatus == 0);
    }

    public static String readTextFile(String filePath) {

        BufferedReader br = null;
        String text = "";
        String line = "";
        try {
            br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null) {
                line = line.replace("\t", "  ");
                text = text + "\n" + line;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return text;
    }

    public static void updateMsg(String type, String content) {
        if (type.equalsIgnoreCase("error")) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", content));
        } else if (type.equalsIgnoreCase("warning")) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", content));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", content));
        }
    }

    public static void showMessage(FacesMessage.Severity type, String header, String body) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(type, header, body));
    }

    public static DefaultStreamedContent getDownloadFile(String filePath) {
        try {
            //File file = new File(currentUser.getHomeDir() + "/" + fileNm);
            File file = new File(filePath);
            InputStream input = new FileInputStream(file);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            return (new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setupFileDownloadZip(User currentUser) {

        File folder = new File(currentUser.getHomeDir());

        //remove previous (if any) zip file
        DataUtils.deleteFile(currentUser, "Download.zip");

        File[] listOfFiles = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".csv");
            }
        });
        DataUtils.createZipFile(listOfFiles, currentUser.getHomeDir());
    }


}
