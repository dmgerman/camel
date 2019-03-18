begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.dto.bulk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|bulk
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
name|XmlEnum
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
name|XmlEnumValue
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
name|XmlType
import|;
end_import

begin_comment
comment|/**  *<p>Java class for BatchStateEnum.  *<p/>  *<p>The following schema fragment specifies the expected content contained within this class.  *<p/>  *<pre>  *&lt;simpleType name="BatchStateEnum">  *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">  *&lt;enumeration value="Queued"/>  *&lt;enumeration value="InProgress"/>  *&lt;enumeration value="Completed"/>  *&lt;enumeration value="Failed"/>  *&lt;enumeration value="NotProcessed"/>  *&lt;/restriction>  *&lt;/simpleType>  *</pre>  */
end_comment

begin_enum
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"BatchStateEnum"
argument_list|)
annotation|@
name|XmlEnum
DECL|enum|BatchStateEnum
specifier|public
enum|enum
name|BatchStateEnum
block|{
DECL|enumConstant|XmlEnumValue
annotation|@
name|XmlEnumValue
argument_list|(
literal|"Queued"
argument_list|)
DECL|enumConstant|QUEUED
name|QUEUED
argument_list|(
literal|"Queued"
argument_list|)
block|,
DECL|enumConstant|XmlEnumValue
annotation|@
name|XmlEnumValue
argument_list|(
literal|"InProgress"
argument_list|)
DECL|enumConstant|IN_PROGRESS
name|IN_PROGRESS
argument_list|(
literal|"InProgress"
argument_list|)
block|,
DECL|enumConstant|XmlEnumValue
annotation|@
name|XmlEnumValue
argument_list|(
literal|"Completed"
argument_list|)
DECL|enumConstant|COMPLETED
name|COMPLETED
argument_list|(
literal|"Completed"
argument_list|)
block|,
DECL|enumConstant|XmlEnumValue
annotation|@
name|XmlEnumValue
argument_list|(
literal|"Failed"
argument_list|)
DECL|enumConstant|FAILED
name|FAILED
argument_list|(
literal|"Failed"
argument_list|)
block|,
DECL|enumConstant|XmlEnumValue
annotation|@
name|XmlEnumValue
argument_list|(
literal|"NotProcessed"
argument_list|)
DECL|enumConstant|NOT_PROCESSED
name|NOT_PROCESSED
argument_list|(
literal|"NotProcessed"
argument_list|)
decl_stmt|;
DECL|field|value
specifier|private
specifier|final
name|String
name|value
decl_stmt|;
DECL|method|BatchStateEnum (String v)
name|BatchStateEnum
parameter_list|(
name|String
name|v
parameter_list|)
block|{
name|value
operator|=
name|v
expr_stmt|;
block|}
DECL|method|value ()
specifier|public
name|String
name|value
parameter_list|()
block|{
return|return
name|value
return|;
block|}
DECL|method|fromValue (String v)
specifier|public
specifier|static
name|BatchStateEnum
name|fromValue
parameter_list|(
name|String
name|v
parameter_list|)
block|{
for|for
control|(
name|BatchStateEnum
name|c
range|:
name|BatchStateEnum
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|c
operator|.
name|value
operator|.
name|equals
argument_list|(
name|v
argument_list|)
condition|)
block|{
return|return
name|c
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|v
argument_list|)
throw|;
block|}
block|}
end_enum

end_unit

