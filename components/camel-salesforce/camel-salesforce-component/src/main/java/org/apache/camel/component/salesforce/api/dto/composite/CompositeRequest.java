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
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonInclude
import|;
end_import

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
name|JsonInclude
operator|.
name|Include
import|;
end_import

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
name|JsonPropertyOrder
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
name|XStreamConverter
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
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|XStreamFieldOrder
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
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|composite
operator|.
name|SObjectComposite
operator|.
name|Method
import|;
end_import

begin_class
annotation|@
name|XStreamAlias
argument_list|(
literal|"compositeRequest"
argument_list|)
annotation|@
name|XStreamFieldOrder
argument_list|(
block|{
literal|"method"
block|,
literal|"url"
block|,
literal|"referenceId"
block|,
literal|"body"
block|}
argument_list|)
annotation|@
name|JsonInclude
argument_list|(
name|Include
operator|.
name|NON_NULL
argument_list|)
annotation|@
name|JsonPropertyOrder
argument_list|(
block|{
literal|"method"
block|,
literal|"url"
block|,
literal|"referenceId"
block|,
literal|"body"
block|}
argument_list|)
DECL|class|CompositeRequest
specifier|final
class|class
name|CompositeRequest
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
name|XStreamConverter
argument_list|(
name|RichInputConverter
operator|.
name|class
argument_list|)
DECL|field|body
specifier|private
specifier|final
name|Object
name|body
decl_stmt|;
DECL|field|method
specifier|private
specifier|final
name|Method
name|method
decl_stmt|;
DECL|field|referenceId
specifier|private
specifier|final
name|String
name|referenceId
decl_stmt|;
DECL|field|url
specifier|private
specifier|final
name|String
name|url
decl_stmt|;
DECL|method|CompositeRequest (final Method method, final String url, final Object body, final String referenceId)
name|CompositeRequest
parameter_list|(
specifier|final
name|Method
name|method
parameter_list|,
specifier|final
name|String
name|url
parameter_list|,
specifier|final
name|Object
name|body
parameter_list|,
specifier|final
name|String
name|referenceId
parameter_list|)
block|{
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
name|this
operator|.
name|body
operator|=
name|body
expr_stmt|;
name|this
operator|.
name|referenceId
operator|=
name|referenceId
expr_stmt|;
block|}
DECL|method|CompositeRequest (final Method method, final String url, final String referenceId)
name|CompositeRequest
parameter_list|(
specifier|final
name|Method
name|method
parameter_list|,
specifier|final
name|String
name|url
parameter_list|,
specifier|final
name|String
name|referenceId
parameter_list|)
block|{
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
name|this
operator|.
name|referenceId
operator|=
name|referenceId
expr_stmt|;
name|body
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|getBody ()
specifier|public
name|Object
name|getBody
parameter_list|()
block|{
return|return
name|body
return|;
block|}
DECL|method|getMethod ()
specifier|public
name|Method
name|getMethod
parameter_list|()
block|{
return|return
name|method
return|;
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
DECL|method|getUrl ()
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
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
literal|"Batch: "
operator|+
name|method
operator|+
literal|" "
operator|+
name|url
operator|+
literal|", "
operator|+
name|referenceId
operator|+
literal|", data:"
operator|+
name|body
return|;
block|}
block|}
end_class

end_unit

