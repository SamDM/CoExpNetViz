package be.samey.gui.controller;

/*
 * #%L
 * CoExpNetViz
 * %%
 * Copyright (C) 2015 PSB/UGent
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
import be.samey.internal.CyAppManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author sam
 */
public class ProfLoadBtnController extends AbstrController implements ActionListener {

    public ProfLoadBtnController(CyAppManager cyAppManager) {
        super(cyAppManager);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String profileTitle = (String) JOptionPane.showInputDialog(
            getGuiManager().getInpPnl(),
            "Choose a settings profile",
            "title",
            JOptionPane.PLAIN_MESSAGE,
            null,
            getGuiManager().getProfileTitles(),
            getGuiManager().getProfileTitles()[0]);
        if (profileTitle != null) {
            getGuiManager().loadProfile(profileTitle);
        }
    }

}
