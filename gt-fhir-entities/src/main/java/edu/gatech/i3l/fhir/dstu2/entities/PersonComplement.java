package edu.gatech.i3l.fhir.dstu2.entities;

import java.util.Iterator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.StringDt;
import edu.gatech.i3l.fhir.jpa.entity.IResourceEntity;

/** 
 * This class adds some properties to the Omop data model Person, in order to provide
 * all the data specified for FHIR.
 * @author Ismael Sarmento
 */
@Entity
@Table(name="f_person")
@PrimaryKeyJoinColumn(name="person_id")
@Audited
public class PersonComplement extends Person{

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PersonComplement.class);
	
	@Column(name="family_name")
	private String familyName;
	
	@Column(name="given1_name")
	private String givenName1;
	
	@Column(name="given2_name")
	private String givenName2;
	
	@Column(name="prefix_name")
	private String prefixName;
	
	@Column(name="suffix_name")
	private String suffixName;
	
	@Column(name="preferred_language")
	private String preferredLanguage;
	
	@Column(name="ssn")
	private String ssn;
	
	@ManyToOne
	@JoinColumn(name="maritalstatus_concept_id")
	private Concept maritalStatus;
	
	@Column(name="active")
	private Boolean active;
	
	public PersonComplement() {
		super();
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getGivenName1() {
		return givenName1;
	}

	public void setGivenName1(String givenName1) {
		this.givenName1 = givenName1;
	}

	public String getGivenName2() {
		return givenName2;
	}

	public void setGivenName2(String givenName2) {
		this.givenName2 = givenName2;
	}

	public String getPrefixName() {
		return prefixName;
	}

	public void setPrefixName(String prefixName) {
		this.prefixName = prefixName;
	}

	public String getSuffixName() {
		return suffixName;
	}

	public void setSuffixName(String suffixName) {
		this.suffixName = suffixName;
	}

	public String getPreferredLanguage() {
		return preferredLanguage;
	}

	public void setPreferredLanguage(String preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public Concept getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(Concept maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	
	public Boolean getActive() {
		return active;
	}
	
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	@Override
	public Patient getRelatedResource() {
		Patient patient = (Patient) super.getRelatedResource();
		patient.addName().addFamily(this.familyName).addGiven(this.givenName1);
		if(this.givenName2 != null)
			patient.getName().get(0).addGiven(this.givenName2);
		boolean active = this.active != null ? this.active : false;
		patient.setActive(active);
		
		//MARITAL STATUS
//		MaritalStatusCodesEnum[] values = MaritalStatusCodesEnum.values();
//		for (int i = 0; i < values.length; i++) {
//			if(values[i].equals(this.maritalStatus.getName())){
//				patient.setMaritalStatus(values[i]);
//				break;
//			}
//		}
//		or patient.setMaritalStatus(MaritalStatusCodesEnum.valueOf(""));
		return patient;
	}
	
	/**
	 * @notice In Fhir model, {@link Patient} has a collection of names, while in this extension table, {@link PersonComplement},
	 * there is only one name for each {@link Person}.
	 */
	@Override
	public IResourceEntity constructEntityFromResource(IResource resource) {
		super.constructEntityFromResource(resource);
		if(resource instanceof Patient){
			Patient patient = (Patient)resource;
			
			Iterator<HumanNameDt> iterator = patient.getName().iterator();
			//while(iterator.hasNext()){
				if(iterator.hasNext()){
					HumanNameDt next = iterator.next();
					this.givenName1 = next.getGiven().get(0).getValue();//the next method was not advancing to the next element, then the need to use the get(index) method
					if(next.getGiven().size() > 1) //TODO add unit tests, to assure this won't be changed to hasNext
						this.givenName2 = next.getGiven().get(1).getValue();
					Iterator<StringDt> family = next.getFamily().iterator();
					this.familyName = "";
					while(family.hasNext()){
						this.familyName = this.familyName.concat(family.next().getValue()+" ");
					}
					if(next.getSuffix().iterator().hasNext())
						this.suffixName = next.getSuffix().iterator().next().getValue();
					if(next.getPrefix().iterator().hasNext())
						this.prefixName = next.getPrefix().iterator().next().getValue();
				}
			//}
			
			this.active = patient.getActive();
			//MARITAL STATUS
//			this.maritalStatus.setId(OmopConceptMapping.getInstance().get(OmopConceptMapping.MARITAL_STATUS, patient.getMaritalStatus().getText()));
		} else {
			ourLog.error("There was not possible to construct the entity ? using the resource ?. It should be used the resource ?.",
					this.getClass().getSimpleName(), resource.getResourceName(), getResourceType());
		}
		return this;
	}

	
}
