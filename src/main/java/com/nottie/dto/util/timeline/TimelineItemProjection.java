package com.nottie.dto.util.timeline;

import java.time.Instant;

public interface TimelineItemProjection {
    Long getId();
    String getType(); // tipo da interação: POST, LIKE, RETWEET
    String getContent();
    Long getCreatorId();
    Long getWorkstationId();
    Instant getCreatedAt();
    Long getNoteId();
}
