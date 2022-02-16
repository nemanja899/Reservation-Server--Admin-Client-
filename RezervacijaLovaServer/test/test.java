/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class test {
    public static void main(String[] args) {
        JPanel panel = createPanel();
        JFrame frame = createFrame();
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JPanel createPanel() {
        JPanel mainPanel = new JPanel(){
            @Override
            public boolean isOptimizedDrawingEnabled() {
                return false;
            }
        };
        mainPanel.setLayout(new OverlayLayout(mainPanel));

        JButton button = new JButton("Show Message");
        button.setAlignmentX(0.1f);
        button.setAlignmentY(0.1f);

        JPanel popupPanel = createPopupPanel(button);
        popupPanel.setAlignmentX(0.5f);
        popupPanel.setAlignmentY(1.1f);

        button.addActionListener(e -> {
            button.setEnabled(false);
            popupPanel.setVisible(true);
        });

        mainPanel.add(popupPanel);
        mainPanel.add(button);

        return mainPanel;
    }

    private static JPanel createPopupPanel(JComponent overlapComponent) {
        JPanel popupPanel = new JPanel(new BorderLayout());
        popupPanel.setOpaque(false);
        popupPanel.setMaximumSize(new Dimension(150, 70));
        popupPanel.setBorder(new LineBorder(Color.gray));
        popupPanel.setVisible(false);

        JLabel label = new JLabel("HI there!");
        popupPanel.add(wrapInPanel(label), BorderLayout.CENTER);

        JButton popupCloseButton = new JButton("Close");
        popupPanel.add(wrapInPanel(popupCloseButton), BorderLayout.SOUTH);

        popupCloseButton.addActionListener(e -> {
            overlapComponent.setEnabled(true);
            popupPanel.setVisible(false);
        });

        return popupPanel;
    }

    private static JPanel wrapInPanel(JComponent component) {
        JPanel jPanel = new JPanel();
        jPanel.setBackground(new Color(50, 210, 250, 150));
        jPanel.add(component);
        return jPanel;
    }


    private static JFrame createFrame() {
        JFrame frame = new JFrame("OverlayLayout Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(400, 300));
        return frame;
    }
}



 class OverlayLayoutTest extends JFrame {
   public OverlayLayoutTest() {
      setTitle("OverlayLayout Test");
      JPanel panel = new JPanel() {
         public boolean isOptimizedDrawingEnabled() {
            return false;
         }
      };
      LayoutManager overlay = new OverlayLayout(panel);
      panel.setLayout(overlay);
      JButton button = new JButton("Small");
      button.setMaximumSize(new Dimension(75, 50));
      button.setBackground(Color.white);
      button.setAlignmentX(LEFT_ALIGNMENT);
      button.setAlignmentY(BOTTOM_ALIGNMENT);
      panel.add(button);
      button = new JButton("Medium Btn");
      button.setMaximumSize(new Dimension(125, 75));
      button.setBackground(Color.lightGray);
      panel.add(button);
      button = new JButton("Large Button");
      button.setMaximumSize(new Dimension(200, 100));
      button.setBackground(Color.orange);
      panel.add(button);
      add(panel, BorderLayout.CENTER);
      setSize(400, 300);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setVisible(true);
   }  
   public static void main(String args[]) {
      new OverlayLayoutTest();
   }
}

