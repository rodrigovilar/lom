// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.nanuvem.lom.model;

import com.nanuvem.lom.model.Entity;
import com.nanuvem.lom.model.EntityDataOnDemand;
import com.nanuvem.lom.model.EntityIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect EntityIntegrationTest_Roo_IntegrationTest {
    
    declare @type: EntityIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: EntityIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: EntityIntegrationTest: @Transactional;
    
    @Autowired
    EntityDataOnDemand EntityIntegrationTest.dod;
    
    @Test
    public void EntityIntegrationTest.testCountEntitys() {
        Assert.assertNotNull("Data on demand for 'Entity' failed to initialize correctly", dod.getRandomEntity());
        long count = Entity.countEntitys();
        Assert.assertTrue("Counter for 'Entity' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void EntityIntegrationTest.testFindEntity() {
        Entity obj = dod.getRandomEntity();
        Assert.assertNotNull("Data on demand for 'Entity' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Entity' failed to provide an identifier", id);
        obj = Entity.findEntity(id);
        Assert.assertNotNull("Find method for 'Entity' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Entity' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void EntityIntegrationTest.testFindAllEntitys() {
        Assert.assertNotNull("Data on demand for 'Entity' failed to initialize correctly", dod.getRandomEntity());
        long count = Entity.countEntitys();
        Assert.assertTrue("Too expensive to perform a find all test for 'Entity', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Entity> result = Entity.findAllEntitys();
        Assert.assertNotNull("Find all method for 'Entity' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Entity' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void EntityIntegrationTest.testFindEntityEntries() {
        Assert.assertNotNull("Data on demand for 'Entity' failed to initialize correctly", dod.getRandomEntity());
        long count = Entity.countEntitys();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Entity> result = Entity.findEntityEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Entity' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Entity' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void EntityIntegrationTest.testFlush() {
        Entity obj = dod.getRandomEntity();
        Assert.assertNotNull("Data on demand for 'Entity' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Entity' failed to provide an identifier", id);
        obj = Entity.findEntity(id);
        Assert.assertNotNull("Find method for 'Entity' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyEntity(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Entity' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void EntityIntegrationTest.testMergeUpdate() {
        Entity obj = dod.getRandomEntity();
        Assert.assertNotNull("Data on demand for 'Entity' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Entity' failed to provide an identifier", id);
        obj = Entity.findEntity(id);
        boolean modified =  dod.modifyEntity(obj);
        Integer currentVersion = obj.getVersion();
        Entity merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Entity' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void EntityIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'Entity' failed to initialize correctly", dod.getRandomEntity());
        Entity obj = dod.getNewTransientEntity(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Entity' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Entity' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'Entity' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void EntityIntegrationTest.testRemove() {
        Entity obj = dod.getRandomEntity();
        Assert.assertNotNull("Data on demand for 'Entity' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Entity' failed to provide an identifier", id);
        obj = Entity.findEntity(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Entity' with identifier '" + id + "'", Entity.findEntity(id));
    }
    
}
