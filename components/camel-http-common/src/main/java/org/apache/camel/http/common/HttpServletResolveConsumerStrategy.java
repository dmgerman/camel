begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.http.common
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|http
operator|.
name|common
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
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
name|support
operator|.
name|RestConsumerContextPathMatcher
import|;
end_import

begin_comment
comment|/**  * A default implementation of {@link org.apache.camel.http.common.ServletResolveConsumerStrategy}.  */
end_comment

begin_class
DECL|class|HttpServletResolveConsumerStrategy
specifier|public
class|class
name|HttpServletResolveConsumerStrategy
implements|implements
name|ServletResolveConsumerStrategy
block|{
annotation|@
name|Override
DECL|method|resolve (HttpServletRequest request, Map<String, HttpConsumer> consumers)
specifier|public
name|HttpConsumer
name|resolve
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|HttpConsumer
argument_list|>
name|consumers
parameter_list|)
block|{
return|return
name|doResolve
argument_list|(
name|request
argument_list|,
name|request
operator|.
name|getMethod
argument_list|()
argument_list|,
name|consumers
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isHttpMethodAllowed (HttpServletRequest request, String method, Map<String, HttpConsumer> consumers)
specifier|public
name|boolean
name|isHttpMethodAllowed
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|String
name|method
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|HttpConsumer
argument_list|>
name|consumers
parameter_list|)
block|{
return|return
name|doResolve
argument_list|(
name|request
argument_list|,
name|method
argument_list|,
name|consumers
argument_list|)
operator|!=
literal|null
return|;
block|}
DECL|method|doResolve (HttpServletRequest request, String method, Map<String, HttpConsumer> consumers)
specifier|protected
name|HttpConsumer
name|doResolve
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|String
name|method
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|HttpConsumer
argument_list|>
name|consumers
parameter_list|)
block|{
name|String
name|path
init|=
name|request
operator|.
name|getPathInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|HttpConsumer
name|answer
init|=
name|consumers
operator|.
name|get
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|HttpConsumer
argument_list|>
name|candidates
init|=
name|resolveCandidates
argument_list|(
name|request
argument_list|,
name|method
argument_list|,
name|consumers
argument_list|)
decl_stmt|;
comment|// extra filter by restrict
name|candidates
operator|=
name|candidates
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|c
lambda|->
name|matchRestMethod
argument_list|(
name|method
argument_list|,
name|c
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getHttpMethodRestrict
argument_list|()
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|candidates
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|answer
operator|=
name|candidates
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|resolveCandidates (HttpServletRequest request, String method, Map<String, HttpConsumer> consumers)
specifier|private
name|List
argument_list|<
name|HttpConsumer
argument_list|>
name|resolveCandidates
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|String
name|method
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|HttpConsumer
argument_list|>
name|consumers
parameter_list|)
block|{
name|String
name|path
init|=
name|request
operator|.
name|getPathInfo
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|HttpConsumer
argument_list|>
name|candidates
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|consumers
operator|.
name|keySet
argument_list|()
control|)
block|{
comment|//We need to look up the consumer path here
name|String
name|consumerPath
init|=
name|consumers
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|HttpConsumer
name|consumer
init|=
name|consumers
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|boolean
name|matchOnUriPrefix
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|isMatchOnUriPrefix
argument_list|()
decl_stmt|;
comment|// Just make sure the we get the right consumer path first
if|if
condition|(
name|RestConsumerContextPathMatcher
operator|.
name|matchPath
argument_list|(
name|path
argument_list|,
name|consumerPath
argument_list|,
name|matchOnUriPrefix
argument_list|)
condition|)
block|{
name|candidates
operator|.
name|add
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|candidates
return|;
block|}
DECL|method|matchRestMethod (String method, String restrict)
specifier|private
specifier|static
name|boolean
name|matchRestMethod
parameter_list|(
name|String
name|method
parameter_list|,
name|String
name|restrict
parameter_list|)
block|{
return|return
name|restrict
operator|==
literal|null
operator|||
name|restrict
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
operator|.
name|contains
argument_list|(
name|method
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

