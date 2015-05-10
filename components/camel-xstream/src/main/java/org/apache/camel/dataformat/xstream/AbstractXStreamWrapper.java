begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.xstream
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|xstream
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
name|Constructor
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
name|java
operator|.
name|util
operator|.
name|List
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
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|XStream
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|converters
operator|.
name|Converter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|core
operator|.
name|util
operator|.
name|CompositeClassLoader
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|io
operator|.
name|HierarchicalStreamDriver
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|io
operator|.
name|HierarchicalStreamReader
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|io
operator|.
name|HierarchicalStreamWriter
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
name|converter
operator|.
name|jaxp
operator|.
name|StaxConverter
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

begin_comment
comment|/**  * An abstract class which implement<a href="http://camel.apache.org/data-format.html">data format</a>  * ({@link DataFormat}) interface which leverage the XStream library for XML or JSON's marshaling and unmarshaling  */
end_comment

begin_class
DECL|class|AbstractXStreamWrapper
specifier|public
specifier|abstract
class|class
name|AbstractXStreamWrapper
implements|implements
name|DataFormat
block|{
DECL|field|xstream
specifier|private
name|XStream
name|xstream
decl_stmt|;
DECL|field|xstreamDriver
specifier|private
name|HierarchicalStreamDriver
name|xstreamDriver
decl_stmt|;
DECL|field|staxConverter
specifier|private
name|StaxConverter
name|staxConverter
decl_stmt|;
DECL|field|converters
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|converters
decl_stmt|;
DECL|field|aliases
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|aliases
decl_stmt|;
DECL|field|omitFields
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|omitFields
decl_stmt|;
DECL|field|implicitCollections
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|implicitCollections
decl_stmt|;
DECL|field|mode
specifier|private
name|String
name|mode
decl_stmt|;
DECL|method|AbstractXStreamWrapper ()
specifier|public
name|AbstractXStreamWrapper
parameter_list|()
block|{     }
DECL|method|AbstractXStreamWrapper (XStream xstream)
specifier|public
name|AbstractXStreamWrapper
parameter_list|(
name|XStream
name|xstream
parameter_list|)
block|{
name|this
operator|.
name|xstream
operator|=
name|xstream
expr_stmt|;
block|}
comment|/**      * Resolves the XStream instance to be used by this data format. If XStream is not explicitly set, new instance will      * be created and cached.      *      * @param resolver class resolver to be used during a configuration of the XStream instance.      * @return XStream instance used by this data format.      */
DECL|method|getXStream (ClassResolver resolver)
specifier|public
name|XStream
name|getXStream
parameter_list|(
name|ClassResolver
name|resolver
parameter_list|)
block|{
if|if
condition|(
name|xstream
operator|==
literal|null
condition|)
block|{
name|xstream
operator|=
name|createXStream
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
block|}
return|return
name|xstream
return|;
block|}
comment|/**      * Resolves the XStream instance to be used by this data format. If XStream is not explicitly set, new instance will      * be created and cached.      *      * @param context to be used during a configuration of the XStream instance      * @return XStream instance used by this data format.      */
DECL|method|getXStream (CamelContext context)
specifier|public
name|XStream
name|getXStream
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
if|if
condition|(
name|xstream
operator|==
literal|null
condition|)
block|{
name|xstream
operator|=
name|createXStream
argument_list|(
name|context
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|context
operator|.
name|getApplicationContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|xstream
return|;
block|}
DECL|method|setXStream (XStream xstream)
specifier|public
name|void
name|setXStream
parameter_list|(
name|XStream
name|xstream
parameter_list|)
block|{
name|this
operator|.
name|xstream
operator|=
name|xstream
expr_stmt|;
block|}
comment|/**      * @deprecated Use {@link #createXStream(ClassResolver, ClassLoader)}      */
annotation|@
name|Deprecated
DECL|method|createXStream (ClassResolver resolver)
specifier|protected
name|XStream
name|createXStream
parameter_list|(
name|ClassResolver
name|resolver
parameter_list|)
block|{
return|return
name|createXStream
argument_list|(
name|resolver
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|createXStream (ClassResolver resolver, ClassLoader classLoader)
specifier|protected
name|XStream
name|createXStream
parameter_list|(
name|ClassResolver
name|resolver
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|)
block|{
if|if
condition|(
name|xstreamDriver
operator|!=
literal|null
condition|)
block|{
name|xstream
operator|=
operator|new
name|XStream
argument_list|(
name|xstreamDriver
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|xstream
operator|=
operator|new
name|XStream
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|mode
operator|!=
literal|null
condition|)
block|{
name|xstream
operator|.
name|setMode
argument_list|(
name|getModeFromString
argument_list|(
name|mode
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ClassLoader
name|xstreamLoader
init|=
name|xstream
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|classLoader
operator|!=
literal|null
operator|&&
name|xstreamLoader
operator|instanceof
name|CompositeClassLoader
condition|)
block|{
operator|(
operator|(
name|CompositeClassLoader
operator|)
name|xstreamLoader
operator|)
operator|.
name|add
argument_list|(
name|classLoader
argument_list|)
expr_stmt|;
block|}
try|try
block|{
if|if
condition|(
name|this
operator|.
name|implicitCollections
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|entry
range|:
name|this
operator|.
name|implicitCollections
operator|.
name|entrySet
argument_list|()
control|)
block|{
for|for
control|(
name|String
name|name
range|:
name|entry
operator|.
name|getValue
argument_list|()
control|)
block|{
name|xstream
operator|.
name|addImplicitCollection
argument_list|(
name|resolver
operator|.
name|resolveMandatoryClass
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|this
operator|.
name|aliases
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|this
operator|.
name|aliases
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|xstream
operator|.
name|alias
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|resolver
operator|.
name|resolveMandatoryClass
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// It can turn the auto-detection mode off
name|xstream
operator|.
name|processAnnotations
argument_list|(
name|resolver
operator|.
name|resolveMandatoryClass
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|this
operator|.
name|omitFields
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|entry
range|:
name|this
operator|.
name|omitFields
operator|.
name|entrySet
argument_list|()
control|)
block|{
for|for
control|(
name|String
name|name
range|:
name|entry
operator|.
name|getValue
argument_list|()
control|)
block|{
name|xstream
operator|.
name|omitField
argument_list|(
name|resolver
operator|.
name|resolveMandatoryClass
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|this
operator|.
name|converters
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|name
range|:
name|this
operator|.
name|converters
control|)
block|{
name|Class
argument_list|<
name|Converter
argument_list|>
name|converterClass
init|=
name|resolver
operator|.
name|resolveMandatoryClass
argument_list|(
name|name
argument_list|,
name|Converter
operator|.
name|class
argument_list|)
decl_stmt|;
name|Converter
name|converter
decl_stmt|;
name|Constructor
argument_list|<
name|Converter
argument_list|>
name|con
init|=
literal|null
decl_stmt|;
try|try
block|{
name|con
operator|=
name|converterClass
operator|.
name|getDeclaredConstructor
argument_list|(
operator|new
name|Class
index|[]
block|{
name|XStream
operator|.
name|class
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//swallow as we null check in a moment.
block|}
if|if
condition|(
name|con
operator|!=
literal|null
condition|)
block|{
name|converter
operator|=
name|con
operator|.
name|newInstance
argument_list|(
name|xstream
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|converter
operator|=
name|converterClass
operator|.
name|newInstance
argument_list|()
expr_stmt|;
try|try
block|{
name|Method
name|method
init|=
name|converterClass
operator|.
name|getMethod
argument_list|(
literal|"setXStream"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|XStream
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|ObjectHelper
operator|.
name|invokeMethod
argument_list|(
name|method
argument_list|,
name|converter
argument_list|,
name|xstream
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// swallow, as it just means the user never add an XStream setter, which is optional
block|}
block|}
name|xstream
operator|.
name|registerConverter
argument_list|(
name|converter
argument_list|)
expr_stmt|;
block|}
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
name|RuntimeException
argument_list|(
literal|"Unable to build XStream instance"
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|xstream
return|;
block|}
DECL|method|getModeFromString (String modeString)
specifier|protected
name|int
name|getModeFromString
parameter_list|(
name|String
name|modeString
parameter_list|)
block|{
name|int
name|result
decl_stmt|;
if|if
condition|(
literal|"NO_REFERENCES"
operator|.
name|equalsIgnoreCase
argument_list|(
name|modeString
argument_list|)
condition|)
block|{
name|result
operator|=
name|XStream
operator|.
name|NO_REFERENCES
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"ID_REFERENCES"
operator|.
name|equalsIgnoreCase
argument_list|(
name|modeString
argument_list|)
condition|)
block|{
name|result
operator|=
name|XStream
operator|.
name|ID_REFERENCES
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"XPATH_RELATIVE_REFERENCES"
operator|.
name|equalsIgnoreCase
argument_list|(
name|modeString
argument_list|)
condition|)
block|{
name|result
operator|=
name|XStream
operator|.
name|XPATH_RELATIVE_REFERENCES
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"XPATH_ABSOLUTE_REFERENCES"
operator|.
name|equalsIgnoreCase
argument_list|(
name|modeString
argument_list|)
condition|)
block|{
name|result
operator|=
name|XStream
operator|.
name|XPATH_ABSOLUTE_REFERENCES
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"SINGLE_NODE_XPATH_RELATIVE_REFERENCES"
operator|.
name|equalsIgnoreCase
argument_list|(
name|modeString
argument_list|)
condition|)
block|{
name|result
operator|=
name|XStream
operator|.
name|SINGLE_NODE_XPATH_RELATIVE_REFERENCES
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"SINGLE_NODE_XPATH_ABSOLUTE_REFERENCES"
operator|.
name|equalsIgnoreCase
argument_list|(
name|modeString
argument_list|)
condition|)
block|{
name|result
operator|=
name|XStream
operator|.
name|SINGLE_NODE_XPATH_ABSOLUTE_REFERENCES
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown mode : "
operator|+
name|modeString
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
DECL|method|getStaxConverter ()
specifier|public
name|StaxConverter
name|getStaxConverter
parameter_list|()
block|{
if|if
condition|(
name|staxConverter
operator|==
literal|null
condition|)
block|{
name|staxConverter
operator|=
operator|new
name|StaxConverter
argument_list|()
expr_stmt|;
block|}
return|return
name|staxConverter
return|;
block|}
DECL|method|setStaxConverter (StaxConverter staxConverter)
specifier|public
name|void
name|setStaxConverter
parameter_list|(
name|StaxConverter
name|staxConverter
parameter_list|)
block|{
name|this
operator|.
name|staxConverter
operator|=
name|staxConverter
expr_stmt|;
block|}
DECL|method|getConverters ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getConverters
parameter_list|()
block|{
return|return
name|converters
return|;
block|}
DECL|method|setConverters (List<String> converters)
specifier|public
name|void
name|setConverters
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|converters
parameter_list|)
block|{
name|this
operator|.
name|converters
operator|=
name|converters
expr_stmt|;
block|}
DECL|method|getAliases ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getAliases
parameter_list|()
block|{
return|return
name|aliases
return|;
block|}
DECL|method|setAliases (Map<String, String> aliases)
specifier|public
name|void
name|setAliases
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|aliases
parameter_list|)
block|{
name|this
operator|.
name|aliases
operator|=
name|aliases
expr_stmt|;
block|}
DECL|method|getImplicitCollections ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|getImplicitCollections
parameter_list|()
block|{
return|return
name|implicitCollections
return|;
block|}
DECL|method|setImplicitCollections (Map<String, String[]> implicitCollections)
specifier|public
name|void
name|setImplicitCollections
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|implicitCollections
parameter_list|)
block|{
name|this
operator|.
name|implicitCollections
operator|=
name|implicitCollections
expr_stmt|;
block|}
DECL|method|getOmitFields ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|getOmitFields
parameter_list|()
block|{
return|return
name|omitFields
return|;
block|}
DECL|method|setOmitFields (Map<String, String[]> omitFields)
specifier|public
name|void
name|setOmitFields
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|omitFields
parameter_list|)
block|{
name|this
operator|.
name|omitFields
operator|=
name|omitFields
expr_stmt|;
block|}
DECL|method|getXstreamDriver ()
specifier|public
name|HierarchicalStreamDriver
name|getXstreamDriver
parameter_list|()
block|{
return|return
name|xstreamDriver
return|;
block|}
DECL|method|setXstreamDriver (HierarchicalStreamDriver xstreamDriver)
specifier|public
name|void
name|setXstreamDriver
parameter_list|(
name|HierarchicalStreamDriver
name|xstreamDriver
parameter_list|)
block|{
name|this
operator|.
name|xstreamDriver
operator|=
name|xstreamDriver
expr_stmt|;
block|}
DECL|method|getMode ()
specifier|public
name|String
name|getMode
parameter_list|()
block|{
return|return
name|mode
return|;
block|}
DECL|method|setMode (String mode)
specifier|public
name|void
name|setMode
parameter_list|(
name|String
name|mode
parameter_list|)
block|{
name|this
operator|.
name|mode
operator|=
name|mode
expr_stmt|;
block|}
DECL|method|getXstream ()
specifier|public
name|XStream
name|getXstream
parameter_list|()
block|{
return|return
name|xstream
return|;
block|}
DECL|method|setXstream (XStream xstream)
specifier|public
name|void
name|setXstream
parameter_list|(
name|XStream
name|xstream
parameter_list|)
block|{
name|this
operator|.
name|xstream
operator|=
name|xstream
expr_stmt|;
block|}
DECL|method|marshal (Exchange exchange, Object body, OutputStream stream)
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
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
name|HierarchicalStreamWriter
name|writer
init|=
name|createHierarchicalStreamWriter
argument_list|(
name|exchange
argument_list|,
name|body
argument_list|,
name|stream
argument_list|)
decl_stmt|;
try|try
block|{
name|getXStream
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|)
operator|.
name|marshal
argument_list|(
name|body
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|unmarshal (Exchange exchange, InputStream stream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
name|HierarchicalStreamReader
name|reader
init|=
name|createHierarchicalStreamReader
argument_list|(
name|exchange
argument_list|,
name|stream
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|getXStream
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|reader
argument_list|)
return|;
block|}
finally|finally
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|createHierarchicalStreamWriter ( Exchange exchange, Object body, OutputStream stream)
specifier|protected
specifier|abstract
name|HierarchicalStreamWriter
name|createHierarchicalStreamWriter
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|body
parameter_list|,
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|XMLStreamException
function_decl|;
DECL|method|createHierarchicalStreamReader ( Exchange exchange, InputStream stream)
specifier|protected
specifier|abstract
name|HierarchicalStreamReader
name|createHierarchicalStreamReader
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|stream
parameter_list|)
throws|throws
name|XMLStreamException
function_decl|;
block|}
end_class

end_unit

