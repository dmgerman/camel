begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina
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
name|ExchangeTimedOutException
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
name|RuntimeCamelException
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Unit test with InOut however we want sometimes to not send a response.  */
end_comment

begin_class
DECL|class|MinaInOutWithForcedNoResponseTest
specifier|public
class|class
name|MinaInOutWithForcedNoResponseTest
extends|extends
name|BaseMinaTest
block|{
DECL|field|port1
name|int
name|port1
decl_stmt|;
DECL|field|port2
name|int
name|port2
decl_stmt|;
annotation|@
name|Test
DECL|method|testResponse ()
specifier|public
name|void
name|testResponse
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"mina:tcp://localhost:"
operator|+
name|port1
operator|+
literal|"?sync=true"
argument_list|,
literal|"Woodbine"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Chad"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoResponseDisconnectOnNoReplyFalse ()
specifier|public
name|void
name|testNoResponseDisconnectOnNoReplyFalse
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"mina:tcp://localhost:"
operator|+
name|port2
operator|+
literal|"?sync=true&timeout=100"
argument_list|,
literal|"London"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should throw an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
name|assertIsInstanceOf
argument_list|(
name|ExchangeTimedOutException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|port1
operator|=
name|getPort
argument_list|()
expr_stmt|;
name|port2
operator|=
name|getNextPort
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"mina:tcp://localhost:"
operator|+
name|port1
operator|+
literal|"?sync=true"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"Woodbine"
argument_list|)
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Hello Chad"
argument_list|)
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"mina:tcp://localhost:"
operator|+
name|port2
operator|+
literal|"?sync=true&disconnectOnNoReply=false&noReplyLogLevel=OFF"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"Woodbine"
argument_list|)
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Hello Chad"
argument_list|)
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

