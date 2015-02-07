package speedr.core.notthings;

import speedr.core.things.Email;

import java.util.List;

/**
 * Created by rhizome on 05/02/2015.
 */
public interface EmailInbox {

    Email getLastMessage();
    List<Email> getRecentMessages(int number);

}
