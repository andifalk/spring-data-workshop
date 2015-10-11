package info.novatec.addressbook.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.jpa.domain.AbstractPersistable;

@SuppressWarnings("serial")
@NamedEntityGraph(name = "Person.addresses", attributeNodes = @NamedAttributeNode("addresses"))
@Entity
public class Person extends AbstractPersistable<Long> {

	@NotNull
	@Size(min = 1, max = 30)
	@Column(nullable = false, length = 30)
	private String firstName;
	
	@NotNull
	@Size(min = 1, max = 30)
	@Column(nullable = false, length = 30)
	private String lastName;

	@NotNull
	@Temporal(TemporalType.DATE)
	private Date birthDate;
	
	@Valid
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Address> addresses = new HashSet<>();
	
	public Person() {
		super();
	}

	/**
	 * Constructor.
	 * @param firstName first name
	 * @param lastName last name
	 * @param birthDate birth date
	 * @param addresses {@link Address}es
	 */
	public Person(final String firstName, final String lastName, final Date birthDate,
			final Set<Address> addresses) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.addresses = addresses;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public Date getBirthDate() {
		return birthDate;
	}
	
	public Set<Address> getAddresses() {
		return addresses;
	}
	
	public void addAddress(Address address) {
		if (this.addresses == null) {
			this.addresses = new HashSet<>();
		}
		
		this.addresses.add(address);
	}
	
}
