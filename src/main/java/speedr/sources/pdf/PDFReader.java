package speedr.sources.pdf;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.FileInputStream;
import java.io.IOException;

public class PDFReader {

    private String text;

    public PDFReader(String source){

        try {

            PDFParser parser = new PDFParser(new FileInputStream(source));
            PDFTextStripper stripper = new PDFTextStripper();

            parser.parse();

            this.text = stripper.getText(new PDDocument(parser.getDocument()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String getText() {
        return text;
    }

}
