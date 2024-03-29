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
name|io
operator|.
name|IOException
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|support
operator|.
name|DefaultMessage
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

begin_class
DECL|class|HttpMessage
specifier|public
class|class
name|HttpMessage
extends|extends
name|DefaultMessage
block|{
DECL|field|request
specifier|private
specifier|final
name|HttpServletRequest
name|request
decl_stmt|;
DECL|field|response
specifier|private
specifier|final
name|HttpServletResponse
name|response
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|HttpCommonEndpoint
name|endpoint
decl_stmt|;
DECL|field|requestRead
specifier|private
name|boolean
name|requestRead
decl_stmt|;
DECL|method|HttpMessage (Exchange exchange, HttpCommonEndpoint endpoint, HttpServletRequest request, HttpServletResponse response)
specifier|public
name|HttpMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|HttpCommonEndpoint
name|endpoint
parameter_list|,
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
block|{
name|super
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|this
operator|.
name|requestRead
operator|=
literal|false
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|request
operator|=
name|request
expr_stmt|;
name|this
operator|.
name|response
operator|=
name|response
expr_stmt|;
comment|// Put the request and response into the message header
name|this
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_SERVLET_REQUEST
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|this
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_SERVLET_RESPONSE
argument_list|,
name|response
argument_list|)
expr_stmt|;
comment|// Check the setting of exchange
name|Boolean
name|flag
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|SKIP_WWW_FORM_URLENCODED
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|flag
operator|!=
literal|null
operator|&&
name|flag
condition|)
block|{
name|this
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|SKIP_WWW_FORM_URLENCODED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
comment|// use binding to read the request allowing end users to use their
comment|// implementation of the binding
name|endpoint
operator|.
name|getHttpBinding
argument_list|()
operator|.
name|readRequest
argument_list|(
name|request
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|HttpMessage (HttpServletRequest request, HttpServletResponse response, Exchange exchange, HttpCommonEndpoint endpoint, boolean requestRead)
specifier|private
name|HttpMessage
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|HttpCommonEndpoint
name|endpoint
parameter_list|,
name|boolean
name|requestRead
parameter_list|)
block|{
name|super
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|this
operator|.
name|request
operator|=
name|request
expr_stmt|;
name|this
operator|.
name|response
operator|=
name|response
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|requestRead
operator|=
name|requestRead
expr_stmt|;
block|}
DECL|method|getRequest ()
specifier|public
name|HttpServletRequest
name|getRequest
parameter_list|()
block|{
return|return
name|request
return|;
block|}
DECL|method|getResponse ()
specifier|public
name|HttpServletResponse
name|getResponse
parameter_list|()
block|{
return|return
name|response
return|;
block|}
annotation|@
name|Override
DECL|method|createBody ()
specifier|protected
name|Object
name|createBody
parameter_list|()
block|{
comment|// HTTP request may be read only once
if|if
condition|(
name|requestRead
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
return|return
name|endpoint
operator|.
name|getHttpBinding
argument_list|()
operator|.
name|parseBody
argument_list|(
name|this
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
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
finally|finally
block|{
name|requestRead
operator|=
literal|true
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|newInstance ()
specifier|public
name|HttpMessage
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|HttpMessage
argument_list|(
name|request
argument_list|,
name|response
argument_list|,
name|getExchange
argument_list|()
argument_list|,
name|endpoint
argument_list|,
name|requestRead
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
comment|// do not use toString on HTTP message
return|return
literal|"HttpMessage@"
operator|+
name|ObjectHelper
operator|.
name|getIdentityHashCode
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
end_class

end_unit

