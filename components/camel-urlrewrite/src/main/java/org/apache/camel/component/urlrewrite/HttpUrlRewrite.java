begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.urlrewrite
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|urlrewrite
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
name|Producer
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
name|HttpServletUrlRewrite
import|;
end_import

begin_comment
comment|/**  * The camel-http component implementation of the {@link HttpServletUrlRewrite}.  */
end_comment

begin_class
DECL|class|HttpUrlRewrite
specifier|public
class|class
name|HttpUrlRewrite
extends|extends
name|UrlRewriteFilter
implements|implements
name|HttpServletUrlRewrite
block|{
annotation|@
name|Override
DECL|method|rewrite (String url, String relativeUrl, Producer producer, HttpServletRequest request)
specifier|public
name|String
name|rewrite
parameter_list|(
name|String
name|url
parameter_list|,
name|String
name|relativeUrl
parameter_list|,
name|Producer
name|producer
parameter_list|,
name|HttpServletRequest
name|request
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|rewrite
argument_list|(
name|relativeUrl
argument_list|,
name|request
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|rewrite (String url, String relativeUrl, Producer producer)
specifier|public
name|String
name|rewrite
parameter_list|(
name|String
name|url
parameter_list|,
name|String
name|relativeUrl
parameter_list|,
name|Producer
name|producer
parameter_list|)
throws|throws
name|Exception
block|{
comment|// not in use
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

