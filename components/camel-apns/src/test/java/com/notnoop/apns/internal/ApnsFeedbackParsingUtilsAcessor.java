begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.notnoop.apns.internal
package|package
name|com
operator|.
name|notnoop
operator|.
name|apns
operator|.
name|internal
package|;
end_package

begin_class
DECL|class|ApnsFeedbackParsingUtilsAcessor
specifier|public
specifier|final
class|class
name|ApnsFeedbackParsingUtilsAcessor
block|{
DECL|method|ApnsFeedbackParsingUtilsAcessor ()
specifier|private
name|ApnsFeedbackParsingUtilsAcessor
parameter_list|()
block|{     }
DECL|method|pack (byte[]... args)
specifier|public
specifier|static
name|byte
index|[]
name|pack
parameter_list|(
name|byte
index|[]
modifier|...
name|args
parameter_list|)
block|{
return|return
name|ApnsFeedbackParsingUtils
operator|.
name|pack
argument_list|(
name|args
argument_list|)
return|;
block|}
block|}
end_class

end_unit

