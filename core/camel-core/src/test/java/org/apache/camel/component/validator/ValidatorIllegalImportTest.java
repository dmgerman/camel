begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.validator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|validator
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
comment|/**  *  */
end_comment

begin_class
DECL|class|ValidatorIllegalImportTest
specifier|public
class|class
name|ValidatorIllegalImportTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|broadCastEvent
specifier|private
specifier|final
name|String
name|broadCastEvent
init|=
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
operator|+
literal|"<BroadcastMonitor> "
operator|+
literal|"<updated>2012-03-01T03:46:26</updated>"
operator|+
literal|"<stationName>P7 Mix</stationName>"
operator|+
literal|"<Current>"
operator|+
literal|"<startTime>2012-03-01T03:46:26</startTime>"
operator|+
literal|"<itemId>1000736343:8505553</itemId>"
operator|+
literal|"<titleId>785173</titleId>"
operator|+
literal|"<itemCode>9004342-0101</itemCode>"
operator|+
literal|"<itemReference></itemReference>"
operator|+
literal|"<titleName>Part Of Me</titleName>"
operator|+
literal|"<artistName>Katy Perry</artistName>"
operator|+
literal|"<albumName></albumName>"
operator|+
literal|"</Current>"
operator|+
literal|"<Next>"
operator|+
literal|"<startTime>2012-03-01T03:50:00</startTime>"
operator|+
literal|"<itemId>1000736343:8505554</itemId>"
operator|+
literal|"<titleId>780319</titleId>"
operator|+
literal|"<itemCode>2318050-0101</itemCode>"
operator|+
literal|"<itemReference></itemReference>"
operator|+
literal|"<titleName>Fine</titleName>"
operator|+
literal|"<artistName>Whitney Houston</artistName>"
operator|+
literal|"<albumName></albumName>"
operator|+
literal|"</Next>"
operator|+
literal|"</BroadcastMonitor>"
decl_stmt|;
annotation|@
name|Test
DECL|method|testOk ()
specifier|public
name|void
name|testOk
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
literal|"direct:test"
argument_list|)
operator|.
name|to
argument_list|(
literal|"validator:org/apache/camel/component/validator/BroadcastMonitorFixed.xsd"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:test"
argument_list|,
name|broadCastEvent
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIllegalImport ()
specifier|public
name|void
name|testIllegalImport
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
literal|"direct:test"
argument_list|)
operator|.
name|to
argument_list|(
literal|"validator:org/apache/camel/component/validator/BroadcastMonitor.xsd"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
try|try
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|IllegalArgumentException
name|iae
init|=
name|assertIsInstanceOf
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|iae
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Resource: org/apache/camel/component/validator/BroadcastMonitor.xsd refers an invalid resource without SystemId."
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

