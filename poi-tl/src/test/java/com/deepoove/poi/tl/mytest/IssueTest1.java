package com.deepoove.poi.tl.mytest;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.tl.source.XWPFTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

@DisplayName("Configure test case")
public class IssueTest1 {

    /**
     * [[title]]
     * 
     * [[text]]
     * 
     * [[%word]]
     * 
     * [[姓名]]
     */
    String resource = "src/test/resources/template/mytest/issue_test.docx";
    ConfigureBuilder builder;

    @BeforeEach
    public void init() {
        builder = Configure.builder();
    }

    /**
     * @Description: 用于调试github的issue
	 * 	Issue链接：https://github.com/Sayi/poi-tl/issues/673
     * @Param: []
     * @Return: void
     * @Author: Weijie.Wu
     * @Date: 2021/11/30
     */
	@Test
	public void testNestedBlock() throws Exception {
		class TestObj{
			String name;
			public TestObj(String name) {
				this.name = name;
			}
		}
		class WrapperObj{
			String title = "Hello, poi tl.";
			List<TestObj> testObjs = Arrays.asList(new TestObj("***第一项数据***"), new TestObj("***第二项数据***"),
				new TestObj("***第三项数据***"));
		}
		WrapperObj data = new WrapperObj();

		XWPFTemplate template = XWPFTemplate.compile(resource, builder.build());

		template.render(data);
		template.write(new FileOutputStream("C:\\Users\\roger.wu\\Desktop\\Test\\test.docx"));

		XWPFTemplate renew = XWPFTestSupport.readNewTemplate(template);
//		assertEquals(renew.getElementTemplates().size(), 0);
		renew.close();

	}

}
