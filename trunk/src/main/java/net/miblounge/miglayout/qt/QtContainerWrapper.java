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

import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ContainerWrapper;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QLayoutItemInterface;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QWidgetItem;

public final class QtContainerWrapper extends QtComponentWrapper implements ContainerWrapper {
	public QtContainerWrapper(final QWidget c) {
		super(new QWidgetItem(c));
	}

	@Override
	public ComponentWrapper[] getComponents() {
		final QWidget widget = (QWidget) getComponent();
		final Object o = widget.children().get(0);

		if (o instanceof MigLayout) {
			final MigLayout layout = (MigLayout) o;

			final ComponentWrapper[] cws = new ComponentWrapper[layout.count()];

			for (int i = 0; i < layout.count(); i++) {
				final QLayoutItemInterface item = layout.itemAt(i);
				cws[i] = new QtComponentWrapper(item);
			}
			return cws;
		} else {
			return new ComponentWrapper[0];
		}
	}

	@Override
	public int getComponentCount() {
		return ((QWidget) getComponent()).children().size();
	}

	@Override
	public Object getComponent() {
		return ((QLayoutItemInterface) super.getComponent()).widget();
	}

	@Override
	public Object getLayout() {
		return ((QWidget) getComponent()).layout();
	}

	@Override
	public boolean isLeftToRight() {
		return ((QWidget) getComponent()).layoutDirection() == Qt.LayoutDirection.LeftToRight;
	}

	@Override
	public void paintDebugCell(final int x, final int y, final int width, final int height) {
		final QWidget c = (QWidget) getComponent();
		if (c.isVisible() == false) {
			return;
		}

		// TODO: implement
		//		Graphics2D g = (Graphics2D) c.getGraphics();
		//		if (g == null)
		//			return;
		//
		//		g.setStroke(new BasicStroke(1f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10f, new float[] {2f, 3f}, 0));
		//		g.setPaint(DB_CELL_OUTLINE);
		//		g.drawRect(x, y, width - 1, height - 1);
	}

	@Override
	public int getComponetType(final boolean disregardScrollPane) {
		return TYPE_CONTAINER;
	}

	// Removed for 2.3 because the parent.isValid() in MigLayout will catch this instead.
	@Override
	public int getLayoutHashCode() {
		int h = super.getLayoutHashCode();

		if (isLeftToRight()) {
			h += 416343;
		}

		return 0;
	}
}
