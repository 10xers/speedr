package speedr.sources.email;

/**
 *
 * Note: if you try and use this to log into Gmail, it'll fail and complain that you need to enable
 * "less secure apps" access. You'll get an email in your gmail showing you how to turn that on.
 *
 * We need to get around this somehow.
 *
 */


import org.apache.commons.io.IOUtils;
import speedr.sources.email.Email;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class IMAPInbox {

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

    public Email getLastMessage(){
        return this.getRecentMessages(0).get(0);
    }

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
                    // plain text email
                    out.add(new Email(m.getFrom()[0].toString(), m.getSubject(), m.getContent().toString(), read));
                } else {
                    // multi-part email

                    MimeMultipart multi = ((MimeMultipart)m.getContent());

                    String body = "";

                    for(int i = 0; i < multi.getCount(); ++i){

                        BodyPart bp = multi.getBodyPart(i);

                        if( bp.getDisposition() != null && bp.getDisposition().equalsIgnoreCase("ATTACHMENT")){

                        } else {
                            body += IOUtils.toString(bp.getInputStream());
                        }

                    }

                    out.add(new Email(m.getFrom()[0].toString(), m.getSubject(), body, read));

                }

            }

            f.close(false);

        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }

        return out;

    }


}
