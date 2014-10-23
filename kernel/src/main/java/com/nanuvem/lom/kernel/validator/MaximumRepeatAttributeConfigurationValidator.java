package com.nanuvem.lom.kernel.validator;

import static com.nanuvem.lom.kernel.validator.AttributeTypeConfigurationValidator.addError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaximumRepeatAttributeConfigurationValidator implements ValueValidator<Integer> {

	public void validate(List<ValidationError> errors,
			String value, Integer maxRepeat) {

		Map<String, Integer> mapCounter = new HashMap<String, Integer>();
		int characterCounter = 0;

		for (int i = 0; i < value.toCharArray().length; i++) {
			Integer count = mapCounter.get(String.valueOf(value
					.toCharArray()[i]));
			if (count == null) {
				count = new Integer(-1);
			}
			count++;
			mapCounter
					.put(String.valueOf(value.toCharArray()[i]), count);

			if (characterCounter < count) {
				characterCounter = count;
			}
		}

		if (characterCounter > maxRepeat) {
			String messagePlural = characterCounter > 1 ? " more than "
					+ (maxRepeat + 1) + " " : " ";
			addError(errors, "the default value must not have" + messagePlural
					+ "repeated characters");
		}
	}

	public AttributeConfigurationValidator createFieldValidator(String field) {
		return new IntegerAttributeConfigurationValidator(field);
	}

}
