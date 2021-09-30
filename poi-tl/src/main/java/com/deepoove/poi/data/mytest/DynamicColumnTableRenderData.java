package com.deepoove.poi.data.mytest;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @description: 动态列表格渲染数据
 * @author: Weijie.Wu
 * @create: 2021-09-30 08:13
 */
@Data
public class DynamicColumnTableRenderData {

	/**
	 * @Description: 动态列名获取字段
	 * @Detail: 从 {@link #tableData} 中获取有哪些列，将指定字段的值全部取出后去重即为动态列名
	 */
	public final Function<DynamicColumnTableRowData, String> colNameGetter = DynamicColumnTableRowData::getCheckType;
	/**
	 * @Description: 对象名获取字段
	 * @Detail: 从 {@link #tableData} 中获取有哪些对象，取出后去重，会转为 Map<String objName, List<DynamicColumnTableRowData> objRows>
	 */
	public final Function<DynamicColumnTableRowData, String> objNameGetter = DynamicColumnTableRowData::getAssessObj;
	/**
	 * @Description: 值获取字段
	 * @Detail: 从 {@link #tableData} 中获取某一动态列的值
	 */
	public final Function<DynamicColumnTableRowData, String> valNameGetter = DynamicColumnTableRowData::getConform;
	/**
	 * @Description: 表格静态列的值获取字段
	 * @Detail: 按序取出静态列对应的值，注意数量要与 {@link #staticHeadCol} 保持一致
	 */
	public final List<Function<DynamicColumnTableRowData, Object>> staticColValGetters =
		new ArrayList<Function<DynamicColumnTableRowData, Object>>(){{
			add(DynamicColumnTableRowData::getAssessObj);
		}};

	// 固定的表头行数
	private int staticHeadRow = 2;
	/** 固定的表头列数，为 {@link #staticColValGetters} 的元素个数 + 1，有1列是序号列 */
	private int staticHeadCol = 2;


	// 表格数据
	private List<DynamicColumnTableRowData> tableData;

	public DynamicColumnTableRenderData(List<DynamicColumnTableRowData> tableData) {
		this.tableData = tableData;
	}

	public DynamicColumnTableRenderData(int staticHeadRow, int staticHeadCol, List<DynamicColumnTableRowData> tableData) {
		this.staticHeadRow = staticHeadRow;
		this.staticHeadCol = staticHeadCol;
		this.tableData = tableData;
	}

}
