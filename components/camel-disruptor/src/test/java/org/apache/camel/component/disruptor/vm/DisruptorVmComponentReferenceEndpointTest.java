begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.disruptor.vm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|disruptor
operator|.
name|vm
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|disruptor
operator|.
name|DisruptorComponent
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
name|disruptor
operator|.
name|DisruptorReference
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|DisruptorVmComponentReferenceEndpointTest
specifier|public
class|class
name|DisruptorVmComponentReferenceEndpointTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testDisruptorVmComponentReference ()
specifier|public
name|void
name|testDisruptorVmComponentReference
parameter_list|()
throws|throws
name|Exception
block|{
name|DisruptorVmComponent
name|vm
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"disruptor-vm"
argument_list|,
name|DisruptorVmComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|key
init|=
name|DisruptorComponent
operator|.
name|getDisruptorKey
argument_list|(
literal|"disruptor-vm://foo"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|vm
operator|.
name|getDisruptors
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|getEndpointCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|numberOfReferences
argument_list|(
name|vm
argument_list|)
argument_list|)
expr_stmt|;
comment|// add a second consumer on the endpoint
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
literal|"disruptor-vm:foo?blockWhenFull=true"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo2"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|vm
operator|.
name|getDisruptors
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|getEndpointCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|numberOfReferences
argument_list|(
name|vm
argument_list|)
argument_list|)
expr_stmt|;
comment|// remove the 1st route
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|context
operator|.
name|removeRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|vm
operator|.
name|getDisruptors
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|getEndpointCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|numberOfReferences
argument_list|(
name|vm
argument_list|)
argument_list|)
expr_stmt|;
comment|// remove the 2nd route
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
literal|"foo2"
argument_list|)
expr_stmt|;
name|context
operator|.
name|removeRoute
argument_list|(
literal|"foo2"
argument_list|)
expr_stmt|;
comment|// and there is no longer queues for the foo key
name|assertNull
argument_list|(
name|vm
operator|.
name|getDisruptors
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
comment|// there should still be a bar
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|numberOfReferences
argument_list|(
name|vm
argument_list|)
argument_list|)
expr_stmt|;
name|key
operator|=
name|DisruptorComponent
operator|.
name|getDisruptorKey
argument_list|(
literal|"disruptor-vm://bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|vm
operator|.
name|getDisruptors
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|getEndpointCount
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
literal|"disruptor-vm:foo"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"disruptor-vm:bar"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bar"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|numberOfReferences (DisruptorVmComponent vm)
specifier|private
name|int
name|numberOfReferences
parameter_list|(
name|DisruptorVmComponent
name|vm
parameter_list|)
block|{
name|int
name|num
init|=
literal|0
decl_stmt|;
name|Iterator
argument_list|<
name|DisruptorReference
argument_list|>
name|it
init|=
name|vm
operator|.
name|getDisruptors
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|num
operator|+=
name|it
operator|.
name|next
argument_list|()
operator|.
name|getEndpointCount
argument_list|()
expr_stmt|;
block|}
return|return
name|num
return|;
block|}
block|}
end_class

end_unit

