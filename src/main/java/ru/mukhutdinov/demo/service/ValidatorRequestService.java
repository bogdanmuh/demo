package ru.mukhutdinov.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mukhutdinov.demo.controller.pojo.MailingRequest;
import ru.mukhutdinov.demo.controller.pojo.MovingRequest;
import ru.mukhutdinov.demo.domain.Mailing;
import ru.mukhutdinov.demo.domain.Moving;
import ru.mukhutdinov.demo.domain.PostOffice;
import ru.mukhutdinov.demo.domain.TypeMailing;
import ru.mukhutdinov.demo.repository.MailingRepository;
import ru.mukhutdinov.demo.repository.PostOfficeRepository;

import java.util.Arrays;

@Service
public class ValidatorRequestService {
    @Autowired
    private MailingRepository mailingRepository;
    @Autowired
    private PostOfficeRepository postOfficeRepository;

    public Mailing convertToMailing(MailingRequest mailingRequest) throws ValidatorException {
        if (mailingRequest.getId().isEmpty() ||
                mailingRequest.getType().isEmpty() ||
                mailingRequest.getRecipientIndex().isEmpty() ||
                mailingRequest.getRecipientName().isEmpty() ||
                mailingRequest.getRecipientAddress().isEmpty())
            throw new ValidatorException("Все поля должны быть не пустыми");


        for (int i = 0; i < mailingRequest.getRecipientName().length(); i++) {
            if (Character.isDigit(mailingRequest.getRecipientName().charAt(i))) {
                throw new ValidatorException("Поле recipientName не должно содержать чисел");
            }
        }
        if (Arrays.stream(TypeMailing.values())
                .noneMatch(x -> x.toString().compareToIgnoreCase(mailingRequest.getType()) == 0))
            throw new ValidatorException("Указан неверный тип почтового отправления");

        mailingRequest.setType(mailingRequest.getType().toUpperCase());

        Mailing mailing = new Mailing(
                getInt(mailingRequest.getId(), "Id"),
                TypeMailing.valueOf(mailingRequest.getType()),
                getCorrectIndex(mailingRequest.getRecipientIndex(), "recipientIndex"),
                mailingRequest.getRecipientName(),
                mailingRequest.getRecipientAddress()
        );
        if (!postOfficeRepository.existsByIndex(mailing.getRecipientIndex()))
            throw new ValidatorException("Почтового отделение не существует");
        if (mailingRepository.existsById(mailing.getId()))
            throw new ValidatorException("Почтовое отправление уже существует");

        return mailing;
    }

    public Moving convertToMoving(MovingRequest mailingRequest) throws ValidatorException {
        if (mailingRequest.getMailing().isEmpty() ||
                mailingRequest.getFrom().isEmpty() ||
                mailingRequest.getTo().isEmpty())
            throw new ValidatorException("Все поля должны быть не пустыми");

        PostOffice from = new PostOffice();
        from.setIndex(getCorrectIndex(mailingRequest.getFrom(),"From"));

        PostOffice to = new PostOffice();
        to.setIndex(getCorrectIndex(mailingRequest.getTo(),"To"));

        if (!postOfficeRepository.existsByIndex(from.getIndex()) ||
                !postOfficeRepository.existsByIndex(to.getIndex()))
            throw new ValidatorException("Почтового отделение не существует");

        Mailing mailing = new Mailing();
        mailing.setId(getInt(mailingRequest.getMailing(),"mailing"));

        return new Moving(mailing, from, to, mailingRequest.getDate(), null);
    }

    public int getCorrectIndex (String value, String key) throws ValidatorException {
        if (value.length() > 6) {
            throw new ValidatorException(String.format("Поле %s не должно превышать 6 цифр", key));
        }
        return (int) getInt(value, key);
    }

    public long getInt (String value, String key) throws ValidatorException {
        long number;
        try {
            number = Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new ValidatorException(String.format("Поле %s должен быть положительным числом", key));
        }
        if (number < 0) {
            throw new ValidatorException(String.format("Поле %s должен быть положительным числом", key));
        }
        return number;
    }
}
