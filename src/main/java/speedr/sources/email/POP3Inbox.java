package speedr.sources.email;

import com.sun.mail.pop3.POP3SSLStore;

import javax.mail.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class POP3Inbox {

    private Store store;

    public POP3Inbox(String host, String user, String pass) {

        String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        Properties pop3Props = new Properties();

        pop3Props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
        pop3Props.setProperty("mail.pop3.socketFactory.fallback", "false");
        pop3Props.setProperty("mail.pop3.port",  "995");
        pop3Props.setProperty("mail.pop3.socketFactory.port", "995");

        URLName url = new URLName("pop3", host, 995, "", user, pass);

        Session mailSession = Session.getInstance(pop3Props, null);

        try {
            store = new POP3SSLStore(mailSession, url);
            store.connect();
        } catch (javax.mail.MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    public Store getStore(){
        return this.store;
    }

    public Email getLastMessage(){
        return this.getRecentMessages(0).get(0);
    }

    public List<Email> getRecentMessages(int number) {

        List<Email> out = new ArrayList<>();

        try {

            Folder f = this.store.getFolder("INBOX");
            f.open(Folder.READ_ONLY);

            int max = f.getMessageCount();
            int min = max-number < 1 ? 1 : max-number;

            for(Message m : f.getMessages(min, max)){
                // todo: multipart email parsing.
                boolean read = m.getFlags().contains(Flags.Flag.SEEN);
                out.add(new Email(m.getFrom()[0].toString(), m.getSubject(), m.getContent().toString(), read));
            }

            f.close(false);

        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }

        return out;

    }

}
