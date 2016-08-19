begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.dataformat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|dataformat
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|model
operator|.
name|DataFormatDefinition
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
name|Metadata
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
name|RouteContext
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
name|CollectionStringBuffer
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
comment|/**  * JSon data format  *  * @version   */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"dataformat,transformation,json"
argument_list|,
name|title
operator|=
literal|"JSon"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"json"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|JsonDataFormat
specifier|public
class|class
name|JsonDataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
DECL|field|objectMapper
specifier|private
name|String
name|objectMapper
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|prettyPrint
specifier|private
name|Boolean
name|prettyPrint
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"XStream"
argument_list|)
DECL|field|library
specifier|private
name|JsonLibrary
name|library
init|=
name|JsonLibrary
operator|.
name|XStream
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|unmarshalTypeName
specifier|private
name|String
name|unmarshalTypeName
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|unmarshalType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|jsonView
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|jsonView
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|include
specifier|private
name|String
name|include
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|allowJmsType
specifier|private
name|Boolean
name|allowJmsType
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|collectionTypeName
specifier|private
name|String
name|collectionTypeName
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|collectionType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|collectionType
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|useList
specifier|private
name|Boolean
name|useList
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|enableJaxbAnnotationModule
specifier|private
name|Boolean
name|enableJaxbAnnotationModule
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|moduleClassNames
specifier|private
name|String
name|moduleClassNames
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|moduleRefs
specifier|private
name|String
name|moduleRefs
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|enableFeatures
specifier|private
name|String
name|enableFeatures
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|disableFeatures
specifier|private
name|String
name|disableFeatures
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|permissions
specifier|private
name|String
name|permissions
decl_stmt|;
DECL|method|JsonDataFormat ()
specifier|public
name|JsonDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"json"
argument_list|)
expr_stmt|;
block|}
DECL|method|JsonDataFormat (JsonLibrary library)
specifier|public
name|JsonDataFormat
parameter_list|(
name|JsonLibrary
name|library
parameter_list|)
block|{
name|this
operator|.
name|library
operator|=
name|library
expr_stmt|;
block|}
DECL|method|getObjectMapper ()
specifier|public
name|String
name|getObjectMapper
parameter_list|()
block|{
return|return
name|objectMapper
return|;
block|}
comment|/**      * Lookup and use the existing ObjectMapper with the given id when using Jackson.      */
DECL|method|setObjectMapper (String objectMapper)
specifier|public
name|void
name|setObjectMapper
parameter_list|(
name|String
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
DECL|method|getPrettyPrint ()
specifier|public
name|Boolean
name|getPrettyPrint
parameter_list|()
block|{
return|return
name|prettyPrint
return|;
block|}
comment|/**      * To enable pretty printing output nicely formatted.      *<p/>      * Is by default false.      */
DECL|method|setPrettyPrint (Boolean prettyPrint)
specifier|public
name|void
name|setPrettyPrint
parameter_list|(
name|Boolean
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
DECL|method|getUnmarshalTypeName ()
specifier|public
name|String
name|getUnmarshalTypeName
parameter_list|()
block|{
return|return
name|unmarshalTypeName
return|;
block|}
comment|/**      * Class name of the java type to use when unarmshalling      */
DECL|method|setUnmarshalTypeName (String unmarshalTypeName)
specifier|public
name|void
name|setUnmarshalTypeName
parameter_list|(
name|String
name|unmarshalTypeName
parameter_list|)
block|{
name|this
operator|.
name|unmarshalTypeName
operator|=
name|unmarshalTypeName
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
name|unmarshalType
return|;
block|}
comment|/**      * Class of the java type to use when unarmshalling      */
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
DECL|method|getLibrary ()
specifier|public
name|JsonLibrary
name|getLibrary
parameter_list|()
block|{
return|return
name|library
return|;
block|}
comment|/**      * Which json library to use.      */
DECL|method|setLibrary (JsonLibrary library)
specifier|public
name|void
name|setLibrary
parameter_list|(
name|JsonLibrary
name|library
parameter_list|)
block|{
name|this
operator|.
name|library
operator|=
name|library
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
comment|/**      * When marshalling a POJO to JSON you might want to exclude certain fields from the JSON output.      * With Jackson you can use JSON views to accomplish this. This option is to refer to the class      * which has @JsonView annotations      */
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
comment|/**      * If you want to marshal a pojo to JSON, and the pojo has some fields with null values.      * And you want to skip these null values, you can set this option to<tt>NOT_NULL</tt>      */
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
DECL|method|getAllowJmsType ()
specifier|public
name|Boolean
name|getAllowJmsType
parameter_list|()
block|{
return|return
name|allowJmsType
return|;
block|}
comment|/**      * Used for JMS users to allow the JMSType header from the JMS spec to specify a FQN classname      * to use to unmarshal to.      */
DECL|method|setAllowJmsType (Boolean allowJmsType)
specifier|public
name|void
name|setAllowJmsType
parameter_list|(
name|Boolean
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
DECL|method|getCollectionTypeName ()
specifier|public
name|String
name|getCollectionTypeName
parameter_list|()
block|{
return|return
name|collectionTypeName
return|;
block|}
comment|/**      * Refers to a custom collection type to lookup in the registry to use. This option should rarely be used, but allows      * to use different collection types than java.util.Collection based as default.      */
DECL|method|setCollectionTypeName (String collectionTypeName)
specifier|public
name|void
name|setCollectionTypeName
parameter_list|(
name|String
name|collectionTypeName
parameter_list|)
block|{
name|this
operator|.
name|collectionTypeName
operator|=
name|collectionTypeName
expr_stmt|;
block|}
DECL|method|getUseList ()
specifier|public
name|Boolean
name|getUseList
parameter_list|()
block|{
return|return
name|useList
return|;
block|}
comment|/**      * To unarmshal to a List of Map or a List of Pojo.      */
DECL|method|setUseList (Boolean useList)
specifier|public
name|void
name|setUseList
parameter_list|(
name|Boolean
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
DECL|method|getEnableJaxbAnnotationModule ()
specifier|public
name|Boolean
name|getEnableJaxbAnnotationModule
parameter_list|()
block|{
return|return
name|enableJaxbAnnotationModule
return|;
block|}
comment|/**      * Whether to enable the JAXB annotations module when using jackson. When enabled then JAXB annotations      * can be used by Jackson.      */
DECL|method|setEnableJaxbAnnotationModule (Boolean enableJaxbAnnotationModule)
specifier|public
name|void
name|setEnableJaxbAnnotationModule
parameter_list|(
name|Boolean
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
comment|/**      * To use custom Jackson modules com.fasterxml.jackson.databind.Module specified as a String with FQN class names.      * Multiple classes can be separated by comma.      */
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
comment|/**      * Set of features to enable on the Jackson<tt>com.fasterxml.jackson.databind.ObjectMapper</tt>.      *<p/>      * The features should be a name that matches a enum from<tt>com.fasterxml.jackson.databind.SerializationFeature</tt>,      *<tt>com.fasterxml.jackson.databind.DeserializationFeature</tt>, or<tt>com.fasterxml.jackson.databind.MapperFeature</tt>      *<p/>      * Multiple features can be separated by comma      */
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
comment|/**      * Set of features to disable on the Jackson<tt>com.fasterxml.jackson.databind.ObjectMapper</tt>.      *<p/>      * The features should be a name that matches a enum from<tt>com.fasterxml.jackson.databind.SerializationFeature</tt>,      *<tt>com.fasterxml.jackson.databind.DeserializationFeature</tt>, or<tt>com.fasterxml.jackson.databind.MapperFeature</tt>      *<p/>      * Multiple features can be separated by comma      */
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
DECL|method|getPermissions ()
specifier|public
name|String
name|getPermissions
parameter_list|()
block|{
return|return
name|permissions
return|;
block|}
comment|/**      * Adds permissions that controls which Java packages and classes XStream is allowed to use during      * unmarshal from xml/json to Java beans.      *<p/>      * A permission must be configured either here or globally using a JVM system property. The permission      * can be specified in a syntax where a plus sign is allow, and minus sign is deny.      *<br/>      * Wildcards is supported by using<tt>.*</tt> as prefix. For example to allow<tt>com.foo</tt> and all subpackages      * then specfy<tt>+com.foo.*</tt>. Multiple permissions can be configured separated by comma, such as      *<tt>+com.foo.*,-com.foo.bar.MySecretBean</tt>.      *<br/>      * The following default permission is always included:<tt>"-*,java.lang.*,java.util.*"</tt> unless      * its overridden by specifying a JVM system property with they key<tt>org.apache.camel.xstream.permissions</tt>.      */
DECL|method|setPermissions (String permissions)
specifier|public
name|void
name|setPermissions
parameter_list|(
name|String
name|permissions
parameter_list|)
block|{
name|this
operator|.
name|permissions
operator|=
name|permissions
expr_stmt|;
block|}
comment|/**      * To add permission for the given pojo classes.      * @param type the pojo class(es) xstream should use as allowed permission      * @see #setPermissions(String)      */
DECL|method|setPermissions (Class<?>.... type)
specifier|public
name|void
name|setPermissions
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|type
parameter_list|)
block|{
name|CollectionStringBuffer
name|csb
init|=
operator|new
name|CollectionStringBuffer
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
range|:
name|type
control|)
block|{
name|csb
operator|.
name|append
argument_list|(
literal|"+"
argument_list|)
expr_stmt|;
name|csb
operator|.
name|append
argument_list|(
name|clazz
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|setPermissions
argument_list|(
name|csb
operator|.
name|toString
argument_list|()
argument_list|)
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
comment|// json data format is special as the name can be from different bundles
return|return
literal|"json-"
operator|+
name|library
operator|.
name|name
argument_list|()
operator|.
name|toLowerCase
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createDataFormat (RouteContext routeContext)
specifier|protected
name|DataFormat
name|createDataFormat
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
if|if
condition|(
name|library
operator|==
name|JsonLibrary
operator|.
name|XStream
condition|)
block|{
name|setProperty
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|this
argument_list|,
literal|"dataFormatName"
argument_list|,
literal|"json-xstream"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|library
operator|==
name|JsonLibrary
operator|.
name|Jackson
condition|)
block|{
name|setProperty
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|this
argument_list|,
literal|"dataFormatName"
argument_list|,
literal|"json-jackson"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|library
operator|==
name|JsonLibrary
operator|.
name|Gson
condition|)
block|{
name|setProperty
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|this
argument_list|,
literal|"dataFormatName"
argument_list|,
literal|"json-gson"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setProperty
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|this
argument_list|,
literal|"dataFormatName"
argument_list|,
literal|"json-johnzon"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|unmarshalType
operator|==
literal|null
operator|&&
name|unmarshalTypeName
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|unmarshalType
operator|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|unmarshalTypeName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
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
if|if
condition|(
name|collectionType
operator|==
literal|null
operator|&&
name|collectionTypeName
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|collectionType
operator|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|collectionTypeName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
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
return|return
name|super
operator|.
name|createDataFormat
argument_list|(
name|routeContext
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|configureDataFormat (DataFormat dataFormat, CamelContext camelContext)
specifier|protected
name|void
name|configureDataFormat
parameter_list|(
name|DataFormat
name|dataFormat
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
if|if
condition|(
name|objectMapper
operator|!=
literal|null
condition|)
block|{
comment|// must be a reference value
name|String
name|ref
init|=
name|objectMapper
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|?
name|objectMapper
else|:
literal|"#"
operator|+
name|objectMapper
decl_stmt|;
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"objectMapper"
argument_list|,
name|ref
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|unmarshalType
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"unmarshalType"
argument_list|,
name|unmarshalType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|prettyPrint
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"prettyPrint"
argument_list|,
name|prettyPrint
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jsonView
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"jsonView"
argument_list|,
name|jsonView
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
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"include"
argument_list|,
name|include
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|allowJmsType
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"allowJmsType"
argument_list|,
name|allowJmsType
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
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"collectionType"
argument_list|,
name|collectionType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|useList
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"useList"
argument_list|,
name|useList
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|enableJaxbAnnotationModule
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"enableJaxbAnnotationModule"
argument_list|,
name|enableJaxbAnnotationModule
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|moduleClassNames
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"moduleClassNames"
argument_list|,
name|moduleClassNames
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|moduleRefs
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"moduleRefs"
argument_list|,
name|moduleRefs
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
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"enableFeatures"
argument_list|,
name|enableFeatures
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|disableFeatures
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"disableFeatures"
argument_list|,
name|disableFeatures
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|permissions
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"permissions"
argument_list|,
name|permissions
argument_list|)
expr_stmt|;
block|}
comment|// if we have the unmarshal type, but no permission set, then use it to be allowed
if|if
condition|(
name|permissions
operator|==
literal|null
operator|&&
name|unmarshalType
operator|!=
literal|null
condition|)
block|{
name|String
name|allow
init|=
literal|"+"
operator|+
name|unmarshalType
operator|.
name|getName
argument_list|()
decl_stmt|;
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"permissions"
argument_list|,
name|allow
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

