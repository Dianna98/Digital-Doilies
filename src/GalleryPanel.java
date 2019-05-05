import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GalleryPanel extends JPanel {

    static ArrayList<BufferedImage> bufferedImages = new ArrayList<>();

    public GalleryPanel(){
        for(int i=1;i<=12;i++){
            bufferedImages.add(null);
        }
    }

    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        for(int i=0; i<12; i++){
            if(bufferedImages.get(i) != null){
                graphics.fillRect(Main.arrayPanel.get(i).getX(),Main.arrayPanel.get(i).getY(),Main.arrayPanel.get(i).getWidth(),Main.arrayPanel.get(i).getHeight());
                Image image = bufferedImages.get(i).getScaledInstance(Main.arrayPanel.get(i).getWidth(),Main.arrayPanel.get(i).getHeight(),Image.SCALE_SMOOTH);
                graphics.drawImage(image,Main.arrayPanel.get(i).getX(),Main.arrayPanel.get(i).getY(),null);
            }
        }
    }

    static class Save extends JPanel implements ActionListener{

        GalleryPanel galleryPanel;

        public Save(GalleryPanel galleryPanel){
            this.galleryPanel = galleryPanel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            for(int i=0; i<12; i++){
                if(bufferedImages.get(i) == null) {
                    bufferedImages.set(i,DrawingArea.img);
                    break;
                }
            }

            galleryPanel.repaint();
        }
    }

    static class Delete implements ActionListener {

        GalleryPanel galleryPanel;

        public Delete(GalleryPanel galleryPanel){
            this.galleryPanel = galleryPanel;
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            bufferedImages.set(Main.pos, null);
            galleryPanel.repaint();
        }
    }

}
