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
name|String
name|response
init|=
operator|(
name|String
operator|)
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start-auth"
argument_list|,
literal|"<order foo='1'/>"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"received [<order foo='1'/>] as an order id = "
operator|+
literal|89531
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
name|String
name|response
init|=
operator|(
name|String
operator|)
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start-bad-auth"
argument_list|,
literal|"<order foo='1'/>"
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

