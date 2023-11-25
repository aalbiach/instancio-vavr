/*
 * Copyright (c) 2023 MovementSciences AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.movementsciences.instancio.vavr.collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.all;
import static org.instancio.Select.types;

import org.instancio.Instancio;
import org.instancio.TypeToken;
import org.instancio.internal.util.Constants;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import ch.movementsciences.instancio.vavr.GenVavr;
import io.vavr.collection.List;
import io.vavr.collection.Traversable;

@ExtendWith(InstancioExtension.class)
class VavrListTest {
    private static final int EXPECTED_SIZE = 10;

    private static class Holder {
        public Traversable<String> list;
    }

    @Test
    void createViaTypeToken() {
        final var result = Instancio.create(new TypeToken<List<String>>() {});

        assertThat(result).isInstanceOf(List.class);
        assertThat(result.size()).isBetween(Constants.MIN_SIZE, Constants.MAX_SIZE);
    }

    @Test
    void generatorSpecSize() {
        final List<String> result = Instancio.of(new TypeToken<List<String>>() {})
                .generate(types().of(List.class), GenVavr.list().size(EXPECTED_SIZE))
                .create();

        assertThat(result)
                .isInstanceOf(List.class)
                .hasSize(EXPECTED_SIZE)
                .doesNotContainNull();
    }

    @Test
    void generatorSpecSubtype() {
        final Holder result = Instancio.of(Holder.class)
                .subtype(all(Traversable.class), List.class)
                .create();

        assertThat(result.list).isInstanceOf(List.class);
    }

    @Test
    void subtype() {
        final Holder result = Instancio.of(Holder.class)
                .subtype(all(Traversable.class), List.class)
                .generate(all(Traversable.class), GenVavr.list().size(EXPECTED_SIZE))
                .create();

        assertThat(result.list)
                .isInstanceOf(List.class)
                .hasSize(EXPECTED_SIZE)
                .doesNotContainNull();
    }
}