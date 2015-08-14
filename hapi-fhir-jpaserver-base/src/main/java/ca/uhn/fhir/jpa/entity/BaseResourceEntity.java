package ca.uhn.fhir.jpa.entity;

import javax.persistence.MappedSuperclass;

import ca.uhn.fhir.model.primitive.IdDt;

@MappedSuperclass
public abstract class BaseResourceEntity implements IResourceEntity{

	private long version;
	
	@Override
	public IdDt getIdDt() {
		if(version != 0)
			return new IdDt(this.getResourceType(), String.valueOf(this.getId()), String.valueOf(this.getVersion()));
		return new IdDt(getResourceType(), this.getId());
	}
	
	/**
	 * While version is Null, the system treats the entity as being the current version.
	 * @see //TODO document here
	 */
	@Override
	public long getVersion() {
		return version;
	}
	
	public void setVersion(long version) {
		this.version = version;
	}
}