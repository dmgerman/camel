begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
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
name|Processor
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

begin_class
DECL|class|NettyTCPSyncTest
specifier|public
class|class
name|NettyTCPSyncTest
extends|extends
name|BaseNettyTest
block|{
annotation|@
name|Test
DECL|method|testTCPStringInOutWithNettyConsumer ()
specifier|public
name|void
name|testTCPStringInOutWithNettyConsumer
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
literal|"netty:tcp://localhost:{{port}}?sync=true"
argument_list|,
literal|"Epitaph in Kohima, India marking the WWII Battle of Kohima and Imphal, Burma Campaign - Attributed to John Maxwell Edmonds"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"When You Go Home, Tell Them Of Us And Say, For Your Tomorrow, We Gave Our Today."
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTCPStringInOutWithNettyConsumer2Times ()
specifier|public
name|void
name|testTCPStringInOutWithNettyConsumer2Times
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
literal|"netty:tcp://localhost:{{port}}?sync=true"
argument_list|,
literal|"Epitaph in Kohima, India marking the WWII Battle of Kohima and Imphal, Burma Campaign - Attributed to John Maxwell Edmonds"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"When You Go Home, Tell Them Of Us And Say, For Your Tomorrow, We Gave Our Today."
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|response
operator|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty:tcp://localhost:{{port}}?sync=true"
argument_list|,
literal|"Hello World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"When You Go Home, Tell Them Of Us And Say, For Your Tomorrow, We Gave Our Today."
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTCPObjectInOutWithNettyConsumer ()
specifier|public
name|void
name|testTCPObjectInOutWithNettyConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|Poetry
name|poetry
init|=
operator|new
name|Poetry
argument_list|()
decl_stmt|;
name|Poetry
name|response
init|=
operator|(
name|Poetry
operator|)
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty:tcp://localhost:{{port}}?sync=true"
argument_list|,
name|poetry
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Dr. Sarojini Naidu"
argument_list|,
name|response
operator|.
name|getPoet
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
literal|"netty:tcp://localhost:{{port}}?sync=true"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|instanceof
name|Poetry
condition|)
block|{
name|Poetry
name|poetry
init|=
operator|(
name|Poetry
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|poetry
operator|.
name|setPoet
argument_list|(
literal|"Dr. Sarojini Naidu"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|poetry
argument_list|)
expr_stmt|;
return|return;
block|}
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"When You Go Home, Tell Them Of Us And Say, For Your Tomorrow, We Gave Our Today."
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

