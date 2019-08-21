begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Metadata
import|;
end_import

begin_comment
comment|/**  * JacksonXML data format is used for unmarshal a XML payload to POJO or to  * marshal POJO back to XML payload.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|firstVersion
operator|=
literal|"2.16.0"
argument_list|,
name|label
operator|=
literal|"dataformat,transformation,xml"
argument_list|,
name|title
operator|=
literal|"JacksonXML"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"jacksonxml"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|JacksonXMLDataFormat
specifier|public
class|class
name|JacksonXMLDataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
DECL|field|xmlMapper
specifier|private
name|String
name|xmlMapper
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
DECL|field|allowUnmarshallType
specifier|private
name|Boolean
name|allowUnmarshallType
decl_stmt|;
DECL|method|JacksonXMLDataFormat ()
specifier|public
name|JacksonXMLDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"jacksonxml"
argument_list|)
expr_stmt|;
block|}
DECL|method|getXmlMapper ()
specifier|public
name|String
name|getXmlMapper
parameter_list|()
block|{
return|return
name|xmlMapper
return|;
block|}
comment|/**      * Lookup and use the existing XmlMapper with the given id.      */
DECL|method|setXmlMapper (String xmlMapper)
specifier|public
name|void
name|setXmlMapper
parameter_list|(
name|String
name|xmlMapper
parameter_list|)
block|{
name|this
operator|.
name|xmlMapper
operator|=
name|xmlMapper
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
comment|/**      * When marshalling a POJO to JSON you might want to exclude certain fields      * from the JSON output. With Jackson you can use JSON views to accomplish      * this. This option is to refer to the class which has @JsonView      * annotations      */
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
comment|/**      * If you want to marshal a pojo to JSON, and the pojo has some fields with      * null values. And you want to skip these null values, you can set this      * option to<tt>NON_NULL</tt>      */
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
comment|/**      * Used for JMS users to allow the JMSType header from the JMS spec to      * specify a FQN classname to use to unmarshal to.      */
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
comment|/**      * Refers to a custom collection type to lookup in the registry to use. This      * option should rarely be used, but allows to use different collection      * types than java.util.Collection based as default.      */
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
DECL|method|getCollectionType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getCollectionType
parameter_list|()
block|{
return|return
name|collectionType
return|;
block|}
DECL|method|setCollectionType (Class<?> collectionType)
specifier|public
name|void
name|setCollectionType
parameter_list|(
name|Class
argument_list|<
name|?
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
comment|/**      * Whether to enable the JAXB annotations module when using jackson. When      * enabled then JAXB annotations can be used by Jackson.      */
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
comment|/**      * To use custom Jackson modules com.fasterxml.jackson.databind.Module      * specified as a String with FQN class names. Multiple classes can be      * separated by comma.      */
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
comment|/**      * To use custom Jackson modules referred from the Camel registry. Multiple      * modules can be separated by comma.      */
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
comment|/**      * Set of features to enable on the Jackson      *<tt>com.fasterxml.jackson.databind.ObjectMapper</tt>.      *<p/>      * The features should be a name that matches a enum from      *<tt>com.fasterxml.jackson.databind.SerializationFeature</tt>,      *<tt>com.fasterxml.jackson.databind.DeserializationFeature</tt>, or      *<tt>com.fasterxml.jackson.databind.MapperFeature</tt>      *<p/>      * Multiple features can be separated by comma      */
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
comment|/**      * Set of features to disable on the Jackson      *<tt>com.fasterxml.jackson.databind.ObjectMapper</tt>.      *<p/>      * The features should be a name that matches a enum from      *<tt>com.fasterxml.jackson.databind.SerializationFeature</tt>,      *<tt>com.fasterxml.jackson.databind.DeserializationFeature</tt>, or      *<tt>com.fasterxml.jackson.databind.MapperFeature</tt>      *<p/>      * Multiple features can be separated by comma      */
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
comment|/**      * If enabled then Jackson is allowed to attempt to use the      * CamelJacksonUnmarshalType header during the unmarshalling.      *<p/>      * This should only be enabled when desired to be used.      */
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
annotation|@
name|Override
DECL|method|getDataFormatName ()
specifier|public
name|String
name|getDataFormatName
parameter_list|()
block|{
return|return
literal|"jacksonxml"
return|;
block|}
block|}
end_class

end_unit

