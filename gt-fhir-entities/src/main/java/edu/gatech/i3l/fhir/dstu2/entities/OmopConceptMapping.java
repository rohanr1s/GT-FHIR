package edu.gatech.i3l.fhir.dstu2.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import ca.uhn.fhir.jpa.dao.BaseFhirDao;

/**
 * This class serves as cache for Concept values in Omop schema based database.
 * It is suggested to statr a new thread and pass this singleton to run.
 * 
 * @author Ismael Sarmento
 */
public class OmopConceptMapping implements Runnable {
	
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(OmopConceptMapping.class);
	private static OmopConceptMapping omopConceptMapping = new OmopConceptMapping();
	private EntityManager entityManager;
	
	/*
	 * Concepts' Classes
	 */
	public static final String GENDER = "Gender";
	public static final String MARITAL_STATUS = "Marital Status";
	public static final String DRUG_EXPOSURE_TYPE = "Drug Exposure Type";
	
	public static final String GENDER_VOCABULARY = "HL7 Administrative Sex";
	/**
	 * A mapping for some of the existing concepts in the database. The key for the outter mapping is the Concept Class.
	 * The inner map has the value(name) of the Concept as key and the respective id in the database as values in the map.
	 */
	protected Map<String, Map<String, Long>> concepts = new HashMap<String, Map<String, Long>>();
	
	private OmopConceptMapping(){}
	
	public void loadConcepts(){
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		concepts.put(GENDER, findConceptMap(builder, GENDER, GENDER_VOCABULARY));
	}
	
	public static OmopConceptMapping getInstance(){
		return omopConceptMapping;
	}
	
	/**
	 * Searches on the database for the concepts, using, as filters, the concept class and the respective vocabulary name.
	 * @param builder
	 * @param conceptClass
	 * @param vocabularyName
	 * @return A map containing the names(values) of the concepts and their respective id's in the database.
	 */
	private Map<String, Long> findConceptMap(CriteriaBuilder builder, String conceptClass, String vocabularyName){
		CriteriaQuery<Object[]> criteria = builder.createQuery(Object[].class);
		Root<Concept> from = criteria.from(Concept.class);
		Path<Long> idPath = from.get("id");
		Path<String> namePath = from.get("name");
		criteria.multiselect(namePath, idPath); //TODO unit test, order matters here
		Predicate p1 = builder.like(from.get("conceptClass").as(String.class), conceptClass);
		if(vocabularyName != null){
			Predicate p2 = builder.like(from.get("vocabulary").get("name").as(String.class), vocabularyName);  
			criteria.where(builder.and(p1, p2));
		} else{
			criteria.where(builder.and(p1));
		}
		TypedQuery<Object[]> query = entityManager.createQuery(criteria);
		Map<String, Long> retVal = new HashMap<String, Long>();
		List<Object[]> resultList = query.getResultList();
		for (Object[] result : resultList) {
			retVal.put(((String)result[0]).toLowerCase(), (Long)result[1]);
		}
		return retVal; 
	}

	@Override
	public void run() {
		String baseFhirDao = "myBaseDao";
		WebApplicationContext myAppCtx = ContextLoaderListener.getCurrentWebApplicationContext();
		entityManager = myAppCtx.getBean(baseFhirDao, BaseFhirDao.class).getEntityManager();
		loadConcepts();
	}

	public Long get(String conceptClass, String conceptValue) {
		Long retVal = null;
		Map<String, Long> concepts = this.concepts.get(conceptClass);
		retVal = concepts.get(conceptValue.toLowerCase()); 
		if(retVal == null){
			ourLog.error("A respective value for ? '?' could not be found in the database.", conceptClass, conceptValue);
		}
		return retVal;
	}
}