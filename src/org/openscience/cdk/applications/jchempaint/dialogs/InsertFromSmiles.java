/*
 *  $RCSfile$
 *  $Author$
 *  $Date$
 *  $Revision$
 *
 *  Copyright (C) 1997-2005  The JChemPaint project
 *
 *  Contact: jchempaint-devel@lists.sourceforge.net
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1
 *  of the License, or (at your option) any later version.
 *  All we ask is that proper credit is given for our work, which includes
 *  - but is not limited to - adding the above copyright notice to the beginning
 *  of your source code files, and to any copyright notice that you may distribute
 *  with programs based on this work.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.openscience.cdk.applications.jchempaint.dialogs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.vecmath.Vector2d;

import org.openscience.cdk.ChemModel;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.SetOfMolecules;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.geometry.GeometryTools;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.renderer.Renderer2DModel;
import org.openscience.cdk.tools.manipulator.ChemModelManipulator;

import org.openscience.cdk.applications.jchempaint.*;

/**
 *  Internal frame to allow for changing the propterties.
 *
 * @cdk.module jchempaint
 * @author     steinbeck
  *@created    22. April 2005
 */
public class InsertFromSmiles extends JFrame
{

	JChemPaintPanel jcpPanel;
	JTextField valueText;


	/**
	 *  Constructor for the InsertFromSmiles object
	 *
	 *@param  jcp  Description of the Parameter
	 */
	public InsertFromSmiles(JChemPaintPanel jcpPanel)
	{
		super("Insert from SMILES");
		this.jcpPanel = jcpPanel;
		getContentPane().setLayout(new BorderLayout());
		JPanel southPanel = new JPanel();
		JButton cancelButton = new JButton("Cancel");
		JButton openButton = new JButton("Insert");
		openButton.addActionListener(new OpenAction());
		cancelButton.addActionListener(new CancelAction());
		southPanel.add(openButton);
		southPanel.add(cancelButton);

		JPanel centerPanel = new JPanel();
		JLabel valueLabel = new JLabel("Enter SMILES string:");
		valueText = new JTextField(20);
		valueText.addActionListener(new OpenAction());
		centerPanel.add(valueLabel);
		centerPanel.add(valueText);
		//setSize(300,100);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", centerPanel);
		getContentPane().add("South", southPanel);
		pack();
	}


	/**
	 *  Description of the Method
	 */
	public void closeFrame()
	{
		dispose();
	}


	/**
	 *  Description of the Class
	 *
	 *@author     steinbeck
	 *@created    22. April 2005
	 */
	class OpenAction extends AbstractAction
	{
		/**
		 *  Constructor for the OpenAction object
		 */
		OpenAction()
		{
			super("Open");
		}


		/**
		 *  Description of the Method
		 *
		 *@param  e  Description of the Parameter
		 */
		public void actionPerformed(ActionEvent e)
		{
			generateModel();
		}


		/**
		 *  Description of the Method
		 */
		private void generateModel()
		{
			try
			{
				String SMILES = valueText.getText();
				SmilesParser sp = new SmilesParser();
				Molecule m = sp.parseSmiles(SMILES);
				
				// ok, now generate 2D coordinates
				StructureDiagramGenerator sdg = new StructureDiagramGenerator();
				try
				{
					sdg.setMolecule(m);
					sdg.generateCoordinates(new Vector2d(0,1));
					m = sdg.getMolecule();
				} catch (Exception exc)
				{
					exc.printStackTrace();
				}

				// now return structure to model
				SetOfMolecules som = new SetOfMolecules();
				som.addMolecule(m);
				ChemModel chemModel = new ChemModel();
				chemModel.setSetOfMolecules(som);
				
				jcpPanel.processChemModel(chemModel);
				
				String title = "Created from SMILES: " + SMILES;
				jcpPanel.lastUsedJCPP.getJChemPaintModel().setTitle(title);
				((JFrame) jcpPanel.lastUsedJCPP.getParent().getParent().getParent().getParent()).setTitle(title);
				closeFrame();
				

			} catch (InvalidSmilesException ise)
			{
				JOptionPane.showMessageDialog(jcpPanel, "Invalid SMILES String.");
			}
		}
	}


	/**
	 *  Description of the Class
	 *
	 *@author     steinbeck
	 *@created    22. April 2005
	 */
	class CancelAction extends AbstractAction
	{
		/**
		 *  Constructor for the CancelAction object
		 */
		CancelAction()
		{
			super("Cancel");
		}


		/**
		 *  Description of the Method
		 *
		 *@param  e  Description of the Parameter
		 */
		public void actionPerformed(ActionEvent e)
		{
			closeFrame();
		}
	}
}

