package speedr.core.things;

import speedr.core.notthings.HasContent;

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
    private boolean read;

    public Email(String from, String subject, String body, boolean read){
        this.from = from;
        this.body = body;
        this.subject = subject;
        this.read = read;
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

    @Override
    public String toString(){
        return this.from + "\n  " + this.subject;
    }
}
