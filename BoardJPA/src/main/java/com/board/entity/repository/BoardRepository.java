package com.board.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.board.entity.BoardEntity;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepository extends JpaRepository<BoardEntity, Long>{
	
	// 게시물 목록 보기
	public Page<BoardEntity> findByTitleContainingOrWriterContainingOrContentContaining
		(String keyword1, String keyword2, String keyword3, Pageable pageable);
	
	/*
		<select id="list" parameterType="HashMap" resultType="com.board.dto.BoardDTO">
			select seq,seqno,writer,title,regdate,hitno from
				(select row_number() over(order by seqno desc) as seq,seqno,writer,title,
				to_char(regdate,'YYYY-MM-DD HH24:MI:SS') as regdate,hitno from tbl_board 
				where writer like '%'||#{keyword}||'%' or title like '%'||#{keyword}||'%' or 
					content like '%'||#{keyword}||'%' 
				 ) where seq between #{startPoint} and #{endPoint}		 
		</select>
	 */
	
	// 게시물 이전 보기 --> JPQL
	@Query("select max(b.seqno) from board b where b.seqno < :seqno and (b.writer like %:keyword1% or b.title like %:keyword2% or b.content like %:keyword3%)")
	public Long pre_seqno(@Param("seqno") Long seqno, @Param("keyword1") String keyword1, @Param("keyword2") String keyword2, @Param("keyword3") String keyword3);
	
	/* 
		<select id="pre_seqno" parameterType="HashMap" resultType="int">
		    <![CDATA[
				select nvl(max(seqno), 0) as pre_seqno from tbl_board where seqno < #{seqno}
				and (writer like '%'||#{keyword}||'%' or 
						title like '%'||#{keyword}||'%' or 
						content like '%'||#{keyword}||'%')
			]]>
		</select>
	 */
	
	// 게시물 다음 보기 --> JPQL
	@Query("select min(b.seqno) from board b where b.seqno > :seqno and (b.writer like %:keyword1% or b.title like %:keyword2% or b.content like %:keyword3%)")
	public Long next_seqno(@Param("seqno") Long seqno, @Param("keyword1") String keyword1, @Param("keyword2") String keyword2, @Param("keyword3") String keyword3);
	
	/*
		<select id="next_seqno" parameterType="HashMap" resultType="int">
			<![CDATA[
				select nvl(min(seqno), 0) as next_seqno from tbl_board where seqno > #{seqno}
				and (writer like '%'||#{keyword}||'%' or 
						title like '%'||#{keyword}||'%' or 
						content like '%'||#{keyword}||'%')
			]]>
		</select>
	 */
	
	//게시물 조회수 증가 --> Native SQL 
    @Transactional
    @Modifying //테이블에 DML(insert, update, delete)을 실행 시켜 변화를 주었을 경우 테이블에 반영된 내용을 엔티티 클래스에 반영 
    @Query(value="update tbl_board set hitno = (select nvl(hitno,0) from tbl_board where seqno = :seqno) + 1 where seqno = :seqno",nativeQuery = true)
    public void hitno(@Param("seqno") Long seqno);
	
	/* 
	 	<update id="hitno" parameterType="int">
			update tbl_board set hitno = (select nvl(hitno, 0) from tbl_board where seqno = #{seqno}) + 1 where seqno = #{seqno}
		</update>
	 */
	
	// 게시물 시퀀스 번호 가져 오기
	@Query(value="select tbl_board_seq.nextval from dual", nativeQuery = true)
	public Long getSeqnoWithNextval();
	
	/*
		<select id="getSeqnoWithNextval" resultType="int">
			select tbl_board_seq.nextval as seqno from dual
		</select>
	 */
}
