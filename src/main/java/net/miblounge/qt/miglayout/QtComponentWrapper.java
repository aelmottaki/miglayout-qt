/*
 * Copyright (c) 2010, Nikolaus Moll. (developer (at) miblounge (dot) net)
 * All rights reserved.
 * 
 * License (BSD):
 * ==============
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of miblounge.net nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 * 
 * @version 1.0
 * 
 * @author Nikolaus Moll
 * Date: 2010-oct-14
 * 
 * based on source code by Mikael Grev, MiG InfoCom AB */
package net.miblounge.qt.miglayout;
/*
 * ==============
 *
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
 * @author Nikolaus Moll
 *         Date: 2010-oct-14
 *         
 * based on source code by Mikael Grev, MiG InfoCom AB
 */

import java.util.IdentityHashMap;

import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ContainerWrapper;
import net.miginfocom.layout.PlatformDefaults;

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QPointF;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLayoutItemInterface;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QListView;
import com.trolltech.qt.gui.QProgressBar;
import com.trolltech.qt.gui.QScrollArea;
import com.trolltech.qt.gui.QScrollBar;
import com.trolltech.qt.gui.QSlider;
import com.trolltech.qt.gui.QTableView;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QTreeView;
import com.trolltech.qt.gui.QWidget;

public class QtComponentWrapper implements ComponentWrapper
{
	/** Cache.
	 */
	private static final IdentityHashMap<QFontMetrics, QPointF> FM_MAP = new IdentityHashMap<QFontMetrics, QPointF>(4);

	private static boolean maxSet = false;

	private static boolean vp = true;

	/** Debug color for component bounds outline.
	 */
	//private static final QColor DB_COMP_OUTLINE = new QColor(0, 0, 200);

	private final QLayoutItemInterface c;
	private final QWidget widget;
	private int compType = TYPE_UNSET;
	//private Boolean bl = null;

	public QtComponentWrapper(final QLayoutItemInterface c)
	{
		this.c = c;
		this.widget = c.widget();
	}

	public final int getBaseline(final int width, final int height)
	{
		return -1;
/*		TODO: implement
		if (BL_METHOD == null)
			return -1;

		try {
			Object[] args = new Object[] {
				new Integer(width < 0 ? c.width() : width),
				new Integer(height < 0 ? c.height() : height)
			};

			return ((Integer) BL_METHOD.invoke(c, args)).intValue();
		} catch (Exception e) {
			return -1;
		} */
	}

	public Object getComponent()
	{
		return c;
	}

	public final float getPixelUnitFactor(final boolean isHor)
	{
		switch (PlatformDefaults.getLogicalPixelBase()) {
			case PlatformDefaults.BASE_FONT_SIZE:
				if (widget == null) {
					return 1f;
				}
				final QFontMetrics fm = widget.fontMetrics();
				QPointF p = FM_MAP.get(fm);
				if (p == null) {
					final QRect bounds = fm.boundingRect('X');
					p = new QPointF(((float) bounds.width()) / 6f, ((float) bounds.height()) / 13.27734375f);
					FM_MAP.put(fm, p);
				}
				return (float) (isHor ? p.x() : p.y());

			case PlatformDefaults.BASE_SCALE_FACTOR:

				final Float s = isHor ? PlatformDefaults.getHorizontalScaleFactor() : PlatformDefaults.getVerticalScaleFactor();
				if (s != null) {
					return s.floatValue();
				}
				return (isHor ? getHorizontalScreenDPI() : getVerticalScreenDPI()) / (float) PlatformDefaults.getDefaultDPI();

			default:
				return 1f;
		}
	}

//	/** Cache.
//	 */
//	private final static IdentityHashMap<FontMetrics, Point.Float> FM_MAP2 = new IdentityHashMap<FontMetrics, Point.Float>(4);
//	private final static Font SUBST_FONT2 = new Font("sansserif", Font.PLAIN, 11);
//
//	public float getDialogUnit(boolean isHor)
//	{
//		Font font = c.getFont();
//		FontMetrics fm = c.getFontMetrics(font != null ? font : SUBST_FONT2);
//		Point.Float dluP = FM_MAP2.get(fm);
//		if (dluP == null) {
//			float w = fm.charWidth('X') / 4f;
//			int ascent = fm.getAscent();
//			float h = (ascent > 14 ? ascent : ascent + (15 - ascent) / 3) / 8f;
//
//			dluP = new Point.Float(w, h);
//			FM_MAP2.put(fm, dluP);
//		}
//		return isHor ? dluP.x : dluP.y;
//	}

	public final int getX()
	{
		if (widget == null) {
			return 0;
		}
		
		return widget.x();
	}

	public final int getY()
	{
		if (widget == null) {
			return 0;
		}
		
		return widget.y();
	}

	public final int getHeight()
	{
		if (widget == null) {
			return 0;
		}
		
		return widget.height();
	}

	public final int getWidth()
	{
		if (widget == null) {
			return 0;
		}
		
		return widget.width();
	}

	public final int getScreenLocationX()
	{
		if (widget == null) {
			return 0;
		}
		final QPoint p = widget.mapToGlobal(new QPoint(0, 0));
		return p.x();
	}

	public final int getScreenLocationY()
	{
		if (widget == null) {
			return 0;
		}
		final QPoint p = widget.mapToGlobal(new QPoint(0, 0));
		return p.y();
	}

	public final int getMinimumHeight(final int sz)
	{
		return c.minimumSize().height();
	}

	public final int getMinimumWidth(final int sz)
	{
		return c.minimumSize().width();
	}

	public final int getPreferredHeight(final int sz)
	{
		if (c.widget() instanceof QScrollArea) {
			final QWidget client = ((QScrollArea)c.widget()).widget();
			return client.sizeHint().height();
		}
		
		if (c.widget().layout() != null) {
			return c.widget().layout().sizeHint().height();
		} else {
			return c.widget().sizeHint().height();
		}
	}

	public final int getPreferredWidth(final int sz)
	{
		if (c.widget() instanceof QScrollArea) {
			final QWidget client = ((QScrollArea)c.widget()).widget();
			return client.sizeHint().width();
		}

		if (c.widget().layout() != null ) {
			return c.widget().layout().sizeHint().width();
		} else {
			return c.widget().sizeHint().width();
		}
	}

	public final int getMaximumHeight(final int sz)
	{
		if (!isMaxSet(c)) {
			return Short.MAX_VALUE;
		}

		return c.maximumSize().height();
	}

	public final int getMaximumWidth(final int sz)
	{
		if (!isMaxSet(c)) {
			return Short.MAX_VALUE;
		}

		return c.maximumSize().width();
	}


	private boolean isMaxSet(final QLayoutItemInterface c)
	{
//		if (IMS_METHOD != null) {
//			try {
//				return ((Boolean) IMS_METHOD.invoke(c, (Object[]) null)).booleanValue();
//			} catch (final Exception e) {
//				IMS_METHOD = null;  // So we do not try every time.
//			}
//		}
		return isMaxSizeSetOn1p4();
	}

	public final ContainerWrapper getParent()
	{
		if (widget == null) {
			return null;
		}
		
		final QObject p = widget.parent();
		if (p instanceof QWidget) {
			return new QtContainerWrapper((QWidget)p);
		}
		
		if (p instanceof MigLayout) {
			final MigLayout layout = (MigLayout)p;
			return new QtContainerWrapper((QWidget)layout.parent());
		}
		
		return null;
	}

	public final int getHorizontalScreenDPI()
	{
		return PlatformDefaults.getDefaultDPI();
	}

	public final int getVerticalScreenDPI()
	{
		return PlatformDefaults.getDefaultDPI();
	}

	public final int getScreenWidth()
	{
		return QApplication.desktop().width();
	}

	public final int getScreenHeight()
	{
		return QApplication.desktop().height();
	}

	public final boolean hasBaseline()
	{
		return false;
//		if (bl == null) {
//			try {
//				if (BL_RES_METHOD == null || BL_RES_METHOD.invoke(c).toString().equals("OTHER")) {
//					bl = Boolean.FALSE;
//				} else {
//					final QSize d = c.minimumSize();
//					bl = new Boolean(getBaseline(d.width(), d.height()) > -1);
//				}
//			} catch (final Throwable ex) {
//				bl = Boolean.FALSE;
//			}
//		}
//		return bl.booleanValue();
	}

	public final String getLinkId()
	{
		return "";
	}

	public final void setBounds(final int x, final int y, final int width, final int height)
	{
		final QRect rect = new QRect(x, y, width, height);
		c.setGeometry(rect);
		c.widget().setGeometry(rect);
	}

	public boolean isVisible()
	{
		if (widget == null) {
			return false;
		}
		return widget.isVisible();
	}

	public final int[] getVisualPadding()
	{
/*		don't know if necessary later...

		if (vp && c instanceof JTabbedPane) {
			if (UIManager.getLookAndFeel().getClass().getName().endsWith("WindowsLookAndFeel"))
				return new int[] {-1, 0, 2, 2};
		}*/

		return null;
	}

	public static boolean isMaxSizeSetOn1p4()
	{
		return maxSet;
	}

	public static void setMaxSizeSetOn1p4(final boolean b)
	{
		maxSet = b;
	}

	public static boolean isVisualPaddingEnabled()
	{
		return vp;
	}

	public static void setVisualPaddingEnabled(final boolean b)
	{
		vp = b;
	}

	public final void paintDebugOutline()
	{
		if ((widget == null) || (widget.isVisible() == false)) { 
			return;
		}
		

		//	TODO: implement
		// 	Graphics2D g = (Graphics2D) c.getGraphics();
		//	if (g == null)
		//		return;
		//
		//	g.setPaint(DB_COMP_OUTLINE);
		//	g.setStroke(new BasicStroke(1f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10f, new float[] {2f, 4f}, 0));
		//	g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);  
	}

	public int getComponetType(final boolean disregardScrollPane)
	{
		if (compType == TYPE_UNSET) {
			compType = checkType(disregardScrollPane);
		}

		return compType;
	}

	public int getLayoutHashCode()
	{
		QSize d = c.maximumSize();
		int h = d.width() + (d.height() << 5);

		if (c.layout() instanceof MigLayout) {
			d = ((MigLayout)c.layout()).sizeHint();
		} else {
			d = c.sizeHint();
		}
		h += (d.width() << 10) + (d.height() << 15);

		d = c.minimumSize();
		h += (d.width() << 20) + (d.height() << 25);

		if ((widget != null) && (widget.isVisible())) {
			h += 1324511;
		}

		final String id = getLinkId();
		if (id != null) {
			h += id.hashCode();
		}
		return h;

		// Since 2.3 will check the isValid instead everything that affects that can be removed from the layout hashcode.

//		String id = getLinkId();
//		return id != null ? id.hashCode() : 1;
	}

	private int checkType(final boolean disregardScrollPane)
	{
		Object item = this.c;

		if (disregardScrollPane) {
			if (item instanceof QScrollArea) {
				item = ((QScrollArea) c).widget();
			}
		}

		if (item instanceof QLineEdit) {
			return TYPE_TEXT_FIELD;
		} else if (item instanceof QLabel) {
			return TYPE_LABEL;
		} else if (item instanceof QCheckBox) {
			return TYPE_CHECK_BOX;
		} else if (item instanceof QAbstractButton) {
			return TYPE_BUTTON;
		} else if (item instanceof QComboBox) {
			return TYPE_LABEL;
		} else if (item instanceof QTextEdit) {
			return TYPE_TEXT_AREA;
//		} else if (item instanceof Panel || item instanceof Canvas) {
//			return TYPE_PANEL;
		} else if (item instanceof QListView) {
			return TYPE_LIST;
		} else if (item instanceof QTableView || item instanceof QTreeView) {
			return TYPE_TABLE;
//		TODO: find matching componentns
//			} else if (item instanceof JSeparator) {
//			return TYPE_SEPARATOR;
//		} else if (item instanceof JSpinner) {
//			return TYPE_SPINNER;
		} else if (item instanceof QProgressBar) {
			return TYPE_PROGRESS_BAR;
		} else if (item instanceof QSlider) {
			return TYPE_SLIDER;
		} else if (item instanceof QScrollArea) {
			return TYPE_SCROLL_PANE;
		} else if (item instanceof QScrollBar) {
			return TYPE_SCROLL_BAR;
//		} else if (item instanceof Container) {    // only AWT components is not containers.
//			return TYPE_CONTAINER;
		}
		return TYPE_UNKNOWN;
	}

	public final int hashCode()
	{
		return getComponent().hashCode();
	}

	public final boolean equals(final Object o)
	{
		if (o instanceof ComponentWrapper == false) {
			return false;
		}

		return getComponent().equals(((ComponentWrapper) o).getComponent());
	}

	
	//CHECKSTYLE:OFF
//	/** Cached method used for getting base line with reflection.
//	 */
//	private static Method BL_METHOD = null;
//	private static Method BL_RES_METHOD = null;
//	static {
//		try {
//			BL_METHOD = QWidget.class.getDeclaredMethod("getBaseline", new Class[] {int.class, int.class});
//			BL_RES_METHOD = QWidget.class.getDeclaredMethod("getBaselineResizeBehavior"); // 3.7.2: Removed Class<?> null since that made the method inaccessible.
//		} catch (Throwable e) { // No such method or security exception
//		}
//	}
//
//	private static Method IMS_METHOD = null;
//	static {
//		try {
//			IMS_METHOD = QWidget.class.getDeclaredMethod("isMaximumSizeSet", (Class[]) null);
//		} catch (Throwable e) { // No such method or security exception
//		}
//	}
	//CHECKSTYLE:ON
}
