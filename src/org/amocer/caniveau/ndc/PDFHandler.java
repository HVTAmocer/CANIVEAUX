package org.amocer.caniveau.ndc;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.apache.fop.apps.*;
import org.xml.sax.SAXException;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URL;


public class PDFHandler {

    public String createPDFFile(File file, ByteArrayOutputStream xmlSource, File templateFile) throws IOException, SAXException {
        // creation of transform source
        URL url = templateFile.toURI().toURL();
        StreamSource transformSource = new StreamSource(url.openStream());
        // create an instance of fop factory
        FopFactory fopFactory = FopFactory.newInstance(new File("xsl/fop.xconf"));
        // a user agent is needed for transformation
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        // to store output
        ByteArrayOutputStream pdfoutStream = new ByteArrayOutputStream();
        StreamSource source = new StreamSource(new ByteArrayInputStream(xmlSource.toByteArray()));
        try {
            TransformerFactory transfact = TransformerFactory.newInstance();

            Transformer xslfoTransformer = transfact.newTransformer(transformSource);
            // Construct fop with desired output format
            try {
                Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, pdfoutStream);
                // Resulting SAX events (the generated FO)
                // must be piped through to FOP
                Result res = new SAXResult(fop.getDefaultHandler());

                // Start XSLT transformation and FOP processing
                try {
                    // everything will happen here..
                    xslfoTransformer.transform(source, res);

                    // if you want to save PDF file use the following code
                    OutputStream out = new FileOutputStream(file);
                    out = new BufferedOutputStream(out);
                    FileOutputStream str = new FileOutputStream(file);
                    str.write(pdfoutStream.toByteArray());
                    str.close();
                    out.close();

                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            } catch (FOPException e) {
                e.printStackTrace();
            }
        } catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
            e.printStackTrace();
        }
        return file.getPath();
    }

/*    private File copyInputStreamToFile(InputStream inputStream) throws IOException {
        File file = Files.createTempFile("temp",".tmp").toFile();
        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[8192];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
        return file;
    }*/

    public ByteArrayOutputStream getXMLSource(DonneesNDC2 data)  {
        JAXBContext context;

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        try {
            context = JAXBContext.newInstance(DonneesNDC2.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(data, outStream);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return outStream;
    }
}
