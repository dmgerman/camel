begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|model
operator|.
name|InitializationError
import|;
end_import

begin_comment
comment|/**  * The class {@link CamelSpringBootJUnit4ClassRunner} has been renamed to {@link CamelSpringBootRunner}  * which is a shorter and easier to remember name.  *  * @deprecated use {@link CamelSpringBootRunner}  */
end_comment

begin_class
DECL|class|CamelSpringBootJUnit4ClassRunner
specifier|public
class|class
name|CamelSpringBootJUnit4ClassRunner
extends|extends
name|CamelSpringBootRunner
block|{
DECL|method|CamelSpringBootJUnit4ClassRunner (Class<?> clazz)
specifier|public
name|CamelSpringBootJUnit4ClassRunner
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
throws|throws
name|InitializationError
block|{
name|super
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

