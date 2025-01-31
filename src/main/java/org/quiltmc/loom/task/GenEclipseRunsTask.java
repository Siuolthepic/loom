/*
 * This file is part of Quilt Loom, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2016, 2017, 2018 FabricMC, 2021 QuiltMC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.quiltmc.loom.task;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.gradle.api.tasks.TaskAction;
import org.gradle.plugins.ide.eclipse.model.EclipseModel;

import org.quiltmc.loom.LoomGradleExtension;
import org.quiltmc.loom.configuration.ide.RunConfig;
import org.quiltmc.loom.configuration.ide.RunConfigSettings;

public class GenEclipseRunsTask extends AbstractLoomTask {
	@TaskAction
	public void genRuns() throws IOException {
		EclipseModel eclipseModel = getProject().getExtensions().getByType(EclipseModel.class);
		LoomGradleExtension extension = getExtension();

		for (RunConfigSettings settings : extension.getRunConfigs()) {
			if (!settings.isIdeConfigGenerated()) {
				continue;
			}

			String name = settings.getName();

			File configs = new File(getProject().getRootDir(), eclipseModel.getProject().getName() + "_" + name + ".launch");
			RunConfig configInst = RunConfig.runConfig(getProject(), settings);
			String config = configInst.fromDummy("eclipse_run_config_template.xml");

			if (!configs.exists()) {
				FileUtils.writeStringToFile(configs, config, StandardCharsets.UTF_8);
			}

			settings.makeRunDir();
		}
	}
}
