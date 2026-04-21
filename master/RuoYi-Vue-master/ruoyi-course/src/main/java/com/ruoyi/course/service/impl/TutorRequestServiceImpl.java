package com.ruoyi.course.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.course.domain.TutorRequest;
import com.ruoyi.course.domain.TutorRequestBid;
import com.ruoyi.course.mapper.TutorRequestMapper;
import com.ruoyi.course.service.ITutorRequestService;

@Service
public class TutorRequestServiceImpl implements ITutorRequestService
{
    @Autowired
    private TutorRequestMapper tutorRequestMapper;

    @Override
    public int createTutorRequest(TutorRequest row)
    {
        if (row == null)
        {
            throw new ServiceException("参数不能为空");
        }
        if (StringUtils.isEmpty(row.getUserId()))
        {
            throw new ServiceException("请先登录");
        }
        if (StringUtils.isEmpty(row.getSubjectId()))
        {
            throw new ServiceException("请选择科目");
        }
        if (StringUtils.isEmpty(row.getGradeId()))
        {
            throw new ServiceException("请选择年级");
        }
        if (StringUtils.isEmpty(row.getAreaId()))
        {
            throw new ServiceException("请选择地区");
        }
        if (row.getBudgetMin() == null || row.getBudgetMax() == null)
        {
            throw new ServiceException("请填写预算范围");
        }
        if (row.getBudgetMin().compareTo(BigDecimal.ZERO) < 0 || row.getBudgetMax().compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new ServiceException("预算金额必须大于0");
        }
        if (row.getBudgetMin().compareTo(row.getBudgetMax()) > 0)
        {
            throw new ServiceException("预算区间不合法");
        }
        if (StringUtils.isEmpty(row.getContactInfo()))
        {
            throw new ServiceException("请填写联系方式");
        }
        if (row.getContactInfo().trim().length() > 100)
        {
            throw new ServiceException("联系方式不能超过100字符");
        }
        if (StringUtils.isEmpty(row.getDemandDesc()))
        {
            throw new ServiceException("请填写需求描述");
        }
        if (row.getDemandDesc().trim().length() > 1000)
        {
            throw new ServiceException("需求描述不能超过1000字符");
        }
        row.setStatus(0L);
        row.setCreatedAt(new Date());
        return tutorRequestMapper.insertTutorRequest(row);
    }

    @Override
    public List<TutorRequest> selectTutorRequestList(TutorRequest query)
    {
        return tutorRequestMapper.selectTutorRequestList(query == null ? new TutorRequest() : query);
    }

    @Override
    public TutorRequest selectTutorRequestById(String requestId)
    {
        if (StringUtils.isEmpty(requestId))
        {
            return null;
        }
        return tutorRequestMapper.selectTutorRequestById(requestId);
    }

    @Override
    public int closeTutorRequestByOwner(String requestId, String userId)
    {
        if (StringUtils.isEmpty(requestId) || StringUtils.isEmpty(userId))
        {
            throw new ServiceException("参数不能为空");
        }
        TutorRequest row = tutorRequestMapper.selectTutorRequestById(requestId);
        if (row == null)
        {
            throw new ServiceException("需求单不存在");
        }
        if (!userId.equals(row.getUserId()))
        {
            throw new ServiceException("无权关闭该需求单");
        }
        if (row.getStatus() != null && row.getStatus() != 0L)
        {
            throw new ServiceException("当前状态不可关闭");
        }
        return tutorRequestMapper.updateTutorRequestStatusByOwner(requestId, userId, 2L);
    }

    @Override
    public int bidTutorRequest(String requestId, String teacherId, String message, BigDecimal expectedRate)
    {
        if (StringUtils.isEmpty(requestId) || StringUtils.isEmpty(teacherId))
        {
            throw new ServiceException("参数不能为空");
        }
        TutorRequest request = tutorRequestMapper.selectTutorRequestById(requestId);
        if (request == null)
        {
            throw new ServiceException("需求单不存在");
        }
        if (request.getStatus() == null || request.getStatus() != 0L)
        {
            throw new ServiceException("该需求单已结束接单");
        }
        if (tutorRequestMapper.countActiveBidByTeacher(requestId, teacherId) > 0)
        {
            throw new ServiceException("你已接过该需求，请勿重复提交");
        }
        if (StringUtils.isNotEmpty(message) && message.trim().length() > 1000)
        {
            throw new ServiceException("留言不能超过1000字符");
        }
        if (expectedRate != null && expectedRate.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new ServiceException("期望时薪必须大于0");
        }
        TutorRequestBid bid = new TutorRequestBid();
        bid.setRequestId(requestId);
        bid.setTeacherId(teacherId);
        bid.setMessage(StringUtils.trimToEmpty(message));
        bid.setExpectedRate(expectedRate);
        bid.setStatus(0L);
        bid.setCreatedAt(new Date());
        return tutorRequestMapper.insertTutorRequestBid(bid);
    }

    @Override
    public List<TutorRequestBid> selectTutorRequestBidList(String requestId)
    {
        if (StringUtils.isEmpty(requestId))
        {
            return Collections.emptyList();
        }
        return tutorRequestMapper.selectTutorRequestBidList(requestId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int confirmBidByOwner(String bidId, String ownerUserId)
    {
        if (StringUtils.isEmpty(bidId) || StringUtils.isEmpty(ownerUserId))
        {
            throw new ServiceException("参数不能为空");
        }
        TutorRequestBid target = tutorRequestMapper.selectTutorRequestBidById(bidId);
        if (target == null)
        {
            throw new ServiceException("接单记录不存在");
        }
        TutorRequest req = tutorRequestMapper.selectTutorRequestById(target.getRequestId());
        if (req == null)
        {
            throw new ServiceException("需求单不存在");
        }
        if (!ownerUserId.equals(req.getUserId()))
        {
            throw new ServiceException("无权操作该接单");
        }
        if (req.getStatus() == null || req.getStatus() != 0L)
        {
            throw new ServiceException("该需求单当前状态不可确认");
        }
        if (target.getStatus() == null || target.getStatus() != 0L)
        {
            throw new ServiceException("该接单已处理");
        }
        int affected = tutorRequestMapper.updateTutorRequestBidStatus(bidId, 1L);
        tutorRequestMapper.updateTutorRequestBidStatusByRequest(req.getRequestId(), 2L);
        tutorRequestMapper.updateTutorRequestStatusByOwner(req.getRequestId(), ownerUserId, 1L);
        return affected;
    }

    @Override
    public int rejectBidByOwner(String bidId, String ownerUserId)
    {
        if (StringUtils.isEmpty(bidId) || StringUtils.isEmpty(ownerUserId))
        {
            throw new ServiceException("参数不能为空");
        }
        TutorRequestBid target = tutorRequestMapper.selectTutorRequestBidById(bidId);
        if (target == null)
        {
            throw new ServiceException("接单记录不存在");
        }
        TutorRequest req = tutorRequestMapper.selectTutorRequestById(target.getRequestId());
        if (req == null)
        {
            throw new ServiceException("需求单不存在");
        }
        if (!ownerUserId.equals(req.getUserId()))
        {
            throw new ServiceException("无权操作该接单");
        }
        if (target.getStatus() == null || target.getStatus() != 0L)
        {
            throw new ServiceException("该接单已处理");
        }
        return tutorRequestMapper.updateTutorRequestBidStatus(bidId, 2L);
    }

    @Override
    public List<TutorRequestBid> selectMyBidList(String teacherId)
    {
        if (StringUtils.isEmpty(teacherId))
        {
            return Collections.emptyList();
        }
        return tutorRequestMapper.selectMyBidList(teacherId);
    }
}
