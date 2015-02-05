package speedr.sources.email;

import org.apache.commons.io.IOUtils;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * This class parses multipart emails into plaintext for the thingy to consume.
 */

public class MultipartParser {

    public static String parse(Message m) throws IOException, MessagingException {

        MimeMultipart multi = ((MimeMultipart)m.getContent());

        try {

            if (multi.getContentType().startsWith("multipart/ALTERNATIVE")) {
                return parseAlternative(m);
            } else {
                return basicParse(m);
            }

        } catch(Exception e){

            throw new RuntimeException(e);

        }

    }

    private static String basicParse(Message m) throws Exception {

        MimeMultipart multi = ((MimeMultipart)m.getContent());

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

    private static String parseAlternative(Message m) throws Exception {

        // the message type is alternative, so we will pick the first plaintext
        // typed content and return a basic parse of that.

        MimeMultipart multi = ((MimeMultipart)m.getContent());

        String body = "";

        for(int i = 0; i < multi.getCount(); ++i){

            BodyPart bp = multi.getBodyPart(i);

            if( bp.getDisposition() != null && bp.getDisposition().equalsIgnoreCase("ATTACHMENT")){
                System.out.printf("attachment\n");
            } else if(bp.getContentType().contains("TEXT/PLAIN")){

                String text = IOUtils.toString(bp.getInputStream());

                text = Pattern.compile("\\[image:[^\\]]+\\]", Pattern.DOTALL | Pattern.MULTILINE).matcher(text).replaceAll("");
                text = Pattern.compile("<[^<]+>", Pattern.DOTALL | Pattern.MULTILINE).matcher(text).replaceAll("");

                return text;
            }

        }

        // if we got this far, then we didn't find a good body part in the list of alternatives to
        // return. It's time to take drastic measures. We'll return the input of the last one.

        return IOUtils.toString(multi.getBodyPart(multi.getCount() - 1).getInputStream());

    }

}
