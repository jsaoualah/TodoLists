package tudu.web.mvc;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;
import tudu.domain.Property;
import tudu.service.ConfigurationService;
import tudu.service.UserService;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class Level1AdministrationControllerTest {

    @Mock
    private ConfigurationService cfgService;
    @Mock
    private UserService userService;

    @Mock
    private Property property;

    @InjectMocks
    private AdministrationController adminController = new AdministrationController();


    /*
    * Vérifier dans un test que pour la page "configuration" les propriétés smtp (et uniquement celles là) soit donnée au model
    * Méthode :  display
    */
   @Test
    public void display_should_put_smtp_config_properties_in_admin_model_when_page_is_configuration() throws Exception {
        // given
       /* Mockito.when(cfgService.getProperty("smtp.from")).thenReturn(property("FROM"));
        Mockito.when(cfgService.getProperty("smtp.port")).thenReturn(property("PORT"));
        Mockito.when(cfgService.getProperty("smtp.user")).thenReturn(property("USER"));
        Mockito.when(cfgService.getProperty("smtp.password")).thenReturn(property("PSWD"));
        Mockito.when(cfgService.getProperty("smtp.host")).thenReturn(property("HOST"));
        // when  */
      //  ModelAndView modelAndView = adminController.display("configuration");

        // then

       Mockito.when(cfgService.getProperty(anyString())).thenReturn(property(""));
       ModelAndView modelAndView = adminController.display("");

       /* assertThat(modelAndView.getModel().get("smtp.from")).isEqualTo("FROM");
        assertThat(modelAndView.getModel().get("smtp.port")).isEqualTo("PORT");
        assertThat(modelAndView.getModel().get("smtp.user")).isEqualTo("USER");
        assertThat(modelAndView.getModel().get("smtp.password")).isEqualTo("PSWD");
        assertThat(modelAndView.getModel().get("smtp.host")).isEqualTo("HOST");
*/


       Mockito.verify(cfgService, times(5)).getProperty(matches("smtp.*"));

   }

    /*
   * Vérifier que l update ne retourne pas un modele null
   * */
    @Test
    public void update_shouldnt_return_a_null_model() throws Exception {
        AdministrationModel administrationModel = new AdministrationModel();
        administrationModel.setAction("configuration");
        administrationModel.setPropertyStaticPath("path");
        administrationModel.setGoogleAnalyticsKey("key");
        administrationModel.setSmtpHost("localhost");
        administrationModel.setSmtpPort("25");
        administrationModel.setSmtpUser("user");
        administrationModel.setSmtpPassword("password");
        administrationModel.setSmtpFrom("smtpForm");

        cfgService.updateApplicationProperties(administrationModel.getPropertyStaticPath(),
                administrationModel.getGoogleAnalyticsKey());
        cfgService.updateEmailProperties(
                administrationModel.getSmtpHost(),
                administrationModel.getSmtpPort(),
                administrationModel.getSmtpUser(),
                administrationModel.getSmtpPassword(),
                administrationModel.getSmtpFrom()
        );
        when(cfgService.getProperty(anyString())).thenReturn(new Property());
        ModelAndView mv = adminController.update(administrationModel);
        assertThat(mv).isNotNull();
    }

    private Property property(String value) {
        Property property = new Property();
        property.setValue(value);
        return property;
    }
}
