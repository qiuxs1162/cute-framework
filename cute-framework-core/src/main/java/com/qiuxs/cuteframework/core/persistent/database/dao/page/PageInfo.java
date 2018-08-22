package com.qiuxs.cuteframework.core.persistent.database.dao.page;

import org.apache.ibatis.session.RowBounds;

/**
 * 分页对象
 * 
 * @author qiuxs
 * 
 * 创建时间 ： 2018年8月16日 下午10:08:25
 *
 */
public class PageInfo extends RowBounds {

	private static final int DEFAULT_PAGESIZE = 20;

	/** 当前页码 */
	private int pageNo;
	/** 每页行数 */
	private int pageSize;

	/** 偏移量 */
	private int offset;
	/** 每页行数 */
	private int limit;

	/** 总行数 */
	private Integer total;

	/** 不分页 */
	public static final PageInfo NO_PAGE_INFO = new PageInfo(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);

	public PageInfo() {
		this.pageNo = 1;
		this.pageSize = RowBounds.NO_ROW_LIMIT;
	}

	public PageInfo(int offset, int limit) {
		super(offset, limit);
	}

	/**
	 * 构造分页对象
	 * @author qiuxs
	 *
	 * @param pageNo
	 * @param pageSize
	 * @return
	 *
	 * 创建时间：2018年8月16日 下午10:10:46
	 */
	public PageInfo makePageInfo(int pageNo, int pageSize) {
		if (pageNo <= 0) {
			pageNo = 1;
		}
		if (pageSize <= 0) {
			pageSize = DEFAULT_PAGESIZE;
		}
		int limit = (pageNo - 1) * pageSize;
		PageInfo pageInfo = new PageInfo(limit, pageSize);
		pageInfo.setPageNo(pageNo);
		pageInfo.setPageSize(pageSize);
		return pageInfo;
	}

	public int getPageNo() {
		return this.pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		if (pageSize == 0) {
			pageSize = RowBounds.NO_ROW_LIMIT;
		}
		this.pageSize = pageSize;
	}

	public Integer getTotal() {
		return this.total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public int getOffset() {
		return this.offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return this.limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
