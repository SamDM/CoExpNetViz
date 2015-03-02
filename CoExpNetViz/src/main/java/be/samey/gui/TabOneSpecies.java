package be.samey.gui;

import be.samey.cynetw.CevNetworkCreator;
import be.samey.model.CoreStatus;
import be.samey.model.GuiStatus;
import be.samey.model.Model;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;

/**
 *
 * @author sam
 */
public class TabOneSpecies extends JPanel implements Observer {

    private final Model model;
    private final GuiStatus guiStatus;

    //input baits
    private JRadioButton inpBaitRb;
    private JRadioButton fileBaitRb;
    private JLabel inpBaitLbl;
    private JTextArea inpBaitTa;
    private JScrollPane inpBaitSp;
    private JLabel fileBaitLbl;
    private JPanel fileBaitPnl;
    private JTextField fileBaitTf;
    private JButton fileBaitBtn;
    //choose species
    private JLabel chooseSpeciesLbl;
    private JScrollPane chooseSpeciesSp;
    private JPanel chooseSpeciesPnl;
    private JButton addSpeciesBtn;
    //choose cutoff
    private JLabel chooseCutoffLbl;
    private JPanel cutoffPnl;
    private JLabel nCutoffLbl;
    private JSpinner nCutoffSp;
    private JLabel pCutoffLbl;
    private JSpinner pCutoffSp;
    private JCheckBox saveFileChb;
    private JTextField saveFileTf;
    private JButton saveFileBtn;
    private JPanel saveFilePnl;
    private JButton goBtn;
    private JButton resetBtn;

    private ButtonGroup inpBaitOrfileBaitBg;
    private SpinnerModel nCutoffSm;
    private SpinnerModel pCutoffSm;

    public TabOneSpecies(Model model) {
        this.model = model;
        this.guiStatus = model.getGuiStatus();
        guiStatus.addObserver(this);
        constructGui();
        refreshGui();
    }

    private void constructGui() {
        //input bait genes or choose file
        inpBaitRb = new JRadioButton("Input bait genes");
        inpBaitRb.addActionListener(new ChooseSpeciesOrOwnDataBgAl());
        fileBaitRb = new JRadioButton("Upload file with bait genes");
        fileBaitRb.addActionListener(new ChooseSpeciesOrOwnDataBgAl());
        inpBaitOrfileBaitBg = new ButtonGroup();
        inpBaitOrfileBaitBg.add(inpBaitRb);
        inpBaitOrfileBaitBg.add(fileBaitRb);
        inpBaitLbl = new JLabel("Enter bait genes");
        inpBaitTa = new JTextArea();
        inpBaitTa.setToolTipText("Enter gene identifiers seperated by whitespace, eg 'Solyc02g04650'");
        inpBaitSp = new JScrollPane(inpBaitTa);
        inpBaitSp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        fileBaitLbl = new JLabel("Choose file with bait genes");
        fileBaitPnl = new JPanel();
        fileBaitPnl.setLayout(new BoxLayout(fileBaitPnl, BoxLayout.LINE_AXIS));
        fileBaitTf = new JTextField();
        fileBaitTf.setToolTipText("Path to file with gene identifiers, gene identifiers must be separated by white space");
        fileBaitBtn = new JButton("Browse");
        fileBaitBtn.setIcon(UIManager.getIcon("FileView.directoryIcon"));
        fileBaitBtn.addActionListener(new BrowseAl(this, fileBaitTf, "Choose file with bait genes", BrowseAl.FILE));
        //choose species
        chooseSpeciesLbl = new JLabel("<html>Choose which data sets to use,<br>"
            + "pick one dataset for every species you have specified in the bait genes");
        chooseSpeciesPnl = new JPanel();
        chooseSpeciesPnl.setLayout(new BoxLayout(chooseSpeciesPnl, BoxLayout.PAGE_AXIS));
        chooseSpeciesSp = new JScrollPane(chooseSpeciesPnl);
        chooseSpeciesSp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        addSpeciesBtn = new JButton("Add species");
        addSpeciesBtn.addActionListener(new addSpeciesAl());
        //choose cutoffs
        chooseCutoffLbl = new JLabel("Choose cutoff");
        cutoffPnl = new JPanel();
        cutoffPnl.setLayout(new BoxLayout(cutoffPnl, BoxLayout.LINE_AXIS));
        nCutoffLbl = new JLabel("Neg. cutoff");
        nCutoffSm = new SpinnerNumberModel(guiStatus.getDefaultNegCutoff(), -1.0, 0.0, 0.1);
        nCutoffSp = new JSpinner(nCutoffSm);
        nCutoffSp.setMaximumSize(new Dimension(64, 32));
        pCutoffLbl = new JLabel("Pos. cutoff");
        pCutoffSm = new SpinnerNumberModel(guiStatus.getDefaultPosCutoff(), 0.0, 1.0, 0.1);
        pCutoffSp = new JSpinner(pCutoffSm);
        pCutoffSp.setMaximumSize(new Dimension(64, 32));
        //save output
        saveFileChb = new JCheckBox("Save output");
        saveFileChb.addActionListener(new SaveFileAl());
        saveFileTf = new JTextField();
        saveFileTf.setToolTipText("Path to output directory");
        saveFilePnl = new JPanel();
        saveFilePnl.setLayout(new BoxLayout(saveFilePnl, BoxLayout.LINE_AXIS));
        saveFileBtn = new JButton("Browse");
        saveFileBtn.setIcon(UIManager.getIcon("FileView.directoryIcon"));
        saveFileBtn.addActionListener(new BrowseAl(this, saveFileTf, "Choose output directory", BrowseAl.DIRECTORY));
        //run analysis or reset form
        goBtn = new JButton("Run analysis");
        goBtn.addActionListener(new GoAl());
        resetBtn = new JButton("Reset form");
        resetBtn.addActionListener(new ResetAl());

        //create gridbaglayout and add components to it
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weightx = 0.5;
        //----------------------------------------------------------------------
        //input bait genes
        //top two radio buttons (input bait genes or file)
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(10, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 00;
        c.gridwidth = 1;
        add(inpBaitRb, c);
        c.gridx = 1;
        c.gridy = 00;
        c.gridwidth = 1;
        add(fileBaitRb, c);
        //input baits or choose file
        //input bait genes label
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 01;
        c.gridwidth = 3;
        add(inpBaitLbl, c);
        //bait genes text area
        c.insets = new Insets(0, 0, 0, 0);
        c.weighty = 1.0;
        c.ipady = (160);
        c.gridx = 0;
        c.gridy = 02;
        c.gridwidth = 3;
        add(inpBaitSp, c);
        //choose bait genes file label
        c.weighty = 0.0;
        c.ipady = 0;
        c.gridx = 0;
        c.gridy = 03;
        c.gridwidth = 3;
        add(fileBaitLbl, c);
        //bait genes file textfield and button
        c.gridx = 0;
        c.gridy = 04;
        c.gridwidth = 3;
        fileBaitPnl.add(fileBaitTf);
        fileBaitPnl.add(fileBaitBtn);
        add(fileBaitPnl, c);
        //----------------------------------------------------------------------
        //choose species
        //choose species label
        c.insets = new Insets(10, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 10;
        c.gridwidth = 3;
        add(chooseSpeciesLbl, c);
        //choose species scrollpane
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 11;
        c.ipady = (160);
        c.gridwidth = 3;
        add(chooseSpeciesSp, c);
        //add species button
        c.insets = new Insets(0, 0, 0, 90);
        c.gridx = 0;
        c.gridy = 12;
        c.ipady = (0);
        c.gridwidth = 1;
        add(addSpeciesBtn, c);
        //----------------------------------------------------------------------
        //choose cutoffs
        //choose cutoff label
        c.insets = new Insets(10, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 20;
        c.gridwidth = 3;
        add(chooseCutoffLbl, c);
        //cutoff labels and spinners
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 21;
        c.gridwidth = 3;
        cutoffPnl.add(nCutoffLbl);
        cutoffPnl.add(Box.createRigidArea(new Dimension(5, 0))); //spacer
        cutoffPnl.add(nCutoffSp);
        cutoffPnl.add(Box.createRigidArea(new Dimension(10, 0)));
        cutoffPnl.add(pCutoffLbl);
        cutoffPnl.add(Box.createRigidArea(new Dimension(5, 0)));
        cutoffPnl.add(pCutoffSp);
        add(cutoffPnl, c);
        //----------------------------------------------------------------------
        //save file
        //checkbox
        c.insets = new Insets(10, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 30;
        c.gridwidth = 1;
        add(saveFileChb, c);
        //save file textfield and button
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 31;
        c.gridwidth = 3;
        saveFilePnl.add(saveFileTf);
        saveFilePnl.add(saveFileBtn);
        add(saveFilePnl, c);
        //go and clear buttons
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(10, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 32;
        c.gridwidth = 1;
        add(goBtn, c);
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        c.gridx = 2;
        c.gridy = 32;
        c.gridwidth = 1;
        add(resetBtn, c);
    }

    //resets all options on the tab and clears all input fields
    private void refreshGui() {
        //reset species
        guiStatus.removeAllSpecies();

        //reset radiobuttons
        inpBaitRb.setSelected(true);
        guiStatus.setInpBaitSelected(inpBaitRb.isSelected());
        saveFileChb.setSelected(false);
        guiStatus.setSaveFileSelected(saveFileChb.isSelected());

        //reset cutoffs
        nCutoffSp.setValue(guiStatus.getDefaultNegCutoff());
        pCutoffSp.setValue(guiStatus.getDefaultPosCutoff());

        //clear fields
        inpBaitTa.setText("");
        fileBaitTf.setText("");
        saveFileTf.setText("");

    }

    @Override
    //called whenever the model notify's its observers
    //watch out to not trigger an update from the model from within this method.
    public void update(Observable o, Object arg) {

        //update: input bait genes or upload a file
        boolean inpBaitSelected = guiStatus.isInpBaitSelected();
        inpBaitLbl.setEnabled(inpBaitSelected);
        inpBaitTa.setEnabled(inpBaitSelected);
        fileBaitLbl.setEnabled(!inpBaitSelected);
        fileBaitTf.setEnabled(!inpBaitSelected);
        fileBaitBtn.setEnabled(!inpBaitSelected);

        //update: choose species
        chooseSpeciesPnl.removeAll();
        Iterator<SpeciesEntry> seIt = guiStatus.getSpeciesIterator();
        while (seIt.hasNext()) {
            SpeciesEntry se = seIt.next();
            se.setAlignmentX(Component.CENTER_ALIGNMENT);
            chooseSpeciesPnl.add(se);
        }
        chooseSpeciesPnl.revalidate();
        chooseSpeciesPnl.repaint();

        //update: save file or not
        boolean saveFileSelected = guiStatus.isSaveFileSelected();
        saveFileTf.setEnabled(saveFileSelected);
        saveFileBtn.setEnabled(saveFileSelected);
    }

    //some action listeners
    //created when the user chooses to input baits directly or with a file
    private class ChooseSpeciesOrOwnDataBgAl implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            guiStatus.setInpBaitSelected(inpBaitRb.isSelected());
        }

    }

    //created when the user chooses to input baits directly or with a file
    private class addSpeciesAl implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            SpeciesEntry se = new SpeciesEntry();
            guiStatus.addSpecies(se);
        }

    }

    //created when the user checks/unchecks the save file checkbox
    private class SaveFileAl implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            guiStatus.setSaveFileSelected(saveFileChb.isSelected());
        }

    }

    //created when the user clicks the "reset" button
    private class ResetAl implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            refreshGui();
        }

    }

    //created when the user clicks the "Run analysis" button (this.goBtn)
    private class GoAl implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //check if all paths entered by the user are correct
            Path p1, p2, p3, p4;
            try {
                if (!guiStatus.isInpBaitSelected()) {
                    p3 = Paths.get(fileBaitTf.getText()).toRealPath();
                }
                if (!guiStatus.isSaveFileSelected()) {
                    p4 = Paths.get(saveFileTf.getText()).toRealPath();
                }
            } catch (IOException ex) {
                System.out.format("Coult not resolve file %s%n", ex);
                //TODO: warn the user somehow
            }

            //for debugging
            new CevNetworkCreator(model).test();
        }

    }

}
