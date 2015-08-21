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
@Table(name="episode_of_care")
@Audited
public class EpisodeOfCare extends BaseResourceEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="episode_of_care_id")
	private Long id;
	
	@ManyToOne(cascade={CascadeType.MERGE})
	@JoinColumn(name="person_id")
	private Person person;
	
	@Column(name="episode_source_value")
	private String episodeSourceValue;
	
	public EpisodeOfCare() {
		super();
	}
	
	public EpisodeOfCare(Long id, Person person, String episodeSourceValue) {
		super();
		
		this.id = id;
		this.person = person;
		this.episodeSourceValue = episodeSourceValue;
	}
	
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
	public String getEpisodeSourceValue() {
		return episodeSourceValue;
	}
	
	public void setEpisodeSourceValue(String episodeSourceValue) {
		this.episodeSourceValue = episodeSourceValue;
	}
	
	/* (non-Javadoc)
	 * @see edu.gatech.i3l.fhir.dstu2.entities.IResourceTable#getRelatedResource()
	 */
	@Override
	public ca.uhn.fhir.model.dstu2.resource.EpisodeOfCare getRelatedResource() {
		ca.uhn.fhir.model.dstu2.resource.EpisodeOfCare episodeOfCare = new ca.uhn.fhir.model.dstu2.resource.EpisodeOfCare();
		
		// TODO: set parameters.
		
		return episodeOfCare;
	}

	public Class<? extends IResource> getRelatedResourceType() {
		return ca.uhn.fhir.model.dstu2.resource.EpisodeOfCare.class;
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
		return "EpisodeOfCare";
	}

	@Override
	public IResourceEntity constructEntityFromResource(IResource arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FhirVersionEnum getFhirVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InstantDt getUpdated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String translateSearchParam(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
