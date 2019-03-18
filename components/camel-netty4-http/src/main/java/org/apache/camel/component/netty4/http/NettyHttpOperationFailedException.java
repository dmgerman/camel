begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
operator|.
name|http
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
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
name|HttpContent
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
name|CamelException
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
name|netty4
operator|.
name|NettyConverter
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
name|URISupport
import|;
end_import

begin_comment
comment|/**  * Exception when a Netty HTTP operation failed.  */
end_comment

begin_class
DECL|class|NettyHttpOperationFailedException
specifier|public
class|class
name|NettyHttpOperationFailedException
extends|extends
name|CamelException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|uri
specifier|private
specifier|final
name|String
name|uri
decl_stmt|;
DECL|field|redirectLocation
specifier|private
specifier|final
name|String
name|redirectLocation
decl_stmt|;
DECL|field|statusCode
specifier|private
specifier|final
name|int
name|statusCode
decl_stmt|;
DECL|field|statusText
specifier|private
specifier|final
name|String
name|statusText
decl_stmt|;
DECL|field|content
specifier|private
specifier|final
specifier|transient
name|HttpContent
name|content
decl_stmt|;
DECL|field|contentAsString
specifier|private
specifier|final
name|String
name|contentAsString
decl_stmt|;
DECL|method|NettyHttpOperationFailedException (String uri, int statusCode, String statusText, String location, HttpContent content)
specifier|public
name|NettyHttpOperationFailedException
parameter_list|(
name|String
name|uri
parameter_list|,
name|int
name|statusCode
parameter_list|,
name|String
name|statusText
parameter_list|,
name|String
name|location
parameter_list|,
name|HttpContent
name|content
parameter_list|)
block|{
comment|// sanitize uri so we do not show sensitive information such as passwords
name|super
argument_list|(
literal|"Netty HTTP operation failed invoking "
operator|+
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|uri
argument_list|)
operator|+
literal|" with statusCode: "
operator|+
name|statusCode
operator|+
operator|(
name|location
operator|!=
literal|null
condition|?
literal|", redirectLocation: "
operator|+
name|location
else|:
literal|""
operator|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|uri
operator|=
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|this
operator|.
name|statusCode
operator|=
name|statusCode
expr_stmt|;
name|this
operator|.
name|statusText
operator|=
name|statusText
expr_stmt|;
name|this
operator|.
name|redirectLocation
operator|=
name|location
expr_stmt|;
name|this
operator|.
name|content
operator|=
name|content
expr_stmt|;
name|String
name|str
init|=
literal|""
decl_stmt|;
try|try
block|{
name|str
operator|=
name|NettyConverter
operator|.
name|toString
argument_list|(
name|content
operator|.
name|content
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
name|this
operator|.
name|contentAsString
operator|=
name|str
expr_stmt|;
block|}
DECL|method|getUri ()
specifier|public
name|String
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
DECL|method|isRedirectError ()
specifier|public
name|boolean
name|isRedirectError
parameter_list|()
block|{
return|return
name|statusCode
operator|>=
literal|300
operator|&&
name|statusCode
operator|<
literal|400
return|;
block|}
DECL|method|hasRedirectLocation ()
specifier|public
name|boolean
name|hasRedirectLocation
parameter_list|()
block|{
return|return
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|redirectLocation
argument_list|)
return|;
block|}
DECL|method|getRedirectLocation ()
specifier|public
name|String
name|getRedirectLocation
parameter_list|()
block|{
return|return
name|redirectLocation
return|;
block|}
DECL|method|getStatusCode ()
specifier|public
name|int
name|getStatusCode
parameter_list|()
block|{
return|return
name|statusCode
return|;
block|}
DECL|method|getStatusText ()
specifier|public
name|String
name|getStatusText
parameter_list|()
block|{
return|return
name|statusText
return|;
block|}
comment|/**      * Gets the {@link HttpContent}.      *<p/>      * Notice this may be<tt>null</tt> if this exception has been serialized,      * as the {@link HttpContent} instance is marked as transient in this class.      *      * @deprecated use getContentAsString();      */
annotation|@
name|Deprecated
DECL|method|getHttpContent ()
specifier|public
name|HttpContent
name|getHttpContent
parameter_list|()
block|{
return|return
name|content
return|;
block|}
comment|/**      * Gets the HTTP content as a String      *<p/>      * Notice this may be<tt>null</tt> if it was not possible to read the content      */
DECL|method|getContentAsString ()
specifier|public
name|String
name|getContentAsString
parameter_list|()
block|{
return|return
name|contentAsString
return|;
block|}
block|}
end_class

end_unit

