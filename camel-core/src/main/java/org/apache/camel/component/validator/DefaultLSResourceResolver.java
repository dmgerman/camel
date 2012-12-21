begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.validator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|validator
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
name|Reader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|ls
operator|.
name|LSInput
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|ls
operator|.
name|LSResourceResolver
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
name|util
operator|.
name|FileUtil
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
name|ResourceHelper
import|;
end_import

begin_comment
comment|/**  * Default {@link LSResourceResolver} which can included schema resources.  */
end_comment

begin_class
DECL|class|DefaultLSResourceResolver
specifier|public
class|class
name|DefaultLSResourceResolver
implements|implements
name|LSResourceResolver
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|resourceUri
specifier|private
specifier|final
name|String
name|resourceUri
decl_stmt|;
DECL|field|resourcePath
specifier|private
specifier|final
name|String
name|resourcePath
decl_stmt|;
DECL|method|DefaultLSResourceResolver (CamelContext camelContext, String resourceUri)
specifier|public
name|DefaultLSResourceResolver
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|resourceUri
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|resourceUri
operator|=
name|resourceUri
expr_stmt|;
name|this
operator|.
name|resourcePath
operator|=
name|FileUtil
operator|.
name|onlyPath
argument_list|(
name|resourceUri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|resolveResource (String type, String namespaceURI, String publicId, String systemId, String baseURI)
specifier|public
name|LSInput
name|resolveResource
parameter_list|(
name|String
name|type
parameter_list|,
name|String
name|namespaceURI
parameter_list|,
name|String
name|publicId
parameter_list|,
name|String
name|systemId
parameter_list|,
name|String
name|baseURI
parameter_list|)
block|{
comment|// systemId should be mandatory
if|if
condition|(
name|systemId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Resource: %s refers an invalid resource without SystemId."
operator|+
literal|" Invalid resource has type: %s, namespaceURI: %s, publicId: %s, systemId: %s, baseURI: %s"
argument_list|,
name|resourceUri
argument_list|,
name|type
argument_list|,
name|namespaceURI
argument_list|,
name|publicId
argument_list|,
name|systemId
argument_list|,
name|baseURI
argument_list|)
argument_list|)
throw|;
block|}
return|return
operator|new
name|DefaultLSInput
argument_list|(
name|publicId
argument_list|,
name|systemId
argument_list|,
name|baseURI
argument_list|)
return|;
block|}
DECL|class|DefaultLSInput
specifier|private
specifier|final
class|class
name|DefaultLSInput
implements|implements
name|LSInput
block|{
DECL|field|publicId
specifier|private
specifier|final
name|String
name|publicId
decl_stmt|;
DECL|field|systemId
specifier|private
specifier|final
name|String
name|systemId
decl_stmt|;
DECL|field|baseURI
specifier|private
specifier|final
name|String
name|baseURI
decl_stmt|;
DECL|field|uri
specifier|private
specifier|final
name|String
name|uri
decl_stmt|;
DECL|method|DefaultLSInput (String publicId, String systemId, String baseURI)
specifier|private
name|DefaultLSInput
parameter_list|(
name|String
name|publicId
parameter_list|,
name|String
name|systemId
parameter_list|,
name|String
name|baseURI
parameter_list|)
block|{
name|this
operator|.
name|publicId
operator|=
name|publicId
expr_stmt|;
name|this
operator|.
name|systemId
operator|=
name|systemId
expr_stmt|;
name|this
operator|.
name|baseURI
operator|=
name|baseURI
expr_stmt|;
name|this
operator|.
name|uri
operator|=
name|getInputUri
argument_list|()
expr_stmt|;
block|}
DECL|method|getInputUri ()
specifier|private
name|String
name|getInputUri
parameter_list|()
block|{
comment|// find the xsd with relative path
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|baseURI
argument_list|)
condition|)
block|{
name|String
name|inputUri
init|=
name|getUri
argument_list|(
name|getRelativePath
argument_list|(
name|baseURI
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|camelContext
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|inputUri
argument_list|)
expr_stmt|;
return|return
name|inputUri
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore the exception
block|}
block|}
comment|// don't use the relative path
return|return
name|getUri
argument_list|(
literal|""
argument_list|)
return|;
block|}
DECL|method|getRelativePath (String base)
specifier|private
name|String
name|getRelativePath
parameter_list|(
name|String
name|base
parameter_list|)
block|{
name|String
name|userDir
init|=
literal|""
decl_stmt|;
name|String
name|answer
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|base
argument_list|)
condition|)
block|{
try|try
block|{
name|userDir
operator|=
name|FileUtil
operator|.
name|getUserDir
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// do nothing here
block|}
comment|// get the relative path from the userdir
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|base
argument_list|)
operator|&&
name|base
operator|.
name|startsWith
argument_list|(
literal|"file"
argument_list|)
operator|&&
name|base
operator|.
name|startsWith
argument_list|(
name|userDir
argument_list|)
condition|)
block|{
name|answer
operator|=
name|FileUtil
operator|.
name|onlyPath
argument_list|(
name|base
operator|.
name|substring
argument_list|(
name|userDir
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
operator|+
literal|"/"
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|getUri (String relativePath)
specifier|private
name|String
name|getUri
parameter_list|(
name|String
name|relativePath
parameter_list|)
block|{
if|if
condition|(
name|resourcePath
operator|!=
literal|null
condition|)
block|{
return|return
name|FileUtil
operator|.
name|onlyPath
argument_list|(
name|resourceUri
argument_list|)
operator|+
literal|"/"
operator|+
name|relativePath
operator|+
name|systemId
return|;
block|}
else|else
block|{
return|return
name|relativePath
operator|+
name|systemId
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getCharacterStream ()
specifier|public
name|Reader
name|getCharacterStream
parameter_list|()
block|{
name|InputStream
name|is
init|=
name|getByteStream
argument_list|()
decl_stmt|;
return|return
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Reader
operator|.
name|class
argument_list|,
name|is
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setCharacterStream (Reader reader)
specifier|public
name|void
name|setCharacterStream
parameter_list|(
name|Reader
name|reader
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|getByteStream ()
specifier|public
name|InputStream
name|getByteStream
parameter_list|()
block|{
try|try
block|{
return|return
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|camelContext
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|uri
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
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|setByteStream (InputStream inputStream)
specifier|public
name|void
name|setByteStream
parameter_list|(
name|InputStream
name|inputStream
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|getStringData ()
specifier|public
name|String
name|getStringData
parameter_list|()
block|{
name|InputStream
name|is
init|=
name|getByteStream
argument_list|()
decl_stmt|;
return|return
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|is
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setStringData (String stringData)
specifier|public
name|void
name|setStringData
parameter_list|(
name|String
name|stringData
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|getSystemId ()
specifier|public
name|String
name|getSystemId
parameter_list|()
block|{
return|return
name|systemId
return|;
block|}
annotation|@
name|Override
DECL|method|setSystemId (String systemId)
specifier|public
name|void
name|setSystemId
parameter_list|(
name|String
name|systemId
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|getPublicId ()
specifier|public
name|String
name|getPublicId
parameter_list|()
block|{
return|return
name|publicId
return|;
block|}
annotation|@
name|Override
DECL|method|setPublicId (String publicId)
specifier|public
name|void
name|setPublicId
parameter_list|(
name|String
name|publicId
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|getBaseURI ()
specifier|public
name|String
name|getBaseURI
parameter_list|()
block|{
return|return
name|baseURI
return|;
block|}
annotation|@
name|Override
DECL|method|setBaseURI (String baseURI)
specifier|public
name|void
name|setBaseURI
parameter_list|(
name|String
name|baseURI
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|getEncoding ()
specifier|public
name|String
name|getEncoding
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setEncoding (String encoding)
specifier|public
name|void
name|setEncoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|getCertifiedText ()
specifier|public
name|boolean
name|getCertifiedText
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|setCertifiedText (boolean certifiedText)
specifier|public
name|void
name|setCertifiedText
parameter_list|(
name|boolean
name|certifiedText
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"DefaultLSInput["
operator|+
name|uri
operator|+
literal|"]"
return|;
block|}
block|}
block|}
end_class

end_unit

