/* $RCSfile$
 * $Author$
 * $Date$
 * $Revision$
 * 
 * Copyright (C) 2001-2005  The Chemistry Development Kit (CDK) project
 * 
 * Contact: cdk-devel@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * All we ask is that proper credit is given for our work, which includes
 * - but is not limited to - adding the above copyright notice to the beginning
 * of your source code files, and to any copyright notice that you may distribute
 * with programs based on this work.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *  */
package org.openscience.cdk;

import java.util.Collection;
import java.util.Hashtable;

/**
 * Subclass of Molecule to store Poymer specific attributes a Polymer has.
 *
 * @cdk.module data
 *
 * @author     Edgar Luttmann <edgar@uni-paderborn.de>
 * @cdk.created    2001-08-06
 * @cdk.keyword    polymer
 *
 */
public class Polymer extends Molecule implements java.io.Serializable, Cloneable
{
	private Hashtable monomers;	// the list of all the contained Monomers. 

	/**
	 *
	 * Contructs a new Polymer to store the Monomers.
	 *
	 */	
	public Polymer() {
		super();
		monomers = new Hashtable();
		Monomer oMonomer = new Monomer();
		oMonomer.setMonomerName("");
		oMonomer.setMonomerType("UNKNOWN");
		monomers.put("", oMonomer);
	}
	
	/**
	 *
	 * Adds the atom oAtom without specifying a Monomer. Therefore the
	 * atom gets added to a Monomer of type UNKNOWN.
	 *
	 * @param oAtom  The atom to add
	 *
	 */
	public void addAtom(Atom oAtom) {
		addAtom(oAtom, getMonomer(""));
		/* notifyChanged() is called by addAtom in
		   AtomContainer */
	}
	
	/**
	 *
	 * Adds the atom oAtom with specifying a Monomer.
	 *
	 * @param oAtom  The atom to add
	 * @param oMonomer  The monomer the atom belongs to
	 *
	 */
	public void addAtom(Atom oAtom, Monomer oMonomer) {
		super.addAtom(oAtom);
		if (oMonomer == null) {
			oMonomer = getMonomer("");
		}
		oMonomer.addAtom(oAtom);
		if (! monomers.contains(oMonomer.getMonomerName())) {
			monomers.put(oMonomer.getMonomerName(), oMonomer);
		}
		/* notifyChanged() is called by addAtom in
		   AtomContainer */
	}
	
	/**
	 *
	 * Return the amount of monomers present in the Poymer.
	 *
	 * @return amout of monomers
	 *
	 */
	public int getMonomerCount() {
		return monomers.size() - 1;
	}
	
	/**
	 *
	 * Retrieve a Monomer object by specifying its name.
	 *
	 * @param cName  The name of the monomer to look for
	 * @return The Monomer object which was asked for
	 *
	 */
	public Monomer getMonomer(String cName) {
		return (Monomer)monomers.get(cName);
	}

  /**
   * Returns a collection of the names of all <code>Monomer</code>s in this
   * polymer.
   *
   * @return a <code>Collection</code> of all the monomer names.
   */
  public Collection getMonomerNames() {
    return monomers.keySet();
  }
}
