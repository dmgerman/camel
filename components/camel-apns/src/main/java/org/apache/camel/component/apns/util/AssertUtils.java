begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.apns.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|apns
operator|.
name|util
package|;
end_package

begin_class
DECL|class|AssertUtils
specifier|public
specifier|final
class|class
name|AssertUtils
block|{
DECL|method|AssertUtils ()
specifier|private
name|AssertUtils
parameter_list|()
block|{     }
DECL|method|notNull (Object object, String message)
specifier|public
specifier|static
name|void
name|notNull
parameter_list|(
name|Object
name|object
parameter_list|,
name|String
name|message
parameter_list|)
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"[Assert not null] "
operator|+
name|message
argument_list|)
throw|;
block|}
block|}
DECL|method|isTrue (boolean booleanValue, String message)
specifier|public
specifier|static
name|void
name|isTrue
parameter_list|(
name|boolean
name|booleanValue
parameter_list|,
name|String
name|message
parameter_list|)
block|{
if|if
condition|(
operator|!
name|booleanValue
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"[Assert is true] "
operator|+
name|message
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

