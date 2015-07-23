begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
package|;
end_package

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
name|http
operator|.
name|common
operator|.
name|DefaultHttpBinding
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
name|http
operator|.
name|common
operator|.
name|HttpCommonEndpoint
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
name|http
operator|.
name|common
operator|.
name|HttpMessage
import|;
end_import

begin_class
DECL|class|JettyRestHttpBinding
specifier|public
class|class
name|JettyRestHttpBinding
extends|extends
name|DefaultHttpBinding
block|{
annotation|@
name|Deprecated
DECL|method|JettyRestHttpBinding ()
specifier|public
name|JettyRestHttpBinding
parameter_list|()
block|{     }
DECL|method|JettyRestHttpBinding (HttpCommonEndpoint ep)
specifier|public
name|JettyRestHttpBinding
parameter_list|(
name|HttpCommonEndpoint
name|ep
parameter_list|)
block|{
name|super
argument_list|(
name|ep
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|populateRequestParameters (HttpServletRequest request, HttpMessage message)
specifier|protected
name|void
name|populateRequestParameters
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpMessage
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|super
operator|.
name|populateRequestParameters
argument_list|(
name|request
argument_list|,
name|message
argument_list|)
expr_stmt|;
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
return|return;
block|}
comment|// in the endpoint the user may have defined rest {} placeholders
comment|// so we need to map those placeholders with data from the incoming request context path
name|JettyHttpEndpoint
name|endpoint
init|=
operator|(
name|JettyHttpEndpoint
operator|)
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getFromEndpoint
argument_list|()
decl_stmt|;
name|String
name|consumerPath
init|=
name|endpoint
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
name|message
operator|.
name|setHeader
argument_list|(
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

