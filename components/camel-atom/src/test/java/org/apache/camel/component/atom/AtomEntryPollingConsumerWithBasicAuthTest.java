begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atom
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atom
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_class
DECL|class|AtomEntryPollingConsumerWithBasicAuthTest
specifier|public
class|class
name|AtomEntryPollingConsumerWithBasicAuthTest
extends|extends
name|AtomEntryPollingConsumerTest
block|{
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"atom:http://localhost:"
operator|+
name|JettyTestServer
operator|.
name|getInstance
argument_list|()
operator|.
name|port
operator|+
literal|"/?splitEntries=true&delay=500&username=camel&password=camelPass"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result1"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"atom:http://localhost:"
operator|+
name|JettyTestServer
operator|.
name|getInstance
argument_list|()
operator|.
name|port
operator|+
literal|"/?splitEntries=true&filter=false&delay=500&username=camel&password=camelPass"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result2"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"atom:http://localhost:"
operator|+
name|JettyTestServer
operator|.
name|getInstance
argument_list|()
operator|.
name|port
operator|+
literal|"/?splitEntries=true&filter=true&lastUpdate=#myDate&delay=500&username=camel&password=camelPass"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result3"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|BeforeClass
DECL|method|startServer ()
specifier|public
specifier|static
name|void
name|startServer
parameter_list|()
block|{
name|JettyTestServer
operator|.
name|getInstance
argument_list|()
operator|.
name|startServer
argument_list|()
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|stopServer ()
specifier|public
specifier|static
name|void
name|stopServer
parameter_list|()
block|{
name|JettyTestServer
operator|.
name|getInstance
argument_list|()
operator|.
name|stopServer
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

