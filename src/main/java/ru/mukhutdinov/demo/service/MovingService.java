package ru.mukhutdinov.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.mukhutdinov.demo.controller.pojo.MailingInfoResponse;
import ru.mukhutdinov.demo.controller.pojo.MovingRequest;
import ru.mukhutdinov.demo.domain.Mailing;
import ru.mukhutdinov.demo.domain.Moving;
import ru.mukhutdinov.demo.domain.PostOffice;
import ru.mukhutdinov.demo.repository.MovingRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovingService {
    @Autowired
    private MovingRepository movingRepository;
    @Autowired
    private MailingService mailingService;
    @Autowired
    private ValidatorRequestService validatorService;

    public void fixSending(MovingRequest movingRequest) throws Exception {
        movingRepository.save(validatorService.convertToMoving(movingRequest));
    }

    public void fixArrival(MovingRequest movingRequest) throws Exception {
        validatorService.convertToMoving(movingRequest);
        Optional<Moving> mailings = movingRepository.getMails(
                movingRequest.getMailing(),
                movingRequest.getFrom(),
                movingRequest.getTo());
        if (mailings.isEmpty()) throw new Exception("Данное почтовое отправление не найдено");
        mailings.get().setComing(true);
        movingRepository.save(mailings.get());
    }

    ///исхожу из того что порядок вставки коректный
    public MailingInfoResponse getInfo(String id) throws Exception {
        List<PostOffice> paths = new LinkedList<>();
        String status = "";
       ;
        Optional<Mailing> mailing = mailingService.findById(validatorService.getInt(id, "mailing"));
        if (mailing.isEmpty()) throw new Exception("Данное почтовое отправление не найдено");
        List<Moving> mailings = movingRepository.findByMailing(id);
        if (mailings == null || mailings.isEmpty()) {
            status = "Почтовое отправление зарегистрировано";
        } else {
            /* if (!isCorrectOrder(mailings)) {
                sortMailing(mailings);
            }*/
            paths = mailings.stream().map(Moving::getFrom).collect(Collectors.toList());
            if (mailings.get(mailings.size() - 1).isComing()) {
                status = String.format("Находиться в %s по адресу %s",
                        mailings.get(mailings.size() - 1).getTo().getName(),
                        mailings.get(mailings.size() - 1).getTo().getAddress());
                paths.add(mailings.get(mailings.size() - 1).getTo());
                if (mailings.get(mailings.size() - 1).getTo().getIndex().equals(mailing.get().getRecipientIndex())) {
                    status = "Почтовое отправление доставлено адресату";
                }
            } else {
                status = String.format(
                        "Находиться в пути из %s в %s ",
                        mailings.get(mailings.size() - 1).getFrom().getAddress(),
                        mailings.get(mailings.size() - 1).getTo().getAddress()
                );
            }

        }
        return new MailingInfoResponse(status, paths);
    }

    /*private boolean isCorrectOrder(List<Mailings> mailings) {
        for (int i = 0; i < mailings.size() - 1; i++) {
            if (!mailings.get(i).getTo().equals(mailings.get(i + 1).getFrom())) {
                return false;
            }
        }
        return true;
    }

    private void sortMailing(List<Mailings> mailings) {
        int i = 0;
        if (mailings.size() > 2) {
            while (i < mailings.size() - 2) {
                Mailings current = mailings.get(i);
                for (int j = i + 1; j < mailings.size(); j++) {
                    if (current.getTo().equals(mailings.get(j).getFrom())) {
                        Collections.swap(mailings, i + 1, j);
                        i = i + 2;
                    }
                }
            }
        }
    }*/
}
