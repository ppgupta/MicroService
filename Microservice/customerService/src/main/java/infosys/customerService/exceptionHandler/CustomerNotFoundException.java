package infosys.customerService.exceptionHandler;

public class CustomerNotFoundException extends RuntimeException{

    //construct to accept a custom error message

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
