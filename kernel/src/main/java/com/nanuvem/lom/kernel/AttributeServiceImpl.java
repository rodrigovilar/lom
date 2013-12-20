package com.nanuvem.lom.kernel;

import java.util.List;
import java.util.regex.Pattern;

import com.nanuvem.lom.kernel.dao.AttributeDao;
import com.nanuvem.lom.kernel.dao.DaoFactory;

public class AttributeServiceImpl {

	private AttributeDao attributeDao;

	private ClassServiceImpl classService;

	private final Integer MINIMUM_VALUE_FOR_THE_ATTRIBUTE_SEQUENCE = 1;

	private final String TRUE_VALUE_FOR_THE_ATTRIBUTE_CONFIGURATION = "{mandatory:true}";
	private final String FALSE_VALUE_FOR_THE_ATTRIBUTE_CONFIGURATION = "{mandatory:false}";

	public AttributeServiceImpl(DaoFactory dao) {
		this.classService = new ClassServiceImpl(dao);
		this.attributeDao = dao.createAttributeDao();
	}

	private void validate(Attribute attribute) {
		int currentNumberOfAttributes = attribute.getClazz().getAttributes()
				.size();
		this.validateExistingAttributeNotInClass(attribute.getClazz(),
				attribute);

		if (attribute.getSequence() != null) {
			boolean minValueForSequence = attribute.getSequence() < MINIMUM_VALUE_FOR_THE_ATTRIBUTE_SEQUENCE;
			boolean maxValueForSequence = currentNumberOfAttributes + 1 < attribute
					.getSequence();

			if (minValueForSequence || maxValueForSequence) {
				throw new MetadataException(
						"Invalid value for Attribute sequence: "
								+ attribute.getSequence());
			}
		} else {
			attribute.setSequence(currentNumberOfAttributes + 1);
		}

		if (attribute.getName() == null || attribute.getName().isEmpty()) {
			throw new MetadataException("The name of a Attribute is mandatory");
		}

		if (!Pattern.matches("[a-zA-Z1-9]{1,}", attribute.getName())) {
			throw new MetadataException("Invalid value for Attribute name: "
					+ attribute.getName());
		}

		if (attribute.getType() == null) {
			throw new MetadataException("The type of a Attribute is mandatory");
		}

		boolean isValueValidForConfiguration = !(attribute.getConfiguration() == null
				|| attribute.getConfiguration().isEmpty()
				|| (attribute.getConfiguration()
						.equals(TRUE_VALUE_FOR_THE_ATTRIBUTE_CONFIGURATION)) || (attribute
				.getConfiguration()
				.equals(FALSE_VALUE_FOR_THE_ATTRIBUTE_CONFIGURATION)));

		if (isValueValidForConfiguration) {
			throw new MetadataException(
					"Invalid value for Attribute configuration: "
							+ attribute.getConfiguration());
		}
	}

	private Class validateExistingClassForAttribute(Attribute attribute) {
		Class clazz = null;
		try {
			clazz = classService.readClass(attribute.getClazz().getFullName());
		} catch (MetadataException e) {
			throw new MetadataException("Invalid Class: "
					+ attribute.getClazz().getFullName());
		}
		return clazz;
	}

	private void validateExistingAttributeNotInClass(Class clazz,
			Attribute attribute) {

		for (Attribute attrib : clazz.getAttributes()) {
			if (attrib.getName().equalsIgnoreCase(attribute.getName())) {
				throw new MetadataException("Attribute duplication on "
						+ clazz.getFullName()
						+ " Class. It already has an attribute "
						+ attrib.getName() + ".");
			}
		}
	}

	public void create(Attribute attribute) {
		Class clazz = validateExistingClassForAttribute(attribute);
		attribute.setClazz(clazz);
		this.validate(attribute);
		this.attributeDao.create(attribute);
	}

	public List<Attribute> listAllAttributes(String classFullName) {
		return this.attributeDao.listAllAttributes(classFullName);
	}

	public Attribute findAttributeById(Long id) {
		if (id != null) {
			return this.attributeDao.findAttributeById(id);
		} else {
			return null;
		}
	}

	public Attribute findAttributeByNameAndClassFullName(String nameAttribute,
			String classFullName) {

		if ((nameAttribute != null && !nameAttribute.isEmpty())
				&& (classFullName != null && !classFullName.isEmpty())) {
			if (!classFullName.contains(".")) {
				classFullName = ClassServiceImpl.PREVIOUS_NAME_DEFAULT_OF_THE_CLASSFULLNAME
						+ "." + classFullName;
			}

			return this.attributeDao.findAttributeByNameAndClassFullName(
					nameAttribute, classFullName);
		}
		return null;
	}
}