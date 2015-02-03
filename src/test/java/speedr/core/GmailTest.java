package speedr.core;

/**
 *
 * This is an integration test which contacts Gmail and retrieves some email messages.
 *
 */

import org.junit.Test;
import speedr.sources.email.Email;
import speedr.sources.email.IMAPInbox;
import speedr.sources.email.POP3Inbox;

import static org.junit.Assert.assertTrue;

public class GmailTest {

    @Test
    public void IMAPTest() throws Exception {

        IMAPInbox inbox = new IMAPInbox("imap.gmail.com", "speedrorg@gmail.com", "speedrspeedr");

        assertTrue(inbox.getStore().isConnected());

        for(Email e : inbox.getRecentMessages(1)){
            assertTrue(e.getFrom() != null);
            assertTrue(e.getSubject() != null);
            assertTrue(e.getContent() != null);
        }

    }

    @Test
    public void POP3Test() throws Exception {

        POP3Inbox inbox = new POP3Inbox("pop.gmail.com", "speedrorg@gmail.com", "speedrspeedr");

        assertTrue(inbox.getStore().isConnected());

        for(Email e : inbox.getRecentMessages(1)){
            assertTrue(e.getFrom() != null);
            assertTrue(e.getSubject() != null);
            assertTrue(e.getContent() != null);
        }

    }

}
