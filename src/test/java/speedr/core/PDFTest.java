package speedr.core;

import org.junit.Test;
import speedr.core.notthings.PDFReader;

import static org.junit.Assert.assertTrue;

public class PDFTest {

    @Test
    public void LoadPDFTest(){
        PDFReader pdf = new PDFReader("/mock_pdf.pdf");
        assertTrue(pdf.getContent() != null);
        assertTrue(pdf.getContent().startsWith("Higher"));
    }

}
