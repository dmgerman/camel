begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.dto
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
package|;
end_package

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonProperty
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|annotations
operator|.
name|XStreamAlias
import|;
end_import

begin_comment
comment|/**  * DTO for Salesforce SOSL Search result record.  */
end_comment

begin_class
annotation|@
name|XStreamAlias
argument_list|(
literal|"SearchResult"
argument_list|)
comment|//CHECKSTYLE:OFF
DECL|class|SearchResult
specifier|public
specifier|final
class|class
name|SearchResult
extends|extends
name|AbstractDTOBase
block|{
comment|// WARNING: these fields have case sensitive names,
comment|// the field name MUST match the field name used by Salesforce
comment|// DO NOT change these field names to camel case!!!
DECL|field|attributes
specifier|private
name|Attributes
name|attributes
decl_stmt|;
DECL|field|Id
specifier|private
name|String
name|Id
decl_stmt|;
DECL|method|getAttributes ()
specifier|public
name|Attributes
name|getAttributes
parameter_list|()
block|{
return|return
name|attributes
return|;
block|}
DECL|method|setAttributes (Attributes attributes)
specifier|public
name|void
name|setAttributes
parameter_list|(
name|Attributes
name|attributes
parameter_list|)
block|{
name|this
operator|.
name|attributes
operator|=
name|attributes
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Id"
argument_list|)
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|Id
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Id"
argument_list|)
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|Id
operator|=
name|id
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|//CHECKSTYLE:ON
end_comment

end_unit

