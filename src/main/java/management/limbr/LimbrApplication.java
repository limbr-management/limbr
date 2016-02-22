/*
 * Copyright (c) 2016 Tyrel Haveman and contributors.
 *
 * This file is part of Limbr.
 *
 * Limbr is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Limbr is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Limbr.  If not, see <http://www.gnu.org/licenses/>.
 */

package management.limbr;

import management.limbr.data.UserRepository;
import management.limbr.data.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LimbrApplication {

    private static final Logger LOG = LoggerFactory.getLogger(LimbrApplication.class);

    public static void main(String[] args) {
        LOG.info("Limbr is starting up.");
        SpringApplication.run(LimbrApplication.class, args).close();
        LOG.info("Limbr has stopped.");
    }

    @Bean
    public CommandLineRunner loadData(UserRepository repository) {
        return args -> {
            repository.save(new User("admin", "Admin", "deadbeef", "admin@limbr.management"));
            repository.save(new User("bob", "Bob Builder", "deadbeef", "bob@limbr.management"));
        };
    }
}
