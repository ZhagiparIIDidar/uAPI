package kz.uapi.domain.other_obj;

import kz.uapi.enums.*;
import kz.uapi.interfaces.*;

public class Request implements RegistrationRequest, ScheduleRequest, ResearchRequest {

    private int requestId;
    private String senderLogin;
    private String content;
    private RequestStatus status;
    private RequestType requestType;

    public Request(int requestId, String senderLogin, String content,
            RequestStatus status, RequestType requestType) {
        this.requestId = requestId;
        this.senderLogin = senderLogin;
        this.content = content;
        this.status = status;
        this.requestType = requestType;
    }

    public int getRequestId() {
        return requestId;
    }

    public String getSenderLogin() {
        return senderLogin;
    }

    public String getContent() {
        return content;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public void setSenderLogin(String senderLogin) {
        this.senderLogin = senderLogin;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    @Override
    public String toString() {
        return requestId + " | " + senderLogin + " | " + requestType + " | " + status + " | " + content;
    }

    @Override
    public void sendResearchRequest(String projectId) {
        throw new UnsupportedOperationException("Unimplemented method 'sendResearchRequest'");
    }

    @Override
    public void sendScheduleRequest(String slot) {
        throw new UnsupportedOperationException("Unimplemented method 'sendScheduleRequest'");
    }

    @Override
    public void sendRegistrationRequest(String courseId) {
        throw new UnsupportedOperationException("Unimplemented method 'sendRegistrationRequest'");
    }
}