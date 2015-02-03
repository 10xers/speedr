package speedr.sources.email;

import speedr.sources.HasContent;

/**
 *
 * This is a proxy object used to hold the contents of Emails retrieved from an
 * inbox. They are built by the Inbox classes.
 *
 */

public class Email implements HasContent {

    private String from;
    private String body;
    private String subject;

    public Email(String from, String subject, String body){
        this.from = from;
        this.body = body;
        this.subject = subject;
    }

    @Override
    public String getContent() {
        return this.body;
    }

    public String getFrom() {
        return from;
    }

    public String getSubject() {
        return subject;
    }
}
