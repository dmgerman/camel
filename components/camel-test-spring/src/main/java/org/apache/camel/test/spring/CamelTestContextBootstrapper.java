begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextLoader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|support
operator|.
name|DefaultTestContextBootstrapper
import|;
end_import

begin_comment
comment|/**  * To bootstrap Camel for testing with Spring 4.1 onwards.  */
end_comment

begin_class
DECL|class|CamelTestContextBootstrapper
specifier|public
class|class
name|CamelTestContextBootstrapper
extends|extends
name|DefaultTestContextBootstrapper
block|{
annotation|@
name|Override
DECL|method|getDefaultContextLoaderClass (Class<?> testClass)
specifier|protected
name|Class
argument_list|<
name|?
extends|extends
name|ContextLoader
argument_list|>
name|getDefaultContextLoaderClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|testClass
parameter_list|)
block|{
return|return
name|CamelSpringTestContextLoader
operator|.
name|class
return|;
block|}
block|}
end_class

end_unit

