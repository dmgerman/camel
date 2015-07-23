begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty9
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty9
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
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataSource
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
name|Part
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
name|HttpMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|util
operator|.
name|MultiPartInputStreamParser
import|;
end_import

begin_class
DECL|class|AttachmentHttpBinding
specifier|final
class|class
name|AttachmentHttpBinding
extends|extends
name|DefaultHttpBinding
block|{
DECL|method|AttachmentHttpBinding ()
name|AttachmentHttpBinding
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|populateAttachments (HttpServletRequest request, HttpMessage message)
specifier|protected
name|void
name|populateAttachments
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpMessage
name|message
parameter_list|)
block|{
name|Object
name|object
init|=
name|request
operator|.
name|getAttribute
argument_list|(
literal|"org.eclipse.jetty.servlet.MultiPartFile.multiPartInputStream"
argument_list|)
decl_stmt|;
if|if
condition|(
name|object
operator|instanceof
name|MultiPartInputStreamParser
condition|)
block|{
name|MultiPartInputStreamParser
name|parser
init|=
operator|(
name|MultiPartInputStreamParser
operator|)
name|object
decl_stmt|;
name|Collection
argument_list|<
name|Part
argument_list|>
name|parts
decl_stmt|;
try|try
block|{
name|parts
operator|=
name|parser
operator|.
name|getParts
argument_list|()
expr_stmt|;
for|for
control|(
name|Part
name|part
range|:
name|parts
control|)
block|{
name|String
name|contentType
init|=
name|part
operator|.
name|getContentType
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|contentType
operator|.
name|startsWith
argument_list|(
literal|"application/octet-stream"
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|DataSource
name|ds
init|=
operator|new
name|PartDataSource
argument_list|(
name|part
argument_list|)
decl_stmt|;
name|message
operator|.
name|addAttachment
argument_list|(
name|part
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|DataHandler
argument_list|(
name|ds
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Cannot populate attachments"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
DECL|class|PartDataSource
specifier|final
class|class
name|PartDataSource
implements|implements
name|DataSource
block|{
DECL|field|part
specifier|private
specifier|final
name|Part
name|part
decl_stmt|;
DECL|method|PartDataSource (Part part)
name|PartDataSource
parameter_list|(
name|Part
name|part
parameter_list|)
block|{
name|this
operator|.
name|part
operator|=
name|part
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getOutputStream ()
specifier|public
name|OutputStream
name|getOutputStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|part
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getInputStream ()
specifier|public
name|InputStream
name|getInputStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|part
operator|.
name|getInputStream
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getContentType ()
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
name|part
operator|.
name|getContentType
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

