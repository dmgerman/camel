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
name|XmlType
import|;
end_import

begin_comment
comment|/**  *<p>Java class for ContentType.  *<p/>  *<p>The following schema fragment specifies the expected content contained within this class.  *<p/>  *<pre>  *&lt;simpleType name="ContentType">  *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">  *&lt;enumeration value="XML"/>  *&lt;enumeration value="CSV"/>  *&lt;enumeration value="ZIP_XML"/>  *&lt;enumeration value="ZIP_CSV"/>  *&lt;/restriction>  *&lt;/simpleType>  *</pre>  */
end_comment

begin_enum
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"ContentType"
argument_list|)
annotation|@
name|XmlEnum
DECL|enum|ContentType
specifier|public
enum|enum
name|ContentType
block|{
DECL|enumConstant|XML
name|XML
block|,
DECL|enumConstant|CSV
name|CSV
block|,
DECL|enumConstant|ZIP_XML
name|ZIP_XML
block|,
DECL|enumConstant|ZIP_CSV
name|ZIP_CSV
block|;
DECL|method|value ()
specifier|public
name|String
name|value
parameter_list|()
block|{
return|return
name|name
argument_list|()
return|;
block|}
DECL|method|fromValue (String v)
specifier|public
specifier|static
name|ContentType
name|fromValue
parameter_list|(
name|String
name|v
parameter_list|)
block|{
return|return
name|valueOf
argument_list|(
name|v
argument_list|)
return|;
block|}
block|}
end_enum

end_unit

