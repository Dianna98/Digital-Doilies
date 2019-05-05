import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/*  this algorithm creates the app and every time we draw something on the drawing area
    we call repaint()
 */

public class DrawingArea extends JPanel {

    //the array for the drawn points
    ArrayList<Point2D> pointsArray = new ArrayList<>();
    //the array for the lines drawn
    static ArrayList<ArrayList<Point2D> > arrayArray = new ArrayList<>();
    //the array for colors, each line will have its color; same for size reflect and eraser
    static ArrayList<Color> arrayColor = new ArrayList<>();
    static ArrayList<Integer> arraySize = new ArrayList<>();
    static ArrayList<Boolean> arrayReflect = new ArrayList<>();
    static ArrayList<Boolean> arrayEraser = new ArrayList<>();

    //initially we have 1 sector
    static int noSectors = 1;
    //we set to not reflect from the begining
    static boolean reflect = false;
    static int index = -1;
    static Color color = (Color.white);
    //the size of the pen
    static int penSize = 1;
    //booleans to determine if the buttons for eraser or toggle were pressed or not
    static boolean eraser = false;
    static boolean toggle = false;
    static BufferedImage img;


    public DrawingArea(){

        MA ma = new MA();

        this.addMouseListener(ma);

        this.addMouseMotionListener(ma);
    }

    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        Graphics2D g1 = (Graphics2D) graphics;
        g1.setColor(Color.white);
        double sectors = noSectors;

        img = new BufferedImage(this.getWidth(),this.getHeight(),BufferedImage.TYPE_INT_ARGB);
        AlphaComposite alphaComposite1 = AlphaComposite.getInstance(AlphaComposite.DST_OUT);
        AlphaComposite alphaComposite2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
        Graphics2D g = img.createGraphics();

        if(sectors > 1) {
            if (toggle == true) {
                for (int i = 1; i <= noSectors; i++) {
                    g1.draw(new Line2D.Double(this.getWidth() / 2, this.getHeight() / 2, this.getWidth() / 2, -1000));
                    g1.rotate(Math.toRadians(360 / sectors), this.getWidth() / 2, this.getHeight() / 2);
                }
            }
        }
        for(int i = 0; i <= index; i++) {
            g.setColor(arrayColor.get(i));
            g.setStroke(new BasicStroke(arraySize.get(i),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
            for(int j = 0; j < arrayArray.get(i).size()-1; j++){
                Line2D line = new Line2D.Double(arrayArray.get(i).get(j).getX(),arrayArray.get(i).get(j).getY(),arrayArray.get(i).get(j+1).getX(),arrayArray.get(i).get(j+1).getY());
                Line2D rLine = new Line2D.Double(this.getWidth()-arrayArray.get(i).get(j).getX(), arrayArray.get(i).get(j).getY(), this.getWidth()-arrayArray.get(i).get(j+1).getX(), arrayArray.get(i).get(j+1).getY());
                if(arrayEraser.get(i) == true){
                    g.setComposite(alphaComposite1);
                }else{
                    g.setComposite(alphaComposite2);
                }
                for(int k=1;k <= noSectors;k++){
                    g.draw(line);
                    if(arrayReflect.get(i) == true){
                        g.draw(rLine);
                    }
                    g.rotate(Math.toRadians(360/sectors),this.getWidth()/2,this.getHeight()/2);
                }

            }
        }
        g1.drawImage(img,0,0,null);
    }


    public class MA extends MouseAdapter{

        boolean draw = false;

        @Override
        public void mousePressed(MouseEvent e) {
            draw = true;
            index++;

            for(int i=index; i<arrayArray.size();i++){
                arrayReflect.remove(i);
                arrayEraser.remove(i);
                arraySize.remove(i);
                arrayColor.remove(i);
                arrayArray.remove(i);
            }

            Point2D p = new Point2D.Double(e.getX(), e.getY());
            pointsArray = new ArrayList<>();
            pointsArray.add(p);
            arrayArray.add(pointsArray);
            arrayArray.set(index, pointsArray);
            arrayColor.add(color);
            arraySize.add(penSize);
            arrayEraser.add(eraser);
            arrayReflect.add(reflect);
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            draw = false;
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (draw == true) {
                Point2D p = new Point2D.Double(e.getX(), e.getY());
                pointsArray.add(p);
                arrayArray.set(index, pointsArray);
                repaint();
            }
        }
    }


    static class ReflectListener implements ActionListener{

        DrawingArea da;

        public ReflectListener(DrawingArea da){
            this.da=da;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            reflect = !reflect;
        }
    }


    static class ClearListener implements ActionListener{

        DrawingArea da;

        public ClearListener(DrawingArea da){
            this.da = da;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            arrayArray.clear();
            arrayColor.clear();
            arraySize.clear();
            arrayEraser.clear();
            arrayReflect.clear();
            index = -1;
            da.repaint();
        }
    }


    static class UndoListener implements ActionListener{

        DrawingArea da;

        public UndoListener(DrawingArea da){
            this.da = da;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(index > -1) index--;
            da.repaint();
        }
    }


    static class RedoListener implements ActionListener{

        DrawingArea da;

        public RedoListener(DrawingArea da){
            this.da = da;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(arrayArray.size()-1 != index ) index++;
            da.repaint();
        }
    }

    static class DisplayLines implements ActionListener{

        DrawingArea da;

        public DisplayLines(DrawingArea da){
            this.da = da;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            toggle = !toggle;
            da.repaint();
        }
    }

    static class ColorChooser implements ActionListener{

        DrawingArea da;

        public ColorChooser(DrawingArea da){
            this.da = da;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            color = JColorChooser.showDialog(null,"Pick your color!",color);
            if(color == null){
                color = (Color.white);
            }
        }
    }

    public static class SizeSlider extends JPanel {
        JSlider slider;
        JLabel label;
        DrawingArea da;
        public SizeSlider(DrawingArea da){
            this.da = da;
            slider = new JSlider(JSlider.HORIZONTAL,1,50,1);
            slider.setMajorTickSpacing(5);
            slider.setPaintTicks(true);
            add(slider);

            label = new JLabel("Pen size: " + penSize);
            add(label);
            event e = new event();
            slider.addChangeListener(e);

        }

        public class event implements ChangeListener{

            public void stateChanged(ChangeEvent e){
                int value = slider.getValue();
                label.setText("Pen size: " + value);
                penSize = value;
                da.repaint();
            }
        }

    }

    public static class SectorSlider extends JPanel{

        JSlider slider;
        JLabel label;
        DrawingArea da;

        public SectorSlider(DrawingArea da){
            this.da = da;
            slider = new JSlider(JSlider.HORIZONTAL,1,50,1);
            slider.setMajorTickSpacing(5);
            slider.setPaintTicks(true);
            add(slider);

            label = new JLabel("Sectors: " + noSectors);
            add(label);
            event e = new event();
            slider.addChangeListener(e);
        }

        public class event implements ChangeListener{
            public void stateChanged(ChangeEvent e){
                int value = slider.getValue();
                label.setText("Sectors: " + value);
                noSectors = value;
                da.repaint();
            }
        }
    }

    static class Eraser extends JPanel implements ActionListener {

        DrawingArea da;

        public Eraser(DrawingArea da){
            this.da = da;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            eraser = !eraser;
        }
    }

}

