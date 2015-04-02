package adv.main;

import edu.usfca.xj.appkit.document.XJDataXML;
import edu.usfca.xj.appkit.document.XJDocument;

public class Document extends XJDocument {

	protected static final String DATA_KEY = "data";

	public void documentWillWriteData() {
		Window w = (Window) getWindow();
		XJDataXML data = (XJDataXML) getDocumentData();
		data.setDataForKey(this, DATA_KEY, w.getData());
	}

	public void documentDidReadData() {
		Window w = (Window) getWindow();
		XJDataXML data = (XJDataXML) getDocumentData();
		w.setData(data.getDataForKey(DATA_KEY));
	}

}
