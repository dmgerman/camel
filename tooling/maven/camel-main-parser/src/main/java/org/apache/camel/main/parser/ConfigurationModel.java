begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.main.parser
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|main
operator|.
name|parser
package|;
end_package

begin_class
DECL|class|ConfigurationModel
specifier|public
class|class
name|ConfigurationModel
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|javaType
specifier|private
name|String
name|javaType
decl_stmt|;
DECL|field|sourceType
specifier|private
name|String
name|sourceType
decl_stmt|;
DECL|field|description
specifier|private
name|String
name|description
decl_stmt|;
DECL|field|defaultValue
specifier|private
name|String
name|defaultValue
decl_stmt|;
DECL|field|deprecated
specifier|private
name|boolean
name|deprecated
decl_stmt|;
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
DECL|method|getJavaType ()
specifier|public
name|String
name|getJavaType
parameter_list|()
block|{
return|return
name|javaType
return|;
block|}
DECL|method|setJavaType (String javaType)
specifier|public
name|void
name|setJavaType
parameter_list|(
name|String
name|javaType
parameter_list|)
block|{
name|this
operator|.
name|javaType
operator|=
name|javaType
expr_stmt|;
block|}
DECL|method|getSourceType ()
specifier|public
name|String
name|getSourceType
parameter_list|()
block|{
return|return
name|sourceType
return|;
block|}
DECL|method|setSourceType (String sourceType)
specifier|public
name|void
name|setSourceType
parameter_list|(
name|String
name|sourceType
parameter_list|)
block|{
name|this
operator|.
name|sourceType
operator|=
name|sourceType
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
DECL|method|isDeprecated ()
specifier|public
name|boolean
name|isDeprecated
parameter_list|()
block|{
return|return
name|deprecated
return|;
block|}
DECL|method|setDeprecated (boolean deprecated)
specifier|public
name|void
name|setDeprecated
parameter_list|(
name|boolean
name|deprecated
parameter_list|)
block|{
name|this
operator|.
name|deprecated
operator|=
name|deprecated
expr_stmt|;
block|}
block|}
end_class

end_unit

