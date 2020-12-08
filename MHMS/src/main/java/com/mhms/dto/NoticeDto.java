package com.mhms.dto;

import com.mysema.query.annotations.QueryProjection;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class NoticeDto {
	
	int sid;
	int cid;
	int bid;
	String title;
	int notice_lv;
	int viewcnt;
	String content;
	String writer;
	String writedate;
	
	@QueryProjection
	public NoticeDto(int sid, int cid, int bid, String title, int notice_lv, int viewcnt, String content, String writer, String writedate) {
		this.sid = sid;
		this.cid = cid;
		this.bid = bid;
		this.title = title;
		this.notice_lv = notice_lv;
		this.viewcnt = viewcnt;
		this.content = content;
		this.writer = writer;
		this.writedate = writedate;
	}
	
}