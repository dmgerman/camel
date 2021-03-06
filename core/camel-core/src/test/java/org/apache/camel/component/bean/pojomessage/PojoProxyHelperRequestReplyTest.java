begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean.pojomessage
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
operator|.
name|pojomessage
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ContextTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Endpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
operator|.
name|PojoProxyHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|PojoProxyHelperRequestReplyTest
specifier|public
class|class
name|PojoProxyHelperRequestReplyTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|receiver
name|PersonReceiver
name|receiver
init|=
operator|new
name|PersonReceiver
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testRequestReply ()
specifier|public
name|void
name|testRequestReply
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|personEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:person"
argument_list|)
decl_stmt|;
name|Person
name|person
init|=
operator|new
name|Person
argument_list|(
literal|"Chris"
argument_list|)
decl_stmt|;
name|PersonHandler
name|sender
init|=
name|PojoProxyHelper
operator|.
name|createProxy
argument_list|(
name|personEndpoint
argument_list|,
name|PersonHandler
operator|.
name|class
argument_list|)
decl_stmt|;
name|Person
name|resultPerson
init|=
name|sender
operator|.
name|onPerson
argument_list|(
name|person
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|person
operator|.
name|getName
argument_list|()
operator|+
literal|"1"
argument_list|,
name|resultPerson
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:person"
argument_list|)
operator|.
name|bean
argument_list|(
name|receiver
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|PersonReceiver
specifier|public
specifier|final
class|class
name|PersonReceiver
implements|implements
name|PersonHandler
block|{
annotation|@
name|Override
DECL|method|onPerson (Person person)
specifier|public
name|Person
name|onPerson
parameter_list|(
name|Person
name|person
parameter_list|)
block|{
return|return
operator|new
name|Person
argument_list|(
name|person
operator|.
name|getName
argument_list|()
operator|+
literal|"1"
argument_list|)
return|;
block|}
block|}
DECL|interface|PersonHandler
specifier|public
interface|interface
name|PersonHandler
block|{
DECL|method|onPerson (Person person)
name|Person
name|onPerson
parameter_list|(
name|Person
name|person
parameter_list|)
function_decl|;
block|}
block|}
end_class

end_unit

