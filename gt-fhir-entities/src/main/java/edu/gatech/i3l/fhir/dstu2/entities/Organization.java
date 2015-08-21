/**
 * 
 */
package edu.gatech.i3l.fhir.dstu2.entities;

import javax.persistence.CascadeType;
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
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.InstantDt;
import edu.gatech.i3l.fhir.jpa.entity.BaseResourceEntity;
import edu.gatech.i3l.fhir.jpa.entity.IResourceEntity;

/**
 * @author Myung Choi
 *
 */
@Entity
@Table(name="organization")
@Audited
public class Organization extends BaseResourceEntity {

	public static final String RESOURCE_TYPE = "Organization";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="organization_id")
	private Long id;
	
	@ManyToOne(cascade={CascadeType.MERGE})
	@JoinColumn(name="place_of_service_concept_id")
	private Concept placeOfServiceConcept;
	
	@ManyToOne(cascade={CascadeType.MERGE})
	@JoinColumn(name="location_id")
	private Location location;
	
	@Column(name="organization_source_value")
	private String organizationSourceValue;
	
	@Column(name="place_of_service_source_value")
	private String placeOfServiceSourceValue;
	
	public Organization() {
		super();
	}
	
	public Organization(Long id, Concept placeOfServiceConcept, Location location,
			String organizationSourceValue, String placeOfServiceSourceValue) {
		super();
		
		this.id = id;
		this.placeOfServiceConcept = placeOfServiceConcept;
		this.location = location;
		this.organizationSourceValue = organizationSourceValue;
		this.placeOfServiceSourceValue = placeOfServiceSourceValue;
	}
	
	public Concept getPlaceOfServiceConcept() {
		return placeOfServiceConcept;
	}
	
	public void setPlaceOfServiceConcept(Concept placeOfServiceConcept) {
		this.placeOfServiceConcept = placeOfServiceConcept;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public String getOrganizationSourceValue() {
		return organizationSourceValue;
	}
	
	public void setOrganizationSourceValue(String organizationSourceValue) {
		this.organizationSourceValue = organizationSourceValue;
	}
	
	public String getPlaceOfServiceSourceValue() {
		return placeOfServiceSourceValue;
	}
	
	public void setPlaceOfServiceSourceValue(String placeOfServiceSourceValue) {
		this.placeOfServiceSourceValue = placeOfServiceSourceValue;
	}
	
	/* (non-Javadoc)
	 * @see edu.gatech.i3l.fhir.dstu2.entities.IResourceTable#getRelatedResource()
	 */
	@Override
	public ca.uhn.fhir.model.dstu2.resource.Organization getRelatedResource() {
		ca.uhn.fhir.model.dstu2.resource.Organization organization = new ca.uhn.fhir.model.dstu2.resource.Organization();
		// TODO set parameters
		return organization;
	}

	/* (non-Javadoc)
	 * @see ca.uhn.fhir.jpa.entity.BaseHapiResourceTable#getId()
	 */
	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
		
	/* (non-Javadoc)
	 * @see ca.uhn.fhir.jpa.entity.BaseHapiResourceTable#getResourceType()
	 */
	@Override
	public String getResourceType() {
		return RESOURCE_TYPE;
	}

	@Override
	public FhirVersionEnum getFhirVersion() {
		return FhirVersionEnum.DSTU2;
	}

	@Override
	public InstantDt getUpdated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IResourceEntity constructEntityFromResource(IResource resource) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String translateSearchParam(String chain) {
		// TODO Auto-generated method stub
		return null;
	}

}
