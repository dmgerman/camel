begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|apache
operator|.
name|camel
operator|.
name|Component
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
name|Endpoint
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
name|EndpointResolver
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
name|util
operator|.
name|FactoryFinder
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * An implementation of {@link org.apache.camel.EndpointResolver} that delegates to   * other {@link EndpointResolver} which are selected based on the uri prefix.  *   * The delegate {@link EndpointResolver} are associated with uri prefixes by   * adding a property file with the same uri prefix in the  * META-INF/services/org/apache/camel/EndpointResolver/  * directory on the classpath.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultEndpointResolver
specifier|public
class|class
name|DefaultEndpointResolver
parameter_list|<
name|E
parameter_list|>
implements|implements
name|EndpointResolver
argument_list|<
name|E
argument_list|>
block|{
DECL|field|endpointResolverFactory
specifier|static
specifier|final
specifier|private
name|FactoryFinder
name|endpointResolverFactory
init|=
operator|new
name|FactoryFinder
argument_list|(
literal|"META-INF/services/org/apache/camel/EndpointResolver/"
argument_list|)
decl_stmt|;
DECL|method|resolveEndpoint (CamelContext container, String uri)
specifier|public
name|Endpoint
argument_list|<
name|E
argument_list|>
name|resolveEndpoint
parameter_list|(
name|CamelContext
name|container
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|EndpointResolver
name|resolver
init|=
name|getDelegate
argument_list|(
name|uri
argument_list|)
decl_stmt|;
return|return
name|resolver
operator|.
name|resolveEndpoint
argument_list|(
name|container
argument_list|,
name|uri
argument_list|)
return|;
block|}
DECL|method|resolveComponent (CamelContext container, String uri)
specifier|public
name|Component
name|resolveComponent
parameter_list|(
name|CamelContext
name|container
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|EndpointResolver
name|resolver
init|=
name|getDelegate
argument_list|(
name|uri
argument_list|)
decl_stmt|;
return|return
name|resolver
operator|.
name|resolveComponent
argument_list|(
name|container
argument_list|,
name|uri
argument_list|)
return|;
block|}
DECL|method|getDelegate (String uri)
specifier|private
name|EndpointResolver
name|getDelegate
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|String
name|splitURI
index|[]
init|=
name|ObjectHelper
operator|.
name|splitOnCharacter
argument_list|(
name|uri
argument_list|,
literal|":"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
name|splitURI
index|[
literal|1
index|]
operator|==
literal|null
condition|)
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid URI, it did not contain a scheme: "
operator|+
name|uri
argument_list|)
throw|;
name|EndpointResolver
name|resolver
decl_stmt|;
try|try
block|{
name|resolver
operator|=
operator|(
name|EndpointResolver
operator|)
name|endpointResolverFactory
operator|.
name|newInstance
argument_list|(
name|splitURI
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid URI, no EndpointResolver registered for scheme : "
operator|+
name|splitURI
index|[
literal|0
index|]
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|resolver
return|;
block|}
block|}
end_class

end_unit

