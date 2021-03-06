begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
package|;
end_package

begin_class
DECL|class|MyBean
specifier|public
class|class
name|MyBean
block|{
DECL|field|hi
specifier|private
name|String
name|hi
decl_stmt|;
DECL|field|bye
specifier|private
name|String
name|bye
decl_stmt|;
DECL|method|MyBean (String hi, String bye)
specifier|public
name|MyBean
parameter_list|(
name|String
name|hi
parameter_list|,
name|String
name|bye
parameter_list|)
block|{
name|this
operator|.
name|hi
operator|=
name|hi
expr_stmt|;
name|this
operator|.
name|bye
operator|=
name|bye
expr_stmt|;
block|}
DECL|method|hello ()
specifier|public
name|String
name|hello
parameter_list|()
block|{
return|return
name|hi
operator|+
literal|" how are you?"
return|;
block|}
DECL|method|bye ()
specifier|public
name|String
name|bye
parameter_list|()
block|{
return|return
name|bye
operator|+
literal|" World"
return|;
block|}
block|}
end_class

end_unit

