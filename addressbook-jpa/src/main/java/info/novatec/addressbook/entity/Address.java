package info.novatec.addressbook.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.jpa.domain.AbstractPersistable;

@SuppressWarnings("serial")
@Entity
public class Address extends AbstractPersistable<Long> {
	@Size(min = 0, max = 30)
	@Column(nullable = true, length = 30)
	private String street;

	@Size(min = 1, max = 30)
	@Column(nullable = true, length = 20)
	private String postOfficeBox;

	@NotNull
	@Size(min = 1, max = 10)
	@Column(nullable = false, length = 10)
	private String zip;
	
	@NotNull
	@Size(min = 1, max = 30)
	@Column(nullable = false, length = 30)
	private String city;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Country country;
	
	@NotNull
	@OneToOne
	private Person person;

	/**
	 * Constructor.
	 * @param street to set
	 * @param postOfficeBox to set
	 * @param zip to set
	 * @param city to set
	 * @param country to set
	 */
	public Address(final String street, final String postOfficeBox, final String zip,
			final String city, final Country country) {
		this(null, street, postOfficeBox, zip, city, country);
	}
	
	/**
	 * Constructor.
	 * @param person to set
	 * @param street to set
	 * @param postOfficeBox to set
	 * @param zip to set
	 * @param city to set
	 * @param country to set
	 */
	public Address(final Person person, final String street, 
			final String postOfficeBox, final String zip,
			final String city, final Country country) {
		super();
		this.person = person;
		this.street = street;
		this.postOfficeBox = postOfficeBox;
		this.zip = zip;
		this.city = city;
		this.country = country;
	}
	
	public Address() {
		super();
	}
	
	public String getStreet() {
		return street;
	}
	
	public String getPostOfficeBox() {
		return postOfficeBox;
	}
	
	public String getZip() {
		return zip;
	}
	
	public String getCity() {
		return city;
	}
	
	public Country getCountry() {
		return country;
	}
	
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
}
