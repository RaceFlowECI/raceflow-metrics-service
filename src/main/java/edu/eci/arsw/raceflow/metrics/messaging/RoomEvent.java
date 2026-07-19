package edu.eci.arsw.raceflow.metrics.messaging;

import java.time.Instant;

/** Domain event received from RabbitMQ describing a room lifecycle change. */
public class RoomEvent {

    private String eventType;
    private String roomCode;
    private String createdBy;
    private Instant timestamp;

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getRoomCode() { return roomCode; }
    public void setRoomCode(String roomCode) { this.roomCode = roomCode; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
