begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servlet
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
name|Iterator
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
name|component
operator|.
name|http
operator|.
name|HttpConsumer
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
name|component
operator|.
name|http
operator|.
name|HttpServletResolveConsumerStrategy
import|;
end_import

begin_comment
comment|/**  * A {@link org.apache.camel.component.http.HttpServletResolveConsumerStrategy} that supports the Rest DSL.  */
end_comment

begin_class
DECL|class|ServletRestServletResolveConsumerStrategy
specifier|public
class|class
name|ServletRestServletResolveConsumerStrategy
extends|extends
name|HttpServletResolveConsumerStrategy
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
name|HttpConsumer
name|answer
init|=
literal|null
decl_stmt|;
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
name|String
name|method
init|=
name|request
operator|.
name|getMethod
argument_list|()
decl_stmt|;
if|if
condition|(
name|method
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|HttpConsumer
argument_list|>
name|candidates
init|=
operator|new
name|ArrayList
argument_list|<
name|HttpConsumer
argument_list|>
argument_list|()
decl_stmt|;
comment|// first match by http method
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|HttpConsumer
argument_list|>
name|entry
range|:
name|consumers
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|restrict
init|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getHttpMethodRestrict
argument_list|()
decl_stmt|;
if|if
condition|(
name|matchRestMethod
argument_list|(
name|method
argument_list|,
name|restrict
argument_list|)
condition|)
block|{
name|candidates
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// then see if we got a direct match
name|Iterator
argument_list|<
name|HttpConsumer
argument_list|>
name|it
init|=
name|candidates
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|HttpConsumer
name|consumer
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|consumerPath
init|=
name|consumer
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|matchRestPath
argument_list|(
name|path
argument_list|,
name|consumerPath
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|answer
operator|=
name|consumer
expr_stmt|;
break|break;
block|}
block|}
comment|// then match by non wildcard path
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|it
operator|=
name|candidates
operator|.
name|iterator
argument_list|()
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|HttpConsumer
name|consumer
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|consumerPath
init|=
name|consumer
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|matchRestPath
argument_list|(
name|path
argument_list|,
name|consumerPath
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
comment|// there should only be one
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
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
comment|// fallback to default
name|answer
operator|=
name|super
operator|.
name|resolve
argument_list|(
name|request
argument_list|,
name|consumers
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Matches the given request path with the configured consumer path      *      * @param requestPath   the request path      * @param consumerPath  the consumer path which may use { } tokens      * @return<tt>true</tt> if matched,<tt>false</tt> otherwise      */
DECL|method|matchRestPath (String requestPath, String consumerPath, boolean wildcard)
specifier|public
name|boolean
name|matchRestPath
parameter_list|(
name|String
name|requestPath
parameter_list|,
name|String
name|consumerPath
parameter_list|,
name|boolean
name|wildcard
parameter_list|)
block|{
comment|// remove starting/ending slashes
if|if
condition|(
name|requestPath
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|requestPath
operator|=
name|requestPath
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|requestPath
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|requestPath
operator|=
name|requestPath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|requestPath
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// remove starting/ending slashes
if|if
condition|(
name|consumerPath
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|consumerPath
operator|=
name|consumerPath
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|consumerPath
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|consumerPath
operator|=
name|consumerPath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|consumerPath
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// split using single char / is optimized in the jdk
name|String
index|[]
name|requestPaths
init|=
name|requestPath
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
name|String
index|[]
name|consumerPaths
init|=
name|consumerPath
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
comment|// must be same number of path's
if|if
condition|(
name|requestPaths
operator|.
name|length
operator|!=
name|consumerPaths
operator|.
name|length
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|requestPaths
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|p1
init|=
name|requestPaths
index|[
name|i
index|]
decl_stmt|;
name|String
name|p2
init|=
name|consumerPaths
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|wildcard
operator|&&
name|p2
operator|.
name|startsWith
argument_list|(
literal|"{"
argument_list|)
operator|&&
name|p2
operator|.
name|endsWith
argument_list|(
literal|"}"
argument_list|)
condition|)
block|{
comment|// always matches
continue|continue;
block|}
if|if
condition|(
operator|!
name|p1
operator|.
name|equals
argument_list|(
name|p2
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
comment|// assume matching
return|return
literal|true
return|;
block|}
comment|/**      * Matches the given request HTTP method with the configured HTTP method of the consumer      *      * @param method    the request HTTP method      * @param restrict  the consumer configured HTTP restrict method      * @return<tt>true</tt> if matched,<tt>false</tt> otherwise      */
DECL|method|matchRestMethod (String method, String restrict)
specifier|public
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
if|if
condition|(
name|restrict
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// always match OPTIONS as some REST clients uses that prior to calling the service
if|if
condition|(
literal|"OPTIONS"
operator|.
name|equals
argument_list|(
name|method
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|restrict
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
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
name|US
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

