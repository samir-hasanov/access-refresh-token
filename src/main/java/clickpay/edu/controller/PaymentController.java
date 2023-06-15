package clickpay.edu.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay")
public class PaymentController {

    @GetMapping("/nar")
    public String getNar(){
        return "WELL COME NAR MOBILE";
    }
}
