package com.fjss23.jobsearch.email.ses;

public class EmailNotification {
    private Long id;
    private String source;
    private String destination;
    private String eventType;
    private String messageId;

    public EmailNotification(String source, String destination, String eventType, String messageId) {
        this.source = source;
        this.destination = destination;
        this.eventType = eventType;
        this.messageId = messageId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
