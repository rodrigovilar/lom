package com.nanuvem.lom.kernel.dao.memory;

import java.util.ArrayList;
import java.util.List;

import com.nanuvem.lom.kernel.AttributeValue;
import com.nanuvem.lom.kernel.Instance;
import com.nanuvem.lom.kernel.dao.AttributeValueDao;
import com.nanuvem.lom.kernel.dao.InstanceDao;

public class MemoryAttributeValueDao implements AttributeValueDao {

	private Long id = 1L;
	private InstanceDao instanceDao;

	public MemoryAttributeValueDao(InstanceDao instanceDao) {
		this.instanceDao = instanceDao;
	}

	public void create(AttributeValue value) {
		Instance instance = this.instanceDao.findInstanceById(value
				.getInstance().getId());

		value.setId(id++);
		value.setVersion(0);

		if (instance.getValues() != null && instance.getValues().size() > 0) {
			for (AttributeValue aValueOfInstance : instance.getValues()) {
				if (aValueOfInstance.getAttribute()
						.equals(value.getAttribute())) {
					aValueOfInstance.setValue(value.getValue());
					aValueOfInstance.setInstance(instance);
				}
			}
		} else {
			List<AttributeValue> values = new ArrayList<AttributeValue>();
			values.add(value);
			instance.setValues(values);
		}
		this.instanceDao.update(value.getInstance());
	}
}
