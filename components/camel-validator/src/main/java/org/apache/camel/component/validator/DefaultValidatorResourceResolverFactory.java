begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.validator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|validator
package|;
end_package

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|ls
operator|.
name|LSResourceResolver
import|;
end_import

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

begin_comment
comment|/**  * Default resource rsolver factory which instantiates the default resource  * rsolver ({@link DefaultLSResourceResolver}).  */
end_comment

begin_class
DECL|class|DefaultValidatorResourceResolverFactory
specifier|public
class|class
name|DefaultValidatorResourceResolverFactory
implements|implements
name|ValidatorResourceResolverFactory
block|{
annotation|@
name|Override
DECL|method|createResourceResolver (CamelContext camelContext, String rootResourceUri)
specifier|public
name|LSResourceResolver
name|createResourceResolver
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|rootResourceUri
parameter_list|)
block|{
return|return
operator|new
name|DefaultLSResourceResolver
argument_list|(
name|camelContext
argument_list|,
name|rootResourceUri
argument_list|)
return|;
block|}
block|}
end_class

end_unit

