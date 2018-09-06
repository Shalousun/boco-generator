package com.boco.generator.builder;

import com.boco.generator.annotation.Table;
import com.power.common.util.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CodeBuilder {
	private static final String EXCEPTION_MSG = " is unmarked by Table annotation";

	private static final String DATA_ERROR = "可能不符合模板规范，请检查！";

	/**
	 * 创建dao接口代码
	 *
	 * @param clazz
	 * @return
	 */
	public static String createDao(Class clazz) {
		String allName = clazz.getName();
		String prePackgeName = allName.substring(0, allName.lastIndexOf(".") - 5);
		String name = clazz.getSimpleName();
		StringBuffer buffer = new StringBuffer();
		buffer.append("package ").append(prePackgeName).append("dao;\n\n");
		buffer.append("import java.util.List;\n");
		buffer.append("import java.util.Map;\n\n");
		buffer.append("import ").append(clazz.getName()).append(";\n\n");
		buffer.append("public interface ").append(name).append("Dao{\n");

		buffer.append("	boolean save(").append(name).append(" entity);\n");
		buffer.append("	boolean update(").append(name).append(" entity);\n");
		buffer.append("	boolean delete(int id);\n");
		buffer.append("	").append(name).append(" getById(int id);\n");
		buffer.append("	List<").append(name).append("> getAll();\n");
		buffer.append("	List<").append(name).append("> getPage(Map<String,Object> map);\n");
		buffer.append("}");
		return buffer.toString();
	}

	/**
	 * 创建service层接口代码
	 *
	 * @param clazz
	 * @return
	 */
	public static String createService(Class clazz) {
		String allName = clazz.getName();
		String prePackgeName = allName.substring(0, allName.lastIndexOf(".") - 5);
		String name = clazz.getSimpleName();
		StringBuffer buffer = new StringBuffer();
		buffer.append("package ").append(prePackgeName).append("service;\n\n");
		buffer.append("import java.util.List;\n");
		buffer.append("import java.io.InputStream;\n\n");
		buffer.append("import com.github.pagehelper.PageInfo;\n");
		buffer.append("import com.yizhi.common.util.model.CommonResult;\n");
		buffer.append("import ").append(clazz.getName()).append(";\n\n");
		buffer.append("public interface ").append(name).append("Service{\n");
		// 构造保存方法
		buffer.append("	CommonResult save(").append(clazz.getSimpleName());
		buffer.append(" ").append(StringUtil.firstToLowerCase(clazz.getSimpleName()));
		buffer.append(");\n");
		// 构造更新接口
		buffer.append("	CommonResult update(").append(clazz.getSimpleName());
		buffer.append(" ").append(StringUtil.firstToLowerCase(clazz.getSimpleName()));
		buffer.append(");\n");
		// 构造删除数据接口
		buffer.append("	CommonResult delete(int id);\n");
		buffer.append("	").append(name).append(" getById(int id);\n");
		buffer.append("	List<").append(name).append("> getAll();\n");
		buffer.append("	PageInfo getPage(String keyword,int pageIndex,int pageSize);\n");
		// buffer.append(" public InputStream exportExcel(String
		// sheetName);\n");
		buffer.append("}");
		return buffer.toString();
	}

	/**
	 * 创建service实现类的代码
	 *
	 * @param clazz
	 * @return
	 */
	public static String createServiceImpl(Class clazz) {
		String allName = clazz.getName();
		String prePackgeName = allName.substring(0, allName.lastIndexOf(".") - 5);
		String name = clazz.getSimpleName();
		Field[] fields = clazz.getDeclaredFields();
		String lowerName = getFirstLowerName(clazz);
		StringBuffer buffer = new StringBuffer();
		buffer.append("package  ").append(prePackgeName).append("service.serviceImpl;\n\n");
		buffer.append("import java.io.InputStream;\n");
		buffer.append("import java.util.ArrayList;\n");
		buffer.append("import java.util.HashMap;\n");
		buffer.append("import java.util.Map;\n");
		buffer.append("import java.util.List;\n\n");
		buffer.append("import javax.annotation.Resource;\n");
		buffer.append("import org.springframework.stereotype.Service;\n");

		buffer.append("import com.github.pagehelper.PageHelper;\n");
		buffer.append("import com.github.pagehelper.PageInfo;\n");
		buffer.append("import com.yizhi.common.util.ExcelUtil;\n");
		buffer.append("import com.yizhi.common.util.StringUtil;\n");
		buffer.append("import com.yizhi.common.util.model.CommonResult;\n");
		buffer.append("import ").append(clazz.getName()).append(";\n");
		buffer.append("import ").append(prePackgeName).append("dao.").append(name).append("Dao;\n");
		buffer.append("import ").append(prePackgeName).append("service.").append(name).append("Service;\n\n");
		// user annotation
		buffer.append("@Service(\"").append(lowerName).append("Service\")\n");
		buffer.append("public class ").append(name).append("serviceImpl ");
		buffer.append(" implements ").append(name).append("Service{\n");

		buffer.append("	@Resource\n");
		buffer.append("	private ").append(name).append("Dao ").append(lowerName).append("Dao;\n");
		// 保存
		buffer.append("	@Override\n");
		buffer.append("	public CommonResult save(").append(clazz.getSimpleName());
		buffer.append(" ").append(StringUtil.firstToLowerCase(clazz.getSimpleName()));
		buffer.append("){\n");
		buffer.append("		CommonResult result = new CommonResult();\n");
		buffer.append("		boolean flag = this.").append(lowerName);
		buffer.append("Dao.save(").append(StringUtil.firstToLowerCase(clazz.getSimpleName()));
		buffer.append(");\n");
		buffer.append("		if(flag){\n");
		buffer.append("			result.setMessage(\"Add success!\");\n");
		buffer.append("			result.setSuccess(true);\n");
		buffer.append("		}else{\n");
		buffer.append("			result.setMessage(\"Add failed\");\n");
		buffer.append("		}\n");
		buffer.append("		return result;\n");
		buffer.append("	}\n");
		// for (int i = 1, length = fields.length; i < length; i++) {
		// buffer.append(fields[i].getType().getSimpleName()).append(" ");
		// buffer.append(fields[i].getName()).append(",");
		// }
		// buffer.deleteCharAt(buffer.lastIndexOf(","));
		// buffer.append("){\n");
		// buffer.append(" ").append(name).append(" ").append(lowerName)
		// .append(" = new ");
		// buffer.append(name).append("();\n");
		// for (int i = 1, length = fields.length; i < length; i++) {
		// String str = fields[i].getName();
		// buffer.append(" ");
		// buffer.append(lowerName).append(".set");
		// buffer.append(getFirstUpperName(str)).append("(");
		// buffer.append(str).append(");\n");
		// }
		// buffer.append(" return ").append(lowerName).append("Dao.save(")
		// .append(lowerName);
		// buffer.append(");\n");
		// buffer.append(" }\n");
		// 更新
		buffer.append("	@Override\n");
		buffer.append("	public CommonResult update(").append(clazz.getSimpleName());
		buffer.append(" ").append(StringUtil.firstToLowerCase(clazz.getSimpleName()));
		buffer.append("){\n");
		buffer.append("		CommonResult result = new CommonResult();\n");
		buffer.append("		boolean flag = this.").append(lowerName);
		buffer.append("Dao.update(").append(StringUtil.firstToLowerCase(clazz.getSimpleName()));
		buffer.append(");\n");
		buffer.append("		if(flag){\n");
		buffer.append("			result.setMessage(\"Update success!\");\n");
		buffer.append("			result.setSuccess(true);\n");
		buffer.append("		}else{\n");
		buffer.append("			result.setMessage(\"update failed\");\n");
		buffer.append("		}\n");
		buffer.append("		return result;\n");
		buffer.append("	}\n");

		// for (int i = 0, length = fields.length; i < length; i++) {
		// buffer.append(fields[i].getType().getSimpleName()).append(" ");
		// buffer.append(fields[i].getName()).append(",");
		// }
		// buffer.deleteCharAt(buffer.lastIndexOf(","));
		// buffer.append("){\n");
		// buffer.append(" ").append(name).append(" ").append(lowerName)
		// .append(" = new ");
		// buffer.append(name).append("();\n");
		// for (int i = 0, length = fields.length; i < length; i++) {
		// String str = fields[i].getName();
		// buffer.append(" ");
		// buffer.append(lowerName).append(".set");
		// buffer.append(getFirstUpperName(str)).append("(");
		// buffer.append(str).append(");\n");
		// }
		// buffer.append(" return ").append(lowerName).append("Dao.update(")
		// .append(lowerName);
		// buffer.append(");\n");
		// buffer.append(" }\n");
		// 删除
		buffer.append("	@Override\n");
		buffer.append("	public CommonResult").append(" delete(int id){\n");
		buffer.append("		CommonResult result = new CommonResult();\n");
		buffer.append("		boolean flag = this.").append(lowerName);
		buffer.append("Dao.delete(id);\n");
		buffer.append("		if(flag){\n");
		buffer.append("			result.setMessage(\"delete success!\");\n");
		buffer.append("			result.setSuccess(true);\n");
		buffer.append("		}else{\n");
		buffer.append("			result.setMessage(\"delete failed\");\n");
		buffer.append("		}\n");
		buffer.append("		return result;\n");
		buffer.append("	}\n");
		// 查询全部
		buffer.append("	@Override\n");
		buffer.append("	public List<").append(name).append("> getAll(){\n");
		buffer.append("		return ").append(lowerName).append("Dao.getAll();\n");
		buffer.append("	}\n");

		// 根据id查找
		buffer.append("	@Override\n");
		buffer.append("	public ").append(name).append(" getById(int id){\n");
		buffer.append("		return ").append(lowerName).append("Dao.getById(id);\n");
		buffer.append("	}\n");

		// 分页查询
		buffer.append("	@Override\n");
		buffer.append("	public PageInfo getPage(String keyword,int pageIndex,int pageSize){\n");
		buffer.append("		PageHelper.startPage(pageIndex,pageSize);\n");
		buffer.append("		Map<String,Object> map = new HashMap<>();\n");
		buffer.append("		map.put(\"id\",1);\n");
		buffer.append("		map.put(\"name\",\"%\"+keyword+\"%\");\n");
		buffer.append("		List<").append(name).append("> ").append("list = ");
		buffer.append(lowerName).append("Dao.").append("getPage(map);\n");
		buffer.append("		return new PageInfo(list);\n").append("	}\n");
		// 导出excel
		// buffer.append(" @Override\n");
		// buffer.append(" public InputStream exportExcel(String
		// sheetName){\n");
		// buffer.append(" List<").append(name).append("> ").append("list = ");
		// buffer.append("this.getAll();\n");
		// buffer.append(" ExcelUtil<").append(name)
		// .append("> util = new ExcelUtil<").append(name);
		// buffer.append(">(").append(name).append(".class);\n");
		// buffer.append(" return util.generateExcel(list, sheetName);\n");
		// buffer.append(" }\n");
		buffer.append("}");

		return buffer.toString();
	}

	/**
	 * 创建spring环境的service的测试代码
	 *
	 * @param clazz
	 * @return
	 */
	public static String createServiceTest(Class clazz) {
		String allName = clazz.getName();
		String prePackgeName = allName.substring(0, allName.lastIndexOf(".") - 5);
		String name = clazz.getName();
		String simpleName = clazz.getSimpleName();
		String lowerName = getFirstLowerName(clazz);
		StringBuffer buffer = new StringBuffer();
		buffer.append("package ").append(prePackgeName).append("service.serviceImpl;\n\n");
		buffer.append("import javax.annotation.Resource;\n\n");

		buffer.append("import org.junit.Test;\n");
		buffer.append("import org.junit.runner.RunWith;\n");
		buffer.append("import org.springframework.test.context.ContextConfiguration;\n");
		buffer.append("import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;\n");
		buffer.append("import ").append(name).append(";\n");
		buffer.append("import ").append(prePackgeName).append("service.").append(simpleName);
		buffer.append("Service;\n\n");
		// 注解
		buffer.append("@RunWith(SpringJUnit4ClassRunner.class)\n");
		buffer.append("@ContextConfiguration(locations = {\"classpath:spring-mybatis.xml\"})\n");
		buffer.append("public class ").append(simpleName).append("ServiceTest{\n\n");

		buffer.append("	@Resource\n");
		buffer.append("	private ").append(simpleName).append("Service ").append(lowerName).append("Service;\n\n");

		buffer.append("	@Test\n");
		buffer.append("	public void testSave(){\n\n");
		buffer.append("	}\n\n");
		buffer.append("	@Test\n");
		buffer.append("	public void testUpdate(){\n\n");
		buffer.append("	}\n\n");
		buffer.append("	@Test\n");
		buffer.append("	public void testDelete(){\n\n");
		buffer.append("	}\n\n");
		buffer.append("	@Test\n");
		buffer.append("	public void testFindAll(){\n\n");
		buffer.append("	}\n\n");
		buffer.append("}");
		return buffer.toString();
	}

	/**
	 * 创建springmvc controller代码
	 *
	 * @param clazz
	 * @return
	 */
	public static String createMVCController(Class clazz) {
		String allName = clazz.getName();
		String prePackgeName = allName.substring(0, allName.lastIndexOf(".") - 5);
		String name = clazz.getSimpleName();
		String lowerName = getFirstLowerName(clazz);

		StringBuilder buffer = new StringBuilder();
		buffer.append("package ").append(prePackgeName).append("controller;\n\n");

		buffer.append("import javax.annotation.Resource;\n");
		buffer.append("import javax.servlet.http.HttpServletRequest;\n\n");
		buffer.append("import org.springframework.stereotype.Controller;\n");
		buffer.append("import org.springframework.ui.Model;\n");
		buffer.append("import org.springframework.web.bind.annotation.RequestMethod;\n");
		buffer.append("import org.springframework.web.bind.annotation.ResponseBody;\n");
		buffer.append("import org.springframework.web.bind.annotation.RequestMapping;\n\n");

		buffer.append("import com.github.pagehelper.PageInfo;\n");
		buffer.append("import com.yizhi.common.util.NumberUtil;\n");
		buffer.append("import com.yizhi.common.util.model.CommonResult;\n");
		buffer.append("import com.yizhi.edu.base.controller.BaseController;\n");
		buffer.append("import ").append(clazz.getName()).append(";\n");
		buffer.append("import ").append(prePackgeName).append("service.").append(name).append("Service;\n\n");
		buffer.append("@Controller\n");
		buffer.append("@RequestMapping(\"").append("admin").append("\")\n");
		buffer.append("public class ").append(name).append("Controller extends BaseController{\n");
		buffer.append("	@Resource\n");
		buffer.append("	private ").append(name).append("Service ").append(lowerName).append("Service;\n\n");
		// build add
		buffer.append("	@ResponseBody\n");
		buffer.append("	@RequestMapping(value=\"/").append(StringUtil.firstToLowerCase(name));
		buffer.append("/add\",method = RequestMethod.POST)\n");
		buffer.append("	public CommonResult add(HttpServletRequest request){\n");
		buffer.append("		return null;\n");
		buffer.append("	}\n");
		// build update
		buffer.append("	@ResponseBody\n");
		buffer.append("	@RequestMapping(value=\"/").append(StringUtil.firstToLowerCase(name));
		buffer.append("/update\",method = RequestMethod.POST)\n");
		buffer.append("	public CommonResult update(HttpServletRequest request){\n");
		buffer.append("		return null;\n");
		buffer.append("	}\n");
		// build delete
		buffer.append("	@ResponseBody\n");
		buffer.append("	@RequestMapping(value=\"/").append(StringUtil.firstToLowerCase(name));
		buffer.append("/delete\",method = RequestMethod.POST)\n");
		buffer.append("	public CommonResult delete(HttpServletRequest request){\n");
		buffer.append("		int id = NumberUtil.getInteger(request.getParameter(\"id\"));\n");
		buffer.append("		return ").append(lowerName).append("Service.delete(id);\n");
		buffer.append("	}\n");

		// build grid
		buffer.append("	@ResponseBody\n");
		buffer.append("	@RequestMapping(value=\"/").append(StringUtil.firstToLowerCase(name));
		buffer.append("/grid\",method = RequestMethod.POST)\n");
		buffer.append("	public PageInfo grid(HttpServletRequest request){\n");
		buffer.append("		int pageIndex = NumberUtil.getInteger(request.getParameter(\"page\"));\n");
		buffer.append("		int pageSize = NumberUtil.getInteger(request.getParameter(\"rows\"));\n");
		buffer.append("		return null;\n");
		buffer.append("	}\n");

		buffer.append("}\n");

		return buffer.toString();
	}

	/**
	 * 创建mybaits mapper映射文件
	 *
	 * @param clazz
	 * @return
	 */
	public static String createMapper(Class clazz) {
		StringBuilder buffer = new StringBuilder();
		String name = clazz.getName();
		Field[] fields = clazz.getDeclaredFields();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
		buffer.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" "
				+ "\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");

		for (int i = 0; i < 2; i++) {
			int index = name.lastIndexOf(".");
			name = name.substring(0, index);
		}
		buffer.append("<mapper namespace=\"").append(name).append(".dao.");
		buffer.append(clazz.getSimpleName()).append("Dao").append("\">\n");
		// add cache
		buffer.append("<!--\n");
		buffer.append("<cache type=\"org.mybatis.caches.memcached.MemcachedCache\"/>\n");
		buffer.append("-->\n");
		buffer.append("	<resultMap id=\"").append(clazz.getSimpleName()).append("Result\"");
		buffer.append(" type=\"").append(clazz.getSimpleName()).append("\">\n");
		for (Field field : fields) {
			buffer.append("		<result property=\"").append(field.getName()).append("\" ");
			buffer.append("column=\"").append(StringUtil.camelToUnderline(field.getName())).append("\"").append("/>\n");
		}
		buffer.append("	</resultMap>\n");
		// 构建保存
		buffer.append("<!--保存-->\n");
		buffer.append("	<insert id=\"save\" ").append("parameterType=\"");
		buffer.append(clazz.getSimpleName()).append("\">\n");
		buffer.append("	<!-- <selectKey resultType=\"int\" keyProperty=\"id\" order=\"AFTER\">-->\n");
		buffer.append("		<!--	SELECT LAST_INSERT_ID()-->\n");
		buffer.append("	<!--	 </selectKey>-->\n");
		buffer.append("		").append(MyBatisSqlBuilder.insertSql(clazz)).append("\n");
		buffer.append("	</insert>\n");
		// 批量插入
		buffer.append("<!--批量插入-->\n");
		buffer.append("	<insert id=\"batchSave\" ").append("parameterType=\"java.util.List\"");
		buffer.append(">\n");
		buffer.append("		").append(MyBatisSqlBuilder.batchInsert(clazz));
		buffer.append("	</insert>\n");
		// 删除
		buffer.append("<!--删除-->\n");
		buffer.append("	<delete id=\"delete\" ").append("parameterType=\"");
		buffer.append(fields[0].getType().getName()).append("\">\n");
		buffer.append("		").append(MyBatisSqlBuilder.deleteSql(clazz)).append("\n");
		buffer.append("	</delete>\n");
		// 根据id查询
		buffer.append("	<select id=\"getById\" ").append("parameterType=\"");
		buffer.append(fields[0].getType().getName()).append("\" ").append("resultType=\"");
		buffer.append(clazz.getSimpleName()).append("\">\n");
		buffer.append("		").append(MyBatisSqlBuilder.findByIdSql(clazz)).append("\n");
		buffer.append("	</select>\n");

		// 根据id修改
		buffer.append("	<update id=\"update\" ").append("parameterType=\"");
		buffer.append(clazz.getSimpleName()).append("\">\n");
		buffer.append("		").append(MyBatisSqlBuilder.updateSql(clazz)).append("\n");
		buffer.append("	</update>\n");

		// 查询全部
		buffer.append("<!--查询全部-->\n");
		buffer.append("	<select id=\"queryAll\" ").append("resultMap=\"").append(clazz.getSimpleName());
		buffer.append("Result").append("\">\n");
		buffer.append("		").append(MyBatisSqlBuilder.findAllSql(clazz)).append("\n");
		buffer.append("	</select>\n");
		// 根据参数模糊查询
		buffer.append("<!--分页查询-->");
		buffer.append("	<select id=\"getPage\" ").append("resultMap=\"").append(clazz.getSimpleName());
		buffer.append("Result").append("\" ").append("parameterType=\"");
		buffer.append("hashmap").append("\">\n");
		buffer.append("		").append(MyBatisSqlBuilder.findAllSql(clazz)).append("\n");
		buffer.append("		<if test=\"name !=null and name !=''\">\n");
		buffer.append("			where name LIKE '%${name}%'\n");
		buffer.append("		</if>\n");
		buffer.append("	</select>\n");
		// 获取总记录
		// buffer.append(" <select id=\"getTotalRecord\" ")
		// .append("resultType=\"").append(fields[0].getType().getName());
		// buffer.append("\">\n");
		// buffer.append(" ").append(SqlBuilder.buildTotalRecordSql(clazz,
		// null))
		// .append("\n");
		// buffer.append(" </select>\n");
		buffer.append("</mapper>");
		return buffer.toString();
	}

	public static String createMapperWithAssociation(Class clazz) {
		StringBuilder buffer = new StringBuilder();
		String name = clazz.getName();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
		buffer.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" "
				+ "\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
		for (int i = 0; i < 2; i++) {
			int index = name.lastIndexOf(".");
			name = name.substring(0, index);
		}
		buffer.append("<mapper namespace=\"").append(name).append(".dao.");
		buffer.append(clazz.getSimpleName()).append("Dao").append("\">\n");
		buffer.append("<!--\n");
		buffer.append("<cache type=\"org.mybatis.caches.memcached.MemcachedCache\"/>\n");
		buffer.append("-->\n");
		buffer.append("	<resultMap type=\"").append(clazz.getSimpleName()).append("\" ");
		buffer.append("id=\"").append(clazz.getSimpleName()).append("Result\">\n");
		Map<String, StringBuilder> map = new HashMap<>();
		try {
			Field[] fields = clazz.getDeclaredFields();

			for (Field field : fields) {
				if (field.getType().getName().contains("com.yizhi") == false
						&& field.getType().getName().equals("java.util.List") == false) {
					buffer.append("		<result property=\"").append(field.getName()).append("\" ");
					buffer.append("column=\"").append(StringUtil.camelToUnderline(field.getName())).append("\"")
							.append("/>\n");
				} else if (field.getType().getName().contains("com.yizhi")) {
					buffer.append("        <association property=\"").append(field.getName()).append("\"");
					buffer.append(" resultMap=\"").append(field.getType().getSimpleName()).append("Result\"/>\n");
					if (map.get(field.getType().getSimpleName()) == null) {
						map.put(field.getType().getSimpleName(), new StringBuilder());
					}
					Field[] fields1 = field.getType().getDeclaredFields();
					StringBuilder buffer1 = map.get(field.getType().getSimpleName());
					buffer1.append("	<resultMap type=\"").append(field.getType().getSimpleName()).append("\" ");
					buffer1.append("id=\"").append(field.getType().getSimpleName()).append("Result\">\n");
					for (Field field1 : fields1) {
						buffer1.append("		<result property=\"").append(field1.getName()).append("\" ");
						buffer1.append("column=\"").append(StringUtil.camelToUnderline(field1.getName())).append("\"")
								.append("/>\n");
					}
					buffer1.append("	</resultMap>\n");
				} else if (field.getType().getName().equals("java.util.List")) {
					System.out.println("invoked me");
					Type fc = field.getGenericType();
					if (map.get(field.getType().getSimpleName()) == null) {
						map.put(field.getType().getSimpleName(), new StringBuilder());
					}
					if (fc == null)
						continue;
					if (fc instanceof ParameterizedType) {
						ParameterizedType pt = (ParameterizedType) fc;
						Class genericClazz = (Class) pt.getActualTypeArguments()[0];
						buffer.append("        <collection property=\"").append(field.getName()).append("\"");
						buffer.append(" resultMap=\"").append(genericClazz.getSimpleName()).append("Result\"/>\n");
						Field[] fields1 = genericClazz.getDeclaredFields();
						StringBuilder buffer1 = map.get(field.getType().getSimpleName());
						buffer1.append("	<resultMap type=\"").append(genericClazz.getSimpleName()).append("\" ");
						buffer1.append("id=\"").append(genericClazz.getSimpleName()).append("Result\">\n");
						for (Field field1 : fields1) {
							buffer1.append("		<result property=\"").append(field1.getName()).append("\" ");
							buffer1.append("column=\"").append(StringUtil.camelToUnderline(field1.getName()))
									.append("\"").append("/>\n");
						}
						buffer1.append("	</resultMap>\n");
					}
				}
			}
			buffer.append("	</resultMap>\n");
			for (Map.Entry<String, StringBuilder> entry : map.entrySet()) {
				buffer.append(entry.getValue().toString());
				// System.out.println(entry.getValue().toString());
			}
			// 构建保存
			buffer.append("	<insert id=\"save\" ").append("parameterType=\"");
			buffer.append(clazz.getSimpleName()).append("\">\n");
			buffer.append("		").append(MyBatisSqlBuilder.insertSql(clazz)).append("\n");
			buffer.append("	</insert>\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	/**
	 * 构建spring与mybatis整合的配置文件
	 *
	 * @param basePackageName
	 * @return
	 */
	public static String createSpringCfgForMyBatis(String basePackageName) {

		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		buffer.append("<beans xmlns=\"http://www.springframework.org/schema/beans\"\n");
		buffer.append("	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n");
		buffer.append("	xmlns:p=\"http://www.springframework.org/schema/p\"\n");
		buffer.append("	xmlns:context=\"http://www.springframework.org/schema/context\"\n");
		buffer.append("	xmlns:mvc=\"http://www.springframework.org/schema/mvc\"\n");
		buffer.append("	xsi:schemaLocation=\"http://www.springframework.org/schema/beans \n");
		buffer.append("						http://www.springframework.org/schema/beans/spring-beans-3.1.xsd \n");
		buffer.append("						http://www.springframework.org/schema/context \n");
		buffer.append("						http://www.springframework.org/schema/context/spring-context-3.1.xsd \n");
		buffer.append("						http://www.springframework.org/schema/mvc \n");
		buffer.append("						http://www.springframework.org/schema/mvc/spring-mvc-4.0.xsd\">\n");
		buffer.append("	<!-- 自动扫描 -->\n");
		buffer.append("	<context:component-scan base-package=\"").append(basePackageName).append("\" />\n");
		buffer.append("	<!-- 引入配置文件 -->\n");
		buffer.append("	<bean id=\"propertyConfigurer\"\n");
		buffer.append("		class=\"org.springframework.beans.factory.config.PropertyPlaceholderConfigurer\">\n");
		buffer.append("		<property name=\"location\" value=\"classpath:jdbc.properties\" />\n");
		buffer.append("	</bean>\n\n");
		buffer.append("	<!-- spring和MyBatis完美整合，不需要mybatis的配置映射文件 -->\n");
		buffer.append("	<bean id=\"sqlSessionFactory\" class=\"org.mybatis.spring.SqlSessionFactoryBean\">\n");
		buffer.append("		<property name=\"dataSource\" ref=\"dataSource\" />\n");
		buffer.append("		<property name=\"typeAliasesPackage\" value=\"");
		buffer.append(basePackageName).append(".model").append("\" />\n");
		buffer.append("		<!-- 自动扫描mapping.xml文件 -->\n");
		buffer.append("		<property name=\"mapperLocations\" value=\"classpath:");
		buffer.append(basePackageName.replace(".", "/")).append("/mapping/*.xml\"/>\n");
		buffer.append("		<property name=\"configLocation\" value=\"classpath:mybatis-config.xml\" />\n");
		buffer.append("	</bean>\n\n");
		buffer.append("	<!-- DAO接口所在包名，Spring会自动查找其下的类 -->\n");
		buffer.append("	<bean class=\"org.mybatis.spring.mapper.MapperScannerConfigurer\">\n");
		buffer.append("		<property name=\"basePackage\" value=\"");
		buffer.append(basePackageName).append(".dao\" />\n");
		buffer.append("		<property name=\"sqlSessionFactoryBeanName\" value=\"sqlSessionFactory\"/>\n");
		buffer.append("	</bean>\n\n");
		buffer.append("	<!-- transaction manager, use JtaTransactionManager for global tx -->\n");
		buffer.append("	<bean id=\"transactionManager\"\n");
		buffer.append("		class=\"org.springframework.jdbc.datasource.DataSourceTransactionManager\">\n");
		buffer.append("		<property name=\"dataSource\" ref=\"dataSource\" />\n");
		buffer.append("	</bean>\n");
		buffer.append("</beans>\n");
		return buffer.toString();
	}

	public static String createTable(Class clazz) {
		StringBuffer buffer = new StringBuffer();
		String tableName = getTableName(clazz);
		String description = getTableDesc(clazz);
		Field[] fields = clazz.getDeclaredFields();
		buffer.append("drop table if exists ").append(tableName).append(";\n");
		buffer.append("/*==============================================================*/\n");
		buffer.append("/*  Table: ").append(tableName).append("  ").append(description);
		buffer.append("  */\n");
		buffer.append("/*==============================================================*/\n");
		buffer.append("create table ").append(tableName);
		buffer.append("(").append("\n");
		if (fields[0].getType().getName() == "java.lang.Integer" || fields[0].getType().getName() == "int") {
			buffer.append("  ").append(fields[0].getName()).append(" int");
		} else if (fields[0].getType().getName() == "java.lang.Long" || fields[0].getType().getName() == "long") {
			buffer.append("  ").append(fields[0].getName()).append(" bigint");
		} else {
			throw new RuntimeException(clazz + ":First field type is " + fields[0].getType().getName()
					+ ",please set you first field type to int or long as a primary key");
		}
		buffer.append(" auto_increment,\n");
		for (int i = 1; i < fields.length; i++) {
			if (fields[i].getType().getName() == "java.lang.String") {
				buffer.append("  ").append(StringUtil.camelToUnderline(fields[i].getName())).append(" ");
				buffer.append("varchar(100) not null,").append("\n");
			} else if (fields[i].getType().getName() == "boolean") {
				buffer.append("  ").append(StringUtil.camelToUnderline(fields[i].getName())).append(" ");
				buffer.append("tinyint(1) not null default 1,").append("\n");
			} else if (fields[i].getType().getName() == "java.sql.Date") {
				buffer.append("  ").append(StringUtil.camelToUnderline(fields[i].getName())).append(" ");
				buffer.append(" date not null,\n");
			} else if (fields[i].getType().getName() == "java.lang.Double"
					|| fields[i].getType().getName() == "double") {
				buffer.append("  ").append(StringUtil.camelToUnderline(fields[i].getName())).append(" ");
				buffer.append(" double not null,\n");
			} else if (fields[i].getType().getName() == "java.lang.Long" || fields[i].getType().getName() == "long") {
				buffer.append("  ").append(StringUtil.camelToUnderline(fields[i].getName())).append(" ");
				buffer.append(" bigint not null,\n");
			} else if (fields[i].getType().getName() == "java.lang.Integer" || fields[i].getType().getName() == "int") {
				buffer.append("  ").append(StringUtil.camelToUnderline(fields[i].getName())).append(" ");
				buffer.append(" int not null default '0',\n");
			} else if (fields[i].getType().getName() == "java.sql.Timestamp"
					|| fields[i].getType().getName() == "Timestamp") {
				buffer.append("  ").append(StringUtil.camelToUnderline(fields[i].getName())).append(" ");
				buffer.append(" TIMESTAMP(14) not null,\n");
			}
		}
		buffer.append("  primary key (").append(fields[0].getName()).append(")\n");
		buffer.append(")").append("ENGINE=InnoDB DEFAULT CHARSET=utf8;\n\n");
		return buffer.toString();
	}

	/**
	 * create controller test
	 *
	 * @param clazz
	 * @return
	 */
	public static String createControllerTest(Class clazz) {
		String allName = clazz.getName();
		String prePackageName = allName.substring(0, allName.lastIndexOf(".") - 5);
		String simpleName = clazz.getSimpleName();
		String lowerName = getFirstLowerName(clazz);
		StringBuilder builder = new StringBuilder();
		builder.append("package ").append(prePackageName).append("controller;\n\n");
		// import requires class
		builder.append("import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;\n");
		builder.append("import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;\n");
		builder.append("import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;\n");
		builder.append("import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;\n");
		builder.append(
				"import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;\n\n");
		builder.append("import org.junit.Before;\n");
		builder.append("import org.junit.Test;\n");
		builder.append("import org.junit.runner.RunWith;\n");
		builder.append("import org.springframework.beans.factory.annotation.Autowired;\n");
		builder.append("import org.springframework.http.MediaType;\n");
		builder.append("import org.springframework.test.context.ContextConfiguration;\n");
		builder.append("import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;\n");
		builder.append("import org.springframework.test.context.web.WebAppConfiguration;\n");

		builder.append("import org.springframework.test.web.servlet.MockMvc;\n");
		builder.append("import org.springframework.test.web.servlet.MvcResult;\n");
		builder.append("import org.springframework.web.context.WebApplicationContext;\n\n");
		// annotation
		builder.append("@RunWith(SpringJUnit4ClassRunner.class)\n");
		builder.append("@ContextConfiguration({\"classpath:spring-mybatis.xml\", \"classpath:spring-mvc.xml\"})\n");
		builder.append("@WebAppConfiguration\n");
		builder.append("public class ").append(simpleName).append("ControllerTest{\n\n");

		builder.append("	@Autowired\n");
		builder.append("	private WebApplicationContext wac;\n\n");
		builder.append("	private MockMvc mockMvc;\n\n");

		builder.append("	@Before\n");
		builder.append("	public void setup() {\n");
		builder.append("		this.mockMvc = webAppContextSetup(this.wac).build();\n");
		builder.append("	}\n\n");
		// test add function
		builder.append("	@Test\n");
		builder.append("	public void testAdd() throws Exception {\n");
		builder.append("		MvcResult result = mockMvc.perform(post(\"/");
		builder.append(lowerName).append("/add\")\n");
		builder.append(" 			.contentType(MediaType.APPLICATION_JSON)\n");
		builder.append(" 			.param(\"param1\", \"pm1\")\n");
		builder.append(" 		).andExpect(status().isOk()).andDo(print()).andReturn();\n");
		builder.append("		System.out.println(\"result:\"+result.getResponse().getContentAsString());\n");
		builder.append("	}\n\n");
		// get by id
		builder.append("	@Test\n");
		builder.append("	public void testGetById() throws Exception {\n");
		builder.append("		MvcResult result = mockMvc.perform(post(\"/");
		builder.append(lowerName).append("/get/{id}\", 1)\n");
		builder.append(" 			.contentType(MediaType.APPLICATION_JSON)\n");
		builder.append(" 			.param(\"param1\", \"pm1\")\n");
		builder.append(" 		).andExpect(status().isOk()).andDo(print()).andReturn();\n");
		builder.append("		System.out.println(\"result:\"+result.getResponse().getContentAsString());\n");
		builder.append("	}\n\n");
		// test update function
		builder.append("	@Test\n");
		builder.append("	public void testUpdate() throws Exception {\n\n");
		builder.append("	}\n\n");
		// test delete function
		builder.append("	@Test\n");
		builder.append("	public void testDelete() throws Exception {\n\n");
		builder.append("	}\n");
		builder.append("}\n");

		return builder.toString();
	}

	@SuppressWarnings("unchecked")
	public static String getTableName(Class clazz) {
		String str = null;
		Table annotation = (Table) clazz.getAnnotation(Table.class);
		if (annotation != null) {
			str = annotation.name();
		} else {
			str = "t" + StringUtil.camelToUnderline(clazz.getSimpleName());
			// throw new RuntimeException(clazz + EXCEPTION_MSG);
		}
		return str;
	}

	@SuppressWarnings("unchecked")
	public static String getTableDesc(Class clazz) {
		String str = null;
		Table annotation = (Table) clazz.getAnnotation(Table.class);
		if (annotation != null) {
			str = annotation.desc();
		} else {
			str = "";
			// throw new RuntimeException(clazz + EXCEPTION_MSG);
		}
		return str;
	}

	public static String getFirstLowerName(Class clazz) {
		String name = clazz.getSimpleName();
		StringBuffer buffer = new StringBuffer();
		buffer.append(name.substring(0, 1).toLowerCase());
		buffer.append(name.substring(1));
		return buffer.toString();
	}

	public static String getFirstUpperName(String str) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(str.substring(0, 1).toUpperCase());
		buffer.append(str.substring(1));
		return buffer.toString();
	}
}
