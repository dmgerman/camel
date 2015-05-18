begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|rest
package|;
end_package

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
name|model
operator|.
name|OptionalIdentifiedDefinition
import|;
end_import

begin_comment
comment|// TODO: Should not be a Definition as its a builder for Java DSL instead.
end_comment

begin_comment
comment|// instead the builder methods should be on RestOperationParam
end_comment

begin_class
annotation|@
name|XmlTransient
DECL|class|RestParamDefinition
specifier|public
class|class
name|RestParamDefinition
extends|extends
name|OptionalIdentifiedDefinition
argument_list|<
name|RestParamDefinition
argument_list|>
block|{
DECL|field|parameter
specifier|private
name|RestOperationParam
name|parameter
init|=
operator|new
name|RestOperationParam
argument_list|()
decl_stmt|;
DECL|field|verb
specifier|private
name|VerbDefinition
name|verb
decl_stmt|;
DECL|method|RestParamDefinition (VerbDefinition verb)
specifier|public
name|RestParamDefinition
parameter_list|(
name|VerbDefinition
name|verb
parameter_list|)
block|{
name|this
operator|.
name|verb
operator|=
name|verb
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
literal|"param"
return|;
block|}
DECL|method|name (String name)
specifier|public
name|RestParamDefinition
name|name
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|parameter
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|description (String name)
specifier|public
name|RestParamDefinition
name|description
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|parameter
operator|.
name|setDescription
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|defaultValue (String name)
specifier|public
name|RestParamDefinition
name|defaultValue
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|parameter
operator|.
name|setDefaultValue
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|required (Boolean required)
specifier|public
name|RestParamDefinition
name|required
parameter_list|(
name|Boolean
name|required
parameter_list|)
block|{
name|parameter
operator|.
name|setRequired
argument_list|(
name|required
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|allowMultiple (Boolean allowMultiple)
specifier|public
name|RestParamDefinition
name|allowMultiple
parameter_list|(
name|Boolean
name|allowMultiple
parameter_list|)
block|{
name|parameter
operator|.
name|setAllowMultiple
argument_list|(
name|allowMultiple
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|dataType (String type)
specifier|public
name|RestParamDefinition
name|dataType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|parameter
operator|.
name|setDataType
argument_list|(
name|type
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|allowableValues (List<String> allowableValues)
specifier|public
name|RestParamDefinition
name|allowableValues
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|allowableValues
parameter_list|)
block|{
name|parameter
operator|.
name|setAllowableValues
argument_list|(
name|allowableValues
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|type (RestParamType type)
specifier|public
name|RestParamDefinition
name|type
parameter_list|(
name|RestParamType
name|type
parameter_list|)
block|{
name|parameter
operator|.
name|setParamType
argument_list|(
name|type
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|paramAccess (String paramAccess)
specifier|public
name|RestParamDefinition
name|paramAccess
parameter_list|(
name|String
name|paramAccess
parameter_list|)
block|{
name|parameter
operator|.
name|setParamAccess
argument_list|(
name|paramAccess
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|endParam ()
specifier|public
name|RestDefinition
name|endParam
parameter_list|()
block|{
name|verb
operator|.
name|getParams
argument_list|()
operator|.
name|add
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
return|return
name|verb
operator|.
name|getRest
argument_list|()
return|;
block|}
block|}
end_class

end_unit

