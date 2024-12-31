package test;

import org.springframework.stereotype.Component;

// Bean đầu tiên
@Component("petrolEngine")
class PetrolEngine implements Engine {
    @Override
    public String getType() {
        return "Petrol Engine";
    }
}
