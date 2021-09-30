package com.deepoove.poi.util;

import com.deepoove.poi.data.CellRenderData;
import com.deepoove.poi.data.ParagraphRenderData;
import com.deepoove.poi.data.style.CellStyle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @description: Word工具类
 * @author: Weijie.Wu
 * @create: 2021-09-30 09:26
 */
public class WordUtil {

	public static CellRenderData convert2Cell(Integer content, CellStyle cellStyle) {
		return convert2Cell(String.valueOf(content), cellStyle);
	}

	public static CellRenderData convert2Cell(String content, CellStyle cellStyle) {
		return convert2Cell(Collections.singletonList(content), cellStyle);
	}

	public static CellRenderData convert2Cell(List<String> contents, CellStyle cellStyle) {
		CellRenderData cellRenderData = new CellRenderData();

		List<ParagraphRenderData> paragraphs = new ArrayList<>();
		for (String content : contents) {
			ParagraphRenderData paragraphRenderData = new ParagraphRenderData();
			paragraphRenderData.addText(content);
			paragraphs.add(paragraphRenderData);
		}

		cellRenderData.setCellStyle(cellStyle);
		cellRenderData.setParagraphs(paragraphs);
		return cellRenderData;
	}
}
