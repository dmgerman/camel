begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  *<p>Java class for OperationEnum.  *   *<p>The following schema fragment specifies the expected content contained within this class.  *<p>  *<pre>  *&lt;simpleType name="OperationEnum">  *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">  *&lt;enumeration value="insert"/>  *&lt;enumeration value="upsert"/>  *&lt;enumeration value="update"/>  *&lt;enumeration value="delete"/>  *&lt;enumeration value="hardDelete"/>  *&lt;enumeration value="query"/>  *&lt;/restriction>  *&lt;/simpleType>  *</pre>  *   */
end_comment

begin_enum
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"OperationEnum"
argument_list|)
annotation|@
name|XmlEnum
DECL|enum|OperationEnum
specifier|public
enum|enum
name|OperationEnum
block|{
DECL|enumConstant|XmlEnumValue
annotation|@
name|XmlEnumValue
argument_list|(
literal|"insert"
argument_list|)
DECL|enumConstant|INSERT
name|INSERT
argument_list|(
literal|"insert"
argument_list|)
block|,
DECL|enumConstant|XmlEnumValue
annotation|@
name|XmlEnumValue
argument_list|(
literal|"upsert"
argument_list|)
DECL|enumConstant|UPSERT
name|UPSERT
argument_list|(
literal|"upsert"
argument_list|)
block|,
DECL|enumConstant|XmlEnumValue
annotation|@
name|XmlEnumValue
argument_list|(
literal|"update"
argument_list|)
DECL|enumConstant|UPDATE
name|UPDATE
argument_list|(
literal|"update"
argument_list|)
block|,
DECL|enumConstant|XmlEnumValue
annotation|@
name|XmlEnumValue
argument_list|(
literal|"delete"
argument_list|)
DECL|enumConstant|DELETE
name|DELETE
argument_list|(
literal|"delete"
argument_list|)
block|,
DECL|enumConstant|XmlEnumValue
annotation|@
name|XmlEnumValue
argument_list|(
literal|"hardDelete"
argument_list|)
DECL|enumConstant|HARD_DELETE
name|HARD_DELETE
argument_list|(
literal|"hardDelete"
argument_list|)
block|,
DECL|enumConstant|XmlEnumValue
annotation|@
name|XmlEnumValue
argument_list|(
literal|"query"
argument_list|)
DECL|enumConstant|QUERY
name|QUERY
argument_list|(
literal|"query"
argument_list|)
decl_stmt|;
DECL|field|value
specifier|private
specifier|final
name|String
name|value
decl_stmt|;
DECL|method|OperationEnum (String v)
name|OperationEnum
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
name|OperationEnum
name|fromValue
parameter_list|(
name|String
name|v
parameter_list|)
block|{
for|for
control|(
name|OperationEnum
name|c
range|:
name|OperationEnum
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

