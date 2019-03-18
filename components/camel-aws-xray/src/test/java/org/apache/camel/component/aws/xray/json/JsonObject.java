begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.xray.json
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|xray
operator|.
name|json
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|JsonObject
specifier|public
class|class
name|JsonObject
implements|implements
name|JsonStructure
block|{
DECL|field|data
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|data
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|addElement (String key, Object value)
name|void
name|addElement
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|data
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|get (String key)
specifier|public
name|Object
name|get
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|data
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
DECL|method|getKeys ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getKeys
parameter_list|()
block|{
return|return
name|data
operator|.
name|keySet
argument_list|()
return|;
block|}
DECL|method|has (String key)
specifier|public
name|boolean
name|has
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|data
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
DECL|method|getString (String key)
specifier|public
name|String
name|getString
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|data
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
operator|&&
literal|null
operator|!=
name|data
operator|.
name|get
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|data
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|getDouble (String key)
specifier|public
name|Double
name|getDouble
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|data
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
operator|&&
literal|null
operator|!=
name|data
operator|.
name|get
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|Object
name|value
init|=
name|data
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
return|return
name|Double
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
return|;
block|}
return|return
operator|(
name|Double
operator|)
name|value
return|;
block|}
return|return
literal|0D
return|;
block|}
DECL|method|getLong (String key)
specifier|public
name|Long
name|getLong
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|data
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
operator|&&
literal|null
operator|!=
name|data
operator|.
name|get
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|Object
name|value
init|=
name|data
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
return|return
name|Long
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
return|;
block|}
return|return
operator|(
name|Long
operator|)
name|value
return|;
block|}
return|return
literal|0L
return|;
block|}
DECL|method|getInteger (String key)
specifier|public
name|Integer
name|getInteger
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|data
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
operator|&&
literal|null
operator|!=
name|data
operator|.
name|get
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|Object
name|value
init|=
name|data
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
return|return
name|Integer
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
return|;
block|}
return|return
operator|(
name|Integer
operator|)
name|value
return|;
block|}
return|return
literal|0
return|;
block|}
DECL|method|getBoolean (String key)
specifier|public
name|Boolean
name|getBoolean
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|data
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
operator|&&
literal|null
operator|!=
name|data
operator|.
name|get
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|Object
name|value
init|=
name|data
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
return|return
name|Boolean
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
return|;
block|}
return|return
operator|(
name|Boolean
operator|)
name|value
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

