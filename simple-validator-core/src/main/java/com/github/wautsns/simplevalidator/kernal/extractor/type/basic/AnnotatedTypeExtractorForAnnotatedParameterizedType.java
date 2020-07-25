package com.github.wautsns.simplevalidator.kernal.extractor.type.basic;

import com.github.wautsns.simplevalidator.util.common.TypeUtils;

import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;

/**
 * Annotated type extractor for {@code AnnotatedParameterizedType}.
 *
 * @author wautsns
 * @since Jul 24, 2020
 */
public abstract class AnnotatedTypeExtractorForAnnotatedParameterizedType extends AnnotatedTypeExtractor {

    /** Type parameter metadata. */
    private final TypeParameterMetadata typeParameterMetadata = initTypeParameterMetadata();

    @Override
    public final AnnotatedType extract(AnnotatedType source) {
        if (!(source instanceof AnnotatedParameterizedType)) { return null; }
        AnnotatedParameterizedType annotatedParameterizedType = (AnnotatedParameterizedType) source;
        Class<?> sourceParameterizedType = TypeUtils.getClass(annotatedParameterizedType.getType());
        Class<?> parameterizedTypeClass = typeParameterMetadata.getParameterizedTypeClass();
        int typeParameterIndex = typeParameterMetadata.getIndex();
        int sourceTypeParameterIndex = TypeUtils.getTypeParameterIndex(
                sourceParameterizedType, parameterizedTypeClass, typeParameterIndex);
        if (sourceTypeParameterIndex < 0) { return null; }
        return annotatedParameterizedType.getAnnotatedActualTypeArguments()[sourceTypeParameterIndex];
    }

    /**
     * Initialize type parameter metadata.
     *
     * @return type parameter metadata
     */
    protected abstract TypeParameterMetadata initTypeParameterMetadata();

    /** Type parameter metadata. */
    protected abstract static class TypeParameterMetadata {

        /**
         * Get parameterized type class.
         *
         * @return parameterized type class
         */
        public abstract Class<?> getParameterizedTypeClass();

        /**
         * Get index of parameterized type in the container type.
         *
         * @return index of parameterized type in the container type
         */
        public abstract int getIndex();

    }

}
