package speedr.sources.email;


import javax.mail.AuthenticationFailedException;
import java.util.ArrayList;
import java.util.List;

public class MockInbox implements EmailInbox {

    private String username = "speedrorg@gmail.com";
    private String hostname = "imap.gmail.com";
    private String password = "speedrspeedr";

    private List<Email> emails = new ArrayList<>();

    public MockInbox(String host, String user, String pass) throws AuthenticationFailedException {

        if(!user.equals(username) || !host.equals(hostname) || !pass.equals(password)){
            throw new AuthenticationFailedException("Authentication failed!");
        }

        emails.add(new Email(
            "testing@test.com",
            "Hot singles in your area",
            "There are hot singles. In your area.",
            false
        ));

        emails.add(new Email(
            "testing@test.com",
            "10 simple tips to peppering the perfect angus.",
            "Of all the ways to pepper your angus, using a spatula is the best.",
            true
        ));

        emails.add(new Email(
            "icequeen42@googlemail.com",
            "Can't hold it back anymore",
            "Don't let them in, don't let them see. Be the good girl you always have to be. Conceal, don't feel, don't let them know. Well, now they know! Let it go, let it go. Can't hold it back anymore. Let it go, let it go. Turn away and slam the door! ",
            false
        ));

    }

    @Override
    public Email getLastMessage() {
        return emails.get(emails.size()-1);
    }

    @Override
    public List<Email> getRecentMessages(int number) {

        if(number > emails.size()){
            number = emails.size();
        }

        return emails.subList(0, number);
    }
}
