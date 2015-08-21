package edu.gatech.i3l.fhir.jpa.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

import ca.uhn.fhir.model.dstu2.resource.Medication;
import edu.gatech.i3l.fhir.dstu2.entities.MedicationConcept;
import edu.gatech.i3l.fhir.jpa.entity.IResourceEntity;
import edu.gatech.i3l.fhir.jpa.query.AbstractPredicateBuilder;
import edu.gatech.i3l.fhir.jpa.query.PredicateBuilder;

public class MedicationFhirResourceDao extends BaseFhirResourceDao<Medication> {
	

	public MedicationFhirResourceDao() {
		super();
		setResourceEntity(MedicationConcept.class);
		setValidateBean(true);
	}

	@Override
	public PredicateBuilder getPredicateBuilder() {
		return new AbstractPredicateBuilder() {

			@Override
			public Predicate addCommonPredicate(CriteriaBuilder builder, From<? extends IResourceEntity, ? extends IResourceEntity> from) {
				return builder.equal(from.get("vocabulary").get("id"), 8);//In Omop database, the dictionary is static; that means we can reference id's directly: the id for the vocabulary RxNorm is 8
			}
		};
	}

}
