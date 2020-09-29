/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package figurasapps;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Stack;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Paint extends JFrame {

  private final int UPDATE_RATE = 60;

  private int width;
  private int height;

  private PanelCanvas canvas;
  private JPanel mainPanel, toolsPanel;
  private JLabel lblMousePosition;
  private JButton btnUnDo, btnClear;
  private JComboBox<String> coBoxColor, coBoxShape;
  private JCheckBox chBoxIsFilled;

  private Stack<PaintableShape> shapes;

  private int startX, startY;
  private int endX, endY;

  private boolean isDrawing = false;

  public Paint(int width, int height) {
    this.width = width;
    this.height = height;

    this.canvas = new PanelCanvas();
    this.mainPanel = new JPanel(new BorderLayout());
    this.toolsPanel = new JPanel();
    this.lblMousePosition = new JLabel("0, 0");
    this.btnUnDo = new JButton("Undo");
    this.btnClear = new JButton("Clear");
    this.coBoxColor = new JComboBox<String>(new String[] { "RED", "GREEN", "BLUE", "CYAN", "MAGENTA", "YELLOW","BLACK","ORANGE","PINK","DARCK GREY","GRAY","SUNRAY",
        "LAPIS LAZULI","DARCK BLUE GRAY","LIGTH BLUE","CELADON","SPRING GREEN","MYSTIC MAROON"});
    
    this.coBoxShape = new JComboBox<String>(new String[] { "RECTANGLE", "ELLIPSE", "LINE" });
    this.chBoxIsFilled = new JCheckBox("Filled");

    shapes = new Stack<PaintableShape>();

    addAttributes();
    addListeners();
    build();

    this.pack();
    this.setLocationRelativeTo(null);

    startLoop();
  }

  public void addAttributes() {
    this.setTitle("FIGURES/TOPICOS/ITL/DIAZ CERVERA BRIAN NOE");
    this.setVisible(true);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
  }

  public void addListeners() {
    // Undo button.
    btnUnDo.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (shapes.size() > 0)
          shapes.pop();
      }
    });

    // Clear button.
    btnClear.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (shapes.size() > 0)
          shapes.clear();
      }
    });

    // Canvas mouse coordinates.
    canvas.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        startX = e.getX();
        startY = e.getY();

        isDrawing = true;
      }

      public void mouseReleased(MouseEvent e) {
        shapes.push(new PaintableShape(startX, startY, endX, endY, coBoxShape.getSelectedIndex(),
            chBoxIsFilled.isSelected(), PaintableShape.COLORS[coBoxColor.getSelectedIndex()]));

        isDrawing = false;
      }
    });

    canvas.addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {
        lblMousePosition.setText(e.getX() + ", " + e.getY());
      }

      public void mouseDragged(MouseEvent e) {
        boolean lineSelected = coBoxShape.getSelectedIndex() == PaintableShape.LINE;

        endX = (lineSelected) ? e.getX() : e.getX() - startX;
        endY = (lineSelected) ? e.getY() : e.getY() - startY;

        lblMousePosition.setText(startX + ", " + startY + "; " + endX + ", " + endY);
      }
    });
  }

  public void build() {
    this.toolsPanel.add(btnUnDo);
    this.toolsPanel.add(btnClear);
    this.toolsPanel.add(coBoxColor);
    this.toolsPanel.add(coBoxShape);
    this.toolsPanel.add(chBoxIsFilled);

    this.mainPanel.add(toolsPanel, BorderLayout.NORTH);
    this.mainPanel.add(canvas, BorderLayout.CENTER);
    this.mainPanel.add(lblMousePosition, BorderLayout.SOUTH);

    this.add(mainPanel);
  }

  public void startLoop() {
    Thread drawLoop = new Thread() {
      public void run() {
        while (true) {
          repaint();

          try {
            Thread.sleep(1000 / 60); // Desired frame rate.
          } catch (InterruptedException ex) {
          }
        }
      }
    }
            ;

    drawLoop.start();
  }

  class PanelCanvas extends JPanel {

    protected void paintComponent(Graphics g) {
      super.paintComponent(g);

      g.setColor(Color.decode("#F4F7F5"));
      g.fillRect(0, 0, width, height);

      shapes.stream().forEach(shape -> {
        g.setColor(shape.getColor());

        switch (shape.getType()) {
          case PaintableShape.RECTANGLE:
            if (shape.isFilled())
              g.fillRect(shape.startX(), shape.startY(), shape.endX(), shape.endY());
            else
              g.drawRect(shape.startX(), shape.startY(), shape.endX(), shape.endY());
            break;
          case PaintableShape.ELLIPSE:
            if (shape.isFilled())
              g.fillOval(shape.startX(), shape.startY(), shape.endX(), shape.endY());
            else
              g.drawOval(shape.startX(), shape.startY(), shape.endX(), shape.endY());
            break;
          case PaintableShape.LINE:
            g.drawLine(shape.startX(), shape.startY(), shape.endX(), shape.endY());
            break;
        }

      });

      if (isDrawing) {
        g.setColor(Color.decode("#BBAADD"));
        switch (coBoxShape.getSelectedIndex()) {
          case PaintableShape.RECTANGLE:
            g.drawRect(startX, startY, endX, endY);
            break;
          case PaintableShape.ELLIPSE:
            g.drawOval(startX, startY, endX, endY);
            break;
          case PaintableShape.LINE:
            g.drawLine(startX, startY, endX, endY);
            break;
        }
      }
    }

    @Override
    public Dimension getPreferredSize() {
      return new Dimension(width, height);
    }

  }

}