begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jsonapi.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jsonapi
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
comment|/**  * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.dataformat.jsonapi"
argument_list|)
DECL|class|JsonApiDataFormatConfiguration
specifier|public
class|class
name|JsonApiDataFormatConfiguration
extends|extends
name|DataFormatConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the jsonApi data format. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
DECL|field|dataFormatTypes
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|dataFormatTypes
decl_stmt|;
DECL|field|mainFormatType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|mainFormatType
decl_stmt|;
comment|/**      * Whether the data format should set the Content-Type header with the type      * from the data format if the data format is capable of doing so. For      * example application/xml for data formats marshalling to XML, or      * application/json for data formats marshalling to JSon etc.      */
DECL|field|contentTypeHeader
specifier|private
name|Boolean
name|contentTypeHeader
init|=
literal|false
decl_stmt|;
DECL|method|getDataFormatTypes ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|getDataFormatTypes
parameter_list|()
block|{
return|return
name|dataFormatTypes
return|;
block|}
DECL|method|setDataFormatTypes (Class<?>[] dataFormatTypes)
specifier|public
name|void
name|setDataFormatTypes
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|dataFormatTypes
parameter_list|)
block|{
name|this
operator|.
name|dataFormatTypes
operator|=
name|dataFormatTypes
expr_stmt|;
block|}
DECL|method|getMainFormatType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getMainFormatType
parameter_list|()
block|{
return|return
name|mainFormatType
return|;
block|}
DECL|method|setMainFormatType (Class<?> mainFormatType)
specifier|public
name|void
name|setMainFormatType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|mainFormatType
parameter_list|)
block|{
name|this
operator|.
name|mainFormatType
operator|=
name|mainFormatType
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

