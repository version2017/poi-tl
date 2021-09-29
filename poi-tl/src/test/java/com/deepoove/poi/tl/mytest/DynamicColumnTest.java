package com.deepoove.poi.tl.mytest;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.tl.source.XWPFTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 表格-动态渲染列
 * @Author: Weijie.Wu
 * @Date: 2021/9/29
 */
@DisplayName("Configure test case")
public class DynamicColumnTest {

	String resource = "src/test/resources/template/mytest/dynamic_column.docx";
	ConfigureBuilder builder;

	@BeforeEach
	public void init() {
		builder = Configure.builder();
	}

	@Test
	public void testDynamicColumnTable() throws Exception {
		Map<String, Object> datas = new HashMap<String, Object>() {
			{
				put("title", "Hello, poi tl.");
				put("text", new PictureRenderData(100, 120, "src/test/resources/logo.png"));
				put("word", "虽然我是百分号开头，但是我也被自定义成文本了");
			}
		};

		XWPFTemplate template = XWPFTemplate.compile(resource, builder.build());

		template.render(datas);
		template.write(new FileOutputStream("C:\\Users\\mayn\\Desktop\\Test\\test.docx"));

		XWPFTemplate renew = XWPFTestSupport.readNewTemplate(template);
//		assertEquals(renew.getElementTemplates().size(), 0);
		renew.close();
	}

}
