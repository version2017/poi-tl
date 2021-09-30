package com.deepoove.poi.policy;

import com.deepoove.poi.data.mytest.DynamicColumnTableRenderData;
import com.deepoove.poi.data.mytest.DynamicColumnTableRowData;
import com.deepoove.poi.util.CollectionTools;
import com.deepoove.poi.util.TableTools;
import com.deepoove.poi.util.WordUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description: 表格-动态列数（指定对象中的某一个字段集合为列名）
 * @author: Weijie.Wu
 * @create: 2021-09-30 07:02
 */
public class DynamicColumnTableRenderPolicy extends DynamicTableRenderPolicy{

	private static final String DEFAULT_EMPTY_VAL = "/";

	public DynamicColumnTableRenderPolicy() {
	}

	@Override
	public void render(XWPFTable table, Object data) throws Exception {
		if (null == data) {
			System.out.println("数据为空");
			return;
		}

		DynamicColumnTableRenderData renderData = (DynamicColumnTableRenderData) data;
		Set<String> colNames = getColNames(renderData.getTableData(), renderData.colNameGetter);
		// 后面操作中存在对list顺序的调整，故分开保存以保留原顺序
		List<String> listedColNames = colNames.stream().collect(Collectors.toList());
		List<String> listedColNames2 = colNames.stream().collect(Collectors.toList());
		// 填充表头
		fillTableHeader(table, listedColNames, renderData);
		// 填充表格内容
		Map<String, List<DynamicColumnTableRowData>> objectMap = getObjectMap(renderData.getTableData(),
			renderData.objNameGetter);
		fillContent(table, renderData, listedColNames2, objectMap);

		// 删除最后一行空白行
		table.removeRow(table.getRows().size() - 2);
	}

	private Set<String> getColNames(List<DynamicColumnTableRowData> tableData, Function<DynamicColumnTableRowData,
									String> colNameGetter) {
		if (null == tableData) {
			return new HashSet<>();
		}
		return tableData.stream().map(colNameGetter).collect(Collectors.toSet());
	}

	private void fillTableHeader(XWPFTable table, List<String> colNames, DynamicColumnTableRenderData renderData) {
		if (CollectionUtils.isEmpty(colNames)) {
			return;
		}
		CollectionTools.reverseCollection(String.class, colNames);
		XWPFTableRow header = table.getRow(0);

		int modelWidth = table.getRow(renderData.getStaticHeadRow()).getCell(renderData.getStaticHeadCol()).getWidth();
		header.getCell(renderData.getStaticHeadCol()).setWidthType(TableWidthType.AUTO);

		// 清理模板内的数据：{{xxx}}
		table.removeRow(renderData.getStaticHeadRow() + 1);
		XWPFTableRow newTableRow = table.insertNewTableRow(renderData.getStaticHeadRow());
		XWPFTableCell headerCell = header.getCell(renderData.getStaticHeadCol());

		for (int i = 0; i < renderData.getStaticHeadCol(); i++) {
			newTableRow.createCell();
			TableTools.mergeCellsVertically(table, i, 0, renderData.getStaticHeadRow());
		}

		CTTblGrid tblGrid = TableTools.getTblGrid(table);
		// 填充动态的二级列名
		for (String colName : colNames) {
			CTTblGridCol newGridCol = tblGrid.insertNewGridCol(renderData.getStaticHeadCol());
			newGridCol.setW(BigInteger.valueOf(modelWidth / colNames.size()));

			CTRow row = newTableRow.getCtRow();
			CTTc newTc = row.insertNewTc(renderData.getStaticHeadCol());
			XWPFTableCell cell = new XWPFTableCell(newTc, newTableRow, newTableRow.getTable().getBody());
			printHeader(headerCell, colName, cell);
		}

	}

	/**
	 * @Description: 设置表头样式
	 * @Param: [headerCell, colName, cell]
	 * @Return: void
	 */
	private void printHeader(XWPFTableCell headerCell, String colName, XWPFTableCell cell) {
		XWPFRun modelRun = headerCell.getParagraphArray(0).getRuns().get(0);
		cell.removeParagraph(0);
		XWPFParagraph paragraph = cell.addParagraph();
		paragraph.removeRun(0);
		XWPFRun run = paragraph.createRun();
		run.setFontSize(modelRun.getFontSize());
		run.setBold(modelRun.isBold());
		run.setText(colName);
		paragraph.addRun(run);
		cell.setColor(headerCell.getColor());
	}

	private void fillContent(XWPFTable table, DynamicColumnTableRenderData renderData, List<String> colNames,
							 Map<String, List<DynamicColumnTableRowData>> objectMap) throws Exception {
		if (null == renderData || CollectionUtils.isEmpty(renderData.getTableData())) {
			return;
		}

		int startInsertRowPos = renderData.getStaticHeadRow() + 1;

		Set<Map.Entry<String, List<DynamicColumnTableRowData>>> objEntries = objectMap.entrySet();
		int objIdx = 1;
		for (Map.Entry<String, List<DynamicColumnTableRowData>> entry : objEntries) {
			XWPFTableRow row = table.insertNewTableRow(startInsertRowPos);
			List<DynamicColumnTableRowData> objDatas = entry.getValue();

			// 填充序号和静态列的值
			List<String> staticVals = new ArrayList<>();
			renderData.staticColValGetters.forEach(getter -> {
				Object val = getter.apply(objDatas.get(0));
				if (null == val) {
					staticVals.add(null);
					return;
				}
				staticVals.add(val.toString());
			});
			fillStaticColValues(row, objIdx, staticVals);

			// 填充动态列内容
			fillDynamicColValues(row, renderData, objDatas, colNames);

			objIdx++;
			startInsertRowPos++;
		}
	}

	private Map<String, List<DynamicColumnTableRowData>> getObjectMap(List<DynamicColumnTableRowData> tableData,
																	  Function<DynamicColumnTableRowData, String> objectNameGetter) {
		if (CollectionUtils.isEmpty(tableData)) {
			return new HashMap<>();
		}
		Map<String, List<DynamicColumnTableRowData>> objectMap = new HashMap<>();
		tableData.forEach(rowData -> {
			String objectName = objectNameGetter.apply(rowData);
			List<DynamicColumnTableRowData> objRows = objectMap.get(objectName);
			if (null == objRows) {
				objRows = new ArrayList<>();
				objectMap.put(objectName, objRows);
			}
			objRows.add(rowData);
		});
		return objectMap;
	}

	private void fillStaticColValues(XWPFTableRow row, int objectNumber, List<String> staticColValues) throws Exception {
		// 序号
		XWPFTableCell numberCell = row.createCell();
		TableRenderPolicy.Helper.renderCell(numberCell, WordUtil.convert2Cell(objectNumber, null), null);

		for (String val : staticColValues) {
			XWPFTableCell objectCell = row.createCell();
			TableRenderPolicy.Helper.renderCell(objectCell, WordUtil.convert2Cell(val, null), null);
		}
	}

	private void fillDynamicColValues(XWPFTableRow row, DynamicColumnTableRenderData renderData,
									  List<DynamicColumnTableRowData> datas, List<String> colNames) throws Exception {
		for (int i = 0; i < colNames.size(); i++) {
			String colName = colNames.get(i);
			// "/"为默认空值
			String colVal = DEFAULT_EMPTY_VAL;
			for (DynamicColumnTableRowData data:datas) {
				if (colName.equals(renderData.colNameGetter.apply(data))) {
					colVal = renderData.valNameGetter.apply(data);
				}
			}
			TableRenderPolicy.Helper.renderCell(row.createCell(), WordUtil.convert2Cell(colVal, null), null);
		}
	}

}
