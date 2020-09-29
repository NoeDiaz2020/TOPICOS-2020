/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package figurasapps;
import java.awt.Color;

public class PaintableShape {

  public static final int RECTANGLE = 0;
  public static final int ELLIPSE = 1;
  public static final int LINE = 2;

  public static final Color[] COLORS = new Color[] { Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA,
      Color.YELLOW, Color.BLACK,Color.ORANGE, Color.pink, Color.darkGray,Color.gray, Color.decode("#EDAE49"),Color.decode("#00798C"),Color.decode("#726DA8"),
      Color.decode("#A0D2DB"),Color.decode("#A6D49F"),Color.decode("#2AFC98"),Color.decode("#AF3B6E")};

  private int type;
  private boolean isFilled;
  private Color color;

  private int startX, startY;
  private int endX, endY;

  public PaintableShape(int startX, int startY, int endX, int endY, int type, boolean isFilled, Color color) {

    this.type = type;
    this.isFilled = isFilled;
    this.color = color;
    this.startX = startX;
    this.startY = startY;
    this.endX = endX;
    this.endY = endY;

  }

  public int getType() {
    return this.type;
  }

  public boolean isFilled() {
    return this.isFilled;
  }

  public Color getColor() {
    return this.color;
  }

  public int startX() {
    return this.startX;
  }

  public int startY() {
    return this.startY;
  }

  public int endX() {
    return this.endX;
  }

  public int endY() {
    return this.endY;
  }

}