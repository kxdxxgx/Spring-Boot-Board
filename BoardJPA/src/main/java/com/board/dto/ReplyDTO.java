package com.board.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.board.entity.AddressEntity;
import com.board.entity.BoardEntity;
import com.board.entity.MemberEntity;
import com.board.entity.ReplyEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyDTO{

	private Long replyseqno;
	private Long seqno;
	private String email;
	private String replywriter;
	private String replycontent;
	private LocalDateTime replyregdate;
	
	// x Entity -> DTO로 이동
    public ReplyDTO(ReplyEntity replyEntity) {
        this.replyseqno = replyEntity.getReplyseqno();
        this.seqno = replyEntity.getSeqno().getSeqno(); // 추가
        this.replywriter = replyEntity.getReplywriter();
        this.replycontent = replyEntity.getReplycontent();
        this.replyregdate = replyEntity.getReplyregdate();
        this.email = replyEntity.getEmail().getEmail(); // 추가
    } 

    // DTO -> Entity로 이동 (Builder 패턴 사용)
    // public ReplyEntity toDtoEntity(ReplyDTO reply) {
    //        ReplyEntity replyEntity = ReplyEntity.builder()
	// 						                	.replyseqno(reply.getReplyseqno())
	// 						                	.seqno(reply.getSeqno())
	// 						                	.replywriter(reply.getReplywriter())
	// 						                	.replycontent(reply.getReplycontent())
	// 						                	.replyregdate(reply.getReplyregdate())
	// 						                	.email(reply.getEmail())
	// 						                	.build();
    // 
    //     return replyEntity;
    // }
}
