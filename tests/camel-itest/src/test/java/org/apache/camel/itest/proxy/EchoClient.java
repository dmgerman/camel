begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.proxy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|proxy
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Component
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"client"
argument_list|)
DECL|class|EchoClient
specifier|public
class|class
name|EchoClient
block|{
annotation|@
name|Resource
argument_list|(
name|name
operator|=
literal|"myEcho"
argument_list|)
DECL|field|echo
specifier|private
name|Echo
name|echo
decl_stmt|;
DECL|method|hello (String name)
specifier|public
name|String
name|hello
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|echo
operator|.
name|hello
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
end_class

end_unit

