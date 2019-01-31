begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_comment
comment|/**  * Generic holder object for pair values.  */
end_comment

begin_class
DECL|class|Pair
specifier|public
class|class
name|Pair
parameter_list|<
name|T
parameter_list|>
block|{
DECL|field|left
specifier|private
name|T
name|left
decl_stmt|;
DECL|field|right
specifier|private
name|T
name|right
decl_stmt|;
DECL|method|Pair (T left, T right)
specifier|public
name|Pair
parameter_list|(
name|T
name|left
parameter_list|,
name|T
name|right
parameter_list|)
block|{
name|this
operator|.
name|left
operator|=
name|left
expr_stmt|;
name|this
operator|.
name|right
operator|=
name|right
expr_stmt|;
block|}
DECL|method|getLeft ()
specifier|public
name|T
name|getLeft
parameter_list|()
block|{
return|return
name|left
return|;
block|}
DECL|method|getRight ()
specifier|public
name|T
name|getRight
parameter_list|()
block|{
return|return
name|right
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Pair
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|Pair
argument_list|<
name|?
argument_list|>
operator|)
name|o
decl_stmt|;
return|return
name|Objects
operator|.
name|equals
argument_list|(
name|left
argument_list|,
name|that
operator|.
name|left
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|right
argument_list|,
name|that
operator|.
name|right
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hash
argument_list|(
name|left
argument_list|,
name|right
argument_list|)
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
literal|"("
operator|+
name|left
operator|+
literal|", "
operator|+
name|right
operator|+
literal|")"
return|;
block|}
block|}
end_class

end_unit

