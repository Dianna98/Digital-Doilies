import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Main {

    //this is the array for the gallery for up to 12 images
    static ArrayList<JPanel> arrayPanel;

    //the position for the image to be deleted
    static int pos;

    public static void main(String[] args){

        JFrame window = new JFrame("Digital Doily");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        GalleryPanel gallery = new GalleryPanel();
        JPanel southPanel = new JPanel();

        southPanel.setLayout(new FlowLayout());

        DrawingArea drawingArea = new DrawingArea();
        JPanel controlPanel = new JPanel();

        //the control panel is made of 18 rows and 1 column to display the available buttons/sliders/checkboxes
        controlPanel.setLayout(new GridLayout(18,1));
        controlPanel.setPreferredSize(new Dimension(320,650));

        //the first element of the SOUTH part of the app is the button DELETE to delete an image
        JButton delete = new JButton("Delete");

        gallery.setLayout(new GridLayout(1,12));

        arrayPanel = new ArrayList<>();

        /* here I add to the SOUTH part of the app the remaining panels which will contain the images
        *  after the button delete; set the border to white and the dimension to 80x80
        *
        */
        for(int i=0;i<12;i++){
            arrayPanel.add(new JPanel());
            arrayPanel.get(i).setOpaque(false);
            arrayPanel.get(i).setBorder(BorderFactory.createLineBorder(Color.WHITE));
            arrayPanel.get(i).setPreferredSize(new Dimension(80,80));
            gallery.add(arrayPanel.get(i));
            arrayPanel.get(i).addMouseListener(new SelectListener(i));
        }

        //just adding some buttons
        JButton colourChooser = new JButton("Pen Colour Chooser");
        JButton undo = new JButton("Undo");
        JButton save = new JButton("Save");
        JButton clear = new JButton("Clear");
        JButton redo = new JButton("Redo");
        JCheckBox eraser = new JCheckBox("Eraser");

        save.addActionListener(new GalleryPanel.Save(gallery));
        gallery.setLayout(new GridLayout(1,13));

        drawingArea.setBackground(Color.black);

        controlPanel.add(new JLabel("Control panel"));
        controlPanel.add(new JPanel());

        controlPanel.add(colourChooser);
        colourChooser.addActionListener(new DrawingArea.ColorChooser(drawingArea));

        controlPanel.add(new JPanel());

        DrawingArea.SizeSlider sizeSlider = new DrawingArea.SizeSlider(drawingArea);
        controlPanel.add(sizeSlider);

        JPanel eraserPanel = new JPanel(new GridLayout(1,3));
        eraserPanel.add(new JPanel());
        eraserPanel.add(eraser);
        eraserPanel.add(new JPanel());
        controlPanel.add(eraserPanel);

        eraser.addActionListener(new DrawingArea.Eraser(drawingArea));

        DrawingArea.SectorSlider sectorSlider = new DrawingArea.SectorSlider(drawingArea);
        controlPanel.add(sectorSlider);

        JCheckBox dLines = new JCheckBox("Display lines");
        dLines.addActionListener(new DrawingArea.DisplayLines(drawingArea));

        JCheckBox mirror = new JCheckBox("Reflection");
        mirror.addActionListener(new DrawingArea.ReflectListener(drawingArea));

        JPanel checkBox = new JPanel();
        checkBox.setLayout(new GridLayout(1,2));

        checkBox.add(dLines);
        checkBox.add(mirror);

        controlPanel.add(new JPanel());
        controlPanel.add(checkBox);

        controlPanel.add(new JPanel());
        controlPanel.add(undo);
        undo.addActionListener(new DrawingArea.UndoListener(drawingArea));

        controlPanel.add(redo);
        redo.addActionListener(new DrawingArea.RedoListener(drawingArea));

        controlPanel.add(clear);
        clear.addActionListener(new DrawingArea.ClearListener(drawingArea));

        controlPanel.add(new JPanel());
        controlPanel.add(save);

        main.add(drawingArea, BorderLayout.CENTER);
        southPanel.add(delete);
        delete.addActionListener(new GalleryPanel.Delete(gallery));

        southPanel.add(gallery);

        main.add(southPanel, BorderLayout.SOUTH);
        main.add(controlPanel, BorderLayout.WEST);

        window.add(main);

        window.setSize(1300,750);
        window.setVisible(true);

    }

    //the listener for the "select" button
    static class SelectListener extends MouseAdapter{

        int k;

        public SelectListener(int k){
            this.k = k;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            for(int i=0;i<12;i++){
                arrayPanel.get(i).setBorder(BorderFactory.createLineBorder(Color.WHITE));
            }

            /*when the user selects the image, it will have blue borders and the position for the
            image is saved
             */
            arrayPanel.get(k).setBorder(BorderFactory.createLineBorder(Color.blue));
            pos = k;
        }
    }


}

