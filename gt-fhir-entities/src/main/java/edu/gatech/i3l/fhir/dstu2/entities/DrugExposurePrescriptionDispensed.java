package edu.gatech.i3l.fhir.dstu2.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.entity.BaseResourceEntity;
import ca.uhn.fhir.jpa.entity.IResourceEntity;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.MedicationDispense;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;

@Entity
@Table(name="drug_exposure")
@Audited
public class DrugExposurePrescriptionDispensed extends BaseResourceEntity{
	
	public static final String RES_TYPE = "MedicationDispense";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="drug_exposure_id", updatable= false)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="drug_type_concept_id", updatable= false, nullable=false)
	private Concept drugExposureType;
	
	@ManyToOne
	@JoinColumn(name="person_id", updatable=false, nullable=false)
	private Person person;
	
	@Column(name="quantity", updatable=false)
	private BigDecimal quantity;
	
	@Column(name="days_supply",updatable=false)
	private Integer daysSupply;
	
	@ManyToOne
	@JoinColumn(name="drug_concept_id", updatable=false)
	private Concept medication;
	
	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public Integer getDaysSupply() {
		return daysSupply;
	}

	public void setDaysSupply(Integer daysSupply) {
		this.daysSupply = daysSupply;
	}

	public Concept getMedication() {
		return medication;
	}

	public void setMedication(Concept medication) {
		this.medication = medication;
	}

	public Concept getDrugExposureType() {
		return drugExposureType;
	}

	public void setDrugExposureType(Concept drugExposureType) {
		this.drugExposureType = drugExposureType;
	}

	@Override
	public FhirVersionEnum getFhirVersion() {
		return FhirVersionEnum.DSTU2;
	}

	@Override
	public String getResourceType() {
		return RES_TYPE;
	}

	@Override
	public InstantDt getUpdated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String translateSearchParam(String theSearchParam) {
		switch (theSearchParam) {
		case MedicationDispense.SP_PATIENT:
			return "person";
		case MedicationDispense.SP_MEDICATION:
			return "medication.name";
		default:
			break;
		}
		return theSearchParam;
	}

	@Override
	public IResource getRelatedResource() {
		MedicationDispense resource = new MedicationDispense();
		resource.setId(this.getIdDt());
		resource.setPatient(new ResourceReferenceDt(this.person.getIdDt()));
		resource.setMedication(new ResourceReferenceDt(new IdDt("Medication", this.medication.getId())));
		if(this.quantity != null)
			resource.setQuantity(new QuantityDt(this.quantity.doubleValue()));
		if(this.daysSupply != null)
			resource.setDaysSupply(new QuantityDt(this.daysSupply));
		return resource;
	}

	/*
	 * Not Updatable. So this is not meant to be implemented.
	 * @see ca.uhn.fhir.jpa.entity.IResourceEntity#constructEntityFromResource(ca.uhn.fhir.model.api.IResource)
	 */
	@Override
	public IResourceEntity constructEntityFromResource(IResource resource) {
		return null;
	}

}