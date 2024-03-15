package com.board.dto;

import com.board.entity.AddressEntity;

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
public class AddressDTO {
	private Long seqno;
	private String zipcode;
	private String province;
	private String road;
	private String building;
	private String oldaddr;
	
	// Entity -> DTO로 이동
	public AddressDTO(AddressEntity addressEntity) {
	    this.zipcode = addressEntity.getZipcode();
	    this.province = addressEntity.getProvince();
	    this.road = addressEntity.getRoad();
	    this.building = addressEntity.getBuilding();
	    this.oldaddr = addressEntity.getOldaddr();
	}

	// DTO -> Entity로 이동 (Builder 패턴 사용)
	public AddressEntity dtoToEntity(AddressDTO addressDTO) {
	    AddressEntity addressEntity = AddressEntity.builder()
	                                .zipcode(addressDTO.getZipcode())
	                                .province(addressDTO.getProvince())
	                                .road(addressDTO.getRoad())
	                                .building(addressDTO.getBuilding())
	                                .oldaddr(addressDTO.getOldaddr())
	                                .build();
	    return addressEntity;
	}
}
