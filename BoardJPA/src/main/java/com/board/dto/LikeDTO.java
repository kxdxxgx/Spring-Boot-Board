package com.board.dto;

import com.board.entity.BoardEntity;
import com.board.entity.LikeEntity;
import com.board.entity.MemberEntity;

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
public class LikeDTO {
	private BoardEntity seqno;
	private MemberEntity email;
	private String mylikecheck;
	private String mydislikecheck;
	private String likedate;
	private String dislikedate;
	
	 // Entity -> DTO로 이동
    public LikeDTO(LikeEntity likeEntity) {
        this.seqno = likeEntity.getSeqno();
        this.email = likeEntity.getEmail();
        this.mylikecheck = likeEntity.getMylikecheck();
        this.mydislikecheck = likeEntity.getMydislikecheck();
        this.likedate = likeEntity.getLikedate();
        this.dislikedate = likeEntity.getDislikedate();
    }
    
    // DTO -> Entity로 이동 (Builder 패턴 사용)
    public LikeEntity dtoToEntity(LikeDTO like) {
        LikeEntity likeEntity = LikeEntity.builder()
						                .seqno(like.getSeqno())
						                .email(like.getEmail())
						                .mylikecheck(like.getMylikecheck())
						                .mydislikecheck(like.getMydislikecheck())
						                .likedate(like.getLikedate())
						                .dislikedate(like.getDislikedate())
						                .build();
        return likeEntity;
	}
}