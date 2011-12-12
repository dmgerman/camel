begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|util
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_comment
comment|/**  * Helper to resolve {@link CamelContext} from the Spring {@link org.springframework.context.ApplicationContext}.  */
end_comment

begin_class
DECL|class|CamelContextResolverHelper
specifier|public
specifier|final
class|class
name|CamelContextResolverHelper
block|{
DECL|method|CamelContextResolverHelper ()
specifier|private
name|CamelContextResolverHelper
parameter_list|()
block|{
comment|// The helper class
block|}
DECL|method|getCamelContextWithId (ApplicationContext context, String contextId)
specifier|public
specifier|static
name|CamelContext
name|getCamelContextWithId
parameter_list|(
name|ApplicationContext
name|context
parameter_list|,
name|String
name|contextId
parameter_list|)
block|{
try|try
block|{
return|return
name|context
operator|.
name|getBean
argument_list|(
name|contextId
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot find the CamelContext with id "
operator|+
name|contextId
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

