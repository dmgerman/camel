begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.guice.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|guice
operator|.
name|support
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Lists
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Maps
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|TypeLiteral
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
name|guice
operator|.
name|support
operator|.
name|internal
operator|.
name|MethodKey
import|;
end_import

begin_comment
comment|/**  * Some reflection helper methods  *   */
end_comment

begin_class
DECL|class|Reflectors
specifier|public
specifier|final
class|class
name|Reflectors
block|{
DECL|method|Reflectors ()
specifier|private
name|Reflectors
parameter_list|()
block|{
comment|//Helper class
block|}
comment|/** Returns all the methods on the given type ignoring overloaded methods */
DECL|method|getAllMethods (Class<?> type)
specifier|public
specifier|static
name|List
argument_list|<
name|Method
argument_list|>
name|getAllMethods
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|getAllMethods
argument_list|(
name|TypeLiteral
operator|.
name|get
argument_list|(
name|type
argument_list|)
argument_list|)
return|;
block|}
comment|/** Returns all the methods on the given type ignoring overloaded methods */
DECL|method|getAllMethods (TypeLiteral<?> startType)
specifier|public
specifier|static
name|List
argument_list|<
name|Method
argument_list|>
name|getAllMethods
parameter_list|(
name|TypeLiteral
argument_list|<
name|?
argument_list|>
name|startType
parameter_list|)
block|{
name|List
argument_list|<
name|Method
argument_list|>
name|answer
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|MethodKey
argument_list|,
name|Method
argument_list|>
name|boundMethods
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|startType
operator|.
name|getRawType
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|==
name|Object
operator|.
name|class
condition|)
block|{
break|break;
block|}
name|Method
index|[]
name|methods
init|=
name|type
operator|.
name|getDeclaredMethods
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|Method
name|method
range|:
name|methods
control|)
block|{
name|MethodKey
name|key
init|=
operator|new
name|MethodKey
argument_list|(
name|method
argument_list|)
decl_stmt|;
if|if
condition|(
name|boundMethods
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|==
literal|null
condition|)
block|{
name|boundMethods
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|method
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
block|}
comment|// startType = startType.getSupertype(type);
name|Class
argument_list|<
name|?
argument_list|>
name|supertype
init|=
name|type
operator|.
name|getSuperclass
argument_list|()
decl_stmt|;
if|if
condition|(
name|supertype
operator|==
name|Object
operator|.
name|class
condition|)
block|{
break|break;
block|}
name|startType
operator|=
name|startType
operator|.
name|getSupertype
argument_list|(
name|supertype
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

