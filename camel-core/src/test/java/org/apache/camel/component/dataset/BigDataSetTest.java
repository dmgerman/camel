begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dataset
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dataset
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|BigDataSetTest
specifier|public
class|class
name|BigDataSetTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|dataSet
specifier|protected
name|SimpleDataSet
name|dataSet
init|=
operator|new
name|SimpleDataSet
argument_list|(
literal|20000
argument_list|)
decl_stmt|;
DECL|method|testDataSet ()
specifier|public
name|void
name|testDataSet
parameter_list|()
throws|throws
name|Exception
block|{
comment|// data set will itself set its assertions so we should just
comment|// assert that all mocks is ok
comment|// TODO: For testing with bigger number of messages that takes a longer time
comment|// MockEndpoint.assertIsSatisfied(context, 5, TimeUnit.MINUTES);
block|}
annotation|@
name|Override
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|Context
name|context
init|=
name|super
operator|.
name|createJndiContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|bind
argument_list|(
literal|"foo"
argument_list|,
name|dataSet
argument_list|)
expr_stmt|;
return|return
name|context
return|;
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
comment|// start this first to make sure the "direct:foo" consumer is ready
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"dataset:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"dataset:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:foo"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

