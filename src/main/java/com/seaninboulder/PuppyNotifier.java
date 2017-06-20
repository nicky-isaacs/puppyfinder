package com.seaninboulder;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import javax.inject.Inject;
import java.util.Set;

public class PuppyNotifier {
    private final PhoneNumber FROM_NUMBER;
    private final Set<PhoneNumber> TO_NUMBERS;

    @Inject
    public PuppyNotifier(PhoneNumber from_number, Set<PhoneNumber> to_numbers) {
        FROM_NUMBER = from_number;
        TO_NUMBERS = to_numbers;
    }

    public void execute(Set<Puppy> puppies) {
        for (Puppy puppy : puppies) {
            for (PhoneNumber phoneNumber : TO_NUMBERS) {
                Message.creator(
                        phoneNumber,
                        FROM_NUMBER,
                        puppy.toString())
                        .setMediaUrl(puppy.getPhotoURL())
                        .create();
            }
        }
    }
}
