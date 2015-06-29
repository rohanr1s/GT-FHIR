package edu.gatech.i3l.jpa.model.omop;

import java.util.Calendar;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.entity.BaseResourceEntity;
import ca.uhn.fhir.jpa.entity.IResourceEntity;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.composite.AddressDt;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;

public class Person extends BaseResourceEntity{

	public static final String RESOURCE_TYPE = "Patient";
	
	private Long id;
	private Integer yearOfBirth;
	private Integer monthOfBirth;
	private Integer dayOfBirth;
	private Location location;
	/**
	 * Makes the indirect relationship between the person and the Care Site.
	 */
	private Provider provider;
	private String personSourceValue;
	private String genderSourceValue;
	private Concept genderConcept;
	private String ethnicitySourceValue;
	private Concept ethnicityConcept;
	private String raceSourceValue;
	private Concept raceConcept;
	
	private Death death;

	public Person() {
		super();
	}

	public Person(Long id, Integer yearOfBirth, Integer monthOfBirth,
			Integer dayOfBirth, Location location, Provider provider, String personSourceValue,
			String genderSourceValue, Concept genderConcept,
			String ethnicitySourceValue, Concept ethnicityConcept,
			String raceSourceValue, Concept raceConcept) {
		super();
		this.id = id;
		this.yearOfBirth = yearOfBirth;
		this.monthOfBirth = monthOfBirth;
		this.dayOfBirth = dayOfBirth;
		this.location = location;
		this.provider = provider;
		this.personSourceValue = personSourceValue;
		this.genderSourceValue = genderSourceValue;
		this.genderConcept = genderConcept;
		this.ethnicitySourceValue = ethnicitySourceValue;
		this.ethnicityConcept = ethnicityConcept;
		this.raceSourceValue = raceSourceValue;
		this.raceConcept = raceConcept;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getYearOfBirth() {
		return yearOfBirth;
	}

	public void setYearOfBirth(Integer yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}

	public Integer getMonthOfBirth() {
		return monthOfBirth;
	}

	public void setMonthOfBirth(Integer monthOfBirth) {
		this.monthOfBirth = monthOfBirth;
	}

	public Integer getDayOfBirth() {
		return dayOfBirth;
	}

	public void setDayOfBirth(Integer dayOfBirth) {
		this.dayOfBirth = dayOfBirth;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public String getPersonSourceValue() {
		return personSourceValue;
	}

	public void setPersonSourceValue(String personSourceValue) {
		this.personSourceValue = personSourceValue;
	}

	public String getGenderSourceValue() {
		return genderSourceValue;
	}

	public void setGenderSourceValue(String genderSourceValue) {
		this.genderSourceValue = genderSourceValue;
	}

	public Concept getGenderConcept() {
		return genderConcept;
	}

	public void setGenderConcept(Concept genderConcept) {
		this.genderConcept = genderConcept;
	}

	public String getEthnicitySourceValue() {
		return ethnicitySourceValue;
	}

	public void setEthnicitySourceValue(String ethnicitySourceValue) {
		this.ethnicitySourceValue = ethnicitySourceValue;
	}

	public Concept getEthnicityConcept() {
		return ethnicityConcept;
	}

	public void setEthnicityConcept(Concept ethnicityConcept) {
		this.ethnicityConcept = ethnicityConcept;
	}

	public String getRaceSourceValue() {
		return raceSourceValue;
	}

	public void setRaceSourceValue(String raceSourceValue) {
		this.raceSourceValue = raceSourceValue;
	}

	public Concept getRaceConcept() {
		return raceConcept;
	}

	public void setRaceConcept(Concept raceConcept) {
		this.raceConcept = raceConcept;
	}
	
	public Death getDeath() {
		return death;
	}
	
	public void setDeath(Death death) {
		this.death = death;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Patient getRelatedResource() {
		Patient patient = new Patient();
		patient.setId(new IdDt(this.getId()));
		
		Location location = this.getLocation();
		if(location != null){
			patient.addAddress()
				.addLine(location.getAddress1())
				.addLine(location.getAddress2())//WARNING check if mapping for lines are correct
				.setCity(location.getCity())
				.setPostalCode(location.getZipCode())
				.setCountry(location.getCountry());
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(this.getYearOfBirth(), this.getMonthOfBirth(), this.getDayOfBirth());
		patient.setBirthDate(new DateDt(calendar.getTime()));
		
		Concept gender = this.getGenderConcept();
		if(gender != null){
			AdministrativeGenderEnum admGender = null;//TODO check if DSTU2 uses values coherent with this enum
			String gName = gender.getName(); 
			AdministrativeGenderEnum[] values = AdministrativeGenderEnum.values();
			for (int i = 0; i < values.length; i++) {
				if(gName.equalsIgnoreCase(values[i].getCode())){
					admGender = values[i];
				}
			}
			patient.setGender(admGender);
		}
		
		return patient;
	}

	@Override
	public String getResourceType() {
		return RESOURCE_TYPE;
	}

	public Class<? extends IResource> getRelatedResourceType() {
		return Patient.class;
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
	public IResourceEntity constructEntityFromResource(IResource resource) {
		if(resource instanceof Patient){
			Patient patient = (Patient) resource;
			Calendar c = Calendar.getInstance();
			c.setTime(patient.getBirthDate());
			this.yearOfBirth = c.get(Calendar.YEAR);
			this.monthOfBirth = c.get(Calendar.MONTH);
			this.dayOfBirth = c.get(Calendar.DAY_OF_MONTH);
			//TODO set deceased value in Person; Set gender concept (source value is set); list of addresses (?)
//			this.death = patient.getDeceased(); 
			String gender = patient.getGender();
			this.genderSourceValue = gender;
			this.genderConcept = OmopConceptMapping.getInstance().get(OmopConceptMapping.GENDER, patient.getGender());
			
			Location location = new Location();
			AddressDt address = patient.getAddress().get(0);
			location.setAddress1(address.getLine().iterator().next().getValue());
			if(address.getLine().iterator().hasNext())
				location.setAddress2(address.getLine().iterator().next().getValue());
			location.setZipCode(address.getPostalCode());
			location.setCity(address.getCity());
			location.setState(address.getState());
			location.setCountry(address.getCountry());
			this.location = location;
			
			//TODO complete mapping
		}
		
		return this;
	}


}