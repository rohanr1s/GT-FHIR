package edu.gatech.i3l.fhir.dstu2.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import ca.uhn.fhir.model.dstu2.valueset.AddressUseEnum;

@Entity
@Table(name="f_location")
@PrimaryKeyJoinColumn(name="location_id")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class LocationComplement extends Location{

	@Column(name="address_use")
	@Enumerated(EnumType.STRING)
	private AddressUseEnum addressUse;
	
	@Column(name="start_date")
	private Date startDate;
	
	@Column(name="end_date")
	private Date endDate;
	
	public LocationComplement() {
		super();
	}

	public AddressUseEnum getAddressUse() {
		return addressUse;
	}

	public void setAddressUse(AddressUseEnum addressUse) {
		this.addressUse = addressUse;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}
