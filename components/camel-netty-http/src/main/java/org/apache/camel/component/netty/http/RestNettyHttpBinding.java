begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
operator|.
name|http
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpRequest
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
name|Exchange
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
name|RuntimeCamelException
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
name|spi
operator|.
name|HeaderFilterStrategy
import|;
end_import

begin_comment
comment|/**  * A {@link org.apache.camel.component.netty.http.NettyHttpBinding} that supports the Rest DSL.  */
end_comment

begin_class
DECL|class|RestNettyHttpBinding
specifier|public
class|class
name|RestNettyHttpBinding
extends|extends
name|DefaultNettyHttpBinding
block|{
DECL|method|RestNettyHttpBinding ()
specifier|public
name|RestNettyHttpBinding
parameter_list|()
block|{     }
DECL|method|RestNettyHttpBinding (HeaderFilterStrategy headerFilterStrategy)
specifier|public
name|RestNettyHttpBinding
parameter_list|(
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
block|{
name|super
argument_list|(
name|headerFilterStrategy
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|copy ()
specifier|public
name|RestNettyHttpBinding
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|RestNettyHttpBinding
operator|)
name|this
operator|.
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|populateCamelHeaders (HttpRequest request, Map<String, Object> headers, Exchange exchange, NettyHttpConfiguration configuration)
specifier|public
name|void
name|populateCamelHeaders
parameter_list|(
name|HttpRequest
name|request
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|NettyHttpConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
block|{
name|super
operator|.
name|populateCamelHeaders
argument_list|(
name|request
argument_list|,
name|headers
argument_list|,
name|exchange
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
name|String
name|path
init|=
name|request
operator|.
name|uri
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// skip the scheme/host/port etc, as we only want the context-path
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|path
operator|=
name|uri
operator|.
name|getPath
argument_list|()
expr_stmt|;
comment|// in the endpoint the user may have defined rest {} placeholders
comment|// so we need to map those placeholders with data from the incoming request context path
name|String
name|consumerPath
init|=
name|configuration
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|useRestMatching
argument_list|(
name|consumerPath
argument_list|)
condition|)
block|{
comment|// split using single char / is optimized in the jdk
name|String
index|[]
name|paths
init|=
name|path
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
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|consumerPaths
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|paths
operator|.
name|length
operator|<
name|i
condition|)
block|{
break|break;
block|}
name|String
name|p1
init|=
name|consumerPaths
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|p1
operator|.
name|startsWith
argument_list|(
literal|"{"
argument_list|)
operator|&&
name|p1
operator|.
name|endsWith
argument_list|(
literal|"}"
argument_list|)
condition|)
block|{
name|String
name|key
init|=
name|p1
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|p1
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|paths
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|NettyHttpHelper
operator|.
name|appendHeader
argument_list|(
name|headers
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
DECL|method|useRestMatching (String path)
specifier|private
name|boolean
name|useRestMatching
parameter_list|(
name|String
name|path
parameter_list|)
block|{
comment|// only need to do rest matching if using { } placeholders
return|return
name|path
operator|.
name|indexOf
argument_list|(
literal|'{'
argument_list|)
operator|>
operator|-
literal|1
return|;
block|}
block|}
end_class

end_unit

