package speedr.sources.email;

import org.apache.commons.io.IOUtils;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;

/**
 * This class parses multipart emails into plaintext for the thingy to consume.
 */

public class MultipartParser {

    public static String parse(Message m) throws IOException, MessagingException {

        MimeMultipart multi = ((MimeMultipart)m.getContent());

        System.out.println("The type of this email was " + multi.getContentType());

        String body = "";

        for(int i = 0; i < multi.getCount(); ++i){

            BodyPart bp = multi.getBodyPart(i);

            System.out.printf("  Processing body part: ");

            if( bp.getDisposition() != null && bp.getDisposition().equalsIgnoreCase("ATTACHMENT")){
                System.out.printf("attachment\n");
            } else {
                System.out.printf("not attachment. ");
                System.out.printf("(%s)", bp.getContentType());

                body += IOUtils.toString(bp.getInputStream());
                System.out.printf("\n");
            }

        }
        return body;
    }

}
