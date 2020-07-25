package com.github.wautsns.simplevalidator.kernal.extractor.type.basic;

import com.github.wautsns.simplevalidator.kernal.criterion.wrapper.CriterionWrapper;

import java.lang.reflect.AnnotatedType;

/**
 * Annotated type extractor.
 *
 * @author wautsns
 * @since Jul 24, 2020
 */
public abstract class AnnotatedTypeExtractor {

    /**
     * Get name of extracted annotated type.
     *
     * @return name of extracted annotated type
     */
    public abstract String getNameOfExtractedType();

    /**
     * Extract annotated type from source.
     *
     * @param source source
     * @return annotated type extracted from source, or {@code null} if cannot extract
     */
    public abstract AnnotatedType extract(AnnotatedType source);

    /**
     * Get criterion wrapper.
     *
     * @return criterion wrapper
     */
    public abstract CriterionWrapper getCriterionWrapper();

}
