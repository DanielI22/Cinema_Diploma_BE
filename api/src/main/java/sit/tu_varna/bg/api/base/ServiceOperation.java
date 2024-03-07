package sit.tu_varna.bg.api.base;


public interface ServiceOperation<I extends ServiceRequest, R extends ServiceResponse> {
    R process(I request);
}
