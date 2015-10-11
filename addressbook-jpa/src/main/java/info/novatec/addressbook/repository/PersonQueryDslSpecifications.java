package info.novatec.addressbook.repository;

import info.novatec.addressbook.entity.QPerson;

import java.util.Calendar;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.Predicate;

/**
 * Specifications for {@link Person} taken from the concept in 
 * Eric Evans' book "Domain Driven Design".
 */
public final class PersonQueryDslSpecifications {
	private static final int OF_AGE = -18;
	
	private PersonQueryDslSpecifications() {
		super();
	}

	/**
	 * Specification to query {@link Person}s that are <em>of age</em>, i.e. over 18 years old.
	 * @return {@link Specification}
	 */
	public static Predicate isOfAge() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, OF_AGE);

		QPerson person = QPerson.person;
		return person.birthDate.before(cal.getTime());
	}
	
	/**
	 * Specification to query {@link Person}s that are <em>of age</em>, i.e. over 18 years old 
	 * and have at least one address.
	 * @return {@link Specification}
	 */
	public static Predicate isOfAgeAndHasAnAddress() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, OF_AGE);

		QPerson person = QPerson.person;
		
		BooleanBuilder builder = new BooleanBuilder();
		
		return builder.and(person.birthDate.before(cal.getTime())).and(
				person.addresses.isNotEmpty());
	}
	
	/**
	 * Specification to query {@link Person}s that are <em>minor</em>, i.e. under 18 years old.
	 * @return {@link Specification}
	 */
	public static Predicate isMinor() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, OF_AGE);

		QPerson person = QPerson.person;
		return person.birthDate.after(cal.getTime());
	}
}
