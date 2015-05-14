begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|*
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
comment|/**  * Created by seb on 5/13/15.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"rest"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"param"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|RestOperationParam
specifier|public
class|class
name|RestOperationParam
block|{
annotation|@
name|XmlAttribute
DECL|field|paramType
name|String
name|paramType
init|=
literal|"query"
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|name
name|String
name|name
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|description
name|String
name|description
init|=
literal|""
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|defaultValue
name|String
name|defaultValue
init|=
literal|""
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|required
name|Boolean
name|required
init|=
literal|true
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|allowMultiple
name|Boolean
name|allowMultiple
init|=
literal|false
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|dataType
name|String
name|dataType
init|=
literal|"string"
decl_stmt|;
annotation|@
name|XmlElementWrapper
argument_list|(
name|name
operator|=
literal|"allowableValues"
argument_list|)
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"value"
argument_list|)
DECL|field|allowableValues
name|List
argument_list|<
name|String
argument_list|>
name|allowableValues
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|paramAccess
name|String
name|paramAccess
init|=
literal|null
decl_stmt|;
DECL|method|RestOperationParam ()
specifier|public
name|RestOperationParam
parameter_list|()
block|{          }
DECL|method|getParamType ()
specifier|public
name|String
name|getParamType
parameter_list|()
block|{
return|return
name|paramType
return|;
block|}
DECL|method|setParamType (String paramType)
specifier|public
name|void
name|setParamType
parameter_list|(
name|String
name|paramType
parameter_list|)
block|{
name|this
operator|.
name|paramType
operator|=
name|paramType
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
DECL|method|setDescription (String description)
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
DECL|method|getDefaultValue ()
specifier|public
name|String
name|getDefaultValue
parameter_list|()
block|{
return|return
name|defaultValue
return|;
block|}
DECL|method|setDefaultValue (String defaultValue)
specifier|public
name|void
name|setDefaultValue
parameter_list|(
name|String
name|defaultValue
parameter_list|)
block|{
name|this
operator|.
name|defaultValue
operator|=
name|defaultValue
expr_stmt|;
block|}
DECL|method|getRequired ()
specifier|public
name|Boolean
name|getRequired
parameter_list|()
block|{
return|return
name|required
return|;
block|}
DECL|method|setRequired (Boolean required)
specifier|public
name|void
name|setRequired
parameter_list|(
name|Boolean
name|required
parameter_list|)
block|{
name|this
operator|.
name|required
operator|=
name|required
expr_stmt|;
block|}
DECL|method|getAllowMultiple ()
specifier|public
name|Boolean
name|getAllowMultiple
parameter_list|()
block|{
return|return
name|allowMultiple
return|;
block|}
DECL|method|setAllowMultiple (Boolean allowMultiple)
specifier|public
name|void
name|setAllowMultiple
parameter_list|(
name|Boolean
name|allowMultiple
parameter_list|)
block|{
name|this
operator|.
name|allowMultiple
operator|=
name|allowMultiple
expr_stmt|;
block|}
DECL|method|getDataType ()
specifier|public
name|String
name|getDataType
parameter_list|()
block|{
return|return
name|dataType
return|;
block|}
DECL|method|setDataType (String dataType)
specifier|public
name|void
name|setDataType
parameter_list|(
name|String
name|dataType
parameter_list|)
block|{
name|this
operator|.
name|dataType
operator|=
name|dataType
expr_stmt|;
block|}
DECL|method|getAllowableValues ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getAllowableValues
parameter_list|()
block|{
return|return
name|allowableValues
return|;
block|}
DECL|method|setAllowableValues (List<String> allowableValues)
specifier|public
name|void
name|setAllowableValues
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|allowableValues
parameter_list|)
block|{
name|this
operator|.
name|allowableValues
operator|=
name|allowableValues
expr_stmt|;
block|}
DECL|method|getParamAccess ()
specifier|public
name|String
name|getParamAccess
parameter_list|()
block|{
return|return
name|paramAccess
return|;
block|}
DECL|method|setParamAccess (String paramAccess)
specifier|public
name|void
name|setParamAccess
parameter_list|(
name|String
name|paramAccess
parameter_list|)
block|{
name|this
operator|.
name|paramAccess
operator|=
name|paramAccess
expr_stmt|;
block|}
block|}
end_class

end_unit

