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
name|Label
import|;
end_import

begin_comment
comment|/**  * HL7 data format  *  * @version   */
end_comment

begin_class
annotation|@
name|Label
argument_list|(
literal|"dataformat,transformation,hl7"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"hl7"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|HL7DataFormat
specifier|public
class|class
name|HL7DataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
DECL|field|validate
specifier|private
name|Boolean
name|validate
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|parser
specifier|private
name|Object
name|parser
decl_stmt|;
DECL|method|HL7DataFormat ()
specifier|public
name|HL7DataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"hl7"
argument_list|)
expr_stmt|;
block|}
DECL|method|isValidate ()
specifier|public
name|boolean
name|isValidate
parameter_list|()
block|{
comment|// defaults to true if not configured
return|return
name|validate
operator|!=
literal|null
condition|?
name|validate
else|:
literal|true
return|;
block|}
DECL|method|getValidate ()
specifier|public
name|Boolean
name|getValidate
parameter_list|()
block|{
return|return
name|validate
return|;
block|}
comment|/**      * Whether to validate the HL7 message      *<p/>      * Is by default true.      */
DECL|method|setValidate (Boolean validate)
specifier|public
name|void
name|setValidate
parameter_list|(
name|Boolean
name|validate
parameter_list|)
block|{
name|this
operator|.
name|validate
operator|=
name|validate
expr_stmt|;
block|}
DECL|method|getParser ()
specifier|public
name|Object
name|getParser
parameter_list|()
block|{
return|return
name|parser
return|;
block|}
comment|/**      * To use a custom HL7 parser      */
DECL|method|setParser (Object parser)
specifier|public
name|void
name|setParser
parameter_list|(
name|Object
name|parser
parameter_list|)
block|{
name|this
operator|.
name|parser
operator|=
name|parser
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
name|getParser
argument_list|()
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
literal|"parser"
argument_list|,
name|getParser
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"validate"
argument_list|,
name|isValidate
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

