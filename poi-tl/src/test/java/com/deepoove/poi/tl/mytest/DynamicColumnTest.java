package com.deepoove.poi.tl.mytest;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.mytest.DynamicColumnTableRenderData;
import com.deepoove.poi.data.mytest.DynamicColumnTableRowData;
import com.deepoove.poi.policy.DynamicColumnTableRenderPolicy;
import com.deepoove.poi.tl.source.XWPFTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
		builder.bind("dynamic", new DynamicColumnTableRenderPolicy());
	}

	@Test
	public void testDynamicColumnTable() throws Exception {
		List<DynamicColumnTableRowData> tableData = new ArrayList<DynamicColumnTableRowData>(){{
			add(new DynamicColumnTableRowData(1, "923", "身份鉴别", "3"));
			add(new DynamicColumnTableRowData(2, "923", "电子门禁", "2"));
			add(new DynamicColumnTableRowData(3, "923", "视频监控", "1"));
			add(new DynamicColumnTableRowData(4, "926", "身份鉴别", "1"));
			add(new DynamicColumnTableRowData(5, "926", "电子门禁", "2"));
			add(new DynamicColumnTableRowData(6, "926", "视频监控", "3"));
		}};
		DynamicColumnTableRenderData dynamicColumnTableRenderData = new DynamicColumnTableRenderData(tableData);

		Map<String, Object> datas = new HashMap<String, Object>() {
			{
				put("title", "Hello, poi tl.");
				put("text", new PictureRenderData(100, 120, "src/test/resources/logo.png"));
				put("word", "虽然我是百分号开头，但是我也被自定义成文本了");
				put("dynamic", dynamicColumnTableRenderData);
			}
		};

		XWPFTemplate template = XWPFTemplate.compile(resource, builder.build());

		template.render(datas);
		template.write(new FileOutputStream("C:\\Users\\roger.wu\\Desktop\\Test\\test.docx"));

		XWPFTemplate renew = XWPFTestSupport.readNewTemplate(template);
//		assertEquals(renew.getElementTemplates().size(), 0);
		renew.close();
	}

}
