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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|XmlType
import|;
end_import

begin_comment
comment|/**  *<p>Java class for BatchInfoList complex type.  *<p/>  *<p>The following schema fragment specifies the expected content contained within this class.  *<p/>  *<pre>  *&lt;complexType name="BatchInfoList">  *&lt;complexContent>  *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">  *&lt;sequence>  *&lt;element name="batchInfo" type="{http://www.force.com/2009/06/asyncapi/dataload}BatchInfo" maxOccurs="unbounded" minOccurs="0"/>  *&lt;/sequence>  *&lt;/restriction>  *&lt;/complexContent>  *&lt;/complexType>  *</pre>  */
end_comment

begin_class
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"BatchInfoList"
argument_list|,
name|propOrder
operator|=
block|{
literal|"batchInfo"
block|}
argument_list|)
DECL|class|BatchInfoList
specifier|public
class|class
name|BatchInfoList
block|{
DECL|field|batchInfo
specifier|protected
name|List
argument_list|<
name|BatchInfo
argument_list|>
name|batchInfo
decl_stmt|;
comment|/**      * Gets the value of the batchInfo property.      *<p/>      *<p/>      * This accessor method returns a reference to the live list,      * not a snapshot. Therefore any modification you make to the      * returned list will be present inside the JAXB object.      * This is why there is not a<CODE>set</CODE> method for the batchInfo property.      *<p/>      *<p/>      * For example, to add a new item, do as follows:      *<pre>      *    getBatchInfo().add(newItem);      *</pre>      *<p/>      *<p/>      *<p/>      * Objects of the following type(s) are allowed in the list      * {@link BatchInfo }      */
DECL|method|getBatchInfo ()
specifier|public
name|List
argument_list|<
name|BatchInfo
argument_list|>
name|getBatchInfo
parameter_list|()
block|{
if|if
condition|(
name|batchInfo
operator|==
literal|null
condition|)
block|{
name|batchInfo
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|batchInfo
return|;
block|}
block|}
end_class

end_unit

