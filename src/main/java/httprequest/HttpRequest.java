package httprequest;

/**
 * Represent and validate a Http server
 */
public interface HttpRequest {
    //todo also implement with standard java library

    enum HttpMethod {
        GET,
        PUT
    }
    enum Method {
        TRANSACTION,
        TYPES,
        SUM,
        UNKNOWN
    }

    boolean isValid();

    HttpMethod getHttpMethod();
    Method getMethod();
    String getParameter();

    void setPayload(String payload) throws ImpossibleToAddPayloadException;
    String getPayload();
}
