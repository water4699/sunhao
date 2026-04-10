package com.ruoyi.course.service;

import java.util.List;
import com.ruoyi.course.domain.TutorRequest;
import com.ruoyi.course.domain.TutorRequestBid;

public interface ITutorRequestService
{
    int createTutorRequest(TutorRequest row);

    List<TutorRequest> selectTutorRequestList(TutorRequest query);

    TutorRequest selectTutorRequestById(String requestId);

    int closeTutorRequestByOwner(String requestId, String userId);

    int bidTutorRequest(String requestId, String teacherId, String message, java.math.BigDecimal expectedRate);

    List<TutorRequestBid> selectTutorRequestBidList(String requestId);

    int confirmBidByOwner(String bidId, String ownerUserId);

    int rejectBidByOwner(String bidId, String ownerUserId);

    List<TutorRequestBid> selectMyBidList(String teacherId);
}
