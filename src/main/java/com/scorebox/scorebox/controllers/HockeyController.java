package com.scorebox.scorebox.controllers;

import com.scorebox.scorebox.entity.HockeyScoreBoard;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Named
@SessionScoped
public class HockeyController implements Serializable {
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "ds")
    private EntityManager entityManager;

    @Getter
    @Setter
    HockeyScoreBoard hockeyScoreBoard;

    @Getter
    @Setter
    String startStop = "Start";

    @Getter
    @Setter
    List<Integer> times = new ArrayList(Arrays.asList(20, 15, 10, 0));
//    List<String> times = new ArrayList(Arrays.asList("20:00", "15:00", "10:00", "00:00"));
    ExecutorService executorService = Executors.newCachedThreadPool();
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

    private static final Logger logger = LoggerFactory.getLogger(HockeyController.class);

    @Setter
    String textFieldTime;

    public String getTextFieldTime() {
        return String.format("%02d:%02d", hockeyScoreBoard.getCurrentTime() / 60, hockeyScoreBoard.getCurrentTime() % 60);
    }

    @Getter
    @Setter
    boolean isCountdownModeSelected = false, isFirstStart = true;

//    public void rrr(int t) {
//        logger.info(String.valueOf(t));
//        time = t;
//    }
//
//    public void r() {
//        --time;
//    }

//    public void listener() {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Paused", null));
//        time = (int) FacesContext.getCurrentInstance().getAttributes().get("time");
//        time = (int) event.getComponent().getAttributes().get("time");
//    }


    public String createBoard() {
        hockeyScoreBoard = new HockeyScoreBoard();
        return "/hockey_form.xhtml?faces-redirect=true";
    }

    @Transactional
    public String saveOrUpdateBoard() {
        if (hockeyScoreBoard.getId() == null) entityManager.persist(hockeyScoreBoard);
        else entityManager.merge(hockeyScoreBoard);
        return "/my_boards.xhtml?faces-redirect=true";
    }

    public String openBoard(HockeyScoreBoard hockeyScoreBoard) {
        if (hockeyScoreBoard == null){
            this.hockeyScoreBoard = new HockeyScoreBoard();
        } else {
        this.hockeyScoreBoard = hockeyScoreBoard;
        }
        return "/admin_board.xhtml?faces-redirect=true";
    }

    public String broadcast(HockeyScoreBoard hockeyScoreBoard) {
        this.hockeyScoreBoard = hockeyScoreBoard;
        return "/scorebox/broadcast";
//        return "/broadcast.xhtml?faces-redirect=true";
    }

    @Transactional
    public String editBoard(HockeyScoreBoard hockeyScoreBoard) {
        this.hockeyScoreBoard = hockeyScoreBoard;
        return "/hockey_form.xhtml?faces-redirect=true";
    }

    @Transactional
    public void deleteBoard(Long id) {
        entityManager.createQuery("DELETE FROM HockeyScoreBoard h WHERE h.id = :id").setParameter("id", id).executeUpdate();
    }

    public HockeyScoreBoard findById(Long id) {
        return entityManager.find(HockeyScoreBoard.class, id);
    }

    public List<HockeyScoreBoard> getAllBoards() {
        return entityManager.createQuery("SELECT h FROM HockeyScoreBoard h", HockeyScoreBoard.class).getResultList();
    }


    public void onChange(){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "selected: " + hockeyScoreBoard.getMaxTime(), null));
    }

    @Transactional
    public void plusOne(ActionEvent event) {
        logger.info(event.getComponent().getId());
        switch (event.getComponent().getId()){
            case "homeScorePlusOne": hockeyScoreBoard.setHomeScore(hockeyScoreBoard.getHomeScore() + 1);
                break;
            case "awayScorePlusOne": hockeyScoreBoard.setAwayScore(hockeyScoreBoard.getAwayScore() + 1);
                break;
            case "minutesPlusOne": hockeyScoreBoard.setCurrentTime(hockeyScoreBoard.getCurrentTime() + 60);
                break;
            case "secondsPlusOne": hockeyScoreBoard.setCurrentTime(hockeyScoreBoard.getCurrentTime() + 1);
                break;
            case "periodPlusOne": hockeyScoreBoard.setPeriod(hockeyScoreBoard.getPeriod() + 1);
                break;
//            case "homePenaltyMinutes1PlusOne": hockeyScoreBoard.setHomePenaltyMinutes1(hockeyScoreBoard.getHomePenaltyMinutes1() + 1);
//                break;
//            case "homePenaltyMinutes2": hockeyScoreBoard.setHomePenaltyMinutes2(hockeyScoreBoard.getHomePenaltyMinutes2() + 1);
//                break;
//            case "homePenaltyMinutes3PlusOne": hockeyScoreBoard.setHomePenaltyMinutes3(hockeyScoreBoard.getHomePenaltyMinutes3() + 1);
//                break;
//            case "homePenaltySeconds1PlusOne":
//                hockeyScoreBoard.setHomePenaltySeconds1(hockeyScoreBoard.getHomePenaltySeconds1() == 59 ? 0 : hockeyScoreBoard.getHomePenaltySeconds1() + 1);
//                break;
//            case "homePenaltySeconds2PlusOne":
//                hockeyScoreBoard.setHomePenaltySeconds2(hockeyScoreBoard.getHomePenaltySeconds2() == 59 ? 0 : hockeyScoreBoard.getHomePenaltySeconds2() + 1);
//                break;
//            case "homePenaltySeconds3PlusOne":
//                hockeyScoreBoard.setHomePenaltySeconds3(hockeyScoreBoard.getHomePenaltySeconds3() == 59 ? 0 : hockeyScoreBoard.getHomePenaltySeconds3() + 1);
//                break;
//            case "awayPenaltyMinutes1PlusOne": hockeyScoreBoard.setAwayPenaltyMinutes1(hockeyScoreBoard.getAwayPenaltyMinutes1() + 1);
//                break;
//            case "awayPenaltyMinutes2PlusOne": hockeyScoreBoard.setAwayPenaltyMinutes2(hockeyScoreBoard.getAwayPenaltyMinutes2() + 1);
//                break;
//            case "awayPenaltyMinutes3PlusOne": hockeyScoreBoard.setAwayPenaltyMinutes3(hockeyScoreBoard.getAwayPenaltyMinutes3() + 1);
//                break;
//            case "awayPenaltySeconds1PlusOne":
//                hockeyScoreBoard.setAwayPenaltySeconds1(hockeyScoreBoard.getAwayPenaltySeconds1() == 59 ? 0 : hockeyScoreBoard.getAwayPenaltySeconds1() + 1);
//                break;
//            case "awayPenaltySeconds2PlusOne":
//                hockeyScoreBoard.setAwayPenaltySeconds2(hockeyScoreBoard.getAwayPenaltySeconds2() == 59 ? 0 : hockeyScoreBoard.getAwayPenaltySeconds2() + 1);
//                break;
//            case "awayPenaltySeconds3PlusOne":
//                hockeyScoreBoard.setAwayPenaltySeconds3(hockeyScoreBoard.getAwayPenaltySeconds3() == 59 ? 0 : hockeyScoreBoard.getAwayPenaltySeconds3() + 1);
//                break;
        }
        entityManager.merge(hockeyScoreBoard);
    }

    @Transactional
    public void minusOne(ActionEvent event) {
        switch (event.getComponent().getId()){
            case "homeScoreMinusOne": hockeyScoreBoard.setHomeScore(hockeyScoreBoard.getHomeScore() == 0 ? 0 : hockeyScoreBoard.getHomeScore() - 1);
                break;
            case "awayScoreMinusOne": hockeyScoreBoard.setAwayScore(hockeyScoreBoard.getAwayScore() == 0 ? 0 : hockeyScoreBoard.getAwayScore() - 1);
                break;
            case "minutesMinusOne": hockeyScoreBoard.setCurrentTime(hockeyScoreBoard.getCurrentTime() / 60 >= 1 ? hockeyScoreBoard.getCurrentTime() - 60 : hockeyScoreBoard.getCurrentTime());
                break;
            case "secondsMinusOne": hockeyScoreBoard.setCurrentTime(hockeyScoreBoard.getCurrentTime() == 0 ? 0 : hockeyScoreBoard.getCurrentTime() - 1);
                break;
            case "periodMinusOne": hockeyScoreBoard.setPeriod(hockeyScoreBoard.getPeriod() == 0 ? 0 : hockeyScoreBoard.getPeriod() - 1);
                break;
//            case "homePenaltyMinutes1MinusOne":
//                hockeyScoreBoard.setHomePenaltyMinutes1(hockeyScoreBoard.getHomePenaltyMinutes1() == 0 ? 0 : hockeyScoreBoard.getHomePenaltyMinutes1() - 1);
//                break;
//            case "homePenaltyMinutes2MinusOne":
//                hockeyScoreBoard.setHomePenaltyMinutes2(hockeyScoreBoard.getHomePenaltyMinutes2() == 0 ? 0 : hockeyScoreBoard.getHomePenaltyMinutes2() - 1);
//                break;
//            case "homePenaltyMinutes3MinusOne":
//                hockeyScoreBoard.setHomePenaltyMinutes3(hockeyScoreBoard.getHomePenaltyMinutes3() == 0 ? 0 : hockeyScoreBoard.getHomePenaltyMinutes3() - 1);
//                break;
//            case"homePenaltySeconds1MinusOne":
//                hockeyScoreBoard.setHomePenaltySeconds1(hockeyScoreBoard.getHomePenaltySeconds1() == 0 ? 0 : hockeyScoreBoard.getHomePenaltySeconds1() - 1);
//                break;
//            case "homePenaltySeconds2MinusOne":
//                hockeyScoreBoard.setHomePenaltySeconds2(hockeyScoreBoard.getHomePenaltySeconds2() == 0 ? 0 : hockeyScoreBoard.getHomePenaltySeconds2() - 1);
//                break;
//            case "homePenaltySeconds3MinusOne":
//                hockeyScoreBoard.setHomePenaltySeconds3(hockeyScoreBoard.getHomePenaltySeconds3() == 0 ? 0 : hockeyScoreBoard.getHomePenaltySeconds3() - 1);
//                break;
//            case "awayPenaltyMinutes1MinusOne":
//                hockeyScoreBoard.setAwayPenaltyMinutes1(hockeyScoreBoard.getAwayPenaltyMinutes1() == 0 ? 0 : hockeyScoreBoard.getAwayPenaltyMinutes1() - 1);
//                break;
//            case "awayPenaltyMinutes2MinusOne":
//                hockeyScoreBoard.setAwayPenaltyMinutes2(hockeyScoreBoard.getAwayPenaltyMinutes2() == 0 ? 0 : hockeyScoreBoard.getAwayPenaltyMinutes2() - 1);
//                break;
//            case "awayPenaltyMinutes3MinusOne":
//                hockeyScoreBoard.setAwayPenaltyMinutes3(hockeyScoreBoard.getAwayPenaltyMinutes3() == 0 ? 0 : hockeyScoreBoard.getAwayPenaltyMinutes3() - 1);
//                break;
//            case "awayPenaltySeconds1MinusOne":
//                hockeyScoreBoard.setAwayPenaltySeconds1(hockeyScoreBoard.getAwayPenaltySeconds1() == 0 ? 0 : hockeyScoreBoard.getAwayPenaltySeconds1() - 1);
//                break;
//            case "awayPenaltySeconds2MinusOne":
//                hockeyScoreBoard.setAwayPenaltySeconds2(hockeyScoreBoard.getAwayPenaltySeconds2() == 0 ? 0 : hockeyScoreBoard.getAwayPenaltySeconds2() - 1);
//                break;
//            case "awayPenaltySeconds3MinusOne":
//                hockeyScoreBoard.setAwayPenaltySeconds3(hockeyScoreBoard.getAwayPenaltySeconds3() == 0 ? 0 : hockeyScoreBoard.getAwayPenaltySeconds3() - 1);
//                break;
        }
        entityManager.merge(hockeyScoreBoard);
    }


    Runnable forwardTask = () -> {
//        while (ss.equals("Stop")) {
//            try {
//                TimeUnit.SECONDS.sleep(1);
        if (startStop.equals("Stop")){
//                checkPenaltyTime();

            if (hockeyScoreBoard.getCurrentTime() == hockeyScoreBoard.getMaxTime()) {
                hockeyScoreBoard.setCurrentTime(0);
                startStop = "Start";
            } else {
                hockeyScoreBoard.setCurrentTime(hockeyScoreBoard.getCurrentTime() + 1);

//                textFieldTime.replace(0,1, String.valueOf(hockeyScoreBoard.getCurrentTime() / 60));
//                textFieldTime.replace(2,3, String.valueOf((hockeyScoreBoard.getCurrentTime() % 60) * 60));
            }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    };
//
    Runnable countdownTask = () -> {

    if (startStop.equals("Stop")){

        if (hockeyScoreBoard.getCurrentTime() == 0) {
            hockeyScoreBoard.setCurrentTime(hockeyScoreBoard.getMaxTime());
            startStop = "Start";
        } else {
            hockeyScoreBoard.setCurrentTime(hockeyScoreBoard.getCurrentTime() - 1);
        }
    }

////        while (startStop.equals("Stop")) {
////            try {
////                TimeUnit.SECONDS.sleep(1);
//        if (startStop.equals("Stop")){
//                checkPenaltyTime();
//
//                if (hockeyScoreBoard.getMinutes() > 0 && hockeyScoreBoard.getSeconds() == 0) {
//                    hockeyScoreBoard.setMinutes(hockeyScoreBoard.getMinutes() - 1);
//                    hockeyScoreBoard.setSeconds(59);
//                } else if (hockeyScoreBoard.getMinutes() == 0 && hockeyScoreBoard.getSeconds() == 0){
//                    hockeyScoreBoard.setMinutes(times.get(0));
//                    hockeyScoreBoard.setPeriod(hockeyScoreBoard.getPeriod() + 1);
//                    startStop = "Start";
//                } else hockeyScoreBoard.setSeconds(hockeyScoreBoard.getSeconds() - 1);
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
//
//        }
    };

//
//    public void checkPenaltyTime(){
//        if (hockeyScoreBoard.getHomePenaltyNumber1() != null) {
//            if (hockeyScoreBoard.getHomePenaltyMinutes1() > 0 && hockeyScoreBoard.getHomePenaltySeconds1() == 0) {
//                hockeyScoreBoard.setHomePenaltyMinutes1(hockeyScoreBoard.getHomePenaltyMinutes1() - 1);
//                hockeyScoreBoard.setHomePenaltySeconds1(59);
//            } else if (hockeyScoreBoard.getHomePenaltyMinutes1() == 0 && hockeyScoreBoard.getHomePenaltySeconds1() == 0){
//                hockeyScoreBoard.setHomePenaltyNumber1(null);
//            } else hockeyScoreBoard.setHomePenaltySeconds1(hockeyScoreBoard.getHomePenaltySeconds1() - 1);
//        }
//    }

//    @Transactional
//    public void start() {
//        entityManager.merge(hockeyScoreBoard);
//        if (ss.equals("Start")) {
//            ss = "Stop";
//            if (countdown){
//                if (!countdownThread.isAlive()){
//                    countdownThread.setDaemon(true);
//                    countdownThread.start();
//                } else countdownThread.resume();
//            } else {
//                if (!forwardThread.isAlive()){
//                    forwardThread.setDaemon(true);
//                    forwardThread.start();
//                } else forwardThread.resume();
//            }
//        } else if (ss.equals("Stop")){
//            ss = "Start";
//
//            if (countdown) countdownThread.suspend();
//            else forwardThread.suspend();
//        }
//    }

    @Transactional
    public void start() {
        entityManager.merge(hockeyScoreBoard);
        if (startStop.equals("Start")) {
            startStop = "Stop";
            if (isFirstStart){
                isFirstStart = false;
                scheduledExecutorService.scheduleAtFixedRate(isCountdownModeSelected ? countdownTask : forwardTask,1,1, TimeUnit.SECONDS);
            }
        } else startStop = "Start";
    }
}
