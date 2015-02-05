package speedr.core;

import org.junit.Test;
import speedr.sources.email.Email;
import speedr.sources.email.IMAPInbox;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class EmailParsingTest {

    @Test
    public void testing() throws Exception{

        IMAPInbox inbox = new IMAPInbox("imap.gmail.com", "speedrorg@gmail.com", "speedrspeedr");

        List<Email> emails = inbox.getRecentMessages(30);

        Email multipart = emails.get(emails.size()-2);

        System.out.println(multipart.getContent());

        assertTrue(multipart != null);

    }


}
