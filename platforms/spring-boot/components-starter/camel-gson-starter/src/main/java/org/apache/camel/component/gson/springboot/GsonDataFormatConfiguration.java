begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gson.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gson
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|gson
operator|.
name|GsonDataFormat
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
name|dataformat
operator|.
name|JsonLibrary
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
name|spring
operator|.
name|boot
operator|.
name|DataFormatConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * JSon data format is used for unmarshal a JSon payload to POJO or to marshal  * POJO back to JSon payload.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.dataformat.json-gson"
argument_list|)
DECL|class|GsonDataFormatConfiguration
specifier|public
class|class
name|GsonDataFormatConfiguration
extends|extends
name|DataFormatConfigurationPropertiesCommon
block|{
comment|/**      * Lookup and use the existing ObjectMapper with the given id when using      * Jackson.      */
DECL|field|objectMapper
specifier|private
name|String
name|objectMapper
decl_stmt|;
comment|/**      * Whether to lookup and use default Jackson ObjectMapper from the registry.      */
DECL|field|useDefaultObjectMapper
specifier|private
name|Boolean
name|useDefaultObjectMapper
init|=
literal|true
decl_stmt|;
comment|/**      * To enable pretty printing output nicely formatted. Is by default false.      */
DECL|field|prettyPrint
specifier|private
name|Boolean
name|prettyPrint
init|=
literal|false
decl_stmt|;
comment|/**      * Which json library to use.      */
DECL|field|library
specifier|private
name|JsonLibrary
name|library
init|=
name|JsonLibrary
operator|.
name|XStream
decl_stmt|;
comment|/**      * Class name of the java type to use when unarmshalling      */
DECL|field|unmarshalTypeName
specifier|private
name|String
name|unmarshalTypeName
decl_stmt|;
comment|/**      * When marshalling a POJO to JSON you might want to exclude certain fields      * from the JSON output. With Jackson you can use JSON views to accomplish      * this. This option is to refer to the class which has JsonView annotations      */
DECL|field|jsonView
specifier|private
name|Class
name|jsonView
decl_stmt|;
comment|/**      * If you want to marshal a pojo to JSON, and the pojo has some fields with      * null values. And you want to skip these null values, you can set this      * option to NOT_NULL      */
DECL|field|include
specifier|private
name|String
name|include
decl_stmt|;
comment|/**      * Used for JMS users to allow the JMSType header from the JMS spec to      * specify a FQN classname to use to unmarshal to.      */
DECL|field|allowJmsType
specifier|private
name|Boolean
name|allowJmsType
init|=
literal|false
decl_stmt|;
comment|/**      * Refers to a custom collection type to lookup in the registry to use. This      * option should rarely be used, but allows to use different collection      * types than java.util.Collection based as default.      */
DECL|field|collectionTypeName
specifier|private
name|String
name|collectionTypeName
decl_stmt|;
comment|/**      * To unarmshal to a List of Map or a List of Pojo.      */
DECL|field|useList
specifier|private
name|Boolean
name|useList
init|=
literal|false
decl_stmt|;
comment|/**      * Whether to enable the JAXB annotations module when using jackson. When      * enabled then JAXB annotations can be used by Jackson.      */
DECL|field|enableJaxbAnnotationModule
specifier|private
name|Boolean
name|enableJaxbAnnotationModule
init|=
literal|false
decl_stmt|;
comment|/**      * To use custom Jackson modules com.fasterxml.jackson.databind.Module      * specified as a String with FQN class names. Multiple classes can be      * separated by comma.      */
DECL|field|moduleClassNames
specifier|private
name|String
name|moduleClassNames
decl_stmt|;
comment|/**      * To use custom Jackson modules referred from the Camel registry. Multiple      * modules can be separated by comma.      */
DECL|field|moduleRefs
specifier|private
name|String
name|moduleRefs
decl_stmt|;
comment|/**      * Set of features to enable on the Jackson      * com.fasterxml.jackson.databind.ObjectMapper. The features should be a      * name that matches a enum from      * com.fasterxml.jackson.databind.SerializationFeature,      * com.fasterxml.jackson.databind.DeserializationFeature, or      * com.fasterxml.jackson.databind.MapperFeature Multiple features can be      * separated by comma      */
DECL|field|enableFeatures
specifier|private
name|String
name|enableFeatures
decl_stmt|;
comment|/**      * Set of features to disable on the Jackson      * com.fasterxml.jackson.databind.ObjectMapper. The features should be a      * name that matches a enum from      * com.fasterxml.jackson.databind.SerializationFeature,      * com.fasterxml.jackson.databind.DeserializationFeature, or      * com.fasterxml.jackson.databind.MapperFeature Multiple features can be      * separated by comma      */
DECL|field|disableFeatures
specifier|private
name|String
name|disableFeatures
decl_stmt|;
comment|/**      * Adds permissions that controls which Java packages and classes XStream is      * allowed to use during unmarshal from xml/json to Java beans. A permission      * must be configured either here or globally using a JVM system property.      * The permission can be specified in a syntax where a plus sign is allow,      * and minus sign is deny. Wildcards is supported by using . as prefix. For      * example to allow com.foo and all subpackages then specfy com.foo..      * Multiple permissions can be configured separated by comma, such as      * com.foo.,-com.foo.bar.MySecretBean. The following default permission is      * always included: -,java.lang.,java.util. unless its overridden by      * specifying a JVM system property with they key      * org.apache.camel.xstream.permissions.      */
DECL|field|permissions
specifier|private
name|String
name|permissions
decl_stmt|;
comment|/**      * If enabled then Jackson is allowed to attempt to use the      * CamelJacksonUnmarshalType header during the unmarshalling. This should      * only be enabled when desired to be used.      */
DECL|field|allowUnmarshallType
specifier|private
name|Boolean
name|allowUnmarshallType
init|=
literal|false
decl_stmt|;
comment|/**      * If set then Jackson will use the Timezone when marshalling/unmarshalling.      * This option will have no effect on the others Json DataFormat, like gson,      * fastjson and xstream.      */
DECL|field|timezone
specifier|private
name|String
name|timezone
decl_stmt|;
comment|/**      * Whether the data format should set the Content-Type header with the type      * from the data format if the data format is capable of doing so. For      * example application/xml for data formats marshalling to XML, or      * application/json for data formats marshalling to JSon etc.      */
DECL|field|contentTypeHeader
specifier|private
name|Boolean
name|contentTypeHeader
init|=
literal|false
decl_stmt|;
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
DECL|method|getUseDefaultObjectMapper ()
specifier|public
name|Boolean
name|getUseDefaultObjectMapper
parameter_list|()
block|{
return|return
name|useDefaultObjectMapper
return|;
block|}
DECL|method|setUseDefaultObjectMapper (Boolean useDefaultObjectMapper)
specifier|public
name|void
name|setUseDefaultObjectMapper
parameter_list|(
name|Boolean
name|useDefaultObjectMapper
parameter_list|)
block|{
name|this
operator|.
name|useDefaultObjectMapper
operator|=
name|useDefaultObjectMapper
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
DECL|method|getJsonView ()
specifier|public
name|Class
name|getJsonView
parameter_list|()
block|{
return|return
name|jsonView
return|;
block|}
DECL|method|setJsonView (Class jsonView)
specifier|public
name|void
name|setJsonView
parameter_list|(
name|Class
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
DECL|method|getAllowUnmarshallType ()
specifier|public
name|Boolean
name|getAllowUnmarshallType
parameter_list|()
block|{
return|return
name|allowUnmarshallType
return|;
block|}
DECL|method|setAllowUnmarshallType (Boolean allowUnmarshallType)
specifier|public
name|void
name|setAllowUnmarshallType
parameter_list|(
name|Boolean
name|allowUnmarshallType
parameter_list|)
block|{
name|this
operator|.
name|allowUnmarshallType
operator|=
name|allowUnmarshallType
expr_stmt|;
block|}
DECL|method|getTimezone ()
specifier|public
name|String
name|getTimezone
parameter_list|()
block|{
return|return
name|timezone
return|;
block|}
DECL|method|setTimezone (String timezone)
specifier|public
name|void
name|setTimezone
parameter_list|(
name|String
name|timezone
parameter_list|)
block|{
name|this
operator|.
name|timezone
operator|=
name|timezone
expr_stmt|;
block|}
DECL|method|getContentTypeHeader ()
specifier|public
name|Boolean
name|getContentTypeHeader
parameter_list|()
block|{
return|return
name|contentTypeHeader
return|;
block|}
DECL|method|setContentTypeHeader (Boolean contentTypeHeader)
specifier|public
name|void
name|setContentTypeHeader
parameter_list|(
name|Boolean
name|contentTypeHeader
parameter_list|)
block|{
name|this
operator|.
name|contentTypeHeader
operator|=
name|contentTypeHeader
expr_stmt|;
block|}
block|}
end_class

end_unit

