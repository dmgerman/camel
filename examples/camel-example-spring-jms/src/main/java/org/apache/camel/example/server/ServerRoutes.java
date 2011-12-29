begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.server
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|server
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

begin_comment
comment|/**  * This class defines the routes on the Server. The class extends a base class in Camel {@link RouteBuilder}  * that can be used to easily setup the routes in the configure() method.  */
end_comment

begin_comment
comment|// START SNIPPET: e1
end_comment

begin_class
DECL|class|ServerRoutes
specifier|public
class|class
name|ServerRoutes
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// route from the numbers queue to our business that is a spring bean registered with the id=multiplier
comment|// Camel will introspect the multiplier bean and find the best candidate of the method to invoke.
comment|// You can add annotations etc to help Camel find the method to invoke.
comment|// As our multiplier bean only have one method its easy for Camel to find the method to use.
name|from
argument_list|(
literal|"jms:queue:numbers"
argument_list|)
operator|.
name|to
argument_list|(
literal|"multiplier"
argument_list|)
expr_stmt|;
comment|// Camel has several ways to configure the same routing, we have defined some of them here below
comment|// as above but with the bean: prefix
comment|//from("jms:queue:numbers").to("bean:multiplier");
comment|// beanRef is using explicit bean bindings to lookup the multiplier bean and invoke the multiply method
comment|//from("jms:queue:numbers").beanRef("multiplier", "multiply");
comment|// the same as above but expressed as a URI configuration
comment|//from("jms:queue:numbers").to("bean:multiplier?methodName=multiply");
block|}
block|}
end_class

begin_comment
comment|// END SNIPPET: e1
end_comment

end_unit

