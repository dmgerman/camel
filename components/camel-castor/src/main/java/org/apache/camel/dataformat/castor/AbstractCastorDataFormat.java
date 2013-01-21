begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.castor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|castor
package|;
end_package

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
name|InputStreamReader
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
name|io
operator|.
name|OutputStreamWriter
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
name|java
operator|.
name|io
operator|.
name|Writer
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
name|spi
operator|.
name|ClassResolver
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
name|DataFormat
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
name|exolab
operator|.
name|castor
operator|.
name|mapping
operator|.
name|Mapping
import|;
end_import

begin_import
import|import
name|org
operator|.
name|exolab
operator|.
name|castor
operator|.
name|xml
operator|.
name|Marshaller
import|;
end_import

begin_import
import|import
name|org
operator|.
name|exolab
operator|.
name|castor
operator|.
name|xml
operator|.
name|Unmarshaller
import|;
end_import

begin_import
import|import
name|org
operator|.
name|exolab
operator|.
name|castor
operator|.
name|xml
operator|.
name|XMLContext
import|;
end_import

begin_comment
comment|/**  * An abstract class which implement<a  * href="http://camel.apache.org/data-format.html">data format</a> ({@link DataFormat})  * interface which leverage the Castor library for XML marshaling and  * unmarshaling  *  * @version   */
end_comment

begin_class
DECL|class|AbstractCastorDataFormat
specifier|public
specifier|abstract
class|class
name|AbstractCastorDataFormat
implements|implements
name|DataFormat
block|{
comment|/**      * The default encoding used for stream access.      */
DECL|field|DEFAULT_ENCODING
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_ENCODING
init|=
literal|"UTF-8"
decl_stmt|;
DECL|field|encoding
specifier|private
name|String
name|encoding
init|=
name|DEFAULT_ENCODING
decl_stmt|;
DECL|field|mappingFile
specifier|private
name|String
name|mappingFile
decl_stmt|;
DECL|field|classNames
specifier|private
name|String
index|[]
name|classNames
decl_stmt|;
DECL|field|packages
specifier|private
name|String
index|[]
name|packages
decl_stmt|;
DECL|field|validation
specifier|private
name|boolean
name|validation
decl_stmt|;
DECL|field|xmlContext
specifier|private
specifier|volatile
name|XMLContext
name|xmlContext
decl_stmt|;
DECL|field|marshaller
specifier|private
specifier|volatile
name|Marshaller
name|marshaller
decl_stmt|;
DECL|field|unmarshaller
specifier|private
specifier|volatile
name|Unmarshaller
name|unmarshaller
decl_stmt|;
DECL|method|AbstractCastorDataFormat ()
specifier|public
name|AbstractCastorDataFormat
parameter_list|()
block|{     }
DECL|method|AbstractCastorDataFormat (XMLContext xmlContext)
specifier|public
name|AbstractCastorDataFormat
parameter_list|(
name|XMLContext
name|xmlContext
parameter_list|)
block|{
name|this
operator|.
name|xmlContext
operator|=
name|xmlContext
expr_stmt|;
block|}
DECL|method|marshal (Exchange exchange, Object body, OutputStream outputStream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|body
parameter_list|,
name|OutputStream
name|outputStream
parameter_list|)
throws|throws
name|Exception
block|{
name|Writer
name|writer
init|=
operator|new
name|OutputStreamWriter
argument_list|(
name|outputStream
argument_list|,
name|encoding
argument_list|)
decl_stmt|;
name|Marshaller
operator|.
name|marshal
argument_list|(
name|body
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
DECL|method|unmarshal (Exchange exchange, InputStream inputStream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|inputStream
parameter_list|)
throws|throws
name|Exception
block|{
name|Reader
name|reader
init|=
operator|new
name|InputStreamReader
argument_list|(
name|inputStream
argument_list|,
name|encoding
argument_list|)
decl_stmt|;
return|return
name|getUnmarshaller
argument_list|(
name|exchange
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|reader
argument_list|)
return|;
block|}
DECL|method|getXmlContext (ClassResolver resolver, ClassLoader contetClassLoader)
specifier|public
name|XMLContext
name|getXmlContext
parameter_list|(
name|ClassResolver
name|resolver
parameter_list|,
name|ClassLoader
name|contetClassLoader
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|xmlContext
operator|==
literal|null
condition|)
block|{
name|xmlContext
operator|=
operator|new
name|XMLContext
argument_list|()
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getMappingFile
argument_list|()
argument_list|)
condition|)
block|{
name|Mapping
name|xmlMap
decl_stmt|;
if|if
condition|(
name|contetClassLoader
operator|!=
literal|null
condition|)
block|{
name|xmlMap
operator|=
operator|new
name|Mapping
argument_list|(
name|contetClassLoader
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|xmlMap
operator|=
operator|new
name|Mapping
argument_list|()
expr_stmt|;
block|}
name|xmlMap
operator|.
name|loadMapping
argument_list|(
name|resolver
operator|.
name|loadResourceAsURL
argument_list|(
name|getMappingFile
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|xmlContext
operator|.
name|addMapping
argument_list|(
name|xmlMap
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getPackages
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|xmlContext
operator|.
name|addPackages
argument_list|(
name|getPackages
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getClassNames
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|name
range|:
name|getClassNames
argument_list|()
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|resolver
operator|.
name|resolveClass
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|xmlContext
operator|.
name|addClass
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|xmlContext
return|;
block|}
DECL|method|getUnmarshaller (Exchange exchange)
specifier|public
name|Unmarshaller
name|getUnmarshaller
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|this
operator|.
name|unmarshaller
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|unmarshaller
operator|=
name|getXmlContext
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getApplicationContextClassLoader
argument_list|()
argument_list|)
operator|.
name|createUnmarshaller
argument_list|()
expr_stmt|;
name|this
operator|.
name|unmarshaller
operator|.
name|setValidation
argument_list|(
name|isValidation
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
operator|.
name|unmarshaller
return|;
block|}
DECL|method|getMarshaller (Exchange exchange)
specifier|public
name|Marshaller
name|getMarshaller
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|this
operator|.
name|marshaller
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|marshaller
operator|=
name|getXmlContext
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getApplicationContextClassLoader
argument_list|()
argument_list|)
operator|.
name|createMarshaller
argument_list|()
expr_stmt|;
name|this
operator|.
name|marshaller
operator|.
name|setValidation
argument_list|(
name|isValidation
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
operator|.
name|marshaller
return|;
block|}
DECL|method|setXmlContext (XMLContext xmlContext)
specifier|public
name|void
name|setXmlContext
parameter_list|(
name|XMLContext
name|xmlContext
parameter_list|)
block|{
name|this
operator|.
name|xmlContext
operator|=
name|xmlContext
expr_stmt|;
block|}
DECL|method|getMappingFile ()
specifier|public
name|String
name|getMappingFile
parameter_list|()
block|{
return|return
name|mappingFile
return|;
block|}
DECL|method|setMappingFile (String mappingFile)
specifier|public
name|void
name|setMappingFile
parameter_list|(
name|String
name|mappingFile
parameter_list|)
block|{
name|this
operator|.
name|mappingFile
operator|=
name|mappingFile
expr_stmt|;
block|}
DECL|method|setMarshaller (Marshaller marshaller)
specifier|public
name|void
name|setMarshaller
parameter_list|(
name|Marshaller
name|marshaller
parameter_list|)
block|{
name|this
operator|.
name|marshaller
operator|=
name|marshaller
expr_stmt|;
block|}
DECL|method|setUnmarshaller (Unmarshaller unmarshaller)
specifier|public
name|void
name|setUnmarshaller
parameter_list|(
name|Unmarshaller
name|unmarshaller
parameter_list|)
block|{
name|this
operator|.
name|unmarshaller
operator|=
name|unmarshaller
expr_stmt|;
block|}
DECL|method|getClassNames ()
specifier|public
name|String
index|[]
name|getClassNames
parameter_list|()
block|{
return|return
name|classNames
return|;
block|}
DECL|method|setClassNames (String[] classNames)
specifier|public
name|void
name|setClassNames
parameter_list|(
name|String
index|[]
name|classNames
parameter_list|)
block|{
name|this
operator|.
name|classNames
operator|=
name|classNames
expr_stmt|;
block|}
DECL|method|getPackages ()
specifier|public
name|String
index|[]
name|getPackages
parameter_list|()
block|{
return|return
name|packages
return|;
block|}
DECL|method|setPackages (String[] packages)
specifier|public
name|void
name|setPackages
parameter_list|(
name|String
index|[]
name|packages
parameter_list|)
block|{
name|this
operator|.
name|packages
operator|=
name|packages
expr_stmt|;
block|}
DECL|method|getEncoding ()
specifier|public
name|String
name|getEncoding
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
DECL|method|setEncoding (String encoding)
specifier|public
name|void
name|setEncoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|this
operator|.
name|encoding
operator|=
name|encoding
expr_stmt|;
block|}
DECL|method|isValidation ()
specifier|public
name|boolean
name|isValidation
parameter_list|()
block|{
return|return
name|validation
return|;
block|}
DECL|method|setValidation (boolean validation)
specifier|public
name|void
name|setValidation
parameter_list|(
name|boolean
name|validation
parameter_list|)
block|{
name|this
operator|.
name|validation
operator|=
name|validation
expr_stmt|;
block|}
block|}
end_class

end_unit

