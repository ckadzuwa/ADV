/*

[The "BSD licence"]
Copyright (c) 2004 Jean Bovet
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

1. Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.
3. The name of the author may not be used to endorse or promote products
derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/

package edu.usfca.vas.window.tools;

import edu.usfca.vas.graphics.IconManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DesignToolsAbstract extends JPanel {

    protected List toolsButton = new ArrayList();
    protected DesignToolsDG.DesignToolButton mouseButton;

    protected DesignToolsDG.DesignToolButton selectedButton;
    protected int selectedButtonClickCount;


    public DesignToolsDG.DesignToolButton createDesignToolButton(String iconFileName, String tooltip, int tool) {
        DesignToolsDG.DesignToolButton button = new DesignToolsDG.DesignToolButton(IconManager.getIcon(iconFileName), tool);
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(42, 42));
        button.setMaximumSize(new Dimension(42, 42));
        button.addActionListener(new DesignToolsDG.DefaultActionListener());
        button.addMouseListener(new DesignToolsDG.DefaultMouseAdapter());
        toolsButton.add(button);
        add(button);
        return button;
    }

    public void createDesignToolSeparator(int width) {
        add(Box.createHorizontalStrut(width));
    }

    public int getSelectedTool() {
        return selectedButton.tool;
    }

    public void selectButton(DesignToolsDG.DesignToolButton button) {
        button.setSelected(true);
        button.requestFocus();
        selectedButton = button;
    }

    public void buttonActionPerformed(ActionEvent e) {
        DesignToolsDG.DesignToolButton actionButton = (DesignToolsDG.DesignToolButton)e.getSource();

        Iterator iterator = toolsButton.iterator();
        while(iterator.hasNext()) {
            DesignToolsDG.DesignToolButton button = (DesignToolsDG.DesignToolButton)iterator.next();
            button.locked = false;
            if(button != actionButton)
                button.setSelected(false);
        }

        if(actionButton == mouseButton) {
            selectButton(mouseButton);
        } else {
            if(actionButton == selectedButton)
                selectedButtonClickCount++;
            else
                selectedButtonClickCount = 1;

            switch(selectedButtonClickCount) {
                case 1:
                    // Lock the button - So transitions to mouseButton only
                    // occur via user selection and not  automatically
                    actionButton.locked = true;
                    selectButton(actionButton);
                    actionButton.repaint();
                    break;
                case 2:
                    //Transition to mouseButton
                    selectButton(mouseButton);
                    break;
            }
        }
    }

    public void consumeSelectedState() {
        if(!selectedButton.locked) {
            selectedButton.setSelected(false);
            selectButton(mouseButton);
        }
    }

    public class DefaultActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            buttonActionPerformed(e);
        }
    }

    public class DefaultMouseAdapter extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
        }
    }

    public class DesignToolButton extends JToggleButton {

        public int tool;
        public boolean locked = false;

        public DesignToolButton(Icon icon, int tool) {
            super(icon);
            this.tool = tool;
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(locked) {
                g.setColor(Color.black);
                g.drawString("+", 5, getSize().height-5);
            }
        }
    }

}
