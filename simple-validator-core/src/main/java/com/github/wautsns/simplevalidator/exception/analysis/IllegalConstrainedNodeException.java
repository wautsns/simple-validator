/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.github.wautsns.simplevalidator.exception.analysis;

import com.github.wautsns.simplevalidator.model.node.ConstrainedNode;

import java.lang.annotation.Annotation;

/**
 * Illegal constrained node exception.
 *
 * @author wautsns
 * @since Mar 10, 2020
 */
public class IllegalConstrainedNodeException extends ConstraintAnalysisException {

    private static final long serialVersionUID = -8702277455494595168L;

    /**
     * Construct an illegalConstrainedNodeException.
     *
     * @param location location
     * @param constraint constraint
     */
    public IllegalConstrainedNodeException(ConstrainedNode.Location location, Annotation constraint) {
        this(null, location, constraint);
    }

    /**
     * Construct an illegalConstrainedNodeException.
     *
     * @param cause cause
     * @param location location
     * @param constraint constraint
     */
    public IllegalConstrainedNodeException(Throwable cause, ConstrainedNode.Location location, Annotation constraint) {
        super(cause, "Constraint %s on %s is illegal.", constraint, location);
    }

}
