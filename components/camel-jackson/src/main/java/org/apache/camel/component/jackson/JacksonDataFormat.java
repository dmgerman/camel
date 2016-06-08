begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jackson
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jackson
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
name|util
operator|.
name|ArrayList
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
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonInclude
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|DeserializationFeature
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|MapperFeature
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|Module
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|SerializationFeature
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|type
operator|.
name|CollectionType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|module
operator|.
name|jaxb
operator|.
name|JaxbAnnotationModule
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
name|CamelContextHelper
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A<a href="http://camel.apache.org/data-format.html">data format</a> ({@link DataFormat})  * using<a href="http://jackson.codehaus.org/">Jackson</a> to marshal to and from JSON.  */
end_comment

begin_class
DECL|class|JacksonDataFormat
specifier|public
class|class
name|JacksonDataFormat
extends|extends
name|ServiceSupport
implements|implements
name|DataFormat
implements|,
name|DataFormatName
implements|,
name|CamelContextAware
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JacksonDataFormat
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|objectMapper
specifier|private
name|ObjectMapper
name|objectMapper
decl_stmt|;
DECL|field|collectionType
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|Collection
argument_list|>
name|collectionType
decl_stmt|;
DECL|field|modules
specifier|private
name|List
argument_list|<
name|Module
argument_list|>
name|modules
decl_stmt|;
DECL|field|moduleClassNames
specifier|private
name|String
name|moduleClassNames
decl_stmt|;
DECL|field|moduleRefs
specifier|private
name|String
name|moduleRefs
decl_stmt|;
DECL|field|unmarshalType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
decl_stmt|;
DECL|field|jsonView
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|jsonView
decl_stmt|;
DECL|field|include
specifier|private
name|String
name|include
decl_stmt|;
DECL|field|prettyPrint
specifier|private
name|boolean
name|prettyPrint
decl_stmt|;
DECL|field|allowJmsType
specifier|private
name|boolean
name|allowJmsType
decl_stmt|;
DECL|field|useList
specifier|private
name|boolean
name|useList
decl_stmt|;
DECL|field|enableJaxbAnnotationModule
specifier|private
name|boolean
name|enableJaxbAnnotationModule
decl_stmt|;
DECL|field|enableFeatures
specifier|private
name|String
name|enableFeatures
decl_stmt|;
DECL|field|disableFeatures
specifier|private
name|String
name|disableFeatures
decl_stmt|;
DECL|field|enableJacksonTypeConverter
specifier|private
name|boolean
name|enableJacksonTypeConverter
decl_stmt|;
comment|/**      * Use the default Jackson {@link ObjectMapper} and {@link Object}      */
DECL|method|JacksonDataFormat ()
specifier|public
name|JacksonDataFormat
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
comment|/**      * Use the default Jackson {@link ObjectMapper} and with a custom      * unmarshal type      *      * @param unmarshalType the custom unmarshal type      */
DECL|method|JacksonDataFormat (Class<?> unmarshalType)
specifier|public
name|JacksonDataFormat
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
parameter_list|)
block|{
name|this
argument_list|(
name|unmarshalType
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Use the default Jackson {@link ObjectMapper} and with a custom      * unmarshal type and JSON view      *      * @param unmarshalType the custom unmarshal type      * @param jsonView marker class to specify properties to be included during marshalling.      *                 See also http://wiki.fasterxml.com/JacksonJsonViews      */
DECL|method|JacksonDataFormat (Class<?> unmarshalType, Class<?> jsonView)
specifier|public
name|JacksonDataFormat
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|jsonView
parameter_list|)
block|{
name|this
argument_list|(
name|unmarshalType
argument_list|,
name|jsonView
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Use the default Jackson {@link ObjectMapper} and with a custom      * unmarshal type and JSON view      *      * @param unmarshalType the custom unmarshal type      * @param jsonView marker class to specify properties to be included during marshalling.      *                 See also http://wiki.fasterxml.com/JacksonJsonViews      * @param enableJaxbAnnotationModule if it is true, will enable the JaxbAnnotationModule.      */
DECL|method|JacksonDataFormat (Class<?> unmarshalType, Class<?> jsonView, boolean enableJaxbAnnotationModule)
specifier|public
name|JacksonDataFormat
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|jsonView
parameter_list|,
name|boolean
name|enableJaxbAnnotationModule
parameter_list|)
block|{
name|this
operator|.
name|unmarshalType
operator|=
name|unmarshalType
expr_stmt|;
name|this
operator|.
name|jsonView
operator|=
name|jsonView
expr_stmt|;
name|this
operator|.
name|enableJaxbAnnotationModule
operator|=
name|enableJaxbAnnotationModule
expr_stmt|;
block|}
comment|/**      * Use a custom Jackson mapper and and unmarshal type      *      * @param mapper        the custom mapper      * @param unmarshalType the custom unmarshal type      */
DECL|method|JacksonDataFormat (ObjectMapper mapper, Class<?> unmarshalType)
specifier|public
name|JacksonDataFormat
parameter_list|(
name|ObjectMapper
name|mapper
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
parameter_list|)
block|{
name|this
argument_list|(
name|mapper
argument_list|,
name|unmarshalType
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Use a custom Jackson mapper, unmarshal type and JSON view      *      * @param mapper        the custom mapper      * @param unmarshalType the custom unmarshal type      * @param jsonView marker class to specify properties to be included during marshalling.      *                 See also http://wiki.fasterxml.com/JacksonJsonViews      */
DECL|method|JacksonDataFormat (ObjectMapper mapper, Class<?> unmarshalType, Class<?> jsonView)
specifier|public
name|JacksonDataFormat
parameter_list|(
name|ObjectMapper
name|mapper
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|jsonView
parameter_list|)
block|{
name|this
operator|.
name|objectMapper
operator|=
name|mapper
expr_stmt|;
name|this
operator|.
name|unmarshalType
operator|=
name|unmarshalType
expr_stmt|;
name|this
operator|.
name|jsonView
operator|=
name|jsonView
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
literal|"json-jackson"
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
DECL|method|marshal (Exchange exchange, Object graph, OutputStream stream)
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
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|objectMapper
operator|.
name|writerWithView
argument_list|(
name|jsonView
argument_list|)
operator|.
name|writeValue
argument_list|(
name|stream
argument_list|,
name|graph
argument_list|)
expr_stmt|;
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
comment|// is there a header with the unmarshal type?
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|unmarshalType
decl_stmt|;
name|String
name|type
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JacksonConstants
operator|.
name|UNMARSHAL_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
operator|&&
name|isAllowJmsType
argument_list|()
condition|)
block|{
name|type
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"JMSType"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|clazz
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|collectionType
operator|!=
literal|null
condition|)
block|{
name|CollectionType
name|collType
init|=
name|objectMapper
operator|.
name|getTypeFactory
argument_list|()
operator|.
name|constructCollectionType
argument_list|(
name|collectionType
argument_list|,
name|clazz
argument_list|)
decl_stmt|;
return|return
name|this
operator|.
name|objectMapper
operator|.
name|readValue
argument_list|(
name|stream
argument_list|,
name|collType
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|this
operator|.
name|objectMapper
operator|.
name|readValue
argument_list|(
name|stream
argument_list|,
name|clazz
argument_list|)
return|;
block|}
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getObjectMapper ()
specifier|public
name|ObjectMapper
name|getObjectMapper
parameter_list|()
block|{
return|return
name|this
operator|.
name|objectMapper
return|;
block|}
DECL|method|setObjectMapper (ObjectMapper objectMapper)
specifier|public
name|void
name|setObjectMapper
parameter_list|(
name|ObjectMapper
name|objectMapper
parameter_list|)
block|{
name|this
operator|.
name|objectMapper
operator|=
name|objectMapper
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
DECL|method|getCollectionType ()
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|Collection
argument_list|>
name|getCollectionType
parameter_list|()
block|{
return|return
name|collectionType
return|;
block|}
DECL|method|setCollectionType (Class<? extends Collection> collectionType)
specifier|public
name|void
name|setCollectionType
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Collection
argument_list|>
name|collectionType
parameter_list|)
block|{
name|this
operator|.
name|collectionType
operator|=
name|collectionType
expr_stmt|;
block|}
DECL|method|getJsonView ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getJsonView
parameter_list|()
block|{
return|return
name|jsonView
return|;
block|}
DECL|method|setJsonView (Class<?> jsonView)
specifier|public
name|void
name|setJsonView
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|jsonView
parameter_list|)
block|{
name|this
operator|.
name|jsonView
operator|=
name|jsonView
expr_stmt|;
block|}
DECL|method|getInclude ()
specifier|public
name|String
name|getInclude
parameter_list|()
block|{
return|return
name|include
return|;
block|}
DECL|method|setInclude (String include)
specifier|public
name|void
name|setInclude
parameter_list|(
name|String
name|include
parameter_list|)
block|{
name|this
operator|.
name|include
operator|=
name|include
expr_stmt|;
block|}
DECL|method|isAllowJmsType ()
specifier|public
name|boolean
name|isAllowJmsType
parameter_list|()
block|{
return|return
name|allowJmsType
return|;
block|}
DECL|method|isPrettyPrint ()
specifier|public
name|boolean
name|isPrettyPrint
parameter_list|()
block|{
return|return
name|prettyPrint
return|;
block|}
DECL|method|setPrettyPrint (boolean prettyPrint)
specifier|public
name|void
name|setPrettyPrint
parameter_list|(
name|boolean
name|prettyPrint
parameter_list|)
block|{
name|this
operator|.
name|prettyPrint
operator|=
name|prettyPrint
expr_stmt|;
block|}
DECL|method|isUseList ()
specifier|public
name|boolean
name|isUseList
parameter_list|()
block|{
return|return
name|useList
return|;
block|}
DECL|method|setUseList (boolean useList)
specifier|public
name|void
name|setUseList
parameter_list|(
name|boolean
name|useList
parameter_list|)
block|{
name|this
operator|.
name|useList
operator|=
name|useList
expr_stmt|;
block|}
DECL|method|isEnableJaxbAnnotationModule ()
specifier|public
name|boolean
name|isEnableJaxbAnnotationModule
parameter_list|()
block|{
return|return
name|enableJaxbAnnotationModule
return|;
block|}
DECL|method|setEnableJaxbAnnotationModule (boolean enableJaxbAnnotationModule)
specifier|public
name|void
name|setEnableJaxbAnnotationModule
parameter_list|(
name|boolean
name|enableJaxbAnnotationModule
parameter_list|)
block|{
name|this
operator|.
name|enableJaxbAnnotationModule
operator|=
name|enableJaxbAnnotationModule
expr_stmt|;
block|}
DECL|method|getModules ()
specifier|public
name|List
argument_list|<
name|Module
argument_list|>
name|getModules
parameter_list|()
block|{
return|return
name|modules
return|;
block|}
comment|/**      * To use custom Jackson {@link Module}s      */
DECL|method|setModules (List<Module> modules)
specifier|public
name|void
name|setModules
parameter_list|(
name|List
argument_list|<
name|Module
argument_list|>
name|modules
parameter_list|)
block|{
name|this
operator|.
name|modules
operator|=
name|modules
expr_stmt|;
block|}
DECL|method|getModuleClassNames ()
specifier|public
name|String
name|getModuleClassNames
parameter_list|()
block|{
return|return
name|moduleClassNames
return|;
block|}
comment|/**      * To use the custom Jackson module      */
DECL|method|addModule (Module module)
specifier|public
name|void
name|addModule
parameter_list|(
name|Module
name|module
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|modules
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|modules
operator|=
operator|new
name|ArrayList
argument_list|<
name|Module
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|modules
operator|.
name|add
argument_list|(
name|module
argument_list|)
expr_stmt|;
block|}
comment|/**      * To use custom Jackson {@link Module}s specified as a String with FQN class names.      * Multiple classes can be separated by comma.      */
DECL|method|setModuleClassNames (String moduleClassNames)
specifier|public
name|void
name|setModuleClassNames
parameter_list|(
name|String
name|moduleClassNames
parameter_list|)
block|{
name|this
operator|.
name|moduleClassNames
operator|=
name|moduleClassNames
expr_stmt|;
block|}
DECL|method|getModuleRefs ()
specifier|public
name|String
name|getModuleRefs
parameter_list|()
block|{
return|return
name|moduleRefs
return|;
block|}
comment|/**      * To use custom Jackson modules referred from the Camel registry.      * Multiple modules can be separated by comma.      */
DECL|method|setModuleRefs (String moduleRefs)
specifier|public
name|void
name|setModuleRefs
parameter_list|(
name|String
name|moduleRefs
parameter_list|)
block|{
name|this
operator|.
name|moduleRefs
operator|=
name|moduleRefs
expr_stmt|;
block|}
comment|/**      * Uses {@link java.util.ArrayList} when unmarshalling.      */
DECL|method|useList ()
specifier|public
name|void
name|useList
parameter_list|()
block|{
name|setCollectionType
argument_list|(
name|ArrayList
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|/**      * Uses {@link java.util.HashMap} when unmarshalling.      */
DECL|method|useMap ()
specifier|public
name|void
name|useMap
parameter_list|()
block|{
name|setCollectionType
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|setUnmarshalType
argument_list|(
name|HashMap
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|/**      * Allows jackson to use the<tt>JMSType</tt> header as an indicator what the classname is for unmarshaling json content to POJO      *<p/>      * By default this option is<tt>false</tt>.      */
DECL|method|setAllowJmsType (boolean allowJmsType)
specifier|public
name|void
name|setAllowJmsType
parameter_list|(
name|boolean
name|allowJmsType
parameter_list|)
block|{
name|this
operator|.
name|allowJmsType
operator|=
name|allowJmsType
expr_stmt|;
block|}
DECL|method|isEnableJacksonTypeConverter ()
specifier|public
name|boolean
name|isEnableJacksonTypeConverter
parameter_list|()
block|{
return|return
name|enableJacksonTypeConverter
return|;
block|}
comment|/**      * If enabled then Jackson is allowed to attempt to be used during Camels<a href="https://camel.apache.org/type-converter.html">type converter</a>      * as a {@link org.apache.camel.FallbackConverter} that attempts to convert POJOs to/from {@link Map}/{@link List} types.      *<p/>      * This should only be enabled when desired to be used.      */
DECL|method|setEnableJacksonTypeConverter (boolean enableJacksonTypeConverter)
specifier|public
name|void
name|setEnableJacksonTypeConverter
parameter_list|(
name|boolean
name|enableJacksonTypeConverter
parameter_list|)
block|{
name|this
operator|.
name|enableJacksonTypeConverter
operator|=
name|enableJacksonTypeConverter
expr_stmt|;
block|}
DECL|method|getEnableFeatures ()
specifier|public
name|String
name|getEnableFeatures
parameter_list|()
block|{
return|return
name|enableFeatures
return|;
block|}
comment|/**      * Set of features to enable on the Jackson {@link ObjectMapper}.      * The features should be a name that matches a enum from {@link SerializationFeature}, {@link DeserializationFeature}, or {@link MapperFeature}.      */
DECL|method|setEnableFeatures (String enableFeatures)
specifier|public
name|void
name|setEnableFeatures
parameter_list|(
name|String
name|enableFeatures
parameter_list|)
block|{
name|this
operator|.
name|enableFeatures
operator|=
name|enableFeatures
expr_stmt|;
block|}
DECL|method|getDisableFeatures ()
specifier|public
name|String
name|getDisableFeatures
parameter_list|()
block|{
return|return
name|disableFeatures
return|;
block|}
comment|/**      * Set of features to disable on the Jackson {@link ObjectMapper}.      * The features should be a name that matches a enum from {@link SerializationFeature}, {@link DeserializationFeature}, or {@link MapperFeature}.      */
DECL|method|setDisableFeatures (String disableFeatures)
specifier|public
name|void
name|setDisableFeatures
parameter_list|(
name|String
name|disableFeatures
parameter_list|)
block|{
name|this
operator|.
name|disableFeatures
operator|=
name|disableFeatures
expr_stmt|;
block|}
DECL|method|enableFeature (SerializationFeature feature)
specifier|public
name|void
name|enableFeature
parameter_list|(
name|SerializationFeature
name|feature
parameter_list|)
block|{
if|if
condition|(
name|enableFeatures
operator|==
literal|null
condition|)
block|{
name|enableFeatures
operator|=
name|feature
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|enableFeatures
operator|+=
literal|","
operator|+
name|feature
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|enableFeature (DeserializationFeature feature)
specifier|public
name|void
name|enableFeature
parameter_list|(
name|DeserializationFeature
name|feature
parameter_list|)
block|{
if|if
condition|(
name|enableFeatures
operator|==
literal|null
condition|)
block|{
name|enableFeatures
operator|=
name|feature
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|enableFeatures
operator|+=
literal|","
operator|+
name|feature
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|enableFeature (MapperFeature feature)
specifier|public
name|void
name|enableFeature
parameter_list|(
name|MapperFeature
name|feature
parameter_list|)
block|{
if|if
condition|(
name|enableFeatures
operator|==
literal|null
condition|)
block|{
name|enableFeatures
operator|=
name|feature
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|enableFeatures
operator|+=
literal|","
operator|+
name|feature
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|disableFeature (SerializationFeature feature)
specifier|public
name|void
name|disableFeature
parameter_list|(
name|SerializationFeature
name|feature
parameter_list|)
block|{
if|if
condition|(
name|disableFeatures
operator|==
literal|null
condition|)
block|{
name|disableFeatures
operator|=
name|feature
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|disableFeatures
operator|+=
literal|","
operator|+
name|feature
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|disableFeature (DeserializationFeature feature)
specifier|public
name|void
name|disableFeature
parameter_list|(
name|DeserializationFeature
name|feature
parameter_list|)
block|{
if|if
condition|(
name|disableFeatures
operator|==
literal|null
condition|)
block|{
name|disableFeatures
operator|=
name|feature
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|disableFeatures
operator|+=
literal|","
operator|+
name|feature
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|disableFeature (MapperFeature feature)
specifier|public
name|void
name|disableFeature
parameter_list|(
name|MapperFeature
name|feature
parameter_list|)
block|{
if|if
condition|(
name|disableFeatures
operator|==
literal|null
condition|)
block|{
name|disableFeatures
operator|=
name|feature
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|disableFeatures
operator|+=
literal|","
operator|+
name|feature
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
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
name|objectMapper
operator|==
literal|null
condition|)
block|{
name|objectMapper
operator|=
operator|new
name|ObjectMapper
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|enableJaxbAnnotationModule
condition|)
block|{
comment|// Enables JAXB processing
name|JaxbAnnotationModule
name|module
init|=
operator|new
name|JaxbAnnotationModule
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Registering JaxbAnnotationModule: {}"
argument_list|,
name|module
argument_list|)
expr_stmt|;
name|objectMapper
operator|.
name|registerModule
argument_list|(
name|module
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|useList
condition|)
block|{
name|setCollectionType
argument_list|(
name|ArrayList
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|include
operator|!=
literal|null
condition|)
block|{
name|JsonInclude
operator|.
name|Include
name|inc
init|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|JsonInclude
operator|.
name|Include
operator|.
name|class
argument_list|,
name|include
argument_list|)
decl_stmt|;
name|objectMapper
operator|.
name|setSerializationInclusion
argument_list|(
name|inc
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|prettyPrint
condition|)
block|{
name|objectMapper
operator|.
name|enable
argument_list|(
name|SerializationFeature
operator|.
name|INDENT_OUTPUT
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|enableFeatures
operator|!=
literal|null
condition|)
block|{
name|Iterator
argument_list|<
name|Object
argument_list|>
name|it
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|enableFeatures
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|enable
init|=
name|it
operator|.
name|next
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|// it can be different kind
name|SerializationFeature
name|sf
init|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|tryConvertTo
argument_list|(
name|SerializationFeature
operator|.
name|class
argument_list|,
name|enable
argument_list|)
decl_stmt|;
if|if
condition|(
name|sf
operator|!=
literal|null
condition|)
block|{
name|objectMapper
operator|.
name|enable
argument_list|(
name|sf
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|DeserializationFeature
name|df
init|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|tryConvertTo
argument_list|(
name|DeserializationFeature
operator|.
name|class
argument_list|,
name|enable
argument_list|)
decl_stmt|;
if|if
condition|(
name|df
operator|!=
literal|null
condition|)
block|{
name|objectMapper
operator|.
name|enable
argument_list|(
name|df
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|MapperFeature
name|mf
init|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|tryConvertTo
argument_list|(
name|MapperFeature
operator|.
name|class
argument_list|,
name|enable
argument_list|)
decl_stmt|;
if|if
condition|(
name|mf
operator|!=
literal|null
condition|)
block|{
name|objectMapper
operator|.
name|enable
argument_list|(
name|mf
argument_list|)
expr_stmt|;
continue|continue;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Enable feature: "
operator|+
name|enable
operator|+
literal|" cannot be converted to an accepted enum of types [SerializationFeature,DeserializationFeature,MapperFeature]"
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|disableFeatures
operator|!=
literal|null
condition|)
block|{
name|Iterator
argument_list|<
name|Object
argument_list|>
name|it
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|disableFeatures
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|disable
init|=
name|it
operator|.
name|next
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|// it can be different kind
name|SerializationFeature
name|sf
init|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|tryConvertTo
argument_list|(
name|SerializationFeature
operator|.
name|class
argument_list|,
name|disable
argument_list|)
decl_stmt|;
if|if
condition|(
name|sf
operator|!=
literal|null
condition|)
block|{
name|objectMapper
operator|.
name|disable
argument_list|(
name|sf
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|DeserializationFeature
name|df
init|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|tryConvertTo
argument_list|(
name|DeserializationFeature
operator|.
name|class
argument_list|,
name|disable
argument_list|)
decl_stmt|;
if|if
condition|(
name|df
operator|!=
literal|null
condition|)
block|{
name|objectMapper
operator|.
name|disable
argument_list|(
name|df
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|MapperFeature
name|mf
init|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|tryConvertTo
argument_list|(
name|MapperFeature
operator|.
name|class
argument_list|,
name|disable
argument_list|)
decl_stmt|;
if|if
condition|(
name|mf
operator|!=
literal|null
condition|)
block|{
name|objectMapper
operator|.
name|disable
argument_list|(
name|mf
argument_list|)
expr_stmt|;
continue|continue;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Disable feature: "
operator|+
name|disable
operator|+
literal|" cannot be converted to an accepted enum of types [SerializationFeature,DeserializationFeature,MapperFeature]"
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|modules
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Module
name|module
range|:
name|modules
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Registering module: {}"
argument_list|,
name|module
argument_list|)
expr_stmt|;
name|objectMapper
operator|.
name|registerModules
argument_list|(
name|module
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|moduleClassNames
operator|!=
literal|null
condition|)
block|{
name|Iterable
argument_list|<
name|Object
argument_list|>
name|it
init|=
name|ObjectHelper
operator|.
name|createIterable
argument_list|(
name|moduleClassNames
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|it
control|)
block|{
name|String
name|name
init|=
name|o
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|Module
argument_list|>
name|clazz
init|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|name
argument_list|,
name|Module
operator|.
name|class
argument_list|)
decl_stmt|;
name|Module
name|module
init|=
name|camelContext
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Registering module: {} -> {}"
argument_list|,
name|name
argument_list|,
name|module
argument_list|)
expr_stmt|;
name|objectMapper
operator|.
name|registerModule
argument_list|(
name|module
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|moduleRefs
operator|!=
literal|null
condition|)
block|{
name|Iterable
argument_list|<
name|Object
argument_list|>
name|it
init|=
name|ObjectHelper
operator|.
name|createIterable
argument_list|(
name|moduleRefs
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|it
control|)
block|{
name|String
name|name
init|=
name|o
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|Module
name|module
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|camelContext
argument_list|,
name|name
argument_list|,
name|Module
operator|.
name|class
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Registering module: {} -> {}"
argument_list|,
name|name
argument_list|,
name|module
argument_list|)
expr_stmt|;
name|objectMapper
operator|.
name|registerModule
argument_list|(
name|module
argument_list|)
expr_stmt|;
block|}
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
block|}
end_class

end_unit

