package com.ruoyi.course.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.course.domain.TutorRequest;
import com.ruoyi.course.domain.TutorRequestBid;

public interface TutorRequestMapper
{
    int insertTutorRequest(TutorRequest row);

    List<TutorRequest> selectTutorRequestList(TutorRequest query);

    TutorRequest selectTutorRequestById(@Param("requestId") String requestId);

    int updateTutorRequestStatusByOwner(@Param("requestId") String requestId, @Param("userId") String userId, @Param("status") Long status);

    int insertTutorRequestBid(TutorRequestBid row);

    TutorRequestBid selectTutorRequestBidById(@Param("bidId") String bidId);

    List<TutorRequestBid> selectTutorRequestBidList(@Param("requestId") String requestId);

    int countActiveBidByTeacher(@Param("requestId") String requestId, @Param("teacherId") String teacherId);

    int updateTutorRequestBidStatus(@Param("bidId") String bidId, @Param("status") Long status);

    int updateTutorRequestBidStatusByRequest(@Param("requestId") String requestId, @Param("status") Long status);

    List<TutorRequestBid> selectMyBidList(@Param("teacherId") String teacherId);
}
