/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.data.keyvalue.core.KeyValueTemplate;
import org.springframework.data.map.MapKeyValueAdapter;
import org.springframework.samples.petclinic.PetClinicApplication.Registrar;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.Person;
import org.springframework.samples.petclinic.vet.Vet;

/**
 * PetClinic Spring Boot Application.
 *
 * @author Dave Syer
 */
@SpringBootApplication
@ImportRuntimeHints(Registrar.class)
public class PetClinicApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetClinicApplication.class, args);
	}

	@Bean
	KeyValueTemplate keyValueTemplate() {
		return new KeyValueTemplate(new MapKeyValueAdapter(new ConcurrentHashMap<>()));
	}

	static class Registrar implements RuntimeHintsRegistrar {

		@Override
		public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
			hints.reflection().registerType(ConcurrentHashMap.class,
					hint -> hint.withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS));
			hints.serialization().registerType(BaseEntity.class).registerType(Person.class).registerType(Vet.class)
					.registerType(Integer.class).registerType(String.class).registerType(Number.class);
		}

	}

}
