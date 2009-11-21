package cards.figure;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;

/**
 * @Added by Billy
 * 
 */
public class WrapLabel extends Label {
    private String sourceText;

    private String sourceTextOrig;

    private boolean deal = false;

    private int margin = 0;

    /**
     * 
     */
    public WrapLabel() {
        super();
        this.sourceText = "";
        // TODO Auto-generated constructor stub
    }

    /**
     * @param s
     */
    public WrapLabel(String s) {
        this.sourceText = s;
        // TODO Auto-generated constructor stub
    }

    public WrapLabel(String s, int margin) {
        this.sourceText = s;
        this.margin = margin;
    }

    @Override
    public void setText(String s) {
        this.sourceText = s;
        this.sourceTextOrig = s;
        this.deal = false;
    }

    public String getTextOrig() {
        return this.sourceTextOrig;
    }

    @Override
    protected void paintFigure(Graphics graphics) {
        this.modifyText();
        super.paintFigure(graphics);
    }

    @Override
    public void setBounds(Rectangle rect) {
        this.deal = false;
        super.setBounds(rect);
    }

    private void modifyText() {
        if (this.deal) {
            return;
        }
        Font f = this.getFont();
        if (f != null) {
            int width = this.getBounds().width - this.margin;
            StringBuffer buf = new StringBuffer();
            if (width > 5) {
                String str = this.sourceText;
                while (str.length() > 0) {
                    int sub = this.getLargestSubstringConfinedTo(str, f, width);
                    while (sub > 0 && (!isSubQualified(sub, str)))
                        sub = sub - 1;

                    if ((sub == 0) && (str.length() > 0)) {
                        break;
                    }

                    if (sub < str.length()) {
                        buf.append(str.substring(0, sub));
                        buf.append("\n");
                        str = str.substring(sub);
                    }
                    else {
                        buf.append(str);
                        str = "";
                    }
                }
            }
            this.deal = true;
            super.setText(buf.toString());
        }
    }

    private boolean isSubQualified(int subInside, String strInside) {
        // if(strInside.substring(subInside-1,subInside).equals("
        // ")||strInside.substring(subInside,subInside+1).equals(" "))
        String firstWord;
        int firstSpace = strInside.indexOf(" ");
        if (firstSpace == -1)
            firstWord = strInside;
        else
            firstWord = strInside.substring(0, firstSpace);

        if (subInside < firstWord.length() || subInside >= strInside.length() || strInside.substring(subInside - 1, subInside).equals(" ")
                || strInside.substring(subInside, subInside + 1).equals(" "))
            return true;
        return false;
    }

    private int getLargestSubstringConfinedTo(String s, Font f, int availableWidth) {
        FontMetrics metrics = FigureUtilities.getFontMetrics(f);
        int min, max;
        float avg = metrics.getAverageCharWidth();
        min = 0;
        max = s.length() + 1;

        int guess = 0, guessSize = 0;
        while ((max - min) > 1) {
            guess = guess + (int) ((availableWidth - guessSize) / avg);

            if (guess >= max) {
                guess = max - 1;
            }
            if (guess <= min) {
                guess = min + 1;
            }

            guessSize = FigureUtilities.getTextExtents(s.substring(0, guess), f).width;

            if (guessSize < availableWidth) {
                min = guess;
            }
            else {
                max = guess;
            }
        }
        return min;
    }
}
