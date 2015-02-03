package speedr.core.sources.email;

import speedr.core.entities.HasContent;

/**
 * speedr / Ed
 * 02/02/2015 12:07
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
