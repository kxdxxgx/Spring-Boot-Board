package com.board.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.board.dto.ReplyDTO;
import com.board.dto.ReplyInterface;
import com.board.entity.ReplyEntity;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Long>{

	// 댓글 목록 보기
	@Query(value="select * from tbl_reply where seqno=:seqno order by replyseqno desc", nativeQuery=true)
	List<ReplyInterface> replyView(@Param("seqno") Long seqno);
	
	/*
		<select id="replyView" parameterType="com.board.dto.ReplyDTO" resultType="com.board.dto.ReplyDTO">
			select replyseqno,replywriter,replycontent,to_char(replyregdate,'YYYY-MM-DD HH24:MI:SS') as replyregdate,userid, seqno 
			from tbl_reply where seqno=#{seqno} order by replyseqno desc
		</select>
	*/
	
	// 댓글 목록 보기
	@Query(value="select * from tbl_reply where seqno=:seqno order by replyseqno desc", nativeQuery=true)
	List<ReplyDTO> replyView2(@Param("seqno") Long seqno);

}
