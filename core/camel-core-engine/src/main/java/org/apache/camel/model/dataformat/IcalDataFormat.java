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
comment|/**  * The iCal dataformat is used for working with iCalendar messages.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|firstVersion
operator|=
literal|"2.12.0"
argument_list|,
name|label
operator|=
literal|"dataformat,transformation"
argument_list|,
name|title
operator|=
literal|"iCal"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"ical"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|IcalDataFormat
specifier|public
class|class
name|IcalDataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
DECL|field|validating
specifier|private
name|Boolean
name|validating
decl_stmt|;
DECL|method|IcalDataFormat ()
specifier|public
name|IcalDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"ical"
argument_list|)
expr_stmt|;
block|}
DECL|method|getValidating ()
specifier|public
name|Boolean
name|getValidating
parameter_list|()
block|{
return|return
name|validating
return|;
block|}
comment|/**      * Whether to validate.      */
DECL|method|setValidating (Boolean validating)
specifier|public
name|void
name|setValidating
parameter_list|(
name|Boolean
name|validating
parameter_list|)
block|{
name|this
operator|.
name|validating
operator|=
name|validating
expr_stmt|;
block|}
block|}
end_class

end_unit

