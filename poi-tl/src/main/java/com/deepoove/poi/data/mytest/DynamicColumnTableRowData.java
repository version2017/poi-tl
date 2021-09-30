package com.deepoove.poi.data.mytest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @description: 动态列表格行数据
 * @author: Weijie.Wu
 * @create: 2021-09-30 07:15
 */
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class DynamicColumnTableRowData {
	private Integer index;
	private String assessObj;
	private String checkType;
	private String conform;
}
