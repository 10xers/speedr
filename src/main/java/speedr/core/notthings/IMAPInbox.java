package speedr.core.notthings;

/**
 *
 * Note: if you try and use this to log into Gmail, it'll fail and complain that you need to enable
 * "less secure apps" access. You'll get an email in your gmail showing you how to turn that on.
 *
 * We need to get around this somehow.
 *
 */


import speedr.core.things.Email;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class IMAPInbox implements EmailInbox {

    private Store store;

    public IMAPInbox(String host, String user, String pass) throws AuthenticationFailedException {

        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");

        Session mailSession = Session.getDefaultInstance(props);

        try {
            store = mailSession.getStore("imaps");
            store.connect(host, user, pass);
        } catch (javax.mail.AuthenticationFailedException e) {
            throw e;
        } catch(javax.mail.MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    public Store getStore(){
        return this.store;
    }

    @Override
    public Email getLastMessage(){
        return this.getRecentMessages(0).get(0);
    }

    @Override
    public List<Email> getRecentMessages(int number) {

        List<Email> out = new ArrayList<>();

        try {

            Folder f = this.store.getFolder("INBOX");
            f.open(Folder.READ_ONLY);

            int max = f.getMessageCount();
            int min = max-number < 1 ? 1 : max-(number-1);

            for(Message m : f.getMessages(min, max)){

                boolean read = m.getFlags().contains(Flags.Flag.SEEN);

                if(m.getContent() instanceof String){

                    System.out.println("The type of this email was plaintext");

                    // plain text email
                    out.add(new Email(m.getFrom()[0].toString(), m.getSubject(), m.getContent().toString(), read));

                } else if(m.getContent() instanceof MimeMultipart) {

                    // multi-part email

                    System.out.println("The type of this email was " + ((MimeMultipart)m.getContent()).getContentType());
                    String body = MultipartParser.parse(m);
                    out.add(new Email(m.getFrom()[0].toString(), m.getSubject(), body, read));

                } else {

                    throw new IllegalArgumentException("Unknown email part.");

                }

            }

            f.close(false);

        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }

        Collections.reverse(out);
        return out;

    }




}
