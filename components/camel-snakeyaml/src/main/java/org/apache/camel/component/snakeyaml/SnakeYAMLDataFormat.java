begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.snakeyaml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|snakeyaml
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
name|lang
operator|.
name|ref
operator|.
name|WeakReference
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|support
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
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|yaml
operator|.
name|snakeyaml
operator|.
name|DumperOptions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|yaml
operator|.
name|snakeyaml
operator|.
name|TypeDescription
import|;
end_import

begin_import
import|import
name|org
operator|.
name|yaml
operator|.
name|snakeyaml
operator|.
name|Yaml
import|;
end_import

begin_import
import|import
name|org
operator|.
name|yaml
operator|.
name|snakeyaml
operator|.
name|constructor
operator|.
name|BaseConstructor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|yaml
operator|.
name|snakeyaml
operator|.
name|constructor
operator|.
name|Constructor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|yaml
operator|.
name|snakeyaml
operator|.
name|constructor
operator|.
name|CustomClassLoaderConstructor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|yaml
operator|.
name|snakeyaml
operator|.
name|nodes
operator|.
name|Tag
import|;
end_import

begin_import
import|import
name|org
operator|.
name|yaml
operator|.
name|snakeyaml
operator|.
name|representer
operator|.
name|Representer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|yaml
operator|.
name|snakeyaml
operator|.
name|resolver
operator|.
name|Resolver
import|;
end_import

begin_comment
comment|/**  * A<a href="http://camel.apache.org/data-format.html">data format</a> ({@link DataFormat})  * using<a href="http://www.snakeyaml.org">SnakeYAML</a> to marshal to and from YAML.  */
end_comment

begin_class
DECL|class|SnakeYAMLDataFormat
specifier|public
class|class
name|SnakeYAMLDataFormat
extends|extends
name|ServiceSupport
implements|implements
name|DataFormat
implements|,
name|DataFormatName
block|{
DECL|field|yamlCache
specifier|private
name|ThreadLocal
argument_list|<
name|WeakReference
argument_list|<
name|Yaml
argument_list|>
argument_list|>
name|yamlCache
decl_stmt|;
DECL|field|constructor
specifier|private
name|BaseConstructor
name|constructor
decl_stmt|;
DECL|field|representer
specifier|private
name|Representer
name|representer
decl_stmt|;
DECL|field|dumperOptions
specifier|private
name|DumperOptions
name|dumperOptions
decl_stmt|;
DECL|field|resolver
specifier|private
name|Resolver
name|resolver
decl_stmt|;
DECL|field|classLoader
specifier|private
name|ClassLoader
name|classLoader
decl_stmt|;
DECL|field|unmarshalType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
decl_stmt|;
DECL|field|typeDescriptions
specifier|private
name|List
argument_list|<
name|TypeDescription
argument_list|>
name|typeDescriptions
decl_stmt|;
DECL|field|classTags
specifier|private
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Tag
argument_list|>
name|classTags
decl_stmt|;
DECL|field|useApplicationContextClassLoader
specifier|private
name|boolean
name|useApplicationContextClassLoader
decl_stmt|;
DECL|field|prettyFlow
specifier|private
name|boolean
name|prettyFlow
decl_stmt|;
DECL|method|SnakeYAMLDataFormat ()
specifier|public
name|SnakeYAMLDataFormat
parameter_list|()
block|{
name|this
argument_list|(
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|SnakeYAMLDataFormat (Class<?> type)
specifier|public
name|SnakeYAMLDataFormat
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|this
operator|.
name|unmarshalType
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|yamlCache
operator|=
operator|new
name|ThreadLocal
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|useApplicationContextClassLoader
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|prettyFlow
operator|=
literal|false
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
literal|"yaml-snakeyaml"
return|;
block|}
annotation|@
name|Override
DECL|method|marshal (final Exchange exchange, final Object graph, final OutputStream stream)
specifier|public
name|void
name|marshal
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|Object
name|graph
parameter_list|,
specifier|final
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
specifier|final
name|OutputStreamWriter
name|osw
init|=
operator|new
name|OutputStreamWriter
argument_list|(
name|stream
argument_list|,
name|IOHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
init|)
block|{
name|getYaml
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|)
operator|.
name|dump
argument_list|(
name|graph
argument_list|,
name|osw
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|unmarshal (final Exchange exchange, final InputStream stream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|InputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
specifier|final
name|InputStreamReader
name|isr
init|=
operator|new
name|InputStreamReader
argument_list|(
name|stream
argument_list|,
name|IOHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
init|)
block|{
return|return
name|getYaml
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|)
operator|.
name|loadAs
argument_list|(
name|isr
argument_list|,
name|unmarshalType
argument_list|)
return|;
block|}
block|}
DECL|method|getYaml (CamelContext context)
specifier|protected
name|Yaml
name|getYaml
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|Yaml
name|yaml
init|=
literal|null
decl_stmt|;
name|WeakReference
argument_list|<
name|Yaml
argument_list|>
name|ref
init|=
name|yamlCache
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|yaml
operator|=
name|ref
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|yaml
operator|==
literal|null
condition|)
block|{
name|BaseConstructor
name|yamlConstructor
init|=
name|this
operator|.
name|constructor
decl_stmt|;
name|Representer
name|yamlRepresenter
init|=
name|this
operator|.
name|representer
decl_stmt|;
name|DumperOptions
name|yamlDumperOptions
init|=
name|this
operator|.
name|dumperOptions
decl_stmt|;
name|Resolver
name|yamlResolver
init|=
name|this
operator|.
name|resolver
decl_stmt|;
name|ClassLoader
name|yamlClassLoader
init|=
name|this
operator|.
name|classLoader
decl_stmt|;
if|if
condition|(
name|yamlClassLoader
operator|==
literal|null
operator|&&
name|useApplicationContextClassLoader
condition|)
block|{
name|yamlClassLoader
operator|=
name|context
operator|.
name|getApplicationContextClassLoader
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|yamlConstructor
operator|==
literal|null
condition|)
block|{
name|yamlConstructor
operator|=
name|yamlClassLoader
operator|==
literal|null
condition|?
operator|new
name|Constructor
argument_list|()
else|:
operator|new
name|CustomClassLoaderConstructor
argument_list|(
name|yamlClassLoader
argument_list|)
expr_stmt|;
if|if
condition|(
name|typeDescriptions
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|TypeDescription
name|typeDescription
range|:
name|typeDescriptions
control|)
block|{
operator|(
operator|(
name|Constructor
operator|)
name|yamlConstructor
operator|)
operator|.
name|addTypeDescription
argument_list|(
name|typeDescription
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|yamlRepresenter
operator|==
literal|null
condition|)
block|{
name|yamlRepresenter
operator|=
operator|new
name|Representer
argument_list|()
expr_stmt|;
if|if
condition|(
name|classTags
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Tag
argument_list|>
name|entry
range|:
name|classTags
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|yamlRepresenter
operator|.
name|addClassTag
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|yamlDumperOptions
operator|==
literal|null
condition|)
block|{
name|yamlDumperOptions
operator|=
operator|new
name|DumperOptions
argument_list|()
expr_stmt|;
name|yamlDumperOptions
operator|.
name|setPrettyFlow
argument_list|(
name|prettyFlow
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|yamlResolver
operator|==
literal|null
condition|)
block|{
name|yamlResolver
operator|=
operator|new
name|Resolver
argument_list|()
expr_stmt|;
block|}
name|yaml
operator|=
operator|new
name|Yaml
argument_list|(
name|yamlConstructor
argument_list|,
name|yamlRepresenter
argument_list|,
name|yamlDumperOptions
argument_list|,
name|yamlResolver
argument_list|)
expr_stmt|;
name|yamlCache
operator|.
name|set
argument_list|(
operator|new
name|WeakReference
argument_list|<>
argument_list|(
name|yaml
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|yaml
return|;
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
comment|// noop
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
DECL|method|getConstructor ()
specifier|public
name|BaseConstructor
name|getConstructor
parameter_list|()
block|{
return|return
name|constructor
return|;
block|}
comment|/**      * BaseConstructor to construct incoming documents.      */
DECL|method|setConstructor (BaseConstructor constructor)
specifier|public
name|void
name|setConstructor
parameter_list|(
name|BaseConstructor
name|constructor
parameter_list|)
block|{
name|this
operator|.
name|constructor
operator|=
name|constructor
expr_stmt|;
block|}
DECL|method|getRepresenter ()
specifier|public
name|Representer
name|getRepresenter
parameter_list|()
block|{
return|return
name|representer
return|;
block|}
comment|/**      * Representer to emit outgoing objects.      */
DECL|method|setRepresenter (Representer representer)
specifier|public
name|void
name|setRepresenter
parameter_list|(
name|Representer
name|representer
parameter_list|)
block|{
name|this
operator|.
name|representer
operator|=
name|representer
expr_stmt|;
block|}
DECL|method|getDumperOptions ()
specifier|public
name|DumperOptions
name|getDumperOptions
parameter_list|()
block|{
return|return
name|dumperOptions
return|;
block|}
comment|/**      * DumperOptions to configure outgoing objects.      */
DECL|method|setDumperOptions (DumperOptions dumperOptions)
specifier|public
name|void
name|setDumperOptions
parameter_list|(
name|DumperOptions
name|dumperOptions
parameter_list|)
block|{
name|this
operator|.
name|dumperOptions
operator|=
name|dumperOptions
expr_stmt|;
block|}
DECL|method|getResolver ()
specifier|public
name|Resolver
name|getResolver
parameter_list|()
block|{
return|return
name|resolver
return|;
block|}
comment|/**      * Resolver to detect implicit type      */
DECL|method|setResolver (Resolver resolver)
specifier|public
name|void
name|setResolver
parameter_list|(
name|Resolver
name|resolver
parameter_list|)
block|{
name|this
operator|.
name|resolver
operator|=
name|resolver
expr_stmt|;
block|}
DECL|method|getClassLoader ()
specifier|public
name|ClassLoader
name|getClassLoader
parameter_list|()
block|{
return|return
name|classLoader
return|;
block|}
comment|/**      * Set a custom classloader      */
DECL|method|setClassLoader (ClassLoader classLoader)
specifier|public
name|void
name|setClassLoader
parameter_list|(
name|ClassLoader
name|classLoader
parameter_list|)
block|{
name|this
operator|.
name|classLoader
operator|=
name|classLoader
expr_stmt|;
block|}
DECL|method|getUnmarshalType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getUnmarshalType
parameter_list|()
block|{
return|return
name|this
operator|.
name|unmarshalType
return|;
block|}
comment|/**      * Class of the object to be created      */
DECL|method|setUnmarshalType (Class<?> unmarshalType)
specifier|public
name|void
name|setUnmarshalType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
parameter_list|)
block|{
name|this
operator|.
name|unmarshalType
operator|=
name|unmarshalType
expr_stmt|;
block|}
DECL|method|getTypeDescriptions ()
specifier|public
name|List
argument_list|<
name|TypeDescription
argument_list|>
name|getTypeDescriptions
parameter_list|()
block|{
return|return
name|typeDescriptions
return|;
block|}
comment|/**      * Make YAML aware how to parse a custom Class.      */
DECL|method|setTypeDescriptions (List<TypeDescription> typeDescriptions)
specifier|public
name|void
name|setTypeDescriptions
parameter_list|(
name|List
argument_list|<
name|TypeDescription
argument_list|>
name|typeDescriptions
parameter_list|)
block|{
name|this
operator|.
name|typeDescriptions
operator|=
name|typeDescriptions
expr_stmt|;
block|}
DECL|method|addTypeDescriptions (TypeDescription... typeDescriptions)
specifier|public
name|void
name|addTypeDescriptions
parameter_list|(
name|TypeDescription
modifier|...
name|typeDescriptions
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|typeDescriptions
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|typeDescriptions
operator|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|TypeDescription
name|typeDescription
range|:
name|typeDescriptions
control|)
block|{
name|this
operator|.
name|typeDescriptions
operator|.
name|add
argument_list|(
name|typeDescription
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addTypeDescription (Class<?> type, Tag tag)
specifier|public
name|void
name|addTypeDescription
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Tag
name|tag
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|typeDescriptions
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|typeDescriptions
operator|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|typeDescriptions
operator|.
name|add
argument_list|(
operator|new
name|TypeDescription
argument_list|(
name|type
argument_list|,
name|tag
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getClassTags ()
specifier|public
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Tag
argument_list|>
name|getClassTags
parameter_list|()
block|{
return|return
name|classTags
return|;
block|}
comment|/**      * Define a tag for the<code>Class</code> to serialize.      */
DECL|method|setClassTags (Map<Class<?>, Tag> classTags)
specifier|public
name|void
name|setClassTags
parameter_list|(
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Tag
argument_list|>
name|classTags
parameter_list|)
block|{
name|this
operator|.
name|classTags
operator|=
name|classTags
expr_stmt|;
block|}
DECL|method|addClassTags (Class<?> type, Tag tag)
specifier|public
name|void
name|addClassTags
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Tag
name|tag
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|classTags
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|classTags
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|classTags
operator|.
name|put
argument_list|(
name|type
argument_list|,
name|tag
argument_list|)
expr_stmt|;
block|}
DECL|method|isUseApplicationContextClassLoader ()
specifier|public
name|boolean
name|isUseApplicationContextClassLoader
parameter_list|()
block|{
return|return
name|useApplicationContextClassLoader
return|;
block|}
comment|/**      * Use ApplicationContextClassLoader as custom ClassLoader      */
DECL|method|setUseApplicationContextClassLoader (boolean useApplicationContextClassLoader)
specifier|public
name|void
name|setUseApplicationContextClassLoader
parameter_list|(
name|boolean
name|useApplicationContextClassLoader
parameter_list|)
block|{
name|this
operator|.
name|useApplicationContextClassLoader
operator|=
name|useApplicationContextClassLoader
expr_stmt|;
block|}
DECL|method|isPrettyFlow ()
specifier|public
name|boolean
name|isPrettyFlow
parameter_list|()
block|{
return|return
name|prettyFlow
return|;
block|}
comment|/**      * Force the emitter to produce a pretty YAML document when using the flow      * style.      */
DECL|method|setPrettyFlow (boolean prettyFlow)
specifier|public
name|void
name|setPrettyFlow
parameter_list|(
name|boolean
name|prettyFlow
parameter_list|)
block|{
name|this
operator|.
name|prettyFlow
operator|=
name|prettyFlow
expr_stmt|;
block|}
comment|/**      * Convenience method to set class tag for bot<code>Constructor</code> and      *<code>Representer</code>      */
DECL|method|addTag (Class<?> type, Tag tag)
specifier|public
name|void
name|addTag
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Tag
name|tag
parameter_list|)
block|{
name|addClassTags
argument_list|(
name|type
argument_list|,
name|tag
argument_list|)
expr_stmt|;
name|addTypeDescription
argument_list|(
name|type
argument_list|,
name|tag
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

