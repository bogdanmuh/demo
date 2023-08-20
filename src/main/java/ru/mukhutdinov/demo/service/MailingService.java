package ru.mukhutdinov.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mukhutdinov.demo.controller.pojo.MailingRequest;
import ru.mukhutdinov.demo.domain.Mailing;
import ru.mukhutdinov.demo.repository.MailingRepository;

import java.util.Optional;

@Service
public class MailingService {
    @Autowired
    private MailingRepository mailingRepository;
    @Autowired
    private ValidatorRequestService validatorService;

    public void saveMailing(MailingRequest mailingRequest) throws ValidatorException {
            mailingRepository.save(validatorService.convertToMailing(mailingRequest));
    }

    public Optional<Mailing> findById(Long id){
        return mailingRepository.findById(id);
    }
}
