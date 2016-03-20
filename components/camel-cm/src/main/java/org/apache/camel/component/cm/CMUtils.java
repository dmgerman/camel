begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cm
package|;
end_package

begin_class
DECL|class|CMUtils
specifier|public
specifier|final
class|class
name|CMUtils
block|{
DECL|method|CMUtils ()
specifier|private
name|CMUtils
parameter_list|()
block|{     }
DECL|method|isGsm0338Encodeable (final String message)
specifier|public
specifier|static
name|boolean
name|isGsm0338Encodeable
parameter_list|(
specifier|final
name|String
name|message
parameter_list|)
block|{
return|return
name|message
operator|.
name|matches
argument_list|(
name|CMConstants
operator|.
name|GSM_CHARACTERS_REGEX
argument_list|)
return|;
block|}
comment|// TODO: Have a look at
comment|// https:// github.com/apache/camel/blob/master/components/camel-smpp/src/main/java/org/apache/camel/component/smpp/SmppUtils.java
block|}
end_class

end_unit

