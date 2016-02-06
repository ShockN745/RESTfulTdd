package request;

import org.junit.Test;

import static org.junit.Assert.*;

public class RequestImplTest {

    @Test
    public void extractService() throws Exception {
        String url = "/transactionservice/somethingelse";
        RequestImpl request = new RequestImpl(RequestImpl.HttpMethod.GET, url);
        String result = request.getService();
        assertEquals("transactionservice", result);
    }

    @Test
    public void extractMethod() throws Exception {
        String url = "/transactionservice/types";
        RequestImpl request = new RequestImpl(RequestImpl.HttpMethod.GET, url);
        assertEquals(RequestImpl.Method.TYPES, request.getMethod());
    }

    @Test
    public void extractParameter() throws Exception {
        String url = "/transactionservice/transaction/1024";
        RequestImpl request = new RequestImpl(RequestImpl.HttpMethod.GET, url);
        assertEquals("1024", request.getParameter());
    }

    @Test
    public void ensureWholeUrlIsValid() throws Exception {
        // Valid url
        assertUrlValid("/transactionservice/transaction/123423");
        assertUrlValid("/transactionservice/sum/41");
        assertUrlValid("/transactionservice/types/test");

        // Wrong parameter type
        assertUrlNotValid("/transactionservice/sum/notinteger");
        assertUrlNotValid("/transactionservice/types/152365");
        assertUrlNotValid("/transactionservice/transaction/notinteger");

        // Wrong method DONE
        assertUrlNotValid("/transactionservice/wrongmethod/34");

        // Wrong service DONE
        assertUrlNotValid("/wrongservice/transaction/645");

        // Incomplete url DONE
        assertUrlNotValid("/transactionservice/transaction/");
        assertUrlNotValid("/transactionservice/transaction");
        assertUrlNotValid("/transactionservice/");
        assertUrlNotValid("/transactionservice");
        assertUrlNotValid("/");
        assertUrlNotValid("");

        // Not url DONE
        assertUrlNotValid(null);
    }

    private void assertUrlValid(String url) {
        RequestImpl getRequest = new RequestImpl(RequestImpl.HttpMethod.GET, url);
        RequestImpl putRequest = new RequestImpl(RequestImpl.HttpMethod.PUT, url);
        assertTrue(getRequest.isValid() || putRequest.isValid());
    }

    private void assertUrlNotValid(String url) {
        RequestImpl getRequest = new RequestImpl(RequestImpl.HttpMethod.GET, url);
        RequestImpl putRequest = new RequestImpl(RequestImpl.HttpMethod.PUT, url);
        assertFalse(getRequest.isValid() || putRequest.isValid());
    }

    private void assertRequestValid(RequestImpl.HttpMethod httpMethod, String url) {
        RequestImpl request = new RequestImpl(httpMethod, url);
        assertTrue(request.isValid());
    }

    private void assertRequestNotValid(RequestImpl.HttpMethod httpMethod, String url) {
        RequestImpl request = new RequestImpl(httpMethod, url);
        assertFalse(request.isValid());
    }

    @Test
    public void ensureValidRequestMethod() throws Exception {
        assertRequestValid(RequestImpl.HttpMethod.GET, "/transactionservice/transaction/123423");
        assertRequestValid(RequestImpl.HttpMethod.PUT, "/transactionservice/transaction/123423");
        assertRequestValid(RequestImpl.HttpMethod.GET, "/transactionservice/sum/123423");
        assertRequestValid(RequestImpl.HttpMethod.GET, "/transactionservice/types/test");

        assertRequestNotValid(RequestImpl.HttpMethod.PUT, "/transactionservice/sum/123423");
        assertRequestNotValid(RequestImpl.HttpMethod.PUT, "/transactionservice/types/test");
    }

    @Test
    public void addPayloadOnPutRequest_success() throws Exception {
        RequestImpl request = new RequestImpl(RequestImpl.HttpMethod.PUT, "/transactionservice/transaction/123423");
        assertTrue(request.isValid());
        String payload = "PAYLOAD";
        request.addPayload(payload);
        assertEquals(request.getPayload(), payload);
    }

    @Test(expected= ImpossibleToAddPayloadException.class)
    public void addPayloadOnGetRequest_failure() throws Exception {
        RequestImpl request = new RequestImpl(RequestImpl.HttpMethod.GET, "/transactionservice/transaction/123423");
        assertTrue(request.isValid());
        String payload = "PAYLOAD";
        request.addPayload(payload);
    }
}