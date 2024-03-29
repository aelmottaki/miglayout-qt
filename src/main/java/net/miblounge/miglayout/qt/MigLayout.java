/*
 * Copyright (c) 2010, Nikolaus Moll. (developer (at) miblounge (dot) net)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution.
 * Neither the name of the MiG InfoCom AB nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific
 * prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 * 
 * @version 1.0
 * 
 * @author Nikolaus Moll
 * Date: 2010-nov-09
 * 
 * based on source code by Mikael Grev, MiG InfoCom AB
 */
package net.miblounge.miglayout.qt;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ConstraintParser;
import net.miginfocom.layout.ContainerWrapper;
import net.miginfocom.layout.Grid;
import net.miginfocom.layout.LC;
import net.miginfocom.layout.LayoutCallback;
import net.miginfocom.layout.LayoutUtil;
import net.miginfocom.layout.PlatformDefaults;

import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QLayoutItemInterface;
import com.trolltech.qt.gui.QWidget;

public final class MigLayout extends QLayout implements Externalizable {
	private static boolean layoutVeto = false;

	private static int skips = 0;

	private Object nextConstraints = null;

	private final List<QLayoutItemInterface> items = new ArrayList<QLayoutItemInterface>();
	private final Map<QWidget, QLayoutItemInterface> itemMap = new HashMap<QWidget, QLayoutItemInterface>();

	// ******** Instance part ********

	/**
	 * The component to string constraints mappings.
	 */
	private final Map<QLayoutItemInterface, Object> scrConstrMap = new IdentityHashMap<QLayoutItemInterface, Object>(8);

	/**
	 */
	private Object layoutConstraints = ""; // Should never be null!
	private Object colConstraints = ""; // Should never be null!
	private Object rowConstraints = ""; // Should never be null!

	// ******** Transient part ********

	private transient ContainerWrapper cacheParentW = null;

	private final transient Map<ComponentWrapper, CC> ccMap = new HashMap<ComponentWrapper, CC>(8);

	private transient LC lc = null;
	private transient AC colSpecs = null;
	private transient AC rowSpecs = null;
	private transient Grid grid = null;

	private transient int lastModCount = PlatformDefaults.getModCount();
	private transient int lastHash = -1;

	private transient ArrayList<LayoutCallback> callbackList = null;

	private final QSize[] cachedSizes = new QSize[3];

	private final long lastGrid = 0;

	private Object lastRect;

	/**
	 * Constructor with no constraints.
	 */
	public MigLayout() {
		this("", "", "");
	}

	/**
	 * Constructor.
	 * 
	 * @param layoutConstraints The constraints that concern the whole layout. <code>null</code> will be treated as "".
	 */
	public MigLayout(final String layoutConstraints) {
		this(layoutConstraints, "", "");
	}

	/**
	 * Constructor.
	 * 
	 * @param layoutConstraints The constraints that concern the whole layout. <code>null</code> will be treated as "".
	 * @param colConstraints The constraints for the columns in the grid. <code>null</code> will be treated as "".
	 */
	public MigLayout(final String layoutConstraints, final String colConstraints) {
		this(layoutConstraints, colConstraints, "");
	}

	/**
	 * Constructor.
	 * 
	 * @param layoutConstraints The constraints that concern the whole layout. <code>null</code> will be treated as "".
	 * @param colConstraints The constraints for the columns in the grid. <code>null</code> will be treated as "".
	 * @param rowConstraints The constraints for the rows in the grid. <code>null</code> will be treated as "".
	 */
	public MigLayout(final String layoutConstraints, final String colConstraints, final String rowConstraints) {
		setLayoutConstraints(layoutConstraints);
		setColumnConstraints(colConstraints);
		setRowConstraints(rowConstraints);
	}

	/**
	 * Constructor.
	 * 
	 * @param layoutConstraints The constraints that concern the whole layout. <code>null</code> will be treated as an empty
	 *            cosntraint.
	 */
	public MigLayout(final LC layoutConstraints) {
		this(layoutConstraints, null, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param layoutConstraints The constraints that concern the whole layout. <code>null</code> will be treated as an empty
	 *            cosntraint.
	 * @param colConstraints The constraints for the columns in the grid. <code>null</code> will be treated as an empty
	 *            constraint.
	 */
	public MigLayout(final LC layoutConstraints, final AC colConstraints) {
		this(layoutConstraints, colConstraints, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param layoutConstraints The constraints that concern the whole layout. <code>null</code> will be treated as an empty
	 *            cosntraint.
	 * @param colConstraints The constraints for the columns in the grid. <code>null</code> will be treated as an empty
	 *            constraint.
	 * @param rowConstraints The constraints for the rows in the grid. <code>null</code> will be treated as an empty constraint.
	 */
	public MigLayout(final LC layoutConstraints, final AC colConstraints, final AC rowConstraints) {
		setLayoutConstraints(layoutConstraints);
		setColumnConstraints(colConstraints);
		setRowConstraints(rowConstraints);
	}

	/**
	 * Returns layout constraints eighter as a <code>String</code> or {@link net.miginfocom.layout.LC} depending what was sent in
	 * to the constructor or set with {@link #setLayoutConstraints(Object)}.
	 * 
	 * @return The layout constraints eighter as a <code>String</code> or {@link net.miginfocom.layout.LC} depending what was sent
	 *         in
	 *         to the constructor or set with {@link #setLayoutConstraints(Object)}. Never <code>null</code>.
	 */
	public Object getLayoutConstraints() {
		return layoutConstraints;
	}

	/**
	 * Sets the layout constraints for the layout manager instance as a String.
	 * <p>
	 * See the class JavaDocs for information on how this string is formatted.
	 * 
	 * @param s The layout constraints as a String representation. <code>null</code> is converted to <code>""</code> for storage.
	 * @throws RuntimeException if the constaint was not valid.
	 */
	public void setLayoutConstraints(Object s) {
		if (s == null || s instanceof String) {
			s = ConstraintParser.prepare((String) s);
			lc = ConstraintParser.parseLayoutConstraint((String) s);
		} else if (s instanceof LC) {
			lc = (LC) s;
		} else {
			throw new IllegalArgumentException("Illegal constraint type: " + s.getClass().toString());
		}
		layoutConstraints = s;
		grid = null;
	}

	/**
	 * Returns the column layout constraints either as a <code>String</code> or {@link net.miginfocom.layout.AC}.
	 * 
	 * @return The column constraints eighter as a <code>String</code> or {@link net.miginfocom.layout.LC} depending what was sent
	 *         in
	 *         to the constructor or set with {@link #setLayoutConstraints(Object)}. Never <code>null</code>.
	 */
	public Object getColumnConstraints() {
		return colConstraints;
	}

	/**
	 * Sets the column layout constraints for the layout manager instance as a String.
	 * <p>
	 * See the class JavaDocs for information on how this string is formatted.
	 * 
	 * @param constr The column layout constraints as a String representation. <code>null</code> is converted to <code>""</code>
	 *            for storage.
	 * @throws RuntimeException if the constaint was not valid.
	 */
	public void setColumnConstraints(Object constr) {
		if (constr == null || constr instanceof String) {
			constr = ConstraintParser.prepare((String) constr);
			colSpecs = ConstraintParser.parseColumnConstraints((String) constr);
		} else if (constr instanceof AC) {
			colSpecs = (AC) constr;
		} else {
			throw new IllegalArgumentException("Illegal constraint type: " + constr.getClass().toString());
		}
		colConstraints = constr;
		grid = null;
	}

	/**
	 * Returns the row layout constraints as a String representation. This string is the exact string as set with
	 * {@link #setRowConstraints(Object)} or sent into the constructor.
	 * <p>
	 * See the class JavaDocs for information on how this string is formatted.
	 * 
	 * @return The row layout constraints as a String representation. Never <code>null</code>.
	 */
	public Object getRowConstraints() {
		return rowConstraints;
	}

	/**
	 * Sets the row layout constraints for the layout manager instance as a String.
	 * <p>
	 * See the class JavaDocs for information on how this string is formatted.
	 * 
	 * @param constr The row layout constraints as a String representation. <code>null</code> is converted to <code>""</code> for
	 *            storage.
	 * @throws RuntimeException if the constaint was not valid.
	 */
	public void setRowConstraints(Object constr) {
		if (constr == null || constr instanceof String) {
			constr = ConstraintParser.prepare((String) constr);
			rowSpecs = ConstraintParser.parseRowConstraints((String) constr);
		} else if (constr instanceof AC) {
			rowSpecs = (AC) constr;
		} else {
			throw new IllegalArgumentException("Illegal constraint type: " + constr.getClass().toString());
		}

		rowConstraints = constr;
		grid = null;
	}

	/**
	 * Returns a shallow copy of the constraints map.
	 * 
	 * @return A shallow copy of the constraints map. Never <code>null</code>.
	 */
	public Map<QLayoutItemInterface, Object> getConstraintMap() {
		return new IdentityHashMap<QLayoutItemInterface, Object>(scrConstrMap);
	}

	/**
	 * Sets the constraints map.
	 * 
	 * @param map The map. Will be copied.
	 */
	public void setConstraintMap(final Map<QLayoutItemInterface, Object> map) {
		scrConstrMap.clear();
		ccMap.clear();
		for (final Map.Entry<QLayoutItemInterface, Object> e : map.entrySet()) {
			setComponentConstraintsImpl(e.getKey(), e.getValue(), true);
		}
	}

	/**
	 * Sets the component constraint for the component that already must be handleded by this layout manager.
	 * <p>
	 * See the class JavaDocs for information on how this string is formatted.
	 * 
	 * @param constr The component constraints as a String or {@link net.miginfocom.layout.CC}. <code>null</code> is ok.
	 * @param comp The component to set the constraints for.
	 * @param noCheck Doesn't check if control already is managed.
	 * @throws RuntimeException if the constaint was not valid.
	 * @throws IllegalArgumentException If the component is not handling the component.
	 */
	private void setComponentConstraintsImpl(final QLayoutItemInterface comp, final Object constr, final boolean noCheck) {
		if (noCheck == false && scrConstrMap.containsKey(comp) == false) {
			throw new IllegalArgumentException("Component must already be added to parent!");
		}

		final ComponentWrapper cw = new QtComponentWrapper(comp.widget());

		if (constr == null || constr instanceof String) {
			final String cStr = ConstraintParser.prepare((String) constr);

			scrConstrMap.put(comp, constr);
			ccMap.put(cw, ConstraintParser.parseComponentConstraint(cStr));

		} else if (constr instanceof CC) {

			scrConstrMap.put(comp, constr);
			ccMap.put(cw, (CC) constr);

		} else {
			throw new IllegalArgumentException("Constraint must be String or ComponentConstraint: "
				+ constr.getClass().toString());
		}
		grid = null;
	}

	/**
	 * Returns if this layout manager is currently managing this component.
	 * 
	 * @param c The component to check. If <code>null</code> then <code>false</code> will be returned.
	 * @return If this layout manager is currently managing this component.
	 */
	public boolean isManagingComponent(final QLayoutItemInterface c) {
		return scrConstrMap.containsKey(c);
	}

	/**
	 * Adds the callback function that will be called at different stages of the layout cylce.
	 * 
	 * @param callback The callback. Not <code>null</code>.
	 */
	public void addLayoutCallback(final LayoutCallback callback) {
		if (callback == null) {
			throw new NullPointerException();
		}

		if (callbackList == null) {
			callbackList = new ArrayList<LayoutCallback>(1);
		}

		callbackList.add(callback);
	}

	/**
	 * Removes the callback if it exists.
	 * 
	 * @param callback The callback. May be <code>null</code>.
	 */
	public void removeLayoutCallback(final LayoutCallback callback) {
		if (callbackList != null) {
			callbackList.remove(callback);
		}
	}

	@Override
	public void setGeometry(final QRect rect) {
		if (layoutVeto) {
			return;
		}

		if (!rect.equals(lastRect)) {
			super.setGeometry(rect);
			grid = null;
		}
		lastRect = rect;

		checkCache(parentWidget());

		final int[] b = new int[] {
				rect.x() + parentWidget().contentsMargins().left(), rect.y() + parentWidget().contentsMargins().top(),
				rect.width(), rect.height()};

		grid.layout(b, lc.getAlignX(), lc.getAlignY(), false, true);
	}

	private boolean checkCache(final QWidget parent) {
		if (parent == null) {
			return true;
		}

		//		if (grid == null) {
		//			final ContainerWrapper par = checkParent(parent);
		//			grid = new Grid(par, lc, rowSpecs, colSpecs, ccMap, callbackList);
		//			return false;
		//		}

		//		final long currentTimeInMs = System.currentTimeMillis();
		//		if (Math.abs(currentTimeInMs - lastGrid) >= 100000) {
		int hash = 17;
		for (final Iterator<ComponentWrapper> it = ccMap.keySet().iterator(); it.hasNext();) {
			hash = 17 * hash + it.next().getLayoutHashCode();
		}
		if (hash != lastHash) {
			grid = null;
			lastHash = hash;
		}
		//			lastGrid = currentTimeInMs;
		//		} else {
		//			skip();
		//		}

		if (grid == null) {
			final ContainerWrapper par = checkParent(parent);
			grid = new Grid(par, lc, rowSpecs, colSpecs, ccMap, callbackList);
			return false;
		}

		return true;
	}

	private static void skip() {
		skips++;
		if (skips % 1000 == 0) {
			System.out.println("Skips " + skips);
		}

	}

	private ContainerWrapper checkParent(final QWidget parent) {
		if (parent == null) {
			return null;
		}

		if (cacheParentW == null || cacheParentW.getComponent() != parent) {
			cacheParentW = new QtContainerWrapper(parent);
		}

		return cacheParentW;
	}

	// ************************************************
	// Persistence Delegate and Serializable combined.
	// ************************************************

	private Object readResolve() throws ObjectStreamException {
		return LayoutUtil.getSerializedObject(this);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(in));
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		if (getClass() == MigLayout.class) {
			LayoutUtil.writeAsXML(out, this);
		}
	}

	/*
	 * ************** QLAYOUT *********************
	 */

	public void addWidget(final QWidget widget, final Object constraints) {
		// TODO mutal exclusion
		nextConstraints = constraints;
		addWidget(widget);
	}

	@Override
	public void addItem(final QLayoutItemInterface item) {
		itemMap.put(item.widget(), item);

		setComponentConstraintsImpl(item, nextConstraints, true);
		nextConstraints = null;
		items.add(item);
		grid = null;
	}

	@Override
	public int count() {
		return items.size();
	}

	@Override
	public QLayoutItemInterface itemAt(final int index) {
		if (index < 0 || index >= items.size()) {
			return null;
		}

		return items.get(index);
	}

	private void calcSizes() {
		// add insets
		final int marginWidth = parentWidget().contentsMargins().left() + parentWidget().contentsMargins().right();
		final int marginHeight = parentWidget().contentsMargins().top() + parentWidget().contentsMargins().bottom();

		for (int i = 0; i < 3; i++) {
			final int w = LayoutUtil.getSizeSafe(grid != null ? grid.getWidth() : null, i) + marginWidth;
			final int h = LayoutUtil.getSizeSafe(grid != null ? grid.getHeight() : null, i) + marginHeight;

			cachedSizes[i] = new QSize(w, h);
		}
	}

	private QSize calcSize(final int type) {
		if (!checkCache(parentWidget())) {
			calcSizes();
		}

		return cachedSizes[type];
	}

	@Override
	public QSize minimumSize() {
		return calcSize(LayoutUtil.MIN);
	}

	@Override
	public QSize maximumSize() {
		return calcSize(LayoutUtil.MAX);
	}

	@Override
	public QSize sizeHint() {
		return calcSize(LayoutUtil.PREF);
	}

	@Override
	public QLayoutItemInterface takeAt(final int index) {
		if (index < 0 || index >= items.size()) {
			return null;
		}

		final QLayoutItemInterface result = items.get(index);
		items.remove(index);

		// remove from MigLayout as well
		scrConstrMap.remove(result);
		ccMap.remove(result);

		grid = null;

		return result;
	}

	public QWidget takeWidget(final QWidget widget) {
		final QLayoutItemInterface item = itemMap.get(widget);
		if (item == null) {
			return null;
		}

		itemMap.remove(widget);

		items.remove(item);
		scrConstrMap.remove(item);
		ccMap.remove(item);

		return widget;
	}

	public void setComponentConstraints(final QWidget uiReference, final Object constraints) {
		for (final QLayoutItemInterface item : scrConstrMap.keySet()) {
			if (item.widget() == uiReference) {
				setComponentConstraintsImpl(item, constraints, true);
				break;
			}
		}
	}

	public Object getComponentConstraints(final QWidget uiReference) {
		for (final QLayoutItemInterface item : scrConstrMap.keySet()) {
			if (item.widget() == uiReference) {
				return scrConstrMap.get(item);
			}
		}

		return null;
	}

	public static void setLayoutVeto(final boolean veto) {
		layoutVeto = veto;
	}
}
