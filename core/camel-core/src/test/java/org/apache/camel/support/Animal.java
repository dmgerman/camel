begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
package|;
end_package

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
DECL|field|dangerous
specifier|private
name|boolean
name|dangerous
decl_stmt|;
DECL|method|Animal ()
specifier|public
name|Animal
parameter_list|()
block|{     }
DECL|method|Animal (String name)
specifier|public
name|Animal
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
DECL|method|Animal (boolean dangerous, int foo)
specifier|public
name|Animal
parameter_list|(
name|boolean
name|dangerous
parameter_list|,
name|int
name|foo
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Should not be invoked"
argument_list|)
throw|;
block|}
DECL|method|Animal (String name, boolean dangerous)
specifier|public
name|Animal
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|dangerous
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
name|dangerous
operator|=
name|dangerous
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
DECL|method|isDangerous ()
specifier|public
name|boolean
name|isDangerous
parameter_list|()
block|{
return|return
name|dangerous
return|;
block|}
DECL|method|setDangerous (boolean dangerous)
specifier|public
name|void
name|setDangerous
parameter_list|(
name|boolean
name|dangerous
parameter_list|)
block|{
name|this
operator|.
name|dangerous
operator|=
name|dangerous
expr_stmt|;
block|}
block|}
end_class

end_unit

