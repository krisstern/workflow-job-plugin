/*
 * The MIT License
 *
 * Copyright (c) 2017, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.jenkinsci.plugins.workflow.job.properties;

import hudson.Extension;
import hudson.ExtensionList;
import jenkins.model.Jenkins;
import jenkins.model.OptionalJobProperty;
import org.jenkinsci.Symbol;
import org.jenkinsci.plugins.workflow.flow.FlowDurabilityHint;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.List;

/**
 * Hint about the desired {@link FlowDurabilityHint}.
 * Note that setting {@link DisableResumeJobProperty} overrides this to the minimum durability level.
 * @author Sam Van Oort
 */
public class DurabilityHintJobProperty extends OptionalJobProperty<WorkflowJob> {
    private final FlowDurabilityHint hint;

    public FlowDurabilityHint getHint() {
        return hint;
    }

    @DataBoundConstructor
    public DurabilityHintJobProperty(@Nonnull String hint) {
        this.hint = DescriptorImpl.getDurabilityHintForName(hint);
    }

    public DurabilityHintJobProperty(@Nonnull FlowDurabilityHint hint) {
        this.hint = hint;
    }

    public String getHintName() {
        return hint.getName();
    }

    @Extension
    @Symbol("durabilityHint")
    public static class DescriptorImpl extends OptionalJobProperty.OptionalJobPropertyDescriptor {

        public List<FlowDurabilityHint> getDurabilityHintValues() {
            return FlowDurabilityHint.allSorted();
        }

        @CheckForNull
        public static FlowDurabilityHint getDurabilityHintForName(String hintName) {
            for (FlowDurabilityHint hint : FlowDurabilityHint.all()) {
                if (hint.getName().equals(hintName)) {
                    return hint;
                }
            }
            System.out.println("No hint for name: "+hintName);
            return null;
        }

        public static String getDefaultHintName() {
            return Jenkins.getInstance().getExtensionList(FlowDurabilityHint.FullyDurable.class).get(0).getName();
        }

        @Override public String getDisplayName() {
            return "How hard should we try to render the pipeline durable?";
        }

    }
}
