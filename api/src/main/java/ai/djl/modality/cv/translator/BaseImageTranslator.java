/*
 * Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package ai.djl.modality.cv.translator;

import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.translate.Pipeline;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

/**
 * Built-in {@code Translator} that provides default image pre-processing.
 *
 * @param <T> the output object type
 */
public abstract class BaseImageTranslator<T> implements Translator<Image, T> {

    private Image.Flag flag;
    private Pipeline pipeline;

    /**
     * Constructs an ImageTranslator with the provided builder.
     *
     * @param builder the data to build with
     */
    public BaseImageTranslator(BaseBuilder<?> builder) {
        flag = builder.flag;
        pipeline = builder.pipeline;
    }

    /** {@inheritDoc} */
    @Override
    public Pipeline getPipeline() {
        return pipeline;
    }

    /**
     * Processes the {@link Image} input and converts it to NDList.
     *
     * @param ctx the toolkit that helps create the input NDArray
     * @param input the {@link Image} input
     * @return a {@link NDList}
     */
    @Override
    public NDList processInput(TranslatorContext ctx, Image input) {
        NDArray array = input.toNDArray(ctx.getNDManager(), flag);
        return pipeline.transform(new NDList(array));
    }

    /**
     * A builder to extend for all classes extending the {@link BaseImageTranslator}.
     *
     * @param <T> the concrete builder type
     */
    @SuppressWarnings("rawtypes")
    public abstract static class BaseBuilder<T extends BaseBuilder> {

        protected Image.Flag flag = Image.Flag.COLOR;
        protected Pipeline pipeline;

        /**
         * Sets the optional {@link ai.djl.modality.cv.Image.Flag} (default is {@link
         * Image.Flag#COLOR}).
         *
         * @param flag the color mode for the images
         * @return this builder
         */
        public T optFlag(Image.Flag flag) {
            this.flag = flag;
            return self();
        }

        /**
         * Sets the {@link Pipeline} to use for pre-processing the image.
         *
         * @param pipeline the pre-processing pipeline
         * @return this builder
         */
        public T setPipeline(Pipeline pipeline) {
            this.pipeline = pipeline;
            return self();
        }

        protected abstract T self();
    }
}
