begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.ognl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|ognl
package|;
end_package

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|Animal
specifier|public
class|class
name|Animal
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|age
specifier|private
name|int
name|age
decl_stmt|;
DECL|field|friend
specifier|private
name|Animal
name|friend
decl_stmt|;
DECL|method|Animal (String name, int age)
specifier|public
name|Animal
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|age
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|age
operator|=
name|age
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
DECL|method|getAge ()
specifier|public
name|int
name|getAge
parameter_list|()
block|{
return|return
name|age
return|;
block|}
DECL|method|getFriend ()
specifier|public
name|Animal
name|getFriend
parameter_list|()
block|{
return|return
name|friend
return|;
block|}
DECL|method|setFriend (Animal friend)
specifier|public
name|void
name|setFriend
parameter_list|(
name|Animal
name|friend
parameter_list|)
block|{
name|this
operator|.
name|friend
operator|=
name|friend
expr_stmt|;
block|}
DECL|method|isDangerous ()
specifier|public
name|boolean
name|isDangerous
parameter_list|()
block|{
return|return
name|name
operator|.
name|contains
argument_list|(
literal|"Tiger"
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
name|name
return|;
block|}
DECL|method|getClassName ()
specifier|public
specifier|static
name|String
name|getClassName
parameter_list|()
block|{
return|return
literal|"Animal"
return|;
block|}
block|}
end_class

end_unit

