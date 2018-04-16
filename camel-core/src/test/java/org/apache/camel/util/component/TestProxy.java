begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|component
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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
DECL|class|TestProxy
class|class
name|TestProxy
block|{
DECL|method|sayHi ()
specifier|public
name|String
name|sayHi
parameter_list|()
block|{
return|return
literal|"Hello!"
return|;
block|}
DECL|method|sayHi (final String name)
specifier|public
name|String
name|sayHi
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
literal|"Hello "
operator|+
name|name
return|;
block|}
DECL|method|greetMe (final String name)
specifier|public
specifier|final
name|String
name|greetMe
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
literal|"Greetings "
operator|+
name|name
return|;
block|}
DECL|method|greetUs (final String name1, String name2)
specifier|public
specifier|final
name|String
name|greetUs
parameter_list|(
specifier|final
name|String
name|name1
parameter_list|,
name|String
name|name2
parameter_list|)
block|{
return|return
literal|"Greetings "
operator|+
name|name1
operator|+
literal|", "
operator|+
name|name2
return|;
block|}
DECL|method|greetAll (final String[] names)
specifier|public
specifier|final
name|String
name|greetAll
parameter_list|(
specifier|final
name|String
index|[]
name|names
parameter_list|)
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"Greetings "
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|name
argument_list|)
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|delete
argument_list|(
name|builder
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|,
name|builder
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|greetAll (List<String> names)
specifier|public
specifier|final
name|String
name|greetAll
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|names
parameter_list|)
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"Greetings "
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|name
argument_list|)
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|delete
argument_list|(
name|builder
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|,
name|builder
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|greetTimes (String name, int times)
specifier|public
specifier|final
name|String
index|[]
name|greetTimes
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|times
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|times
condition|;
name|i
operator|++
control|)
block|{
name|result
operator|.
name|add
argument_list|(
literal|"Greetings "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|result
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|result
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
DECL|method|greetAll (Map<String, String> nameMap)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|greetAll
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nameMap
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|nameMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
specifier|final
name|String
name|name
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
specifier|final
name|String
name|greeting
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|result
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|greeting
operator|+
literal|" "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|greetInnerChild (InnerChild child)
specifier|public
specifier|final
name|String
name|greetInnerChild
parameter_list|(
name|InnerChild
name|child
parameter_list|)
block|{
return|return
name|sayHi
argument_list|(
name|child
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
DECL|class|InnerChild
specifier|public
specifier|static
class|class
name|InnerChild
block|{
DECL|field|name
specifier|private
name|String
name|name
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
block|}
block|}
end_class

end_unit

