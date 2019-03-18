begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.tagsoup.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|tagsoup
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
comment|/**  * TidyMarkup data format is used for parsing HTML and return it as pretty  * well-formed HTML.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.dataformat.tidymarkup"
argument_list|)
DECL|class|TidyMarkupDataFormatConfiguration
specifier|public
class|class
name|TidyMarkupDataFormatConfiguration
extends|extends
name|DataFormatConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the tidyMarkup data format. This      * is enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * What data type to unmarshal as, can either be org.w3c.dom.Node or      * java.lang.String. Is by default org.w3c.dom.Node      */
DECL|field|dataObjectType
specifier|private
name|String
name|dataObjectType
init|=
literal|"org.w3c.dom.Node"
decl_stmt|;
comment|/**      * When returning a String, do we omit the XML declaration in the top.      */
DECL|field|omitXmlDeclaration
specifier|private
name|Boolean
name|omitXmlDeclaration
init|=
literal|false
decl_stmt|;
comment|/**      * Whether the data format should set the Content-Type header with the type      * from the data format if the data format is capable of doing so. For      * example application/xml for data formats marshalling to XML, or      * application/json for data formats marshalling to JSon etc.      */
DECL|field|contentTypeHeader
specifier|private
name|Boolean
name|contentTypeHeader
init|=
literal|false
decl_stmt|;
DECL|method|getDataObjectType ()
specifier|public
name|String
name|getDataObjectType
parameter_list|()
block|{
return|return
name|dataObjectType
return|;
block|}
DECL|method|setDataObjectType (String dataObjectType)
specifier|public
name|void
name|setDataObjectType
parameter_list|(
name|String
name|dataObjectType
parameter_list|)
block|{
name|this
operator|.
name|dataObjectType
operator|=
name|dataObjectType
expr_stmt|;
block|}
DECL|method|getOmitXmlDeclaration ()
specifier|public
name|Boolean
name|getOmitXmlDeclaration
parameter_list|()
block|{
return|return
name|omitXmlDeclaration
return|;
block|}
DECL|method|setOmitXmlDeclaration (Boolean omitXmlDeclaration)
specifier|public
name|void
name|setOmitXmlDeclaration
parameter_list|(
name|Boolean
name|omitXmlDeclaration
parameter_list|)
block|{
name|this
operator|.
name|omitXmlDeclaration
operator|=
name|omitXmlDeclaration
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

