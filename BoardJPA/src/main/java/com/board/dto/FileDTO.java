package com.board.dto;

import com.board.entity.FileEntity;

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
public class FileDTO {
	
	private Long fileseqno;
	private Long seqno;
	private String email;
	private String org_filename;
	private String stored_filename;
	private Long filesize;
	private String checkfile;
	
	// Entity --> DTO 이동
	public FileDTO(FileEntity fileEntity) {
		this.fileseqno = fileEntity.getFileseqno();
		this.seqno = fileEntity.getSeqno();
		this.email = fileEntity.getEmail();
		this.org_filename = fileEntity.getOrg_filename();
		this.stored_filename = fileEntity.getStored_filename();
		this.filesize = fileEntity.getFilesize();
		this.checkfile = fileEntity.getCheckfile();
	}
	
	// DTO --> Entity로 이동
	public FileEntity dtoToEntity(FileDTO file) {
		
        FileEntity fileEntity = FileEntity.builder()
						                .fileseqno(file.getFileseqno())
						                .seqno(file.getSeqno())
						                .email(file.getEmail())
						                .org_filename(file.getOrg_filename())
						                .stored_filename(file.getStored_filename())
						                .filesize(file.getFilesize())
						                .checkfile(file.getCheckfile())
						                .build();
        return fileEntity;
    }
}


