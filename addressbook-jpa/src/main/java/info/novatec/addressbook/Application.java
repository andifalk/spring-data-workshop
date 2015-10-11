package info.novatec.addressbook;

import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import info.novatec.addressbook.boundary.PersonManagementService;
import info.novatec.addressbook.boundary.PersonManagementServiceImpl;
import info.novatec.addressbook.control.PersonService;
import info.novatec.addressbook.control.PersonServiceImpl;
import info.novatec.addressbook.entity.Address;
import info.novatec.addressbook.entity.Person;
import info.novatec.addressbook.repository.PersonRepository;

/**
 * Main application configuration.
 */
@SpringBootApplication
@EnableJpaRepositories("info.novatec.addressbook.repository")
@Import(RepositoryRestMvcConfiguration.class)
public class Application {

	private PersonRepository personRepository;
	
	/**
	 * Main entry point.
	 * @param args cmdline args
	 */
    public static void main(final String[] args) {
    	ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
    	PersonRepository repository = context.getBean(PersonRepository.class);
    	
		repository.save(new Person("Kai", "Hansen", new Date(), new HashSet<Address>()));
		repository.save(new Person("Achim", "Maier", new Date(), new HashSet<Address>()));
		

    }
    
    /**
     * Bean configuration for {@link PersonService}.
     * @return {@link PersonService}
     */
    @Bean
    public PersonService createPersonService() {
    	PersonServiceImpl personServiceImpl = new PersonServiceImpl();
    	personServiceImpl.setPersonRepository(personRepository);
    	return personServiceImpl;
    }

    /**
     * Bean configuration for {@link PersonManagementService}.
     * @return {@link PersonManagementService}
     */
    @Bean
    public PersonManagementService createPersonManagementService() {
    	PersonManagementServiceImpl personManagementServiceImpl = new PersonManagementServiceImpl();
    	personManagementServiceImpl.setPersonService(createPersonService());
    	return personManagementServiceImpl;
    }

    /**
     * Sets the {@link PersonRepository}.
     * @param personRepository the repository
     */
    @Autowired
    public void setPersonRepository(final PersonRepository personRepository) {
    	this.personRepository = personRepository;
    }

}
