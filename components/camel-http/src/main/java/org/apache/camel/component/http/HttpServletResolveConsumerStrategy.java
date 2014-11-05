begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
package|;
end_package

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

begin_comment
comment|/**  * A default implementation of {@link org.apache.camel.component.http.ServletResolveConsumerStrategy}.  */
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
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
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
comment|// Just make sure the we get the right consumer path first
if|if
condition|(
name|consumerPath
operator|.
name|equals
argument_list|(
name|path
argument_list|)
operator|||
operator|(
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|isMatchOnUriPrefix
argument_list|()
operator|&&
name|path
operator|.
name|startsWith
argument_list|(
name|consumerPath
argument_list|)
operator|)
condition|)
block|{
name|answer
operator|=
name|consumers
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

