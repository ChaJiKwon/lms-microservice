package test;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
class Car  implements DisposableBean {
    private Engine engine;

    // Sử dụng @Qualifier để chỉ định bean cụ thể
    @Autowired
    public Car(@Qualifier("dieselEngine") Engine engine) {
        this.engine = engine;
    }

    public void showEngineType() {
        System.out.println("Car is using: " + engine.getType());
    }
//    @PreDestroy
//    public void cleanup() {
//        System.out.println("disposing bean");
//    }
    @PostConstruct
    public void init() throws Exception {
        System.out.println("Bean Car has been instantiated and I'm the init() method.");
    }
    @Override
    public void destroy() throws Exception {
        System.out.println("disposing bean");
    }
}
