package info.novatec.addressbook;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import info.novatec.addressbook.boundary.PersonManagementService;
import info.novatec.addressbook.entity.Address;
import info.novatec.addressbook.entity.Country;
import info.novatec.addressbook.entity.Person;

import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Integration test verifying {@link PersonManagementService}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Transactional
public class ApplicationTests {
	private static final int EXPECTED_NUMBER_OF_PERSONS_AFTER_DELETION = 5;

	private static final long EXPECTED_COUNT_OF_PERSONS_AFTER_DELETION = 5L;

	private static final int EXPECTED_NUMBER_OF_PERSONS = 6;

	private static final int EXPECTED_NUMBER_OF_AGE_PERSONS = 3;

	private static final int EXPECTED_NUMBER_OF_AGE_PERSONS_WITH_LASTNAME_K = 1;

	private static final int EXPECTED_NUMBER_OF_MINOR_PERSONS = 3;

	@Autowired
	private PersonManagementService personManagementService;
	
	private TransactionTemplate transactionTemplate;
	
	@SuppressWarnings("unused")
	private Person person1, person2, person3, person4, person5, person6;
	
	/**
	 * Initialize test data.
	 */
	@Before
	public final void setupTestData() {
		Calendar cal = Calendar.getInstance();
		cal.set(1969, 6, 2);
		person1 = personManagementService.createPerson("Hans", "Mustermann", cal.getTime());
		
		cal.set(1972, 8, 20);
		person2 = personManagementService.createPerson("Hansi", "Müller", cal.getTime());
		
		cal.set(2010, 10, 5);
		person3 = personManagementService.createPerson("Sami", "Khedira", cal.getTime());
		
		cal.set(2008, 9, 15);
		person4 = personManagementService.createPerson("Jogi", "Löw", cal.getTime(), 
				new Address("Hauptstr.3", null, "55555", "Freiburg", Country.DE),
				new Address("Bernerstr.33", null, "12345", "Wien", Country.AT));
		
		cal.set(2000, 3, 6);
		person5 = personManagementService.createPerson("Phillip", "Lahm", cal.getTime(), 
				new Address("Hauptstr.3", null, "77777", "Stuttgart", Country.DE),
				new Address("Bernerstr.33", null, "12345", "Bern", Country.CH));
		
		cal.set(1958, 7, 16);		
		person6 = personManagementService.createPerson("Toni", "Kroos", cal.getTime(), 
				new Address("Bayernstr.99", null, "88888", "München", Country.DE),
				new Address("Bernerstr.33", null, "12345", "Bern", Country.CH));
	}
	
	/**
	 * Verifies finding {@link Person} by first and last name.
	 */
	@Test
	public final void verifyFindByFirstAndLastName() {
		Person person = personManagementService.findByFirstNameAndLastName("Hans", "Mustermann");
		assertThat("Should have retrieved valid person", person, is(notNullValue()));
		assertThat("Should have retrieved expected person", person, is(person1));
	}

	/**
	 * Verifies finding all {@link Person}s.
	 */
	@Test
	public final void verifyFindAllPersons() {
		List<Person> persons = personManagementService.findAll();
		assertThat("Should have retrieved valid person list", persons, is(notNullValue()));
		assertThat("Should have retrieved expected number of persons", persons.size(), 
				is(EXPECTED_NUMBER_OF_PERSONS));
	}
	
	/**
	 * Verifies finding {@link Person} together with {@link Address}es.
	 */
	@Test
	public final void verifyFindPersonWithAddress() {
		Person person = personManagementService.findOneWithAddresses(person1.getId());
		assertThat("Should have retrieved valid person", person, is(notNullValue()));
		assertThat("Should have retrieved expected number of addresses for person", 
				person.getAddresses().size(), is(0));

		person = personManagementService.findOneWithAddresses(person4.getId());
		assertThat("Should have retrieved valid person", person, is(notNullValue()));
		assertThat("Should have retrieved expected number of addresses for person", 
				person.getAddresses().size(), is(2));

	}
	
	/**
	 * Verifies finding {@link Person} together with {@link Address}es.
	 */
	@Test
	public final void verifyFindPersonWithAddressEntityGraph() {
		Person person = personManagementService.findOneById(person1.getId());
		assertThat("Should have retrieved valid person", person, is(notNullValue()));
		assertThat("Should have retrieved expected number of addresses for person", 
				person.getAddresses().size(), is(0));

		person = personManagementService.findOneById(person4.getId());
		assertThat("Should have retrieved valid person", person, is(notNullValue()));
		assertThat("Should have retrieved expected number of addresses for person", 
				person.getAddresses().size(), is(2));

	}
	
	/**
	 * Verifies finding {@link Person}s living in 'Bern'.
	 */
	@Test
	public final void verifyFindPersonLivingInBern() {
		List<Person> persons = personManagementService.findAllLivingInCity("Bern");
		assertThat("Should have retrieved valid list of persons", persons, is(notNullValue()));
		assertThat("Should have retrieved expected number of persons living in 'Bern'", 
				persons.size(), is(2));
		assertThat("Should have retrieved expected persons living in 'Bern'", 
				persons, contains(person5, person6));

	}
	
	/**
	 * Verifies deleting a {@link Person}.
	 */
	@Test
	public final void verifyDeletePerson() {
		List<Person> persons = personManagementService.findAll();
		assertThat("Should have retrieved valid person list", persons, is(notNullValue()));
		assertThat("Should have retrieved expected number of persons", persons.size(), 
				is(EXPECTED_NUMBER_OF_PERSONS));
		
		personManagementService.delete(persons.get(0));

		assertThat("Should have retrieved expected person count", 
				personManagementService.count(), is(EXPECTED_COUNT_OF_PERSONS_AFTER_DELETION));
		
		persons = personManagementService.findAll();
		assertThat("Should have retrieved valid person list", persons, is(notNullValue()));
		assertThat("Should have retrieved expected number of persons", persons.size(), 
				is(EXPECTED_NUMBER_OF_PERSONS_AFTER_DELETION));
		
	}
	
	/**
	 * Verifies retrieving list of {@link Person}s that are 'of age'.
	 */
	@Test
	public final void verifyOfAgePersons() {
		List<Person> persons = personManagementService.findAllOfAge();
		assertThat("Should have retrieved valid person list", persons, is(notNullValue()));
		assertThat("Should have retrieved expected number of persons", persons.size(), 
				is(EXPECTED_NUMBER_OF_AGE_PERSONS));
	}

	/**
	 * Verifies retrieving list of {@link Person}s that are 'minor'.
	 */
	@Test
	public final void verifyMinorPersons() {
		List<Person> persons = personManagementService.findAllMinors();
		assertThat("Should have retrieved valid person list", persons, is(notNullValue()));
		assertThat("Should have retrieved expected number of persons", persons.size(), 
				is(EXPECTED_NUMBER_OF_MINOR_PERSONS));
	}
	
	/**
	 * Verifies retrieving list of {@link Person}s that are 'of age' and 
	 * last name beginning with 'K'.
	 */
	@Test
	public final void verifyOfAgePersonsWithLastName() {
		List<Person> persons = personManagementService.findAllOfAgeWithLastName("K");
		assertThat("Should have retrieved valid person list", persons, is(notNullValue()));
		assertThat("Should have retrieved expected number of persons", persons.size(), 
				is(EXPECTED_NUMBER_OF_AGE_PERSONS_WITH_LASTNAME_K));
	}

	/**
	 * Validates that database is empty after executing transactional test.
	 */
	@AfterTransaction
	public final void verifyPostCleanDatabase() {
		List<Person> list = transactionTemplate.execute(new TransactionCallback<List<Person>>() {
			@Override
			public List<Person> doInTransaction(final TransactionStatus status) {
				return personManagementService.findAll();
			}
		});
		
		assertThat("Should have retrieved valid list of persons", list, is(notNullValue()));
		assertThat("Should have retrieved expected number of persons", list.size(), is(0));
	}
	
	/**
	 * Sets the {@link PlatformTransactionManager} for transaction template.
	 * @param platformTransactionManager the platform transaction manager
	 */
	@Autowired
	public final void setPlatFormTransactionManager(
			final PlatformTransactionManager platformTransactionManager) {
		transactionTemplate = new TransactionTemplate(platformTransactionManager);
	}
	
}
