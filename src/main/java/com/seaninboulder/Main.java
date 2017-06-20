package com.seaninboulder;


import com.google.common.collect.Sets;
import com.google.inject.*;
import com.seaninboulder.module.BannerModule;
import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        InputStream input = Main.class.getClassLoader().getResourceAsStream("puppy.properties");
        properties.load(input);

        input.close();

        Injector injector = Guice.createInjector(
            new PuppyModule(properties),
            new BannerModule()
        );

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(
                injector.getInstance(PuppyParser.class),
                0,
                Long.parseLong(properties.getProperty("puppy.check.interval")),
                TimeUnit.MINUTES);
    }

    static final class PuppyModule implements Module {
        private final Properties properties;

        PuppyModule(Properties properties) {
            this.properties = properties;
        }

        @Override
        public void configure(Binder binder) {
            Twilio.init(
                    properties.getProperty("twilio.account.sid"),
                    properties.getProperty("twilio.account.auth_token")
            );

            String[] toNumbers = properties.getProperty("twilio.to.numbers").split(",");

            Set<PhoneNumber> toPhoneNumbers = Sets.newHashSet();
            for (String number : toNumbers) {
                toPhoneNumbers.add(new PhoneNumber(number));
            }

            String fromNumber = properties.getProperty("twilio.from.number");

            binder.bind(PhoneNumber.class).toInstance(new PhoneNumber(fromNumber));
            binder.bind(new TypeLiteral<Set<PhoneNumber>>(){})
                    .toInstance(Sets.newHashSet(toPhoneNumbers));
        }
    }
}
