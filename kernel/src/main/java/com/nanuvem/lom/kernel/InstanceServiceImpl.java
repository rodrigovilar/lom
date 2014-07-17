package com.nanuvem.lom.kernel;

import org.codehaus.jackson.JsonNode;

import com.nanuvem.kernel.util.JsonNodeUtil;
import com.nanuvem.lom.kernel.dao.AttributeValueDao;
import com.nanuvem.lom.kernel.dao.DaoFactory;
import com.nanuvem.lom.kernel.dao.InstanceDao;
import com.nanuvem.lom.kernel.validator.deployer.AttributeTypeDeployer;

public class InstanceServiceImpl {

	private InstanceDao instanceDao;
	private AttributeValueDao attributeValueDao;
	private ClassServiceImpl classService;

	InstanceServiceImpl(DaoFactory daoFactory, ClassServiceImpl classService) {
		this.classService = classService;
		this.instanceDao = daoFactory.createInstanceDao();
		this.attributeValueDao = daoFactory.createAttributeValueDao();
	}

	public void create(Instance instance) {
		if (instance.getClazz() == null) {
			throw new MetadataException(
					"Invalid value for Instance class: The class is mandatory");
		}
		Class clazz;
		try {
			clazz = this.classService.readClass(instance.getClazz()
					.getFullName());
		} catch (MetadataException e) {
			throw new MetadataException("Unknown class: "
					+ instance.getClazz().getFullName());
		}
		instance.setClazz(clazz);
		this.validateAndAssignDefaultValueInAttributesValues(instance, clazz);

		this.instanceDao.create(instance);
		for (AttributeValue value : instance.getValues()) {
			value.setInstance(instance);
			this.attributeValueDao.create(value);

		}
	}

	private void validateAndAssignDefaultValueInAttributesValues(
			Instance instance, Class clazz) {

		for (AttributeValue attributeValue : instance.getValues()) {
			if (!(clazz.getAttributes().contains(attributeValue.getAttribute()))) {
				throw new MetadataException("Unknown attribute for "
						+ instance.getClazz().getFullName() + ": "
						+ attributeValue.getAttribute().getName());
			}
			validateTypeOfValue(attributeValue);

			boolean attributeConfigurationIsNotNullAndNotEmpty = attributeValue
					.getAttribute().getConfiguration() != null
					&& (!attributeValue.getAttribute().getConfiguration()
							.isEmpty());
			if (attributeConfigurationIsNotNullAndNotEmpty) {

				JsonNode jsonNode = JsonNodeUtil.validate(attributeValue
						.getAttribute().getConfiguration(),
						"Invalid value for Attribute configuration: "
								+ attributeValue.getAttribute()
										.getConfiguration());

				if (jsonNode
						.has(AttributeTypeDeployer.DEFAULT_CONFIGURATION_NAME)) {
					String defaultField = jsonNode.get(
							AttributeTypeDeployer.DEFAULT_CONFIGURATION_NAME)
							.asText();
					if (attributeValue.getValue() == null
							&& defaultField != null) {
						attributeValue.setValue(defaultField);
					}
				}
			}
		}
	}

	private void validateTypeOfValue(AttributeValue attributeValue) {
		// Refactor this method.
		if (attributeValue.getValue() != null) {
			if (attributeValue.getAttribute().getType()
					.equals(AttributeType.TEXT)
					&& !(attributeValue.getValue() instanceof String)) {
				throw new MetadataException(
						"Invalid value for the Instance. The '"
								+ attributeValue.getAttribute().getName()
								+ "' attribute can only get values ​​of type TEXT");
			} else if (attributeValue.getAttribute().getType()
					.equals(AttributeType.LONGTEXT)
					&& !(attributeValue.getValue() instanceof String)) {
				throw new MetadataException(
						"Invalid value for the Instance. The '"
								+ attributeValue.getAttribute().getName()
								+ "' attribute can only get values ​​of type LONGTEXT");
			} else if (attributeValue.getAttribute().getType()
					.equals(AttributeType.INTEGER)
					&& !(attributeValue.getValue() instanceof Integer)) {
				throw new MetadataException(
						"Invalid value for the Instance. The '"
								+ attributeValue.getAttribute().getName()
								+ "' attribute can only get values ​​of type INTEGER");
			}
		}
	}

	public Instance findInstanceById(Long id) {
		return this.instanceDao.findInstanceById(id);
	}
}
