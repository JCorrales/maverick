
package py.una.maverick.domain.gui;

import java.net.URL;

import javax.swing.ImageIcon;
import py.una.maverick.domain.Carta;


public abstract class ResourceManager {
    
    private static final String IMAGE_PATH_FORMAT = "/images/card_%s.png"; 
    
    public static ImageIcon getCardImage(Carta card) {
        // Use image order, which is different from value order.
        String sequenceNrString = card.getPalo() + "_"+card.getNumero();
        String path = String.format(IMAGE_PATH_FORMAT, sequenceNrString);
        return getIcon(path);
    }
    
    public static ImageIcon getIcon(String path) {
        URL url = ResourceManager.class.getResource(path);
        if (url != null) {
            return new ImageIcon(url);
        } else {
            throw new RuntimeException("Resource file not found: " + path);
        }
    }
    
}
