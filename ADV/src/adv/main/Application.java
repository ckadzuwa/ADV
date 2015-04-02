package adv.main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.pushingpixels.substance.api.skin.SubstanceGraphiteGlassLookAndFeel;

import edu.usfca.xj.appkit.app.XJApplication;
import edu.usfca.xj.appkit.app.XJApplicationDelegate;
import edu.usfca.xj.appkit.document.XJDataXML;
import edu.usfca.xj.appkit.utils.XJLocalizable;

public class Application extends XJApplicationDelegate {

	public static void main(String[] args) {
		setNimbusLookAndFeel();
		//setSubtanceLookAndFeel();
		XJApplication.run(new Application(), args, "ADV");
		
	}

	private static void setNimbusLookAndFeel() {
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void setSubtanceLookAndFeel() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(new SubstanceGraphiteGlassLookAndFeel());
				} catch (Exception e) {
					System.out.println("Substance Graphite failed to initialize");
				}
			}
		});
	}

	public void appDidLaunch(String[] args) {

		XJApplication.setPropertiesPath("adv/properties/");
		XJApplication.addDocumentType(Document.class, Window.class, XJDataXML.class, "adv",
				XJLocalizable.getString("strings", "ADSVDocumentType"));

		XJApplication.shared();

		if (XJApplication.shared().getDocuments().size() == 0) {
			if (!XJApplication.shared().openLastUsedDocument())
				XJApplication.shared().newDocument();
		}

	}

	@Override
	public Class appPreferencesClass() {
		return Application.class;
	}

	public boolean supportsPersistence() {
		return false;
	}

}
