begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.csv.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|csv
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
name|model
operator|.
name|dataformat
operator|.
name|BindyType
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
comment|/**  * The Bindy data format is used for working with flat payloads (such as CSV,  * delimited, fixed length formats, or FIX messages).  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.dataformat.bindy-csv"
argument_list|)
DECL|class|BindyCsvDataFormatConfiguration
specifier|public
class|class
name|BindyCsvDataFormatConfiguration
extends|extends
name|DataFormatConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the bindy-csv data format. This      * is enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * Whether to use csv, fixed or key value pairs mode.      */
DECL|field|type
specifier|private
name|BindyType
name|type
decl_stmt|;
comment|/**      * Name of model class to use.      */
DECL|field|classType
specifier|private
name|String
name|classType
decl_stmt|;
comment|/**      * To configure a default locale to use, such as us for united states. To      * use the JVM platform default locale then use the name default      */
DECL|field|locale
specifier|private
name|String
name|locale
decl_stmt|;
comment|/**      * When unmarshalling should a single instance be unwrapped and returned      * instead of wrapped in a java.util.List.      */
DECL|field|unwrapSingleInstance
specifier|private
name|Boolean
name|unwrapSingleInstance
init|=
literal|true
decl_stmt|;
comment|/**      * Whether the data format should set the Content-Type header with the type      * from the data format if the data format is capable of doing so. For      * example application/xml for data formats marshalling to XML, or      * application/json for data formats marshalling to JSon etc.      */
DECL|field|contentTypeHeader
specifier|private
name|Boolean
name|contentTypeHeader
init|=
literal|false
decl_stmt|;
DECL|method|getType ()
specifier|public
name|BindyType
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|setType (BindyType type)
specifier|public
name|void
name|setType
parameter_list|(
name|BindyType
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getClassType ()
specifier|public
name|String
name|getClassType
parameter_list|()
block|{
return|return
name|classType
return|;
block|}
DECL|method|setClassType (String classType)
specifier|public
name|void
name|setClassType
parameter_list|(
name|String
name|classType
parameter_list|)
block|{
name|this
operator|.
name|classType
operator|=
name|classType
expr_stmt|;
block|}
DECL|method|getLocale ()
specifier|public
name|String
name|getLocale
parameter_list|()
block|{
return|return
name|locale
return|;
block|}
DECL|method|setLocale (String locale)
specifier|public
name|void
name|setLocale
parameter_list|(
name|String
name|locale
parameter_list|)
block|{
name|this
operator|.
name|locale
operator|=
name|locale
expr_stmt|;
block|}
DECL|method|getUnwrapSingleInstance ()
specifier|public
name|Boolean
name|getUnwrapSingleInstance
parameter_list|()
block|{
return|return
name|unwrapSingleInstance
return|;
block|}
DECL|method|setUnwrapSingleInstance (Boolean unwrapSingleInstance)
specifier|public
name|void
name|setUnwrapSingleInstance
parameter_list|(
name|Boolean
name|unwrapSingleInstance
parameter_list|)
block|{
name|this
operator|.
name|unwrapSingleInstance
operator|=
name|unwrapSingleInstance
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

