begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.thrift.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|thrift
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
comment|/**  * The Thrift data format is used for serialization and deserialization of  * messages using Apache Thrift binary dataformat.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.dataformat.thrift"
argument_list|)
DECL|class|ThriftDataFormatConfiguration
specifier|public
class|class
name|ThriftDataFormatConfiguration
extends|extends
name|DataFormatConfigurationPropertiesCommon
block|{
comment|/**      * Name of class to use when unarmshalling      */
DECL|field|instanceClass
specifier|private
name|String
name|instanceClass
decl_stmt|;
comment|/**      * Defines a content type format in which thrift message will be      * serialized/deserialized from(to) the Java been. The format can either be      * native or json for either native binary thrift, json or simple json      * fields representation. The default value is binary.      */
DECL|field|contentTypeFormat
specifier|private
name|String
name|contentTypeFormat
init|=
literal|"binary"
decl_stmt|;
comment|/**      * Whether the data format should set the Content-Type header with the type      * from the data format if the data format is capable of doing so. For      * example application/xml for data formats marshalling to XML, or      * application/json for data formats marshalling to JSon etc.      */
DECL|field|contentTypeHeader
specifier|private
name|Boolean
name|contentTypeHeader
init|=
literal|false
decl_stmt|;
DECL|method|getInstanceClass ()
specifier|public
name|String
name|getInstanceClass
parameter_list|()
block|{
return|return
name|instanceClass
return|;
block|}
DECL|method|setInstanceClass (String instanceClass)
specifier|public
name|void
name|setInstanceClass
parameter_list|(
name|String
name|instanceClass
parameter_list|)
block|{
name|this
operator|.
name|instanceClass
operator|=
name|instanceClass
expr_stmt|;
block|}
DECL|method|getContentTypeFormat ()
specifier|public
name|String
name|getContentTypeFormat
parameter_list|()
block|{
return|return
name|contentTypeFormat
return|;
block|}
DECL|method|setContentTypeFormat (String contentTypeFormat)
specifier|public
name|void
name|setContentTypeFormat
parameter_list|(
name|String
name|contentTypeFormat
parameter_list|)
block|{
name|this
operator|.
name|contentTypeFormat
operator|=
name|contentTypeFormat
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

