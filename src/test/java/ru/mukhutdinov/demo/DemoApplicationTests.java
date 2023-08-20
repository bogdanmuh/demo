package ru.mukhutdinov.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.mukhutdinov.demo.controller.pojo.MailingRequest;
import ru.mukhutdinov.demo.controller.pojo.MovingRequest;
import ru.mukhutdinov.demo.domain.Mailing;
import ru.mukhutdinov.demo.domain.PostOffice;
import ru.mukhutdinov.demo.domain.TypeMailing;
import ru.mukhutdinov.demo.repository.MailingRepository;
import ru.mukhutdinov.demo.repository.MovingRepository;
import ru.mukhutdinov.demo.repository.PostOfficeRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc


//@ContextConfiguration(classes=DemoApplication.class)
//@WebMvcTest(Controller.class)
@TestPropertySource("/application-test.properties")
class DemoApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MailingRepository mailingRepository;
    @Autowired
    private MovingRepository movingRepository;
    @Autowired
    private PostOfficeRepository postOfficeRepository;

    @BeforeEach
    void init() {
        postOfficeRepository.save(new PostOffice(423040, "Отделение почты  номер 1", "Г.Казань ул. Серова д.1"));
        postOfficeRepository.save(new PostOffice(423010, "Отделение почты  номер 11", "Г.Москва ул. Серова д.1"));
        postOfficeRepository.save(new PostOffice(423000, "Отделение почты  номер 12", "Г.Новосибирск ул. Отрядная д.12"));
        postOfficeRepository.save(new PostOffice(423001, "Отделение почты  номер 13", "Г.Омск ул. Антонова д.1"));
        postOfficeRepository.save(new PostOffice(423002, "Отделение почты  номер 14", "Г.Хабаровск ул.Ленина д.1"));

    }

    @AfterEach
    void delete() {
        movingRepository.deleteAll();
        mailingRepository.deleteAll();
        postOfficeRepository.deleteAll();
    }


    @Test
    public void registrationTest() throws Exception {
        this.mockMvc.perform(post("/registration")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getMailingRequest("1", "LETTER", "423002", "Bogdan", "Kazan")));

        assertThat(mailingRepository.findAll())
                .hasSize(1)
                .filteredOn(x -> x.equals(new Mailing(1L, TypeMailing.LETTER, 423002, "Bogdan", "Kazan")));
    }

    @Test
    public void registrationThrowsException() throws Exception {
        this.mockMvc.perform(post("/registration")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getMailingRequest("1", "LETTER", "423002", "Bogdan", "Kazan")));


        MvcResult result = this.mockMvc.perform(post("/registration")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getMailingRequest("1", "LETTER", "423002", "Bogdan", "Kazan")))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException().getMessage())
                .isEqualTo("Почтовое отправление уже существует");

        result = this.mockMvc.perform(post("/registration")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getMailingRequest("1", "123123", "423002", "Bogdan", "Kazan")))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException().getMessage())
                .isEqualTo("Указан неверный тип почтового отправления");

        result = this.mockMvc.perform(post("/registration")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getMailingRequest("1", "123123", "423002", "Bogdan123", "Kazan")))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException().getMessage())
                .isEqualTo("Поле recipientName не должно содержать чисел");


        result = this.mockMvc.perform(post("/registration")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getMailingRequest("1", "LETTER", "423002", "Bogdan", "")))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException().getMessage())
                .isEqualTo("Все поля должны быть не пустыми");

        result = this.mockMvc.perform(post("/registration")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getMailingRequest("1", "LETTER", "111111", "Bogdan", "Kazan")))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException().getMessage())
                .isEqualTo("Почтового отделение не существует");

        result = this.mockMvc.perform(post("/registration")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getMailingRequest("-1", "LETTER", "111111", "Bogdan", "Kazan")))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException().getMessage())
                .isEqualTo("Поле Id должен быть положительным числом");

        result = this.mockMvc.perform(post("/registration")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getMailingRequest("asdf", "LETTER", "111111", "Bogdan", "Kazan")))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException().getMessage())
                .isEqualTo("Поле Id должен быть положительным числом");

        result = this.mockMvc.perform(post("/registration")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getMailingRequest("1", "LETTER", "11111111111111", "Bogdan", "Kazan")))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException().getMessage())
                .isEqualTo("Поле recipientIndex не должно превышать 6 цифр");


        assertThat(mailingRepository.findAll())
                .hasSize(1)
                .filteredOn(x -> x.equals(new Mailing(1L, TypeMailing.LETTER, 423002, "Bogdan", "Kazan")));
    }

    @Test
    public void departureTest() throws Exception {
        movingRepository.deleteAll();

        this.mockMvc.perform(post("/registration")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(getMailingRequest("1", "LETTER", "423002", "Bogdan", "London")));
        this.mockMvc.perform(post("/departure")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getMailingsRequest("1", "423040", "423010")));

        assertThat(mailingRepository.findAll())
                .hasSize(1)
                .filteredOn(x -> x.equals(new Mailing(1L, TypeMailing.LETTER, 423002, "Bogdan", "Kazan")));

        assertThat(movingRepository.findAll())
                .hasSize(1)
                .filteredOn(x ->
                        x.getTo().getIndex().equals("1") &&
                                x.getFrom().getIndex().equals("423040") &&
                                x.getMailing().equals("423010") &&
                                !x.isComing()
                );
    }


    @Test
    public void departureThrowsExceptionTest() throws Exception {
        this.mockMvc.perform(post("/registration")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(getMailingRequest("1", "LETTER", "423002", "Bogdan", "London")));

        MvcResult result = this.mockMvc.perform(post("/departure")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getMailingsRequest("1", "423040", "")))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException().getMessage())
                .isEqualTo("Все поля должны быть не пустыми");

        result = this.mockMvc.perform(post("/departure")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getMailingsRequest("1", "423040", "12")))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException().getMessage())
                .isEqualTo("Почтового отделение не существует");
    }

    @Test
    public void arrivalTest() throws Exception {
        this.mockMvc.perform(post("/registration")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(getMailingRequest("1", "LETTER", "423002", "Bogdan", "London")));
        this.mockMvc.perform(post("/departure")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getMailingsRequest("1", "423040", "423010")));

        this.mockMvc.perform(post("/arrival")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getMailingsRequest("1", "423040", "423010")));


        assertThat(mailingRepository.findAll())
                .hasSize(1)
                .filteredOn(x -> x.equals(new Mailing(1L, TypeMailing.LETTER, 423002, "Bogdan", "Kazan")));

        assertThat(movingRepository.findAll())
                .hasSize(1)
                .filteredOn(x ->
                        x.getTo().getIndex().equals("1") &&
                                x.getFrom().equals("423040") &&
                                x.getMailing().equals("423010") &&
                                x.isComing()
                );

    }

    @Test
    public void arrivalThrowsExceptions() throws Exception {
        this.mockMvc.perform(post("/registration")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(getMailingRequest("1", "LETTER", "423002", "Bogdan", "London")));
        this.mockMvc.perform(post("/departure")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getMailingsRequest("1", "423040", "423010")));

        MvcResult result = this.mockMvc.perform(post("/arrival")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getMailingsRequest("2", "423040", "423010")))
                .andReturn();

        assertThat(result.getResolvedException().getMessage())
                .isEqualTo("Данное почтовое отправление не найдено");
    }

    @Test
    public void getPathDonnotDeleviredTest() throws Exception {
        this.mockMvc.perform(post("/registration")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(getMailingRequest("1", "LETTER", "423002", "Bogdan", "London")));

        MvcResult result = this.mockMvc.perform(get("/getPath")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("1"))
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .contains("Почтовое отправление зарегистрировано");
    }

    @Test
    public void getPathIsDelevereTest() throws Exception {
        this.mockMvc.perform(post("/registration")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(getMailingRequest("1", "LETTER", "423002", "Bogdan", "London")));

        this.mockMvc.perform(post("/departure")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getMailingsRequest("1", "423040", "423010")));

        MvcResult result = this.mockMvc.perform(get("/getPath")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("1"))
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .contains("Находиться в пути из");
    }

    @Test
    public void getPathTest() throws Exception {
        this.mockMvc.perform(post("/registration")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(getMailingRequest("1", "LETTER", "423002", "Bogdan", "London")));
        this.mockMvc.perform(post("/departure")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getMailingsRequest("1", "423040", "423010")));

        this.mockMvc.perform(post("/arrival")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getMailingsRequest("1", "423040", "423010")));

        this.mockMvc.perform(post("/departure")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getMailingsRequest("1", "423010", "423000")));

        this.mockMvc.perform(post("/arrival")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getMailingsRequest("1", "423010", "423000")));

        this.mockMvc.perform(post("/departure")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getMailingsRequest("1", "423000", "423001")));

        this.mockMvc.perform(post("/arrival")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getMailingsRequest("1", "423000", "423001")));

        this.mockMvc.perform(post("/departure")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getMailingsRequest("1", "423001", "423002")));

        this.mockMvc.perform(post("/arrival")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getMailingsRequest("1", "423001", "423002")));

        MvcResult result = this.mockMvc.perform(get("/getPath")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("1"))
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .contains("Почтовое отправление доставлено адресату");

    }

    private String getMailingRequest(String id,
                                     String type,
                                     String recipientIndex,
                                     String recipientName,
                                     String recipientAddress) throws Exception {

        MailingRequest request = new MailingRequest(id,
                type,
                recipientIndex,
                recipientName,
                recipientAddress);

        return getContent(request);
    }

    private String getMailingsRequest(String mailing,
                                      String from,
                                      String to) throws Exception {
        MovingRequest request = new MovingRequest(mailing,
                from,
                to);
        return getContent(request);
    }

    private String getContent(Object object) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }

}
