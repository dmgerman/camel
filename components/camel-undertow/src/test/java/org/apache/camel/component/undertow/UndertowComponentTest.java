begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.undertow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|undertow
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
name|Exchange
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
name|mock
operator|.
name|MockEndpoint
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|UndertowComponentTest
specifier|public
class|class
name|UndertowComponentTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|UndertowComponentTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testUndertow ()
specifier|public
name|void
name|testUndertow
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"undertow://http://localhost:8888/myapp"
argument_list|,
literal|"Hello Camel!"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|//        MockEndpoint mockEndpoint = getMockEndpoint("mock:myapp");
comment|//        assertTrue(mockEndpoint.getExchanges().size() == 1);
comment|//        for (Exchange exchange : mockEndpoint.getExchanges()) {
comment|//            assertEquals("Bye Camel", exchange.getIn().getBody(String.class));
comment|//        }
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Camel!"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:myapp"
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedHeaderReceived
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
literal|"GET"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Number of exchanges in mock:myapp"
operator|+
name|mockEndpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|mockEndpoint
operator|.
name|getExchanges
argument_list|()
control|)
block|{
name|assertEquals
argument_list|(
literal|"Hello Camel! Bye Camel!"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|//        Exchange out = template.request("undertow:http://localhost:8888/myapp", new Processor() {
comment|//            @Override
comment|//            public void process(Exchange exchange) throws Exception {
comment|//                exchange.getIn().setBody("Hello World!");
comment|//            }
comment|//        });
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
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
block|{
name|from
argument_list|(
literal|"undertow:http://localhost:8888/myapp"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Bye Camel!"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:myapp"
argument_list|)
expr_stmt|;
comment|//                from("undertow:http://localhost:8888/bar")
comment|//                        .transform(bodyAs(String.class).append(" Bar Camel!"))
comment|//                        .to("mock:bar");
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

