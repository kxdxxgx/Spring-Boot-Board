package com.board.dto;

import java.time.LocalDateTime;

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
public class MemberDTO {
	private String email;
	private String username;
	private String password;
	private String job;
	private String gender;
	private String hobby;
	private String telno;
	private String nickname;
	private String zipcode;
	private String address;
	private String description;
	private LocalDateTime regdate; // 초기값 LocalDateTime.now()
	private LocalDateTime lastlogindate;
	private LocalDateTime lastlogoutdate;
	private LocalDateTime lastpwdate; // 초기값 regdate와 동일
	private int pwcheck; // 초기값 1
	private String role; // 초기값 USER
	private String org_filename;
	private String stored_filename;
	private long filesize;
	private String authkey;
	private String fromSocial; // 회원 등록 시 초기값 N
	
    // Entity -> DTO로 이동
    public MemberDTO(MemberEntity memberEntity) {
        this.email = memberEntity.getEmail();
        this.username = memberEntity.getUsername();
        this.password = memberEntity.getPassword();
        this.job = memberEntity.getJob();
        this.gender = memberEntity.getGender();
        this.hobby = memberEntity.getHobby();
        this.telno = memberEntity.getTelno();
        this.nickname = memberEntity.getNickname();
        this.zipcode = memberEntity.getZipcode();
        this.address = memberEntity.getAddress();
        this.description = memberEntity.getDescription();
        this.regdate = memberEntity.getRegdate();
        this.lastlogindate = memberEntity.getLastlogindate();
        this.lastpwdate = memberEntity.getLastpwdate();
        this.lastlogoutdate = memberEntity.getLastlogoutdate();
        this.pwcheck = memberEntity.getPwcheck();
        this.role = memberEntity.getRole();
        this.org_filename = memberEntity.getOrg_filename();
        this.stored_filename = memberEntity.getStored_filename();
        this.filesize = memberEntity.getFilesize();
        this.authkey = memberEntity.getAuthkey();
        this.fromSocial = memberEntity.getFromSocial();
    }
    
    // DTO -> Entity로 이동 (Builder 패턴 사용)
    public MemberEntity dtoToEntity(MemberDTO member) {
    	MemberEntity memberEntity = MemberEntity.builder()
								                .email(member.getEmail())
								                .username(member.getUsername())
								                .password(member.getPassword())
								                .job(member.getJob())
								                .gender(member.getGender())
								                .hobby(member.getHobby())
								                .telno(member.getTelno())
								                .nickname(member.getNickname())
								                .zipcode(member.getZipcode())
								                .address(member.getAddress())
								                .description(member.getDescription())
								                .regdate(member.getRegdate())
								                .lastlogindate(member.getLastlogindate())
								                .lastpwdate(member.getLastpwdate())
								                .lastlogoutdate(member.getLastlogoutdate())
								                .pwcheck(member.getPwcheck())
								                .role(member.getRole())
								                .org_filename(member.getOrg_filename())
								                .stored_filename(member.getStored_filename())
								                .filesize(member.getFilesize())
								                .authkey(member.getAuthkey())
								                .fromSocial(member.getFromSocial())
								                .build();
        return memberEntity;
    }
}
