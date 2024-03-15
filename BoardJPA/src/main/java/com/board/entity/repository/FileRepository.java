package com.board.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

	// 첨부 파일 목록 보기
	public List<FileEntity> findBySeqnoAndCheckfile(Long seqno, String checkfile);
	
	/*
		<select id="fileInfoView" parameterType="int" resultType="com.board.dto.FileDTO">
			select * from tbl_file where seqno = #{seqno} and checkfile= 'O'
		</select>
	*/
	
	// 첨부 파일 삭제를 위한 checkfile 정보 변경을 위한 seqno에 맞는 List 뽑아내기
	public List<FileEntity> findBySeqno(Long seqno);
	/*
		<select id="fileInfoUpdate" parameterType="int">
			select * from tbl_file where seqno=#{seqno}
		</select>
	*/
}
