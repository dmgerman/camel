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

begin_comment
comment|/**  * Google protobuf data format  *  * @version   */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|firstVersion
operator|=
literal|"2.2.0"
argument_list|,
name|label
operator|=
literal|"dataformat,transformation"
argument_list|,
name|title
operator|=
literal|"Protobuf"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"protobuf"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ProtobufDataFormat
specifier|public
class|class
name|ProtobufDataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
DECL|field|instanceClass
specifier|private
name|String
name|instanceClass
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|contentTypeFormat
specifier|private
name|String
name|contentTypeFormat
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|defaultInstance
specifier|private
name|Object
name|defaultInstance
decl_stmt|;
DECL|method|ProtobufDataFormat ()
specifier|public
name|ProtobufDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"protobuf"
argument_list|)
expr_stmt|;
block|}
DECL|method|ProtobufDataFormat (String instanceClass)
specifier|public
name|ProtobufDataFormat
parameter_list|(
name|String
name|instanceClass
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|setInstanceClass
argument_list|(
name|instanceClass
argument_list|)
expr_stmt|;
block|}
DECL|method|ProtobufDataFormat (String instanceClass, String contentTypeFormat)
specifier|public
name|ProtobufDataFormat
parameter_list|(
name|String
name|instanceClass
parameter_list|,
name|String
name|contentTypeFormat
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|setInstanceClass
argument_list|(
name|instanceClass
argument_list|)
expr_stmt|;
name|setContentTypeFormat
argument_list|(
name|contentTypeFormat
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * Name of class to use when unarmshalling      */
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
comment|/**      * Defines a content type format in which protobuf message will be      * serialized/deserialized from(to) the Java been. It can be native protobuf      * format or json fields representation. The default value is 'native'.      */
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
DECL|method|getDefaultInstance ()
specifier|public
name|Object
name|getDefaultInstance
parameter_list|()
block|{
return|return
name|defaultInstance
return|;
block|}
DECL|method|setDefaultInstance (Object defaultInstance)
specifier|public
name|void
name|setDefaultInstance
parameter_list|(
name|Object
name|defaultInstance
parameter_list|)
block|{
name|this
operator|.
name|defaultInstance
operator|=
name|defaultInstance
expr_stmt|;
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
name|this
operator|.
name|instanceClass
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
literal|"instanceClass"
argument_list|,
name|instanceClass
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|contentTypeFormat
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
literal|"contentTypeFormat"
argument_list|,
name|contentTypeFormat
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|defaultInstance
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
literal|"defaultInstance"
argument_list|,
name|defaultInstance
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

