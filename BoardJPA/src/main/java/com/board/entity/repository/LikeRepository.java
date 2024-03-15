package com.board.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.BoardEntity;
import com.board.entity.LikeEntity;
import com.board.entity.LikeEntityID;
import com.board.entity.MemberEntity;

public interface LikeRepository extends JpaRepository<LikeEntity, LikeEntityID>{

	// 좋아요/싫어요 등록 여부 확인
	public LikeEntity findBySeqnoAndEmail(BoardEntity board, MemberEntity member);
	
	/*
	   	<select id="likeCheckView" parameterType="com.board.dto.LikeDTO" resultType="com.board.dto.LikeDTO">
			select * from tbl_like where seqno = #{seqno} and userid = #{userid}
		</select>
	 */
}
