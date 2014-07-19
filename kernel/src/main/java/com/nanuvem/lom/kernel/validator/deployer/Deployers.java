package com.nanuvem.lom.kernel.validator.deployer;

import java.util.HashMap;
import java.util.Map;

import com.nanuvem.lom.kernel.AttributeType;

public class Deployers {

	private Map<String, AttributeTypeDeployer> deployers = new HashMap<String, AttributeTypeDeployer>();

	public Deployers() {
		add(AttributeType.TEXT.name(), new TextAttributeTypeDeployer());
		add(AttributeType.LONGTEXT.name(), new LongTextAttributeTypeDeployer());
		add(AttributeType.PASSWORD.name(), new PasswordAttributeTypeDeployer());
		add(AttributeType.INTEGER.name(), new IntegerAttributeTypeDeployer());

	}

	public void add(String name, AttributeTypeDeployer deployer) {
		deployers.put(name, deployer);
	}

	public AttributeTypeDeployer get(String name) {
		return deployers.get(name);
	}
}
