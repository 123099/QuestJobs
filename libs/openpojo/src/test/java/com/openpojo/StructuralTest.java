/*
 * Copyright (c) 2010-2016 Osman Shoukry
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.openpojo;

import java.util.List;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.TestClassMustBeProperlyNamedRule;
import org.junit.Before;
import org.junit.Test;

/**
 * @author oshoukry
 */
public class StructuralTest {
  private Validator validator;

  @Before
  public void setup() {
    validator = ValidatorBuilder.create().with(new TestClassMustBeProperlyNamedRule()).build();
  }

  @Test
  public void allTestsMustEndWithTest() {
    List<PojoClass> pojoClasses = PojoClassFactory.getPojoClassesRecursively("com.openpojo", null);
    validator.validate(pojoClasses);
  }
}