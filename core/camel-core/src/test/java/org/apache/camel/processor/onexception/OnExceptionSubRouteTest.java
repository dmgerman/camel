begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.onexception
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|onexception
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
comment|/**  * Unit test inspired by user forum.  */
end_comment

begin_class
DECL|class|OnExceptionSubRouteTest
specifier|public
class|class
name|OnExceptionSubRouteTest
extends|extends
name|OnExceptionRouteTest
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
comment|// START SNIPPET: e1
comment|// default should errors go to mock:error
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
argument_list|)
expr_stmt|;
comment|// here we start the routing with the consumer
name|from
argument_list|(
literal|"direct:start"
argument_list|)
comment|// if a MyTechnicalException is thrown we will not try to
comment|// redeliver and we mark it as handled
comment|// so the caller does not get a failure
comment|// since we have no to then the exchange will continue to be
comment|// routed to the normal error handler
comment|// destination that is mock:error as defined above
comment|// we MUST use .end() to indicate that this sub block is
comment|// ended
operator|.
name|onException
argument_list|(
name|MyTechnicalException
operator|.
name|class
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|0
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|end
argument_list|()
comment|// if a MyFunctionalException is thrown we do not want Camel
comment|// to redelivery but handle it our self using
comment|// our bean myOwnHandler, then the exchange is not routed to
comment|// the default error (mock:error)
comment|// we MUST use .end() to indicate that this sub block is
comment|// ended
operator|.
name|onException
argument_list|(
name|MyFunctionalException
operator|.
name|class
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|0
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:myOwnHandler"
argument_list|)
operator|.
name|end
argument_list|()
comment|// here we have the regular routing
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|()
operator|.
name|xpath
argument_list|(
literal|"//type = 'myType'"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:myServiceBean"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

