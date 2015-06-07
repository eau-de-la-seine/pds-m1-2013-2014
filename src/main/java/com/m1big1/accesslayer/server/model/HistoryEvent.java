package com.m1big1.accesslayer.server.model;

import java.util.List;

import com.m1big1.accesslayer.server.dto.HistoryEventDTO;

public class HistoryEvent {
    protected String eventKind;
    protected String completePath;
    protected String eventDate;
    protected HistoryEvent nextHistoryEvent;

    public HistoryEvent(String eventKind, String completePath, String eventDate) {
        this.eventKind = eventKind;
        this.completePath = completePath;
        this.eventDate = eventDate;
    }
    
    public static HistoryEventDTO getDtoFromHistoryEvent(HistoryEvent history) {
        HistoryEventDTO historyDTO = new HistoryEventDTO();
        
        historyDTO.setEventKind(history.eventKind);
        historyDTO.setEventDate(history.eventDate);
        historyDTO.setCompletePath(history.completePath);
        
        return historyDTO;
    }
    
    public static HistoryEventDTO getDTOfromList(List<HistoryEvent> list) {
        HistoryEventDTO rootHistoryEvent = null;
        HistoryEventDTO currentNode;
        if(list.size()>0) {
             rootHistoryEvent = getDtoFromHistoryEvent(list.get(0));
             currentNode = rootHistoryEvent;
             
             for(int i = 1, n=list.size(); i<n; i++) {
                 HistoryEventDTO nextHistoryEvent = getDtoFromHistoryEvent(list.get(i));
                 currentNode.setNextHistoryEvent(nextHistoryEvent);
                 currentNode = nextHistoryEvent;
            }
        }
        return rootHistoryEvent;
    }

    public HistoryEvent getNextHistoryEvent() {
        return nextHistoryEvent;
    }

    public void setNextHistoryEvent(HistoryEvent value) {
        this.nextHistoryEvent = value;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String value) {
        this.eventDate = value;
    }

    public String getEventKind() {
        return eventKind;
    }

    public void setEventKind(String value) {
        this.eventKind = value;
    }

    public String getCompletePath() {
        return completePath;
    }

    public void setCompletePath(String value) {
        this.completePath = value;
    }
}
