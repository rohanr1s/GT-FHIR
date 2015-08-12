package ca.uhn.fhir.jpa.query;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.jpa.entity.IResourceEntity;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;

public abstract class AbstractPredicateBuilder implements PredicateBuilder{

	
	@Override
	public Predicate translatePredicateString(Class<? extends IResourceEntity> entity, String theParamName, String likeExpression,
			From<? extends IResourceEntity, ? extends IResourceEntity> from, CriteriaBuilder theBuilder) {
		return theBuilder.like(getPath(entity, theParamName, from).as(String.class), likeExpression);
	}

	@Override
	public Predicate translatePredicateDateLessThan(Class<? extends IResourceEntity> entity, String theParamName, Date upperBound,
			From<? extends IResourceEntity, ? extends IResourceEntity> from, CriteriaBuilder theBuilder, boolean inclusive) {
		Predicate predicate = null;
		Path<? extends Object> path = getPath(entity, theParamName, from);
		if(inclusive){
			predicate = theBuilder.greaterThanOrEqualTo(path.as(Date.class), upperBound);
		} else {
			predicate = theBuilder.greaterThan(path.as(Date.class), upperBound);
		}
		return predicate;
	}

	@Override
	public Predicate translatePredicateDateGreaterThan(Class<? extends IResourceEntity> entity, String theParamName, Date lowerBound,
			From<? extends IResourceEntity, ? extends IResourceEntity> from, CriteriaBuilder theBuilder, boolean inclusive) {
		Predicate predicate = null;
		Path<? extends Object> path = getPath(entity, theParamName, from);
		if(inclusive){
			predicate = theBuilder.lessThanOrEqualTo(path.as(Date.class), lowerBound);
		} else {
			predicate = theBuilder.lessThan(path.as(Date.class), lowerBound);
		}
		return predicate;
	}

	@Override
	public Predicate translatePredicateTokenSystem(Class<? extends IResourceEntity> entity, String theParamName, String system,
			From<? extends IResourceEntity, ? extends IResourceEntity> from, CriteriaBuilder theBuilder) {
		if (system == null) {
			return null;
		}
		Path<? extends Object> path = getPath( entity, theParamName, from);
		if (StringUtils.isNotBlank(system)) {
			 return theBuilder.equal(path, system);
		} else {
			return theBuilder.isNull(path);
		}
	}

	@Override
	public Predicate translatePredicateTokenCode(Class<? extends IResourceEntity> entity, String theParamName, String code,
			From<? extends IResourceEntity, ? extends IResourceEntity> from, CriteriaBuilder theBuilder) {
		Path<? extends Object> path = getPath(entity, theParamName, from);
		if (StringUtils.isNotBlank(code)) {
			return theBuilder.equal(path, code);
		} else {
			return theBuilder.isNull(path);
		}
	}

	@Override
	public Predicate translatePredicateQuantityCode(Class<? extends IResourceEntity> entity, String theParamName, CriteriaBuilder builder,
			From<? extends IResourceEntity, ? extends IResourceEntity> from, String unitsValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Predicate translatePredicateQuantitySystem(Class<? extends IResourceEntity> entity, String theParamName, CriteriaBuilder builder,
			From<? extends IResourceEntity, ? extends IResourceEntity> from, String systemValue) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Predicate addCommonPredicate(CriteriaBuilder builder, From<? extends IResourceEntity, ? extends IResourceEntity> from) {
		return null;
	}

	@Override
	public Predicate translatePredicateQuantityValue(Class<? extends IResourceEntity> entity, String theParamName, CriteriaBuilder builder,
			From<? extends IResourceEntity, ? extends IResourceEntity> from, QuantityCompararatorEnum cmpValue, BigDecimal valueValue,
			boolean approx) {
		Predicate num;
		Path<? extends Object> path = getPath(entity, theParamName, from);
		
		if (cmpValue == null) {
			BigDecimal mul = approx ? new BigDecimal(0.1) : new BigDecimal(0.01);
			BigDecimal low = valueValue.subtract(valueValue.multiply(mul));
			BigDecimal high = valueValue.add(valueValue.multiply(mul));
			Predicate lowPred = builder.gt(path.as(BigDecimal.class) , low);
			Predicate highPred = builder.lt(path.as(BigDecimal.class), high);
			num = builder.and(lowPred, highPred);
		} else {
			switch (cmpValue) {
			case GREATERTHAN:
				num = builder.gt(path.as(Number.class), valueValue);
				break;
			case GREATERTHAN_OR_EQUALS:
				num = builder.ge(path.as(Number.class), valueValue);
				break;
			case LESSTHAN:
				num = builder.lt(path.as(Number.class), valueValue);
				break;
			case LESSTHAN_OR_EQUALS:
				num = builder.le(path.as(Number.class), valueValue);
				break;
			default:
				throw new IllegalStateException(cmpValue.getCode());
			}
		}
		return num;
	}

	@Override
	public Path<? extends Object> getPath(Class<? extends IResourceEntity> entity, String theParamName, Path<? extends IResourceEntity> from) {
		Path<? extends Object> path = null;
		try {
			String translatedParams = entity.newInstance().translateSearchParam(theParamName);
			String[] chain = translatedParams.contains(".") ? translatedParams.split("\\.") : new String[]{translatedParams}; 
			path = from.get(chain[0]);
			if(chain != null){
				for (int i = 1; i < chain.length; i++) {
					path = path.get(chain[i]);
				}
			}
		} catch (InstantiationException e){
		} catch(IllegalAccessException e) {
		}
		return path;
	}

	
}
