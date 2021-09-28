package com.deepoove.poi.tl.mytest;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.data.*;
import com.deepoove.poi.data.style.BorderStyle;
import lombok.Data;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;

/**
 * @Description: 测试表格渲染
 * @Author: Weijie.Wu
 * @Date: 2021/9/28
 */
@DisplayName("Configure test case")
public class MyTableTest {

    /**
     * [[title]]
     * 
     * [[text]]
     * 
     * [[%word]]
     * 
     * [[姓名]]
     */
    String resource = "src/test/resources/template/mytest/my_table_merge_test.docx";
    ConfigureBuilder builder;

    @Data
	private class DocData {
		private String title;
		private String date;
		private TableRenderData renderData;
	}

	@BeforeEach
    public void init() {
        builder = Configure.builder();
	}

	@Test
	public void testTableMerge() throws Exception {
		DocData docData = new DocData();
		TableRenderData renderData = new TableRenderData();
		//设置的合并规则
		MergeCellRule.MergeCellRuleBuilder mergeCellRuleBuilder = MergeCellRule.builder();
		//设置表头
		RowRenderData header = Rows.of("姓名", "特长", "是否获奖").bgColor("D9D9D9").center()
			.textColor("000000").textFontFamily("宋体（中文正文）").textFontSize(10).textBold().create();
		//表体内容
		RowRenderData row = Rows.of("小红", "游泳", "否").textFontFamily("宋体（中文正文）").textFontSize(10).center().create();
		RowRenderData row1 = Rows.of("小红", "钢琴", "八级").textFontFamily("宋体（中文正文）").textFontSize(10).center().create();
		RowRenderData row2 = Rows.of("小红", "围棋", "否").textFontFamily("宋体（中文正文）").textFontSize(10).center().create();
		RowRenderData row3 = Rows.of("小明", "游泳", "全国一等奖").textFontFamily("宋体（中文正文）").textFontSize(10).center().create();
		RowRenderData row4 = Rows.of("小明", "击剑", "无").textFontFamily("宋体（中文正文）").textFontSize(10).center().create();
		RowRenderData row5 = Rows.of("小梅", "围棋", "无").textFontFamily("宋体（中文正文）").textFontSize(10).center().create();
		RowRenderData row6 = Rows.of("小梅", "跆拳道", "黑带").textFontFamily("宋体（中文正文）").textFontSize(10).center().create();
		RowRenderData row7 = Rows.of("小梅", "长跑", "国家一级运动员").textFontFamily("宋体（中文正文）").textFontSize(10).center().create();
		RowRenderData row8 = Rows.of("小梅", "举重", "无").textFontFamily("宋体（中文正文）").textFontSize(10).center().create();
		RowRenderData row9 = Rows.of("小刚", "脸特长", "").textFontFamily("宋体（中文正文）").textFontSize(10).center().create();

		//设置样式颜色尺寸等等
		BorderStyle borderStyle = new BorderStyle();
		borderStyle.setColor("000000");
		borderStyle.setSize(4);
		borderStyle.setType(XWPFTable.XWPFBorderType.SINGLE);
		TableRenderData tableRenderData = Tables.ofA4MediumWidth().addRow(header)
			.addRow(row).addRow(row1).addRow(row2).addRow(row3).addRow(row4)
			.addRow(row5).addRow(row6).addRow(row7).addRow(row8).addRow(row9)
			.border(borderStyle)
			.center()
			.create();
		/**
		 * 设置表格合并规则
		 * 1.起始行 MergeCellRule.Grid.of(i, j) i: 行 j: 列
		 * 2.结束行 MergeCellRule.Grid.of(i, j) i: 行 j: 列
		 */
		mergeCellRuleBuilder.map(MergeCellRule.Grid.of(0, 0), MergeCellRule.Grid.of(0, 1));//表头合并
		mergeCellRuleBuilder.map(MergeCellRule.Grid.of(1, 0), MergeCellRule.Grid.of(3, 0));//小红合并
		mergeCellRuleBuilder.map(MergeCellRule.Grid.of(4, 0), MergeCellRule.Grid.of(5, 0));//小明合并
		mergeCellRuleBuilder.map(MergeCellRule.Grid.of(6, 0), MergeCellRule.Grid.of(9, 0));//小梅合并

		/**
		 * MergeCellRule支持多合并规则,会以Map的形式存入可以看一下源码
		 * !!! 一定要设置完规则后再调用 MergeCellRule的build方法进行构建
		 */
		tableRenderData.setMergeRule(mergeCellRuleBuilder.build());

		docData.setTitle("测试测试测试");
		docData.setDate("2021-09-28 09:58:00");
		docData.setRenderData(tableRenderData);

		XWPFTemplate template = XWPFTemplate.compile(resource, builder.build());
		template.render(docData);
		template.write(new FileOutputStream("C:\\Users\\roger.wu\\Desktop\\Test\\test.docx"));
	}

}
