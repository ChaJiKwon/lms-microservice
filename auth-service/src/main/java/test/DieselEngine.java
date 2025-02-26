package test;

import org.springframework.stereotype.Component;

// Bean thứ hai
@Component("dieselEngine")
class DieselEngine implements Engine {
    @Override
    public String getType() {
        return "Diesel Engine";
    }
}
