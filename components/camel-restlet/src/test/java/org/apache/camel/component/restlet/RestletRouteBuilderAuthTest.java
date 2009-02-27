begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.restlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|restlet
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|spring
operator|.
name|SpringTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|RestletRouteBuilderAuthTest
specifier|public
class|class
name|RestletRouteBuilderAuthTest
extends|extends
name|SpringTestSupport
block|{
DECL|method|testBasicAuth ()
specifier|public
name|void
name|testBasicAuth
parameter_list|()
throws|throws
name|IOException
block|{
comment|// START SNIPPET: auth_request
specifier|final
name|String
name|id
init|=
literal|"89531"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|RestletConstants
operator|.
name|RESTLET_LOGIN
argument_list|,
literal|"admin"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|RestletConstants
operator|.
name|RESTLET_PASSWORD
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|String
name|response
init|=
operator|(
name|String
operator|)
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:start-auth"
argument_list|,
literal|"<order foo='1'/>"
argument_list|,
name|headers
argument_list|)
decl_stmt|;
comment|// END SNIPPET: auth_request
name|assertEquals
argument_list|(
literal|"received [<order foo='1'/>] as an order id = "
operator|+
name|id
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
DECL|method|testhBasicAuthError ()
specifier|public
name|void
name|testhBasicAuthError
parameter_list|()
throws|throws
name|IOException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|RestletConstants
operator|.
name|RESTLET_LOGIN
argument_list|,
literal|"admin"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|RestletConstants
operator|.
name|RESTLET_PASSWORD
argument_list|,
literal|"bad"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
literal|"xyz"
argument_list|)
expr_stmt|;
name|String
name|response
init|=
operator|(
name|String
operator|)
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:start-auth"
argument_list|,
literal|"<order foo='1'/>"
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|response
operator|.
name|contains
argument_list|(
literal|"requires user authentication"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|ClassPathXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/restlet/camel-context.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

