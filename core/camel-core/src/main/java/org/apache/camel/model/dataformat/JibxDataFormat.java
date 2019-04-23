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
comment|/**  * JiBX data format is used for unmarshal a XML payload to POJO or to marshal POJO back to XML payload.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|firstVersion
operator|=
literal|"2.6.0"
argument_list|,
name|label
operator|=
literal|"dataformat,transformation,xml"
argument_list|,
name|title
operator|=
literal|"JiBX"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"jibx"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|NONE
argument_list|)
DECL|class|JibxDataFormat
specifier|public
class|class
name|JibxDataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"unmarshallClass"
argument_list|)
DECL|field|unmarshallTypeName
specifier|private
name|String
name|unmarshallTypeName
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|bindingName
specifier|private
name|String
name|bindingName
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|unmarshallClass
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshallClass
decl_stmt|;
DECL|method|JibxDataFormat ()
specifier|public
name|JibxDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"jibx"
argument_list|)
expr_stmt|;
block|}
DECL|method|JibxDataFormat (Class<?> unmarshallClass)
specifier|public
name|JibxDataFormat
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshallClass
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|setUnmarshallClass
argument_list|(
name|unmarshallClass
argument_list|)
expr_stmt|;
block|}
DECL|method|getUnmarshallClass ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getUnmarshallClass
parameter_list|()
block|{
return|return
name|unmarshallClass
return|;
block|}
comment|/**      * Class use when unmarshalling from XML to Java.      */
DECL|method|setUnmarshallClass (Class<?> unmarshallClass)
specifier|public
name|void
name|setUnmarshallClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshallClass
parameter_list|)
block|{
name|this
operator|.
name|unmarshallClass
operator|=
name|unmarshallClass
expr_stmt|;
block|}
DECL|method|getUnmarshallTypeName ()
specifier|public
name|String
name|getUnmarshallTypeName
parameter_list|()
block|{
return|return
name|unmarshallTypeName
return|;
block|}
comment|/**      * Class name to use when unmarshalling from XML to Java.      */
DECL|method|setUnmarshallTypeName (String unmarshallTypeName)
specifier|public
name|void
name|setUnmarshallTypeName
parameter_list|(
name|String
name|unmarshallTypeName
parameter_list|)
block|{
name|this
operator|.
name|unmarshallTypeName
operator|=
name|unmarshallTypeName
expr_stmt|;
block|}
DECL|method|getBindingName ()
specifier|public
name|String
name|getBindingName
parameter_list|()
block|{
return|return
name|bindingName
return|;
block|}
comment|/**      * To use a custom binding factory      */
DECL|method|setBindingName (String bindingName)
specifier|public
name|void
name|setBindingName
parameter_list|(
name|String
name|bindingName
parameter_list|)
block|{
name|this
operator|.
name|bindingName
operator|=
name|bindingName
expr_stmt|;
block|}
block|}
end_class

end_unit

