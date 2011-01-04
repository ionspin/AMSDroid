package rs.ac.bg.rcub.ams.config.hgsm;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

/**
 * Quick & dirty XML parser implemented using STAX.
 * <p>
 * Note: STAX is included in jdk6, additional jars are needed for jdk5.
 * <p>
 * http://stax.codehaus.org/ - reference impl
 * <p>
 * https://sjsxp.dev.java.net/ - specification
 * 
 * 
 * @author choppa
 * 
 */
public class XMLReader {
	private URL url;

	public XMLReader(String path) throws Exception {
		url = new URL(path);
	}

	public ArrayList<HGSMSite> readDoc() throws Exception {
		InputStream in = url.openStream();
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader parser = factory.createXMLStreamReader(in);
		int inName = 0;
		int inSite = 0; // could be boolean
		int inAff = 0;
		try {
			ArrayList<HGSMSite> sites = new ArrayList<HGSMSite>();
			HGSMSite site = null;
			for (int event = parser.next(); event != XMLStreamConstants.END_DOCUMENT; event = parser.next()) {
				switch (event) {
				case XMLStreamConstants.START_ELEMENT:
					if ("name".equals(parser.getLocalName())) {
						inName++;
						break;
					} else if ("affiliation".equals(parser.getLocalName())) {
						inAff++;
						break;
					}
					if ("site".equals(parser.getLocalName())) {
						inSite++;
						site = new HGSMSite();
					}
					break;
				case XMLStreamConstants.END_ELEMENT:
					if ("name".equals(parser.getLocalName())) {
						inName--;
						break;
					} else if ("affiliation".equals(parser.getLocalName())) {
						inAff--;
						break;
					}
					if ("site".equals(parser.getLocalName())) {
						inSite--;
						sites.add(site);
					}
					break;
				case XMLStreamConstants.CHARACTERS:
					if (inSite > 0 && inName > 0)
						site.setName(parser.getText());
					else if (inSite > 0 && inAff > 0)
						site.setAffiliation(parser.getText());
					break;
				} // end switch
			}
			return sites;
		} finally {
			parser.close(); // maybe not needed?
			in.close();
		}
	}
}
