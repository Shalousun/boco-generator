package com.boco.generator.builder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 代码输出
 * 
 * @author sunyu
 *
 */
public class CodeOuter {

	/**
	 * 根据一组class生成sql脚本
	 * 
	 * @param list
	 *            List
	 * @param file
	 *            File
	 * @return
	 */
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

	/**
	 * 将字符串写入文件中
	 * 
	 * @param str
	 * @param file
	 * @return
	 */
	public static boolean writeFile(String str, File file) {
		boolean flag = true;
		BufferedWriter output = null;
		try {
			file.createNewFile();
			output = new BufferedWriter(new FileWriter(file));
			output.write(str);
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
	 * 根据项目的model的bean的class数组生成MyBatis映射文件
	 * 
	 * @param list
	 *            List
	 * @param dir
	 *            输出目录
	 * @return
	 */
	public static boolean writeMapperXml(List<Class> list, String dir) {
		boolean flag = false;
		BufferedWriter output = null;
		File file0 = null;
		File file = null;
		try {
			for (Class clazz : list) {
				String name = clazz.getSimpleName();
				name = name + "Mapper.xml";
				file0 = new File(dir);
				file0.mkdir();
				file = new File(dir + "\\" + name);
				file.createNewFile();
				output = new BufferedWriter(new FileWriter(file));
				output.write(CodeBuilder.createMapper(clazz));
			}
		} catch (IOException e) {
			e.printStackTrace();
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
	 * 根据map中的数据生成html代码到指定目录
	 * 本方法用于根据将excel设计原型中的表格生成
	 * @param filesData 文件数据
	 * @param dir 目录
     * @return
     */
	public static boolean writeToHtml(Map<String,StringBuilder> filesData,String dir){
		boolean flag = false;
		BufferedWriter output = null;
		File file0 = new File(dir);
		file0.mkdir();
		File file;
		for (Map.Entry<String,StringBuilder> entry: filesData.entrySet()) {
			try {
				String name = entry.getKey();
				name = name + ".html";
				file = new File(dir+"\\" + name);
				file.createNewFile();
				output = new BufferedWriter(new FileWriter(file));
				output.write(entry.getValue().toString());
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

	/**
	 * 生成jquery插件的基本框架 usage:writeJqueryPlugin("grid","e:\\jqueryPlugin");
	 * 
	 * @param pluginName
	 * @param dir
	 * @return
	 */
	public static boolean writeJqueryPlugin(String pluginName, String dir) {
		boolean flag = false;
		BufferedWriter output = null;
		try {
			String name = "jquery-" + pluginName + ".js";
			File file0 = new File(dir);
			file0.mkdir();
			File file = new File(dir + "\\" + name);
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
  /**
   * 生成到dao层代码
   * usage:writeDaoCode(list,""e:\\dao"")
   * @param list List
   * @param dir 输出路径
   * @return
   */
	public static boolean writeDaoCode(List<Class> list,String dir) {
		boolean flag = false;
		BufferedWriter output = null;
		try {
			for (Class clazz : list) {
				String name = clazz.getSimpleName();
				name = name + "Dao.java";
				File file0 = new File(dir);
				file0.mkdir();
				File file = new File(dir+"\\" + name);
				file.createNewFile();
				output = new BufferedWriter(new FileWriter(file));
				output.write(CodeBuilder.createDao(clazz));
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
	/**
	 * usage:writeServiceCode(list,"e:\\service");
	 * @param list
	 * @param dir
	 * @return
	 */
	public static boolean writeServiceCode(List<Class> list,String dir) {
		boolean flag = false;
		BufferedWriter output = null;
		try{
			for(Class clazz:list){
				String name = clazz.getSimpleName();
				name = name + "Service.java";
				File file0 = new File(dir);
				file0.mkdir();
				File file = new File(dir+"\\" + name);
				file.createNewFile();
				output = new BufferedWriter(new FileWriter(file));
				output.write(CodeBuilder.createService(clazz));
			}
			flag = true;
		}catch(IOException e){
			e.printStackTrace();
			flag = false;
		}finally{
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	/**
	 * 创建controller层的单元测试代码
	 * usage:writeControllerTest(list,"e:\\controllerTest\\");
	 * @param list
	 * @return
	 */
	public static boolean writeControllerTest(List<Class> list,String str) {
		boolean flag = false;
		BufferedWriter output = null;
		try{
			for(Class clazz : list){
				String name = clazz.getSimpleName();
				name = name + "ControllerTest.java";
				File file0 = new File(str);
				file0.mkdir();
				File file = new File(str+"\\" + name);
				file.createNewFile();
				output = new BufferedWriter(new FileWriter(file));
				output.write(CodeBuilder.createControllerTest(clazz));
				flag = true;
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
}
