/* $RCSfile$    
 * $Author$    
 * $Date$    
 * $Revision$
 * 
 * Copyright (C) 2003-2005  The Chemistry Development Kit (CDK) project
 * 
 * Contact: cdk-devel@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA. 
 */

package org.openscience.cdk;

import org.openscience.cdk.event.ChemObjectChangeEvent;

/** 
 * A set of AtomContainers.
 *
 * @cdk.module data
 */
public class SetOfAtomContainers extends ChemObject implements java.io.Serializable, Cloneable, ChemObjectListener{

	/**
	 *  Array of AtomContainers.
	 */
	protected AtomContainer[] atomContainers;
	
	/**
	 *  Number of AtomContainers contained by this container.
	 */
	protected int atomContainerCount;

    /**
     * Defines the number of instances of a certain molecule
     * in the set. It is 1 by default.
     */
    protected double[] multipliers;

	/**
	 *  Amount by which the AtomContainers array grows when elements are added and
	 *  the array is not large enough for that. 
	 */
	protected int growArraySize = 5;


	/**
	 *  Constructs an empty SetOfAtomContainers.
	 */
	public SetOfAtomContainers()   
	{
		atomContainerCount = 0;
		atomContainers = new AtomContainer[growArraySize];
        multipliers = new double[growArraySize];
	}

	/**
	 * Adds an atomContainer to this container.
	 *
	 * @param  atomContainer  The atomContainer to be added to this container 
	 */
	public void addAtomContainer(AtomContainer atomContainer) {
		atomContainer.addListener(this);
		addAtomContainer(atomContainer, 1.0);
		/* notifyChanged is called below */
	}

	/**
     * Sets the coefficient of a AtomContainer to a given value.
     *
     * @param  container  The AtomContainer for which the multiplier is set
     * @param  multiplier The new multiplier for the AtomContatiner
     * @return true if multiplier has been set
     * @see    #getMultiplier(AtomContainer)
     */
    public boolean setMultiplier(AtomContainer container, double multiplier) {
        for (int i=0; i<atomContainers.length; i++) {
            if (atomContainers[i] == container) {
                multipliers[i] = multiplier;
		notifyChanged();
                return true;
            }
        }
        return false;
    }
	
	/**
     * Sets the coefficient of a AtomContainer to a given value.
     *
     * @param  position   The position of the AtomContainer for which the multiplier is
     *                    set in [0,..]
     * @param  multiplier The new multiplier for the AtomContatiner at 
     *                    <code>position</code>
     * @see    #getMultiplier(int)
     */
    public void setMultiplier(int position, double multiplier) {
        multipliers[position] = multiplier;
	notifyChanged();
    }
	
	/**
     * Returns an array of double with the stoichiometric coefficients
	 * of the products.
     *
     * @return The multipliers for the AtomContainer's in this set
     * @see    #setMultipliers
     */
    public double[] getMultipliers() {
        double[] returnArray = new double[this.atomContainerCount];
        System.arraycopy(this.multipliers, 0, returnArray, 0, this.atomContainerCount);
        return returnArray;
    }
    
	/**
     * Sets the multipliers of the AtomContainers.
     *
     * @param  newMultipliers The new multipliers for the AtomContainers in this set
     * @return true if multipliers have been set.
     * @see    #getMultipliers
     */
    public boolean setMultipliers(double[] newMultipliers) {
        if (newMultipliers.length == atomContainerCount) {
			System.arraycopy(newMultipliers, 0, multipliers, 0, atomContainerCount);
			notifyChanged();
			return true;
		}
		
        return false;
    }

	/**
	 * Adds an atomContainer to this container with the given
     * multiplier.
	 *
	 * @param  atomContainer  The atomContainer to be added to this container 
	 * @param  multiplier     The multiplier of this atomContainer
	 */
	public void addAtomContainer(AtomContainer atomContainer, double multiplier)
	{
		if (atomContainerCount + 1 >= atomContainers.length) {
			growAtomContainerArray();
		}
		atomContainer.addListener(this);
		atomContainers[atomContainerCount] = atomContainer;
		multipliers[atomContainerCount] = multiplier;
		atomContainerCount++;
		notifyChanged();
	}

	/**
	 *  Adds all atomContainers in the SetOfAtomContainers to this container.
	 *
	 * @param  atomContainerSet  The SetOfAtomContainers 
	 */
	public void add(SetOfAtomContainers atomContainerSet) {
        AtomContainer[] mols = atomContainerSet.getAtomContainers();
        for (int i=0; i< mols.length; i++) {
            addAtomContainer(mols[i]);
        }
	/* notifyChanged() is called by addAtomContainer() */
    }

	/**
	 *  Returns the array of AtomContainers of this container.
	 *
	 * @return    The array of AtomContainers of this container 
	 */
	public AtomContainer[] getAtomContainers() {
        AtomContainer[] result = new AtomContainer[atomContainerCount];
        System.arraycopy(this.atomContainers, 0, result, 0, result.length);
		return result;
	}
	
	
	/**
	 *  
	 * Returns the AtomContainer at position <code>number</code> in the
	 * container.
	 *
	 * @param  number  The position of the AtomContainer to be returned. 
	 * @return         The AtomContainer at position <code>number</code> . 
	 */
	public AtomContainer getAtomContainer(int number)
	{
		return atomContainers[number];
	}
	
	/**
	 *  
	 * Returns the multiplier for the AtomContainer at position <code>number</code> in the
	 * container.
	 *
	 * @param  number  The position of the multiplier of the AtomContainer to be returned. 
	 * @return         The multiplier for the AtomContainer at position <code>number</code> .
     * @see    #setMultiplier(int, double)
	 */
     public double getMultiplier(int number) {
		return multipliers[number];
	}
	
    /**
     * Returns the multiplier of the given AtomContainer.
     *
     * @param  container The AtomContainer for which the multiplier is given
     * @return -1, if the given molecule is not a container in this set
     * @see    #setMultiplier(AtomContainer, double)
     */
    public double getMultiplier(AtomContainer container) {
        for (int i=0; i<atomContainerCount; i++) {
            if (atomContainers[i].equals(container)) {
                return multipliers[i];
            }
        }
        return -1.0;
    }
	
	/**
	 *  Grows the atomContainer array by a given size.
	 *
	 * @see    growArraySize
	 */
	protected void growAtomContainerArray()
	{
		growArraySize = atomContainers.length;
		AtomContainer[] newatomContainers = new AtomContainer[atomContainers.length + growArraySize];
		System.arraycopy(atomContainers, 0, newatomContainers, 0, atomContainers.length);
		atomContainers = newatomContainers;
		double[] newMultipliers = new double[multipliers.length + growArraySize];
		System.arraycopy(multipliers, 0, newMultipliers, 0, multipliers.length);
		multipliers = newMultipliers;
	}
	

	/**
	 * Returns the number of AtomContainers in this Container.
	 *
	 * @return     The number of AtomContainers in this Container
	 */
	public int getAtomContainerCount()
	{
		return this.atomContainerCount;
	}

    /**
     * Returns the String representation of this SetOfAtomContainers.
     *
     * @return The String representation of this SetOfAtomContainers
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SetOfAtomContainers(");
        buffer.append(this.hashCode() + ", ");
        buffer.append("M=" + getAtomContainerCount() + ", ");
        AtomContainer[] atomContainers = getAtomContainers();
        for (int i=0; i<atomContainers.length; i++) {
            buffer.append(atomContainers[i].toString());
            if (i<atomContainers.length-1) {
                buffer.append(", ");
            }
        }
        buffer.append(")");
        return buffer.toString();
    }
    
	/**
	 *  Called by objects to which this object has
	 *  registered as a listener.
	 *
	 *@param  event  A change event pointing to the source of the change
	 */
	public void stateChanged(ChemObjectChangeEvent event)
	{
		notifyChanged(event);
	}    
	
}
