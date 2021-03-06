/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2015, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.wildfly.extension.clustering.singleton.deployment;

import org.jboss.as.server.deployment.AttachmentKey;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitPhaseBuilder;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.wildfly.clustering.singleton.SingletonPolicy;

/**
 * DUP that attaches the singleton DeploymentUnitPhaseBuilder if a deployment policy is attached.
 * @author Paul Ferraro
 */
public class SingletonDeploymentProcessor implements DeploymentUnitProcessor {

    public static final AttachmentKey<SingletonPolicy> POLICY_KEY = AttachmentKey.create(SingletonPolicy.class);

    @Override
    public void deploy(DeploymentPhaseContext context) throws DeploymentUnitProcessingException {
        SingletonPolicy policy = context.getAttachment(POLICY_KEY);
        if (policy != null) {
            DeploymentUnit parent = context.getDeploymentUnit().getParent();
            DeploymentUnitPhaseBuilder builder = (parent == null) ? new SingletonDeploymentUnitPhaseBuilder(policy) : new SingletonSubDeploymentUnitPhaseBuilder(parent, context.getPhase().next());
            context.putAttachment(Attachments.DEPLOYMENT_UNIT_PHASE_BUILDER, builder);
        }
    }

    @Override
    public void undeploy(DeploymentUnit unit) {
    }
}
