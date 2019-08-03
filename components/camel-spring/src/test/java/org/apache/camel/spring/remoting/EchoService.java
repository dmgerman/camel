begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.remoting
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|remoting
package|;
end_package

begin_class
DECL|class|EchoService
specifier|public
class|class
name|EchoService
implements|implements
name|Echo
block|{
annotation|@
name|Override
DECL|method|echo (String name)
specifier|public
name|String
name|echo
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"Kabom"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|MyEchoRuntimeException
argument_list|(
literal|"Damn something went wrong"
argument_list|)
throw|;
block|}
return|return
name|name
operator|+
literal|" "
operator|+
name|name
return|;
block|}
block|}
end_class

end_unit

