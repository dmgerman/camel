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
comment|/**  * The Avro data format is used for serialization and deserialization of  * messages using Apache Avro binary dataformat.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|firstVersion
operator|=
literal|"2.14.0"
argument_list|,
name|label
operator|=
literal|"dataformat,transformation"
argument_list|,
name|title
operator|=
literal|"Avro"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"avro"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|AvroDataFormat
specifier|public
class|class
name|AvroDataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|instanceClassName
specifier|private
name|String
name|instanceClassName
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|schema
specifier|private
name|Object
name|schema
decl_stmt|;
DECL|method|AvroDataFormat ()
specifier|public
name|AvroDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"avro"
argument_list|)
expr_stmt|;
block|}
DECL|method|AvroDataFormat (String instanceClassName)
specifier|public
name|AvroDataFormat
parameter_list|(
name|String
name|instanceClassName
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|setInstanceClassName
argument_list|(
name|instanceClassName
argument_list|)
expr_stmt|;
block|}
DECL|method|getInstanceClassName ()
specifier|public
name|String
name|getInstanceClassName
parameter_list|()
block|{
return|return
name|instanceClassName
return|;
block|}
comment|/**      * Class name to use for marshal and unmarshalling      */
DECL|method|setInstanceClassName (String instanceClassName)
specifier|public
name|void
name|setInstanceClassName
parameter_list|(
name|String
name|instanceClassName
parameter_list|)
block|{
name|this
operator|.
name|instanceClassName
operator|=
name|instanceClassName
expr_stmt|;
block|}
DECL|method|getSchema ()
specifier|public
name|Object
name|getSchema
parameter_list|()
block|{
return|return
name|schema
return|;
block|}
DECL|method|setSchema (Object schema)
specifier|public
name|void
name|setSchema
parameter_list|(
name|Object
name|schema
parameter_list|)
block|{
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
block|}
block|}
end_class

end_unit

