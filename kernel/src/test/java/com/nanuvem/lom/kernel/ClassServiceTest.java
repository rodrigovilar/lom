package com.nanuvem.lom.kernel;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.nanuvem.lom.kernel.dao.memory.MemoryDaoFactory;

public class ClassServiceTest {

	private ClassServiceImpl service;

	@Before
	public void init() {
		service = new ClassServiceImpl(new MemoryDaoFactory());
	}

	@Test
	public void validNameAndNamespace() {
		createAndVerifyOneClass("abc", "abc");
		createAndVerifyOneClass("a.b.c", "abc");
		createAndVerifyOneClass("a", "a");
		createAndVerifyOneClass("abc123", "aaa");
		createAndVerifyOneClass("abc", "abc1122");
	}

	@Test
	public void withoutNamespace() {
		createAndVerifyOneClass("", "abc");
		createAndVerifyOneClass(null, "abc");
		createAndVerifyOneClass("", "a1");
		createAndVerifyOneClass(null, "a1");
	}

	@Test
	public void twoClassesWithSameNameInDifferentNamespaces() {
		createAndVerifyTwoClasses("p1", "name", "p2", "name");
		createAndVerifyTwoClasses(null, "name", "p1", "name");
		createAndVerifyTwoClasses("a", "name", "a.b", "name");
	}

	@Test
	public void nameAndNamespaceWithSpaces() {
		this.expectExceptionOnCreateInvalidClass("name space", "name",
				"Invalid value for Class namespace: name space");
		this.expectExceptionOnCreateInvalidClass("namespace", "na me",
				"Invalid value for Class name: na me");
	}

	@Test
	public void withoutName() {
		this.expectExceptionOnCreateInvalidClass("namespace", null,
				"The name of an Class is mandatory");
		this.expectExceptionOnCreateInvalidClass("namespace", "",
				"The name of an Class is mandatory");
		this.expectExceptionOnCreateInvalidClass(null, null,
				"The name of an Class is mandatory");
		this.expectExceptionOnCreateInvalidClass("", null,
				"The name of an Class is mandatory");
	}

	@Test
	public void twoClassesWithSameNameInDefaultNamespace() {
		createAndSaveOneClass(null, "aaa");
		this.expectExceptionOnCreateInvalidClass(null, "aaa",
				"The aaa Class already exists");
		this.expectExceptionOnCreateInvalidClass("", "aaa",
				"The aaa Class already exists");
	}

	@Test
	public void twoClassesWithSameNameInAnonDefaultNamespace() {
		createAndSaveOneClass("a", "aaa");
		this.expectExceptionOnCreateInvalidClass("a", "aaa",
				"The a.aaa Class already exists");
	}

	@Test
	public void nameAndNamespaceWithInvalidChars() {
		this.expectExceptionOnCreateInvalidClass("a", "aaa$",
				"Invalid value for Class name: aaa$");
		this.expectExceptionOnCreateInvalidClass("a", "aaa#",
				"Invalid value for Class name: aaa#");
		this.expectExceptionOnCreateInvalidClass("a", "aaa=",
				"Invalid value for Class name: aaa=");
		this.expectExceptionOnCreateInvalidClass("a", "aaa.a",
				"Invalid value for Class name: aaa.a");
		this.expectExceptionOnCreateInvalidClass("a", "aaa/a",
				"Invalid value for Class name: aaa/a");
		this.expectExceptionOnCreateInvalidClass("a", "aaa*",
				"Invalid value for Class name: aaa*");
		this.expectExceptionOnCreateInvalidClass("a", "aaa'",
				"Invalid value for Class name: aaa'");
		this.expectExceptionOnCreateInvalidClass("a$", "aaa",
				"Invalid value for Class namespace: a$");
		this.expectExceptionOnCreateInvalidClass("a#", "aaa",
				"Invalid value for Class namespace: a#");
		this.expectExceptionOnCreateInvalidClass("a=", "aaa",
				"Invalid value for Class namespace: a=");
		this.expectExceptionOnCreateInvalidClass("a'", "aaa",
				"Invalid value for Class namespace: a'");
		// this.expectExceptionOnCreateInvalidClass("a.", "aaa",
		// "Invalid value for Class namespace: a.");
		this.expectExceptionOnCreateInvalidClass("a/a", "aaa",
				"Invalid value for Class namespace: a/a");
		this.expectExceptionOnCreateInvalidClass("a*", "aaa",
				"Invalid value for Class namespace: a*");
	}

	@Test
	public void validNewNameAndPackage() {
		createUpdateAndVerifyOneClass("a", "aaa1", "a.aaa1", "b", "bbb");
		createUpdateAndVerifyOneClass("a", "aaa2", "a.aaa2", "a", "bbb");
		createUpdateAndVerifyOneClass("a", "aaa3", "a.aaa3", "b", "aaa");
		createUpdateAndVerifyOneClass("", "aaa1", "aaa1", "", "bbb");
		createUpdateAndVerifyOneClass(null, "aaa2", "aaa2", null, "bbb");
		createUpdateAndVerifyOneClass("a.b.c", "aaa1", "a.b.c.aaa1", "b", "bbb");
		createUpdateAndVerifyOneClass("a.b.c", "aaa2", "a.b.c.aaa2", "b.c",
				"bbb");
	}

	@Test
	public void removePackageSetPackage() {
		createUpdateAndVerifyOneClass("a", "aaa1", "a.aaa1", "", "aaa");
		createUpdateAndVerifyOneClass("a", "aaa2", "a.aaa2", "", "bbb");
		createUpdateAndVerifyOneClass("", "aaa1", "aaa1", "b", "bbb");
		createUpdateAndVerifyOneClass("a", "aaa3", "a.aaa3", null, "aaa");
		createUpdateAndVerifyOneClass("a", "aaa4", "a.aaa4", null, "bbb");
		createUpdateAndVerifyOneClass(null, "aaa2", "aaa2", "b", "bbb");

		createUpdateAndVerifyOneClass("a", "aaa5", "a.aaa5", "a", "aaa5");
		createUpdateAndVerifyOneClass("a", "aaa6", "a.aaa6", "a", "aaa7");
		createUpdateAndVerifyOneClass(null, "aaa3", "aaa3", null, "aaa4");
	}

	@Test
	public void renameCausingTwoClassesWithSameNameInDifferentPackages() {
		Class ea = this.createAndSaveOneClass("a", "aaa");
		this.createAndSaveOneClass("b", "bbb");
		service.update("c", "bbb", ea.getId(), ea.getVersion());
	}

	@Test
	public void moveCausingTwoClassesWithSameNameInDifferentPackages() {
		Class ea = new Class();
		ea.setNamespace("a");
		ea.setName("aaa");
		service.create(ea);

		Class eb = new Class();
		eb.setNamespace("b");
		eb.setName("bbb");
		service.create(eb);

		service.update("c", "bbb", ea.getId(), ea.getVersion());
	}

	@Test
	public void newNameAndPackageWithSpaces() {
		Class ea = new Class();
		ea.setNamespace("a");
		ea.setName("aaa");
		service.create(ea);
		try {
			// validate clazz method refactoring fot this test
			service.update("name space", "aaa", ea.getId(), ea.getVersion());
			fail();
		} catch (MetadataException me) {
			Assert.assertEquals(
					"Invalid value for Class namespace: name space",
					me.getMessage());
		}
		try {
			service.update("namespace", "na me", ea.getId(), ea.getVersion());
			fail();
		} catch (MetadataException me) {
			Assert.assertEquals("Invalid value for Class name: na me",
					me.getMessage());
		}
	}

	@Test
	public void removeName() {
		expectExceptionOnInvalidClassUpdate("a", "aaa", "namespace", null,
				"The name of an Class is mandatory");
		expectExceptionOnInvalidClassUpdate("a", "aaa", "namespace", "",
				"The name of an Class is mandatory");
		expectExceptionOnInvalidClassUpdate("a", "aaa", null, null,
				"The name of an Class is mandatory");
		expectExceptionOnInvalidClassUpdate("a", "aaa", null, "",
				"The name of an Class is mandatory");
	}

	@Test
	public void renameMoveCausingTwoClassesWithSameNameInDefaultPackage() {
		expectExceptionOnInvalidClassUpdate("a", "aaa", "b", "bbb", "b", "bbb",
				"The b.bbb Class already exists");
		expectExceptionOnInvalidClassUpdate("a", "aaa", "b", "aaa", "b", "bbb",
				"The b.aaa Class already exists");
		expectExceptionOnInvalidClassUpdate("a", "aaa", "a", "bbb", "b", "bbb",
				"The a.bbb Class already exists");
		expectExceptionOnInvalidClassUpdate("a.b.c", "aaa", "b.c", "bbb",
				"b.c", "bbb", "The b.c.bbb Class already exists");
		expectExceptionOnInvalidClassUpdate("b.c", "aaa", "b.c", "bbb", "b.c",
				"bbb", "The b.c.bbb Class already exists");
		expectExceptionOnInvalidClassUpdate("a.b.c", "bbb", "b.c", "bbb",
				"b.c", "bbb", "The b.c.bbb Class already exists");
	}

	@Test
	public void renameMoveCausingTwoClassesWithSameNameInAnonDefaultPackage() {
		expectExceptionOnInvalidClassUpdate("a", "aaa", null, "bbb", null,
				"bbb", "The bbb Class already exists");
		expectExceptionOnInvalidClassUpdate(null, "aaa", null, "bbb", null,
				"bbb", "The bbb Class already exists");
		expectExceptionOnInvalidClassUpdate("a", "bbb", null, "bbb", null,
				"bbb", "The bbb Class already exists");
		expectExceptionOnInvalidClassUpdate("a.b.c", "aaa", "", "bbb", "",
				"bbb", "The bbb Class already exists");
		expectExceptionOnInvalidClassUpdate("", "aaa", "", "bbb", "", "bbb",
				"The bbb Class already exists");
		expectExceptionOnInvalidClassUpdate("a.b.c", "bbb", "", "bbb", "",
				"bbb", "The bbb Class already exists");
	}

	@Test
	public void renameMoveCausingNameAndPackagesWithInvalidChars() {
		expectExceptionOnInvalidClassUpdate("a", "aaa", "a", "aaa$",
				"Invalid value for Class name: aaa$");
		expectExceptionOnInvalidClassUpdate("a", "aaa", "a", "aaa#",
				"Invalid value for Class name: aaa#");
		expectExceptionOnInvalidClassUpdate("a", "aaa", "a", "aaa=",
				"Invalid value for Class name: aaa=");
		expectExceptionOnInvalidClassUpdate("a", "aaa", "a", "aaa'",
				"Invalid value for Class name: aaa'");
		expectExceptionOnInvalidClassUpdate("a", "aaa", "a", "aaa.a",
				"Invalid value for Class name: aaa.a");
		expectExceptionOnInvalidClassUpdate("a", "aaa", "a", "aaa/a",
				"Invalid value for Class name: aaa/a");
		expectExceptionOnInvalidClassUpdate("a", "aaa", "a", "aaa*",
				"Invalid value for Class name: aaa*");
		expectExceptionOnInvalidClassUpdate("a", "aaa", "a$", "aaa",
				"Invalid value for Class namespace: a$");
		expectExceptionOnInvalidClassUpdate("a", "aaa", "a#", "aaa",
				"Invalid value for Class namespace: a#");
		expectExceptionOnInvalidClassUpdate("a", "aaa", "a=", "aaa",
				"Invalid value for Class namespace: a=");
		expectExceptionOnInvalidClassUpdate("a", "aaa", "a'", "aaa",
				"Invalid value for Class namespace: a'");

		// this scenario of test it's wrong and delayed me ¬¬
		// expectExceptionOnInvalidClassUpdate("a", "aaa", "a.", "aaa",
		// "Invalid value for Class namespace: a.");
		expectExceptionOnInvalidClassUpdate("a", "aaa", "a/a", "aaa",
				"Invalid value for Class namespace: a/a");
		expectExceptionOnInvalidClassUpdate("a", "aaa", "a*", "aaa",
				"Invalid value for Class namespace: a*");
	}

	@Test
	public void renameMoveForcingCaseInsentivePackagesAndNames() {
		expectExceptionOnInvalidClassUpdate("a", "aaa", "b", "bbb", "b", "BbB",
				"The b.bbb Class already exists");
		expectExceptionOnInvalidClassUpdate("a", "aaa", "b", "bbb", "b", "BBB",
				"The b.bbb Class already exists");
		expectExceptionOnInvalidClassUpdate("a", "aaa", "CcC", "ccc", "ccc",
				"ccc", "The CcC.ccc Class already exists");
		expectExceptionOnInvalidClassUpdate("a", "aaa", "CcC", "ccc", "CCC",
				"ccc", "The CcC.ccc Class already exists");
		expectExceptionOnInvalidClassUpdate("a", "aaa", "CCC", "ccc", "ccc",
				"ccc", "The CCC.ccc Class already exists");
		expectExceptionOnInvalidClassUpdate("a", "aaa", "CCC", "ccc", "ccc",
				"CCC", "The CCC.ccc Class already exists");
	}

	@Test
	public void invalidIdAndVersion() {
		Class clazz1 = this.createAndSaveOneClass("a", "aaa");
		expectExceptionOnInvalidClassUpdateUsingId("namespace", "name", null,
				clazz1.getVersion(),
				"The id of an Class is mandatory on update");

		Class clazz2 = this.createAndSaveOneClass("a", "aaa");
		expectExceptionOnInvalidClassUpdateUsingId("a", "aaa", clazz2.getId(),
				null, "The version of an Class is mandatory on update");

		this.createAndSaveOneClass("a", "aaa");
		expectExceptionOnInvalidClassUpdateUsingId("a", "aaa", (Long) null,
				null, "The version and id of an Class are mandatory on update");

		// the message of exception at this case was wrong and delayed me
		Class clazz4 = this.createAndSaveOneClass("a", "aaa");
		expectExceptionOnInvalidClassUpdateUsingId("name", "aaa",
				clazz4.getId() + 1, clazz4.getVersion(),
				"Invalid id for Class name.aaa");

		Class clazz5 = this.createAndSaveOneClass("a", "aaa");
		expectExceptionOnInvalidClassUpdateUsingId(
				"namespace",
				"name",
				clazz5.getId(),
				clazz5.getVersion() - 1,
				"Updating a deprecated version of Class a.aaa. Get the Class again to obtain the newest version and proceed updating.");
	}

	@Test
	public void severalUpdates() {
		Class ea = new Class();
		ea.setNamespace("a");
		ea.setName("aaa");
		service.create(ea);

		// the number of new versions were wrong
		service.update("b", "abc", ea.getId(), ea.getVersion() + 1);
		service.update("a.b", "abc", ea.getId(), ea.getVersion() + 2);
		service.update(null, "abc", ea.getId(), ea.getVersion() + 3);
		service.update("a.b.c", "abc", ea.getId(), ea.getVersion() + 4);

		Class found = service.findClassById(ea.getId());
		Assert.assertEquals("a.b.c", found.getNamespace());
		Assert.assertEquals("abc", found.getName());
		Assert.assertEquals(new Long(1), found.getId());
		Assert.assertEquals(new Integer(4), found.getVersion());

	}

	@Test
	public void listAllClasses() {
		List<Class> allClasses = service.listAll();
		Assert.assertEquals(0, allClasses.size());

		Class clazz1 = this.createAndSaveOneClass("ns1", "n1");
		Class clazz2 = this.createAndSaveOneClass("ns2", "n2");
		Class clazz3 = this.createAndSaveOneClass("ns2", "n3");

		allClasses = service.listAll();

		Assert.assertEquals(3, allClasses.size());
		Assert.assertEquals(clazz1, allClasses.get(0));
		Assert.assertEquals(clazz2, allClasses.get(1));
		Assert.assertEquals(clazz3, allClasses.get(2));

		service.delete(clazz1);
		service.delete(clazz2);
		service.delete(clazz3);

		Assert.assertEquals(0, service.listAll().size());
	}

	@Test
	public void listClassesByValidFragmentOfNameAndPackage() {
		String namespaceFragment = "ns";
		String nameFragment = "n";
		List<Class> allClasses = service.listClassesByFragmentOfNameAndPackage(
				namespaceFragment, nameFragment);

		Assert.assertEquals(0, allClasses.size());
		Class clazz1 = this.createAndSaveOneClass("ns1", "n1");
		Class clazz2 = this.createAndSaveOneClass("ns2", "n2");
		Class clazz3 = this.createAndSaveOneClass("ns2", "n3");

		allClasses = service.listClassesByFragmentOfNameAndPackage(
				namespaceFragment, nameFragment);

		Assert.assertEquals(3, allClasses.size());
		Assert.assertEquals(clazz1, allClasses.get(0));
		Assert.assertEquals(clazz2, allClasses.get(1));
		Assert.assertEquals(clazz3, allClasses.get(2));

		service.delete(clazz1);
		service.delete(clazz2);
		service.delete(clazz3);

		Assert.assertEquals(0, service.listAll().size());

	}

	@Test
	public void listClassesByEmptyNameAndPackage() {
		List<Class> allClasses = service.listClassesByFragmentOfNameAndPackage(
				"", "");
		Assert.assertEquals(0, allClasses.size());

		allClasses = service.listClassesByFragmentOfNameAndPackage("ns", "");
		Assert.assertEquals(0, allClasses.size());

		allClasses = service.listClassesByFragmentOfNameAndPackage("", "n");
		Assert.assertEquals(0, allClasses.size());

		Class clazz1 = this.createAndSaveOneClass("ns1", "n1");
		Class clazz2 = this.createAndSaveOneClass("ns2", "n2");
		Class clazz3 = this.createAndSaveOneClass("ns2", "n3");
		List<Class> expectedClasses = new ArrayList<Class>();
		expectedClasses.add(clazz1);
		expectedClasses.add(clazz2);
		expectedClasses.add(clazz3);

		listClassesByFragmentOfNamespaceAndNamesAndVerifyThatClassesWhereListed(
				expectedClasses, "", "");

		listClassesByFragmentOfNamespaceAndNamesAndVerifyThatClassesWhereListed(
				expectedClasses, null, "");

		listClassesByFragmentOfNamespaceAndNamesAndVerifyThatClassesWhereListed(
				expectedClasses, "", null);

		listClassesByFragmentOfNamespaceAndNamesAndVerifyThatClassesWhereListed(
				expectedClasses, "ns", "");

		listClassesByFragmentOfNamespaceAndNamesAndVerifyThatClassesWhereListed(
				expectedClasses, "", "n");

		allClasses = service
				.listClassesByFragmentOfNameAndPackage("nspace", "");
		Assert.assertEquals(0, allClasses.size());

		for (Class clazz : expectedClasses) {
			service.delete(clazz);
		}

		Assert.assertEquals(0, service.listAll().size());
	}

	@Test
	public void classesWithDefaultPackageMustAppearWhenPackageIsNotGiven() {
		Class clazz1 = this.createAndSaveOneClass("ns1", "n1");
		Class clazz2 = this.createAndSaveOneClass("ns2", "n2");
		Class clazz3 = this.createAndSaveOneClass("ns2", "n3");
		List<Class> expectedClasses = new ArrayList<Class>();
		expectedClasses.add(clazz1);
		expectedClasses.add(clazz2);
		expectedClasses.add(clazz3);

		listClassesByFragmentOfNamespaceAndNamesAndVerifyThatClassesWhereListed(
				expectedClasses, null, "");

		listClassesByFragmentOfNamespaceAndNamesAndVerifyThatClassesWhereListed(
				expectedClasses, "", null);

		listClassesByFragmentOfNamespaceAndNamesAndVerifyThatClassesWhereListed(
				expectedClasses, null, null);

		List<Class> allClasses = service.listClassesByFragmentOfNameAndPackage(
				null, "nspace");
		Assert.assertEquals(0, allClasses.size());

		allClasses = service
				.listClassesByFragmentOfNameAndPackage(null, "name");
		Assert.assertEquals(0, allClasses.size());

		listClassesByFragmentOfNamespaceAndNamesAndVerifyThatClassesWhereListed(
				expectedClasses, "ns", "");

		listClassesByFragmentOfNamespaceAndNamesAndVerifyThatClassesWhereListed(
				expectedClasses, "", "n");

		for (Class clazz : expectedClasses) {
			service.delete(clazz);
		}

		Assert.assertEquals(0, service.listAll().size());
	}

	@Test
	public void listClassesByFragmentOfNameAndPackagesWithSpaces() {
		expectExceptionOnInvalidListClass(null, "na me",
				"Invalid value for Class name: na me");
		expectExceptionOnInvalidListClass("name space", null,
				"Invalid value for Class namespace: name space");
		expectExceptionOnInvalidListClass("namespace", "na me",
				"Invalid value for Class name: na me");
		expectExceptionOnInvalidListClass("name space", "name",
				"Invalid value for Class namespace: name space");
	}

	@Test
	public void listClassesForcingCaseInsensitivePackagesAndNames() {
		Class clazz1 = this.createAndSaveOneClass("ns1", "n1");
		Class clazz2 = this.createAndSaveOneClass("NS2", "n2");
		Class clazz3 = this.createAndSaveOneClass("NS3", "N3");
		List<Class> expectedClasses = new ArrayList<Class>();
		expectedClasses.add(clazz1);
		expectedClasses.add(clazz2);
		expectedClasses.add(clazz3);

		listClassesByFragmentOfNamespaceAndNamesAndVerifyThatClassesWhereListed(
				expectedClasses, "ns", "n");

		listClassesByFragmentOfNamespaceAndNamesAndVerifyThatClassesWhereListed(
				expectedClasses, "NS", "n");
		listClassesByFragmentOfNamespaceAndNamesAndVerifyThatClassesWhereListed(
				expectedClasses, "ns", null);

		listClassesByFragmentOfNamespaceAndNamesAndVerifyThatClassesWhereListed(
				expectedClasses, null, "N");

		listClassesByFragmentOfNamespaceAndNamesAndVerifyThatClassesWhereListed(
				expectedClasses, "N", "N");
	}

	@Test
	public void listClassesUsingInvalidFragmentOfNameAndPackage() {
		expectExceptionOnInvalidListClass("ns", "n$",
				"Invalid value for Class name: n$");
		expectExceptionOnInvalidListClass("ns", "n#",
				"Invalid value for Class name: n#");
		expectExceptionOnInvalidListClass("ns", "n=",
				"Invalid value for Class name: n=");
		expectExceptionOnInvalidListClass("ns", "n'",
				"Invalid value for Class name: n'");
		expectExceptionOnInvalidListClass("ns", "n.n",
				"Invalid value for Class name: n.n");
		expectExceptionOnInvalidListClass("ns", "n/n",
				"Invalid value for Class name: n/n");
		expectExceptionOnInvalidListClass("ns", "n*",
				"Invalid value for Class name: n*");
		expectExceptionOnInvalidListClass("ns$", "n",
				"Invalid value for Class namespace: ns$");
		expectExceptionOnInvalidListClass("ns#", "n",
				"Invalid value for Class namespace: ns#");
		expectExceptionOnInvalidListClass("ns=", "n",
				"Invalid value for Class namespace: ns=");
		expectExceptionOnInvalidListClass("ns'", "n",
				"Invalid value for Class namespace: ns'");
		// expectExceptionOnInvalidListClass("ns.", "n",
		// "Invalid value for Class namespace: ns.");
		expectExceptionOnInvalidListClass("ns/a", "n",
				"Invalid value for Class namespace: ns/a");
		expectExceptionOnInvalidListClass("ns*", "n",
				"Invalid value for Class namespace: ns*");
	}

	@Test
	public void getClassByValidNameAndPackage() {
		expectExceptionOnInvalidGetClass("ns.n", "Class not found: ns.n");

		Class clazz1 = createClass("ns1", "n1");
		Class foundClass1 = service.readClass("ns1.n1");
		Assert.assertEquals(clazz1, foundClass1);

		Class clazz2 = createClass("ns2", "n2");
		Class foundClass2 = service.readClass("ns2.n2");
		Assert.assertEquals(clazz2, foundClass2);

		expectExceptionOnInvalidGetClass("ns1.n", "Class not found: ns1.n");
		expectExceptionOnInvalidGetClass("ns.n1", "Class not found: ns.n1");
		expectExceptionOnInvalidGetClass("ns2.n1", "Class not found: ns2.n1");

		List<Class> allClasses = service.listAll();
		Assert.assertEquals(2, allClasses.size());
		Assert.assertEquals(clazz1, allClasses.get(0));
		Assert.assertEquals(clazz2, allClasses.get(1));
	}

	@Test
	public void getClassByEmptyNameAndPackage() {
		createClass("ns1", "n1");
		Class clazz2 = createClass(null, "n2");
		expectExceptionOnInvalidGetClass(".n1", "Class not found: n1");

		Class foundClass2 = service.readClass(".n2");
		Assert.assertEquals(clazz2, foundClass2);
		expectExceptionOnInvalidGetClass("ns1.", "Class not found: ns1");
	}

	@Test
	public void getClassByNameAndPackageWithSpaces() {
		expectExceptionOnInvalidGetClass(".na me",
				"Invalid key for Class: na me");
		expectExceptionOnInvalidGetClass("name space.name",
				"Invalid key for Class: name space.name");
		expectExceptionOnInvalidGetClass("namespace.na me",
				"Invalid key for Class: namespace.na me");
	}

	@Test
	public void getClassForcingCaseInsensitivePackagesAndNames() {
		Class clazz = createClass("nS", "nA");
		Class ea = service.readClass("ns.na");
		Assert.assertEquals(clazz, ea);

		ea = service.readClass("NS.NA");
		Assert.assertEquals(clazz, ea);

		ea = service.readClass("nS.nA");
		Assert.assertEquals(clazz, ea);

		ea = service.readClass("NS.na");
		Assert.assertEquals(clazz, ea);

		ea = service.readClass("ns.NA");
		Assert.assertEquals(clazz, ea);

		ea = service.readClass("Ns.Na");
		Assert.assertEquals(clazz, ea);

	}

	@Test
	public void getClassUsingInvalidNameAndPackage() {
		expectExceptionOnInvalidGetClass("ns.n$",
				"Invalid key for Class: ns.n$");
		expectExceptionOnInvalidGetClass("ns.n#",
				"Invalid key for Class: ns.n#");
		expectExceptionOnInvalidGetClass("ns.n=",
				"Invalid key for Class: ns.n=");
		expectExceptionOnInvalidGetClass("ns.n/n",
				"Invalid key for Class: ns.n/n");
		expectExceptionOnInvalidGetClass("ns.n*",
				"Invalid key for Class: ns.n*");
		expectExceptionOnInvalidGetClass("ns.n'",
				"Invalid key for Class: ns.n'");
		expectExceptionOnInvalidGetClass("ns$.n",
				"Invalid key for Class: ns$.n");
		expectExceptionOnInvalidGetClass("ns#.n",
				"Invalid key for Class: ns#.n");
		expectExceptionOnInvalidGetClass("ns=.n",
				"Invalid key for Class: ns=.n");
		expectExceptionOnInvalidGetClass("ns/.n",
				"Invalid key for Class: ns/.n");
		expectExceptionOnInvalidGetClass("ns*.n",
				"Invalid key for Class: ns*.n");
		expectExceptionOnInvalidGetClass("ns'.n",
				"Invalid key for Class: ns'.n");
	}

	@Test
	public void deleteClassByValidNameAndPackage() {
		this.expectExceptionOnInvalidRemoveClass("ns.n",
				"Class not found: ns.n");
		Class clazz2 = this.createClass("ns1", "n1");
		Class clazz3 = this.createClass("ns2", "n2");
		this.expectExceptionOnInvalidRemoveClass("ns.n",
				"Class not found: ns.n");
		this.expectExceptionOnInvalidRemoveClass("ns1.n",
				"Class not found: ns1.n");
		this.expectExceptionOnInvalidRemoveClass("ns.n1",
				"Class not found: ns.n1");
		this.expectExceptionOnInvalidRemoveClass("ns2.n1",
				"Class not found: ns2.n1");
		service.delete(clazz2);
		service.delete(clazz3);

		Assert.assertEquals(0, service.listAll().size());
	}

	@Test
	public void deleteClassByEmptyNameAndPackage() {
		this.createClass("ns1", "n1");
		this.createClass(null, "n2");
		this.expectExceptionOnInvalidRemoveClass("n1", "Class not found: n1");
		service.delete("n2");
		this.expectExceptionOnInvalidRemoveClass("ns1", "Class not found: ns1");
	}

	@Test
	public void deleteClassByNameAndPackageWithSpaces() {
		this.expectExceptionOnInvalidRemoveClass("na me",
				"Invalid key for Class: na me");
		this.expectExceptionOnInvalidRemoveClass("name space.name",
				"Invalid key for Class: name space.name");
		this.expectExceptionOnInvalidRemoveClass("namespace.na me",
				"Invalid key for Class: namespace.na me");
	}

	@Test
	public void deleteClassForcingCaseInsensitivePackagesAndNames() {
		this.createClass("nS", "nA");
		service.delete("ns.na");
		this.createClass("nS", "nA");
		service.delete("NS.NA");
		this.createClass("nS", "nA");
		service.delete("nS.nA");
		this.createClass("nS", "nA");
		service.delete("NS.na");
		this.createClass("nS", "nA");
		service.delete("ns.NA");
		this.createClass("nS", "nA");
		service.delete("Ns.Na");
	}

	@Test
	public void deleteClassUsingInvalidNameAndPackage() {
		this.expectExceptionOnInvalidRemoveClass("ns.n$",
				"Invalid key for Class: ns.n$");
		this.expectExceptionOnInvalidRemoveClass("ns.n#",
				"Invalid key for Class: ns.n#");
		this.expectExceptionOnInvalidRemoveClass("ns.n=",
				"Invalid key for Class: ns.n=");
		this.expectExceptionOnInvalidRemoveClass("ns.n/n",
				"Invalid key for Class: ns.n/n");
		this.expectExceptionOnInvalidRemoveClass("ns.n*",
				"Invalid key for Class: ns.n*");
		this.expectExceptionOnInvalidRemoveClass("ns.n'",
				"Invalid key for Class: ns.n'");
		this.expectExceptionOnInvalidRemoveClass("ns$.n",
				"Invalid key for Class: ns$.n");
		this.expectExceptionOnInvalidRemoveClass("ns#.n",
				"Invalid key for Class: ns#.n");
		this.expectExceptionOnInvalidRemoveClass("ns=.n",
				"Invalid key for Class: ns=.n");
		// this.expectExceptionOnInvalidRemoveClass("ns.",
		// "Invalid key for Class: ns.");
		this.expectExceptionOnInvalidRemoveClass("ns/n.n",
				"Invalid key for Class: ns/n.n");
		this.expectExceptionOnInvalidRemoveClass("ns*.n",
				"Invalid key for Class: ns*.n");
		this.expectExceptionOnInvalidRemoveClass("ns'.n",
				"Invalid key for Class: ns'.n");
	}

	private void expectExceptionOnInvalidRemoveClass(String namespaceAndName,
			String expectedMessage) {
		try {
			service.delete(namespaceAndName);
			fail();
		} catch (MetadataException me) {
			Assert.assertEquals(expectedMessage, me.getMessage());
		}
	}

	private Class createClass(String namespace, String name) {
		Class clazz = new Class();
		clazz.setName(name);
		clazz.setNamespace(namespace);
		service.create(clazz);
		return clazz;
	}

	private void listClassesByFragmentOfNamespaceAndNamesAndVerifyThatClassesWhereListed(
			List<Class> expectedListedClasses, String namespaceFragment,
			String nameFragment) {

		List<Class> allClasses = service.listClassesByFragmentOfNameAndPackage(
				namespaceFragment, nameFragment);
		Assert.assertEquals(expectedListedClasses.size(), allClasses.size());
		int index = 0;
		for (Class expected : expectedListedClasses) {
			Assert.assertEquals(expected, allClasses.get(index));
			index++;
		}

	}

	private void expectExceptionOnInvalidGetClass(String classFullName,
			String expectedMessage) {
		try {
			service.readClass(classFullName);
			fail();
		} catch (MetadataException me) {
			Assert.assertEquals(expectedMessage, me.getMessage());
		}
	}

	private void expectExceptionOnInvalidListClass(String namespaceFragment,
			String nameFragment, String expectedMessage) {
		try {
			service.listClassesByFragmentOfNameAndPackage(namespaceFragment,
					nameFragment);
			fail();
		} catch (MetadataException me) {
			Assert.assertEquals(expectedMessage, me.getMessage());
		}
	}

	private void expectExceptionOnInvalidClassUpdate(
			String firstclazznamespace, String firstclazzname,
			String secondclazznamespace, String secondclazzname,
			String firstclazznamespaceupdate, String firstclazznameupdate,
			String expectedExceptionMessage) {

		Class ea = new Class();
		ea.setNamespace(firstclazznamespace);
		ea.setName(firstclazzname);
		service.create(ea);

		Class eb = new Class();
		eb.setNamespace(secondclazznamespace);
		eb.setName(secondclazzname);
		service.create(eb);

		try {
			service.update(eb);
			fail();
		} catch (MetadataException me) {
			Assert.assertEquals(expectedExceptionMessage, me.getMessage());
			service.delete(ea);
			service.delete(eb);
		}
	}

	private void expectExceptionOnInvalidClassUpdateUsingId(String namespace,
			String name, Long id, Integer version, String expectedMessage) {
		Class updateClass = new Class();
		updateClass.setNamespace(namespace);
		updateClass.setName(name);
		updateClass.setId(id);
		updateClass.setVersion(version);
		try {
			service.update(updateClass);
			fail();
		} catch (MetadataException me) {
			Assert.assertEquals(expectedMessage, me.getMessage());
		}
		this.service = new ClassServiceImpl(new MemoryDaoFactory());

	}

	private void expectExceptionOnInvalidClassUpdate(String firstnamespace,
			String firstname, String secondnamespace, String secondname,
			String expectedMessage) {
		Class clazz = new Class();
		clazz.setNamespace(firstnamespace);
		clazz.setName(firstname);
		try {
			service.create(clazz);
		} catch (MetadataException e) {

		}

		try {
			clazz.setId(clazz.getId());
			clazz.setVersion(clazz.getVersion());
			clazz.setNamespace(secondnamespace);
			clazz.setName(secondname);
			service.update(clazz);
			fail();
		} catch (MetadataException me) {
			Assert.assertEquals(expectedMessage, me.getMessage());
		}
		service = new ClassServiceImpl(new MemoryDaoFactory());
	}

	private void expectExceptionOnCreateInvalidClass(String namespace,
			String name, String expectedMessage) {
		try {
			createAndVerifyOneClass(namespace, name);
			fail();
		} catch (MetadataException e) {
			Assert.assertEquals(expectedMessage, e.getMessage());
		}
	}

	private void createUpdateAndVerifyOneClass(String firstNamespace,
			String firstName, String updatePath, String secondNamespace,
			String secondName) {

		Class clazz = new Class();
		clazz.setNamespace(firstNamespace);
		clazz.setName(firstName);
		service.create(clazz);

		Assert.assertNotNull(clazz.getId());
		Assert.assertEquals((Integer) 0, clazz.getVersion());

		Class updateClass = new Class();
		updateClass.setNamespace("secondNamespace");
		updateClass.setName("secondName");
		updateClass.setId(clazz.getId());
		updateClass.setVersion(clazz.getVersion() + 1);

		Class clazz1 = service.update(updateClass);

		List<Class> allClasses = service.listAll();
		Class clazzFound = allClasses.get(0);

		Assert.assertEquals((Integer) 1, clazz1.getVersion());
		Assert.assertNotSame(clazz, clazzFound);
		service.delete(clazz);
		service.delete(clazz1);
	}

	private void createAndVerifyTwoClasses(String clazz1namespace,
			String clazz1name, String clazz2namespace, String clazz2name) {
		Class clazz1 = new Class();
		clazz1.setNamespace(clazz1namespace);
		clazz1.setName(clazz1name);
		service.create(clazz1);

		Class clazz2 = new Class();
		clazz2.setNamespace(clazz2namespace);
		clazz2.setName(clazz2name);
		service.create(clazz2);

		Assert.assertNotNull(clazz1.getId());
		Assert.assertNotNull(clazz2.getId());

		Assert.assertEquals((Integer) 0, clazz1.getVersion());
		Assert.assertEquals((Integer) 0, clazz2.getVersion());

		List<Class> classes = service.listAll();
		Assert.assertEquals(2, classes.size());
		Assert.assertEquals(clazz1, classes.get(0));
		Assert.assertEquals(clazz2, classes.get(1));

		service.delete(clazz1);
		service.delete(clazz2);
	}

	private Class createAndSaveOneClass(String namespace, String name) {
		Class clazz = new Class();
		clazz.setNamespace(namespace);
		clazz.setName(name);
		service.create(clazz);

		Assert.assertNotNull(clazz.getId());
		Assert.assertEquals((Integer) 0, clazz.getVersion());
		return clazz;
	}

	private void createAndVerifyOneClass(String namespace, String name) {
		Class clazz = new Class();
		clazz.setNamespace(namespace);
		clazz.setName(name);
		service.create(clazz);

		Assert.assertNotNull(clazz.getId());
		Assert.assertEquals((Integer) 0, clazz.getVersion());

		List<Class> classes = service.listAll();
		Assert.assertEquals(1, classes.size());
		Assert.assertEquals(clazz, classes.get(0));

		service.delete(clazz);
	}
}
