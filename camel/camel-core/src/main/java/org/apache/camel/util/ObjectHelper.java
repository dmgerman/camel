begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|ObjectHelper
specifier|public
class|class
name|ObjectHelper
block|{
DECL|method|equals (Object a, Object b)
specifier|public
specifier|static
name|boolean
name|equals
parameter_list|(
name|Object
name|a
parameter_list|,
name|Object
name|b
parameter_list|)
block|{
if|if
condition|(
name|a
operator|==
name|b
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|a
operator|!=
literal|null
operator|&&
name|b
operator|!=
literal|null
operator|&&
name|a
operator|.
name|equals
argument_list|(
name|b
argument_list|)
return|;
block|}
block|}
end_class

end_unit

