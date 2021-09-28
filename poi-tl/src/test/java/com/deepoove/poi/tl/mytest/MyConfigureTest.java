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
import java.util.List;
import java.util.Map;

@DisplayName("Configure test case")
public class MyConfigureTest {

    /**
     * [[title]]
     * 
     * [[text]]
     * 
     * [[%word]]
     * 
     * [[姓名]]
     */
    String resource = "src/test/resources/template/my_config_test.docx";
    ConfigureBuilder builder;

    @BeforeEach
    public void init() {
        builder = Configure.builder();
    }

	@Test
	public void testNestedBlock() throws Exception {
		class TestObj{
			String name;
			List<Object> arr;
			public TestObj(String name, List<Object> arr) {
				this.name = name;
				this.arr = arr;
			}
		}
		Map<String, Object> datas = new HashMap<String, Object>() {
			{
				put("title", "Hello, poi tl.");
				put("text", new PictureRenderData(100, 120, "src/test/resources/logo.png"));
				put("word", "虽然我是百分号开头，但是我也被自定义成文本了");
				put("testObj", new TestObj("xxx", Arrays.asList()));
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
