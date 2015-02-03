package speedr.core;

/**
 *
 * This is an integration test which contacts Gmail and retrieves some email messages.
 *
 */

import org.junit.Test;
import speedr.core.sources.email.Email;
import speedr.core.sources.email.IMAPInbox;

import static org.junit.Assert.assertTrue;

public class GmailTest {

    @Test
    public void GmailTest() throws Exception {

        IMAPInbox inbox = new IMAPInbox("imap.gmail.com", "speedrorg@gmail.com", "speedrspeedr");

        assertTrue(inbox.getStore().isConnected());

        for(Email e : inbox.getRecentMessages(3)){
            System.out.printf("Mail from: %s with subject '%s'\n", e.getFrom(), e.getSubject());
        }

    }

}
