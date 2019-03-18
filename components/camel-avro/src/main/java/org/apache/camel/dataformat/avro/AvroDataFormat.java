begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.avro
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|avro
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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|Schema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|generic
operator|.
name|GenericContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|generic
operator|.
name|GenericRecord
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|io
operator|.
name|DatumReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|io
operator|.
name|DatumWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|io
operator|.
name|Decoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|io
operator|.
name|DecoderFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|io
operator|.
name|Encoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|io
operator|.
name|EncoderFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|specific
operator|.
name|SpecificData
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|specific
operator|.
name|SpecificDatumReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|specific
operator|.
name|SpecificDatumWriter
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
name|CamelContextAware
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
name|spi
operator|.
name|DataFormatName
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
name|annotations
operator|.
name|Dataformat
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
name|service
operator|.
name|ServiceSupport
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
annotation|@
name|Dataformat
argument_list|(
literal|"avro"
argument_list|)
DECL|class|AvroDataFormat
specifier|public
class|class
name|AvroDataFormat
extends|extends
name|ServiceSupport
implements|implements
name|DataFormat
implements|,
name|DataFormatName
implements|,
name|CamelContextAware
block|{
DECL|field|GENERIC_CONTAINER_CLASSNAME
specifier|private
specifier|static
specifier|final
name|String
name|GENERIC_CONTAINER_CLASSNAME
init|=
name|GenericContainer
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|schema
specifier|private
name|Object
name|schema
decl_stmt|;
DECL|field|actualSchema
specifier|private
specifier|transient
name|Schema
name|actualSchema
decl_stmt|;
DECL|field|instanceClassName
specifier|private
name|String
name|instanceClassName
decl_stmt|;
DECL|method|AvroDataFormat ()
specifier|public
name|AvroDataFormat
parameter_list|()
block|{     }
DECL|method|AvroDataFormat (Schema schema)
specifier|public
name|AvroDataFormat
parameter_list|(
name|Schema
name|schema
parameter_list|)
block|{
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDataFormatName ()
specifier|public
name|String
name|getDataFormatName
parameter_list|()
block|{
return|return
literal|"avro"
return|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|schema
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|schema
operator|instanceof
name|Schema
condition|)
block|{
name|actualSchema
operator|=
operator|(
name|Schema
operator|)
name|schema
expr_stmt|;
block|}
else|else
block|{
name|actualSchema
operator|=
name|loadSchema
argument_list|(
name|schema
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|instanceClassName
operator|!=
literal|null
condition|)
block|{
name|actualSchema
operator|=
name|loadSchema
argument_list|(
name|instanceClassName
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
comment|// the getter/setter for Schema is Object type in the API
DECL|method|getSchema ()
specifier|public
name|Object
name|getSchema
parameter_list|()
block|{
return|return
name|actualSchema
operator|!=
literal|null
condition|?
name|actualSchema
else|:
name|schema
return|;
block|}
DECL|method|setSchema (Object schema)
specifier|public
name|void
name|setSchema
parameter_list|(
name|Object
name|schema
parameter_list|)
block|{
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
block|}
DECL|method|getInstanceClassName ()
specifier|public
name|String
name|getInstanceClassName
parameter_list|()
block|{
return|return
name|instanceClassName
return|;
block|}
DECL|method|setInstanceClassName (String className)
specifier|public
name|void
name|setInstanceClassName
parameter_list|(
name|String
name|className
parameter_list|)
throws|throws
name|Exception
block|{
name|instanceClassName
operator|=
name|className
expr_stmt|;
block|}
DECL|method|loadSchema (String className)
specifier|protected
name|Schema
name|loadSchema
parameter_list|(
name|String
name|className
parameter_list|)
throws|throws
name|CamelException
throws|,
name|ClassNotFoundException
block|{
comment|// must use same class loading procedure to ensure working in OSGi
name|Class
argument_list|<
name|?
argument_list|>
name|instanceClass
init|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|className
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|genericContainer
init|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|GENERIC_CONTAINER_CLASSNAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|genericContainer
operator|.
name|isAssignableFrom
argument_list|(
name|instanceClass
argument_list|)
condition|)
block|{
try|try
block|{
name|Method
name|method
init|=
name|instanceClass
operator|.
name|getMethod
argument_list|(
literal|"getSchema"
argument_list|)
decl_stmt|;
return|return
operator|(
name|Schema
operator|)
name|method
operator|.
name|invoke
argument_list|(
name|camelContext
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|instanceClass
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"Error calling getSchema on "
operator|+
name|instanceClass
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"Class "
operator|+
name|instanceClass
operator|+
literal|" must be instanceof "
operator|+
name|GENERIC_CONTAINER_CLASSNAME
argument_list|)
throw|;
block|}
block|}
DECL|method|marshal (Exchange exchange, Object graph, OutputStream outputStream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|graph
parameter_list|,
name|OutputStream
name|outputStream
parameter_list|)
throws|throws
name|Exception
block|{
comment|// the schema should be from the graph class name
name|Schema
name|useSchema
init|=
name|actualSchema
operator|!=
literal|null
condition|?
name|actualSchema
else|:
name|loadSchema
argument_list|(
name|graph
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|DatumWriter
argument_list|<
name|Object
argument_list|>
name|datum
init|=
operator|new
name|SpecificDatumWriter
argument_list|<>
argument_list|(
name|useSchema
argument_list|)
decl_stmt|;
name|Encoder
name|encoder
init|=
name|EncoderFactory
operator|.
name|get
argument_list|()
operator|.
name|binaryEncoder
argument_list|(
name|outputStream
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|datum
operator|.
name|write
argument_list|(
name|graph
argument_list|,
name|encoder
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|flush
argument_list|()
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|actualSchema
argument_list|,
literal|"schema"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ClassLoader
name|classLoader
init|=
literal|null
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|actualSchema
operator|.
name|getFullName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|clazz
operator|!=
literal|null
condition|)
block|{
name|classLoader
operator|=
name|clazz
operator|.
name|getClassLoader
argument_list|()
expr_stmt|;
block|}
name|SpecificData
name|specificData
init|=
operator|new
name|SpecificDataNoCache
argument_list|(
name|classLoader
argument_list|)
decl_stmt|;
name|DatumReader
argument_list|<
name|GenericRecord
argument_list|>
name|reader
init|=
operator|new
name|SpecificDatumReader
argument_list|<>
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|specificData
argument_list|)
decl_stmt|;
name|reader
operator|.
name|setSchema
argument_list|(
name|actualSchema
argument_list|)
expr_stmt|;
name|Decoder
name|decoder
init|=
name|DecoderFactory
operator|.
name|get
argument_list|()
operator|.
name|binaryDecoder
argument_list|(
name|inputStream
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Object
name|result
init|=
name|reader
operator|.
name|read
argument_list|(
literal|null
argument_list|,
name|decoder
argument_list|)
decl_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

