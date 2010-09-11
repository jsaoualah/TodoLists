package tudu.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tudu.domain.Property;
import tudu.domain.Role;
import tudu.domain.RolesEnum;
import tudu.domain.User;
import tudu.service.ConfigurationService;
import tudu.service.UserAlreadyExistsException;
import tudu.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Set;

/**
 * Implementation of the tudu.service.ConfigurationService interface.
 *
 * @author Julien Dubois
 */
@Service
@Transactional
public class ConfigurationServiceImpl implements ConfigurationService,
        ApplicationListener {

    public static String staticContent = "";

    public static String googleAnalyticsKey = "";

    private final Log log = LogFactory.getLog(ConfigurationServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserService userService;

    /**
     * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
     */
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            log.warn("Spring context is started : " + event.toString());
            this.initDatabase();
            this.initApplicationProperties();
        }
    }

    /**
     * @see tudu.service.ConfigurationService#initDatabase()
     */
    public void initDatabase() {
        log.warn("Testing Database.");
        try {
            em.find(Role.class, RolesEnum.ROLE_USER.name());
        } catch (ObjectRetrievalFailureException erfe) {
            log.warn("Database is empty : populating with default values.");

            log.warn("Populating HSQLDB database.");
            Property hostProperty = new Property();
            hostProperty.setKey("smtp.host");
            hostProperty.setValue("");
            em.persist(hostProperty);
            Property portProperty = new Property();
            portProperty.setKey("smtp.port");
            portProperty.setValue("25");
            em.persist(portProperty);
            Property userProperty = new Property();
            userProperty.setKey("smtp.user");
            userProperty.setValue("");
            em.persist(userProperty);
            Property passwordProperty = new Property();
            passwordProperty.setKey("smtp.password");
            passwordProperty.setValue("");
            em.persist(passwordProperty);
            Property fromProperty = new Property();
            fromProperty.setKey("smtp.from");
            fromProperty.setValue("");
            em.persist(fromProperty);

            Role userRole = new Role();
            userRole.setRole(RolesEnum.ROLE_USER.name());
            em.persist(userRole);
            Role adminRole = new Role();
            adminRole.setRole(RolesEnum.ROLE_ADMIN.name());
            em.persist(adminRole);

            User adminUser = new User();
            adminUser.setLogin("admin");
            adminUser.setPassword("admin");
            adminUser.setFirstName("Albert");
            adminUser.setLastName("Dmin");
            adminUser.setDateFormat("MM/dd/yyyy");
            try {
                userService.createUser(adminUser);
            } catch (UserAlreadyExistsException e) {
                log.error("Error while creating the admin user :"
                        + " the user already exists.");
            }
            Set<Role> roles = adminUser.getRoles();
            roles.add(adminRole);

            User user = new User();
            user.setLogin("user");
            user.setPassword("user");
            user.setFirstName("");
            user.setDateFormat("MM/dd/yyyy");
            try {
                userService.createUser(user);
            } catch (UserAlreadyExistsException e) {
                log.error("Error while creating the admin user : "
                        + "the user already exists.");
            }
        }

    }

    /**
     * @see tudu.service.ConfigurationService#initApplicationProperties()
     */
    @Transactional(readOnly = true)
    public void initApplicationProperties() {
        Property staticFilesPathProperty = this
                .getProperty("application.static.path");

        if (staticFilesPathProperty != null) {
            staticContent = staticFilesPathProperty.getValue();

        } else {
            staticContent = "/tudu/static";
        }

        Property googleAnalyticsKeyProperty = this
                .getProperty("google.analytics.key");

        if (googleAnalyticsKeyProperty != null) {
            googleAnalyticsKey = googleAnalyticsKeyProperty.getValue();
        }
    }

    /**
     * @see tudu.service.ConfigurationService#getProperty(java.lang.String)
     */
    @Transactional(readOnly = true)
    public Property getProperty(String key) {
        return em.find(Property.class, key);
    }

    /**
     * @see tudu.service.ConfigurationService#updateEmailProperties(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    public void updateEmailProperties(String smtpHost, String smtpPort,
                                      String smtpUser, String smtpPassword, String smtpFrom) {

        Property hostProperty = em.find(Property.class, "smtp.host");
        hostProperty.setValue(smtpHost);
        Property portProperty = em.find(Property.class, "smtp.port");
        portProperty.setValue(smtpPort);
        Property userProperty = em.find(Property.class, "smtp.user");
        userProperty.setValue(smtpUser);
        Property passwordProperty = em.find(Property.class, "smtp.password");
        passwordProperty.setValue(smtpPassword);
        Property fromProperty = em.find(Property.class, "smtp.from");
        fromProperty.setValue(smtpFrom);
    }

    /**
     * @see tudu.service.ConfigurationService#updateApplicationProperties(java.lang.String,
     *      java.lang.String)
     */
    public void updateApplicationProperties(String staticPath, String googleKey) {
        Property pathProperty = new Property();
        pathProperty.setKey("application.static.path");
        pathProperty.setValue(staticPath);
        this.setProperty(pathProperty);
        staticContent = staticPath;

        Property googleProperty = new Property();
        googleProperty.setKey("google.analytics.key");
        googleProperty.setValue(googleKey);
        this.setProperty(googleProperty);
        googleAnalyticsKey = googleKey;
    }

    /**
     * Set a property.
     * <p>
     * If the property doesn't exist yet, it is created.
     * </p>
     *
     * @param property The property
     */
    private void setProperty(Property property) {
        Property databaseProperty = this.getProperty(property.getKey());
        if (databaseProperty == null) {
            em.persist(property);
        } else {
            databaseProperty.setValue(property.getValue());
        }
    }
}