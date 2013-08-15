package com.nanuvem.lom.kernel;

public class EntityDTO {

	private Long id;
	private Integer version;
	private String namespace;
	private String name;

	public EntityDTO(Long id, Integer version, String namespace, String name) {
		this.id = id;
		this.version = version;
		this.name = name;
		this.namespace = namespace;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

}
