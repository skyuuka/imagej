/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2009 - 2013 Board of Regents of the University of
 * Wisconsin-Madison, Broad Institute of MIT and Harvard, and Max Planck
 * Institute of Molecular Cell Biology and Genetics.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of any organization.
 * #L%
 */

package imagej.legacy.plugin;

import ij.ImagePlus;
import ij.process.ByteProcessor;
import imagej.command.Command;
import imagej.legacy.LegacyOutputTracker;
import imagej.log.LogService;
import imagej.plugin.Parameter;
import imagej.plugin.Plugin;

/**
 * A command that mixes legacy and modern code without dealing with the legacy
 * layer.
 * 
 * @author Barry DeZonia
 */
@Plugin(menuPath = "Plugins>Sandbox>Mixed World Command")
public class MixedWorldCommand implements Command {

	@Parameter
	private LogService log;

	private boolean ok = true;

	@Override
	public void run() {
		final String title = "junkolaTheFourth";
		final int size = 100;
		final ByteProcessor proc = new ByteProcessor(size, size);
		final ImagePlus imp = new ImagePlus(title, proc);
		if (imp.getWidth() != size) error("size wrong");
		if (imp.getHeight() != size) error("size wrong");
		if (imp.getBitDepth() != 8) error("bit depth wrong");
		if (!imp.getTitle().equals(title)) error("title wrong");
		imp.show();
		if (LegacyOutputTracker.containsOutput(imp)) {
			error("output imp is in output list");
		}
		if (LegacyOutputTracker.containsClosed(imp)) {
			error("output imp is in closed list");
		}
		imp.repaintWindow();
		if (LegacyOutputTracker.containsOutput(imp)) {
			error("output imp is in output list");
		}
		if (LegacyOutputTracker.containsClosed(imp)) {
			error("output imp is in closed list");
		}
		imp.hide();
		if (LegacyOutputTracker.containsOutput(imp)) {
			error("output imp is in output list");
		}
		if (LegacyOutputTracker.containsClosed(imp)) {
			error("output imp is in closed list");
		}
		imp.close();
		if (LegacyOutputTracker.containsOutput(imp)) {
			error("output imp is in output list");
		}
		if (LegacyOutputTracker.containsClosed(imp)) {
			error("output imp is in closed list");
		}
		if (ok) log.info("terminated successfully!");
	}

	private void error(final String msg) {
		log.error(msg);
		ok = false;
	}

}
