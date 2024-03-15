package com.board.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyDTO {
	private int replyseqno;
	private int seqno;
	private String replywriter;
	private String replycontent;
	private LocalDate replyregdate;
	private String userid;
	
}
