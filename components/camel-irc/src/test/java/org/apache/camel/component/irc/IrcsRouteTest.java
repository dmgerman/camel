begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.irc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|irc
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_class
annotation|@
name|Ignore
DECL|class|IrcsRouteTest
specifier|public
class|class
name|IrcsRouteTest
extends|extends
name|IrcRouteTest
block|{
comment|// TODO This test is disabled until we can find a public SSL enabled IRC
comment|// server to test against. To use this you'll need to change the server
comment|// information below and the username/password.
annotation|@
name|Override
DECL|method|sendUri ()
specifier|protected
name|String
name|sendUri
parameter_list|()
block|{
return|return
literal|"ircs://camel-prd@irc.codehaus.org:6667/#camel-test?nickname=camel-prd&password=blah"
return|;
block|}
annotation|@
name|Override
DECL|method|fromUri ()
specifier|protected
name|String
name|fromUri
parameter_list|()
block|{
return|return
literal|"ircs://camel-con@irc.codehaus.org:6667/#camel-test?nickname=camel-con&password=blah"
return|;
block|}
block|}
end_class

end_unit

