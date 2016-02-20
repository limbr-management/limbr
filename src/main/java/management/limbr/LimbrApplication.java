package management.limbr;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

@SpringBootApplication
public class LimbrApplication {

	public static void main(String[] args) {
		SpringApplication.run(LimbrApplication.class, args);
	}

	@Service
    public static class TestServiceOne {
        public String sayHi() {
            return "Hello, Spring Initializer!";
        }
    }

    @Theme("valo")
    @SpringUI(path = "")
    public static class VaadinUI extends UI {
        @Autowired
        TestServiceOne service;

        @Override
        protected void init(VaadinRequest request) {
            setContent(new Label(service.sayHi()));
        }
    }
}
