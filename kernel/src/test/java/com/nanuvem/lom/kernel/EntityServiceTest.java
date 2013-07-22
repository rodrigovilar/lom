package com.nanuvem.lom.kernel;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.nanuvem.lom.kernel.dao.memory.MemoryDaoFactory;


public class EntityServiceTest {
	
	
	private EntityService service;

	
	@Before
	public void init() {
		service = new EntityService(new MemoryDaoFactory());
	}
	
	@Test
	public void validNameAndNamespace() {
		createAndVerifyOneEntity("abc", "abc");
		createAndVerifyOneEntity("a.b.c", "abc");
		createAndVerifyOneEntity("a", "a");
		createAndVerifyOneEntity("abc123", "aaa");
		createAndVerifyOneEntity("abc", "abc1122");
	}

	private void createAndVerifyOneEntity(String name, String namespace) {
		Entity entity = new Entity();
		service.create(entity);
		
		Assert.assertNotNull(entity.getId());
		Assert.assertEquals((Integer)0, entity.getVersion());

		List<Entity> entities = service.listAll();
		Assert.assertEquals(1, entities.size());
		Assert.assertEquals(entity, entities.get(0));
		
		service.remove(entity);
	}
	
}
