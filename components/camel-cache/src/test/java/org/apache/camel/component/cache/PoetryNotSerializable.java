begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cache
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cache
package|;
end_package

begin_class
DECL|class|PoetryNotSerializable
specifier|public
class|class
name|PoetryNotSerializable
block|{
DECL|field|poet
specifier|private
name|String
name|poet
decl_stmt|;
DECL|field|poem
specifier|private
name|String
name|poem
decl_stmt|;
DECL|method|getPoet ()
specifier|public
name|String
name|getPoet
parameter_list|()
block|{
return|return
name|poet
return|;
block|}
DECL|method|setPoet (String poet)
specifier|public
name|void
name|setPoet
parameter_list|(
name|String
name|poet
parameter_list|)
block|{
name|this
operator|.
name|poet
operator|=
name|poet
expr_stmt|;
block|}
DECL|method|getPoem ()
specifier|public
name|String
name|getPoem
parameter_list|()
block|{
return|return
name|poem
return|;
block|}
DECL|method|setPoem (String poem)
specifier|public
name|void
name|setPoem
parameter_list|(
name|String
name|poem
parameter_list|)
block|{
name|this
operator|.
name|poem
operator|=
name|poem
expr_stmt|;
block|}
block|}
end_class

end_unit

