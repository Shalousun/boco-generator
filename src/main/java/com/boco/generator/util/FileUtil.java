package com.boco.generator.util;


import com.boco.generator.builder.CodeBuilder;
import com.boco.generator.builder.JqueryPluginBuilder;
import com.boco.generator.filter.FileNameFilter;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {
    public static String getLastModifiedDate(File file) {
        return new Date(file.lastModified()).toString();
    }

    /**
     * To Suffix
     * getFileExt
     *
     * @param fileName file name
     * @return String
     */
    public static String toSuffix(String fileName) {
        String name = null;
        try {
            int index = fileName.lastIndexOf(".");
            name = fileName.substring(0, index);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 获取真实文件名
     *
     * @param filePath
     * @return
     */
    public static String getRealName(String filePath) {
        filePath = filePath.substring(filePath.lastIndexOf("\\") + 1);
        return filePath;
    }

    public static boolean delete(String path) {
        try {
            File file = new File(path);
            file.delete();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            for (File file : children) {
                return deleteDir(file);
            }
        }
        return dir.delete();
    }

    public static boolean writeDbSql(List<Class> list, File file) {
        boolean flag = true;
        BufferedWriter output = null;
        try {
            file.createNewFile();
            output = new BufferedWriter(new FileWriter(file));
            for (Class clazz : list) {
                String str = CodeBuilder.createTable(clazz);
                output.write(str);
            }
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    public static boolean writeMapperXml(List<Class> list, String dir) {
        boolean flag = false;
        BufferedWriter output = null;
        for (Class clazz : list) {
            try {
                String name = clazz.getSimpleName();
                name = name + "Mapper.xml";
                File file0 = new File(dir);
                file0.mkdir();
                File file = new File(dir + "\\" + name);
                file.createNewFile();
                output = new BufferedWriter(new FileWriter(file));
                output.write("me");
                //output.write(CodeBuilder.createMapper(clazz));
                flag = true;
            } catch (IOException e) {
                e.printStackTrace();
                flag = false;
            } finally {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;

    }

    public static boolean writeSpringMybatisCfg(String basePackageName) {
        boolean flag = false;
        BufferedWriter output = null;
        try {
            String name = "spring-mybatis.xml";
            File file0 = new File("e:\\springCfg");
            file0.mkdir();
            File file = new File("e:\\springCfg\\" + name);
            file.createNewFile();
            output = new BufferedWriter(new FileWriter(file));
            output.write(CodeBuilder.createSpringCfgForMyBatis(basePackageName));
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    public static boolean writeJqueryPlugin(String pluginName) {
        boolean flag = false;
        BufferedWriter output = null;
        try {
            String name = "jquery-" + pluginName + ".js";
            File file0 = new File("e:\\jqueryPlugin");
            file0.mkdir();
            File file = new File("e:\\jqueryPlugin\\" + name);
            file.createNewFile();
            output = new BufferedWriter(new FileWriter(file));
            output.write(JqueryPluginBuilder.buildJqueryPlugin(pluginName));
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    public static boolean writeDaoCode(List<Class> list) {
        boolean flag = false;
        BufferedWriter output = null;
        for (Class clazz : list) {
            try {
                String name = clazz.getSimpleName();
                name = name + "Dao.java";
                File file0 = new File("e:\\dao");
                file0.mkdir();
                File file = new File("e:\\dao\\" + name);
                file.createNewFile();
                output = new BufferedWriter(new FileWriter(file));
                output.write(CodeBuilder.createDao(clazz));
                flag = true;
            } catch (IOException e) {
                e.printStackTrace();
                flag = false;
            } finally {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;

    }

    public static boolean writeServiceCode(List<Class> list) {
        boolean flag = false;
        BufferedWriter output = null;
        for (Class clazz : list) {
            try {
                String name = clazz.getSimpleName();
                name = name + "Service.java";
                File file0 = new File("e:\\service");
                file0.mkdir();
                File file = new File("e:\\service\\" + name);
                file.createNewFile();
                output = new BufferedWriter(new FileWriter(file));
                output.write(CodeBuilder.createService(clazz));
                flag = true;
            } catch (IOException e) {
                e.printStackTrace();
                flag = false;
            } finally {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;

    }

    public static boolean writeServiceImpl(List<Class> list) {
        boolean flag = false;
        BufferedWriter output = null;
        for (Class clazz : list) {
            try {
                String name = clazz.getSimpleName();
                name = name + "serviceImpl.java";
                File file0 = new File("e:\\service\\serviceImpl");
                file0.mkdir();
                File file = new File("e:\\service\\serviceImpl\\" + name);
                file.createNewFile();
                output = new BufferedWriter(new FileWriter(file));
                output.write(CodeBuilder.createServiceImpl(clazz));
                flag = true;
            } catch (IOException e) {
                e.printStackTrace();
                flag = false;
            } finally {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;

    }

    public static boolean writeContoller(List<Class> list) {
        boolean flag = false;
        BufferedWriter output = null;
        for (Class clazz : list) {
            try {
                String name = clazz.getSimpleName();
                name = name + "Controller.java";
                File file0 = new File("e:\\controller");
                file0.mkdir();
                File file = new File("e:\\controller\\" + name);
                file.createNewFile();
                output = new BufferedWriter(new FileWriter(file));
                output.write(CodeBuilder.createMVCController(clazz));
                flag = true;
            } catch (IOException e) {
                e.printStackTrace();
                flag = false;
            } finally {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    public static boolean writeTest(List<Class> list) {
        boolean flag = false;
        BufferedWriter output = null;
        for (Class clazz : list) {
            try {
                String name = clazz.getSimpleName();
                name = name + "ServiceTest.java";
                File file0 = new File("e:\\test");
                file0.mkdir();
                File file = new File("e:\\test\\" + name);
                file.createNewFile();
                output = new BufferedWriter(new FileWriter(file));
                output.write(CodeBuilder.createServiceTest(clazz));
                flag = true;
            } catch (IOException e) {
                e.printStackTrace();
                flag = false;
            } finally {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;

    }

    public static boolean writeControllerTest(List<Class> list) {
        boolean flag = false;
        BufferedWriter output = null;
        for (Class clazz : list) {
            try {
                String name = clazz.getSimpleName();
                name = name + "ControllerTest.java";
                File file0 = new File("e:\\controllerTest");
                file0.mkdir();
                File file = new File("e:\\controllerTest\\" + name);
                file.createNewFile();
                output = new BufferedWriter(new FileWriter(file));
                output.write(CodeBuilder.createControllerTest(clazz));
                flag = true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;

    }

    //将输入流转byte
    public static final byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

    public static String matchChineseCharacters(String source) {
        String reg = "<body><div class=\"container\"><a>.*?</a></div></body>";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(source);
        StringBuilder character = new StringBuilder();
        while (matcher.find()) {
            String result = matcher.group();
            System.out.println("第一次：" + result);
            //对结果进行二次正则，匹配出中文字符
            String reg1 = "[\\u4e00-\\u9fa5]+";
            Pattern p1 = Pattern.compile(reg1);
            Matcher m1 = p1.matcher(result);
            while (m1.find()) {
                character.append(m1.group());
            }
            //System.out.println(character.toString());
        }
        return character.toString();
    }

    public static String getLink(String s) {
        StringBuffer buffer = new StringBuffer();
        String regex;
        final List<String> list = new ArrayList<String>();
        //regex = "<a[^>]*href=(\"([/name/*.html\"])\"|\'([^\']*)\'|([^\\s>]*))[^>]*>(.*?)</a>";
        regex = "<a[^>]*>([^<]*)</a>";
        final Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
        final Matcher ma = pa.matcher(s);
        while (ma.find()) {
            String result = ma.group();
            String reg1 = "[\\u4e00-\\u9fa5]+";
            Pattern p1 = Pattern.compile(reg1);
            Matcher m1 = p1.matcher(result);
            int i = 0;
            while (m1.find()) {
                //&&ma.group().contains("btn btn-link")
                if (ma.group().contains("/name/") && ma.group().contains("btn btn-link")) {
                    //list.add(m1.group());
                    i++;
                    if (i % 2 == 0) {
                        buffer.append(m1.group()).append("\r\n");
                        //System.out.println("姓名:"+m1.group());
                    }

                }

            }

        }
        return buffer.toString();
    }

    public static void main(String[] args) {

        String root = "D:\\name_list";
        //System.getProperty("user.dir");
        File file = new File("d:\\name02.txt");


        StringBuffer buffer = new StringBuffer();
        buffer.append(root.replaceAll("\\\\", "/"));
        //buffer.append("/src/main/java/");
        System.out.println(buffer.toString());
        //buffer.append(modelPackage.replace(".","/"));
        File entryFile = new File(buffer.toString());
        File[] eFiles = entryFile.listFiles(new FileNameFilter("html"));
        String reg = "<a((?btn btn-link).)*?>([^<>]*?[\\u4e00-\\u9fa5]+[^<>]*?)+(?=</a>)";


        for (File ef : eFiles) {
            String curLine = null;
            String content = null;
            System.out.println("文件名：" + ef.getName());
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(ef));
                while ((curLine = reader.readLine()) != null) {
                    content = content + curLine + "\r\n";

                }
            } catch (Exception e) {
                System.out.println(e);
            }
            String resourse = getLink(content);
            try {

                //output.write(resourse);
                //writeTxt(resourse,file);
                writeFile(resourse, "d:\\name03.txt", true, "UTF-8");
            } catch (Exception e) {
                System.out.println(e);
            }


        }
    }

    /**
     * 写文件
     *
     * @param source
     * @param filePath
     * @param append
     * @return
     */
    public static boolean writeFile(String source, String filePath, boolean append) {
        return writeFile(source, filePath, append, "utf-8");
    }

    /**
     * write by OutPutStreamWriter
     *
     * @param source   String source
     * @param filePath File path
     * @param append   is append
     * @param encoding encoding
     * @return boolean
     */
    public static boolean writeFile(String source, String filePath, boolean append, String encoding) {
        boolean flag = false;
        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(new FileOutputStream(filePath, append), encoding);
            osw.write(source);
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
            throw new RuntimeException("找不到指定的文件出处目录！！！");
        } finally {
            try {
                osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * write by BufferedWriter
     *
     * @param source String source
     * @param file   File
     * @return boolean
     */
    public static boolean writeFile(String source, File file) {
        boolean flag = true;
        BufferedWriter output = null;
        try {
            file.createNewFile();
            output = new BufferedWriter(new FileWriter(file, true));
            output.write(source);
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * get file content
     *
     * @param fileName file name
     * @return String
     */
    public static String getFileContent(String fileName) {
        BufferedReader reader = null;
        StringBuilder fileContent = new StringBuilder();
        try {
            File f = new File(fileName);
            reader = new BufferedReader(new FileReader(f));
            String line = "";
            while ((line = reader.readLine()) != null) {
                fileContent.append(line);
                fileContent.append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileContent.toString();
    }

    /**
     * Get String by input stream
     *
     * @param is InputStream
     * @return String
     */
    public static String getFileContent(InputStream is) {
        BufferedReader reader = null;
        StringBuilder fileContent = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while ((line = reader.readLine()) != null) {
                fileContent.append(line);
                fileContent.append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileContent.toString();
    }

    public void mkdir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }

    }
}
