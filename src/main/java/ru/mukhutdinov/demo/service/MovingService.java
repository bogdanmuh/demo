package ru.mukhutdinov.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.mukhutdinov.demo.controller.pojo.MailingInfoResponse;
import ru.mukhutdinov.demo.controller.pojo.MovingRequest;
import ru.mukhutdinov.demo.domain.Mailing;

import ru.mukhutdinov.demo.domain.PostOffice;
import ru.mukhutdinov.demo.domain.dto.MovingDto;
import ru.mukhutdinov.demo.repository.MovingRepository;

import java.util.*;
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
        movingRepository.updateIsComing(movingRequest.getMailing(),
                movingRequest.getFrom(),
                movingRequest.getTo(),
                movingRequest.getDate());
    }

    ///исхожу из того что порядок вставки коректный
    public MailingInfoResponse getInfo(String id) throws Exception {
        List<PostOffice> paths = new LinkedList<>();
        String status = "";
        Optional<Mailing> mailing = mailingService.findById(validatorService.getInt(id, "mailing"));
        if (mailing.isEmpty()) throw new Exception("Данное почтовое отправление не найдено");
        List<MovingDto> mailings = movingRepository.findByMailing(id);
        if (mailings == null || mailings.isEmpty()) {
            status = "Почтовое отправление зарегистрировано";
        } else {
            /* if (!isCorrectOrder(mailings)) {
                sortMailing(mailings);
            }*/
            paths = mailings.stream()
                    .map(x -> new PostOffice(x.getFrom_Index(), x.getFrom_Name(), x.getFrom_Adress()))
                    .collect(Collectors.toList());
            if (mailings.get(mailings.size() - 1).getTo_Date() != null) {
                status = String.format("Находиться в %s по адресу %s",
                        mailings.get(mailings.size() - 1).getTo_Name(),
                        mailings.get(mailings.size() - 1).getTo_Adress());

                paths.add(new PostOffice(
                        mailings.get(mailings.size() - 1).getFrom_Index(),
                        mailings.get(mailings.size() - 1).getFrom_Name(),
                        mailings.get(mailings.size() - 1).getFrom_Adress()));
                if (mailings.get(mailings.size() - 1).getTo_Index().equals(mailing.get().getRecipientIndex())) {
                    status = "Почтовое отправление доставлено адресату";
                }
            } else {
                status = String.format(
                        "Находиться в пути из %s в %s ",
                        mailings.get(mailings.size() - 1).getFrom_Index(),
                        mailings.get(mailings.size() - 1).getTo_Adress()
                );
            }
        }
        return new MailingInfoResponse(status, paths);
    }

   /* private boolean isCorrectOrder(List<MovingDto> mailings) {
        for (int i = 0; i < mailings.size() - 1; i++) {
            if (!mailings.get(i).getFrom_Index().equals(mailings.get(i + 1).getTo_Index())) {
                return false;
            }
        }
        return true;
    }

    private List<MovingDto> sortMailing(List<MovingDto> mailings) {
        int i = 0;
        LinkedList <MovingDto> sortList = new LinkedList<>();
        Map <Integer, Pair<Integer, MovingDto>> container = new HashMap<>();
        sortList.add(mailings.get(0));

        for (int j = 1; j < mailings.size(); j++) {
            if (sortList.getFirst().getTo_Index().equals(mailings.get(j).getFrom_Index())) {
                sortList.addFirst(mailings.get(j));
            } else if (sortList.getLast().getFrom_Index().equals(mailings.get(j).getTo_Index())) {
                sortList.addLast(mailings.get(j));
            } else {
                if (container.containsKey(mailings.get(j).getFrom_Index())){
                    container.get(mailings.get(j).getFrom_Index()).
                } else {

                }

                container.put(mailings.get(j).getTo_Index(), Pair.of(1,mailings.get(j)));
            }

            while (container.containsKey(sortList.getFirst().getTo_Index())) {
                //проверка
                sortList.add(container.get(sortList.getFirst().getTo_Index()).getSecond());
            }

            while (container.containsKey(sortList.getLast().getFrom_Index())){
                //проверка
                sortList.add(container.get(sortList.getLast().getFrom_Index()).getSecond());
            }
        }

        while () {
                        else if (container.containsKey(mailings.get(j).getTo_Index())) {

            } else if (container.containsKey(mailings.get(j).getFrom_Index())) {

            }
        }
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
