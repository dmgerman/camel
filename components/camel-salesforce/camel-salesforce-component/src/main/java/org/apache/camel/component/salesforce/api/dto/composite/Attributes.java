begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.dto.composite
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
name|composite
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
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
name|XStreamAsAttribute
import|;
end_import

begin_comment
comment|/**  * Holds {@code type} and {@code referrenceId} attributes needed for the SObject tree Composite API.  */
end_comment

begin_class
DECL|class|Attributes
specifier|final
class|class
name|Attributes
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|XStreamAsAttribute
DECL|field|type
specifier|final
name|String
name|type
decl_stmt|;
annotation|@
name|XStreamAsAttribute
DECL|field|referenceId
specifier|final
name|String
name|referenceId
decl_stmt|;
DECL|method|Attributes (String referenceId, final String type)
name|Attributes
parameter_list|(
name|String
name|referenceId
parameter_list|,
specifier|final
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|type
argument_list|,
literal|"Type must be specified"
argument_list|)
expr_stmt|;
name|this
operator|.
name|referenceId
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|referenceId
argument_list|,
literal|"Reference ID must be specified"
argument_list|)
expr_stmt|;
block|}
DECL|method|getReferenceId ()
specifier|public
name|String
name|getReferenceId
parameter_list|()
block|{
return|return
name|referenceId
return|;
block|}
DECL|method|getType ()
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Attribute<type: "
operator|+
name|type
operator|+
literal|", referenceId: "
operator|+
name|referenceId
operator|+
literal|">"
return|;
block|}
block|}
end_class

end_unit

