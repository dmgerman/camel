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

begin_comment
comment|/**  *<p>Java class for QueryResultList complex type.  *   *<p>The following schema fragment specifies the expected content contained within this class.  *   *<pre>  *&lt;complexType name="QueryResultList">  *&lt;complexContent>  *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">  *&lt;sequence>  *&lt;element name="result" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>  *&lt;/sequence>  *&lt;/restriction>  *&lt;/complexContent>  *&lt;/complexType>  *</pre>  *   *   */
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
literal|"QueryResultList"
argument_list|,
name|propOrder
operator|=
block|{
literal|"result"
block|}
argument_list|)
DECL|class|QueryResultList
specifier|public
class|class
name|QueryResultList
block|{
DECL|field|result
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|result
decl_stmt|;
comment|/**      * Gets the value of the result property.      *       *<p>      * This accessor method returns a reference to the live list,      * not a snapshot. Therefore any modification you make to the      * returned list will be present inside the JAXB object.      * This is why there is not a<CODE>set</CODE> method for the result property.      *       *<p>      * For example, to add a new item, do as follows:      *<pre>      *    getResult().add(newItem);      *</pre>      *       *       *<p>      * Objects of the following type(s) are allowed in the list      * {@link String }      *       *       */
DECL|method|getResult ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getResult
parameter_list|()
block|{
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|result
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|result
return|;
block|}
block|}
end_class

end_unit

