begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|reactive
operator|.
name|streams
package|;
end_package

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
name|LinkedList
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|AttributeValueExp
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|Query
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|QueryExp
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|StringValueExp
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|CompositeData
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularData
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
name|RoutesBuilder
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
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|CamelReactiveStreams
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
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|CamelReactiveStreamsService
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
name|reactor
operator|.
name|core
operator|.
name|publisher
operator|.
name|Flux
import|;
end_import

begin_comment
comment|/**  * Test exposed services on JMX.  */
end_comment

begin_class
DECL|class|ReactiveStreamsJMXTest
specifier|public
class|class
name|ReactiveStreamsJMXTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testJmxExposedService ()
specifier|public
name|void
name|testJmxExposedService
parameter_list|()
throws|throws
name|Exception
block|{
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|rxService
init|=
name|getReactiveStreamsServiceName
argument_list|(
name|mbeanServer
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|subdata
init|=
name|getValues
argument_list|(
name|mbeanServer
argument_list|,
name|rxService
argument_list|,
literal|"camelSubscribers"
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"unbounded"
argument_list|,
name|subdata
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0L
argument_list|,
name|subdata
operator|.
name|get
argument_list|(
literal|"inflight"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0L
argument_list|,
name|subdata
operator|.
name|get
argument_list|(
literal|"requested"
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|pubdata
init|=
name|getValues
argument_list|(
name|mbeanServer
argument_list|,
name|rxService
argument_list|,
literal|"camelPublishers"
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"strings"
argument_list|,
name|pubdata
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|pubdata
operator|.
name|get
argument_list|(
literal|"subscribers"
argument_list|)
argument_list|)
expr_stmt|;
name|TabularData
name|subTd0
init|=
operator|(
name|TabularData
operator|)
name|pubdata
operator|.
name|get
argument_list|(
literal|"subscriptions"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|subTd0
operator|.
name|values
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Create another subscriber
name|CamelReactiveStreamsService
name|rxCamel
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|()
argument_list|)
decl_stmt|;
name|Flux
operator|.
name|from
argument_list|(
name|rxCamel
operator|.
name|fromStream
argument_list|(
literal|"strings"
argument_list|)
argument_list|)
operator|.
name|subscribe
argument_list|()
expr_stmt|;
name|pubdata
operator|=
name|getValues
argument_list|(
name|mbeanServer
argument_list|,
name|rxService
argument_list|,
literal|"camelPublishers"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|TabularData
name|subTd
init|=
operator|(
name|TabularData
operator|)
name|pubdata
operator|.
name|get
argument_list|(
literal|"subscriptions"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|subTd
operator|.
name|values
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|subscriptions
init|=
name|getValues
argument_list|(
name|subTd
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"BUFFER"
argument_list|,
name|subscriptions
operator|.
name|get
argument_list|(
literal|"back pressure"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getValues (MBeanServer mbeanServer, ObjectName rxService, String name, int index)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getValues
parameter_list|(
name|MBeanServer
name|mbeanServer
parameter_list|,
name|ObjectName
name|rxService
parameter_list|,
name|String
name|name
parameter_list|,
name|int
name|index
parameter_list|)
throws|throws
name|Exception
block|{
name|TabularData
name|td
init|=
operator|(
name|TabularData
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|rxService
argument_list|,
name|name
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
return|return
name|getValues
argument_list|(
name|td
argument_list|,
name|index
argument_list|)
return|;
block|}
DECL|method|getValues (TabularData td, int index)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getValues
parameter_list|(
name|TabularData
name|td
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|td
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|td
operator|.
name|values
argument_list|()
operator|.
name|size
argument_list|()
operator|>
name|index
argument_list|)
expr_stmt|;
name|CompositeData
name|data
init|=
operator|(
name|CompositeData
operator|)
operator|new
name|LinkedList
argument_list|<>
argument_list|(
name|td
operator|.
name|values
argument_list|()
argument_list|)
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|res
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|data
operator|.
name|getCompositeType
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
name|res
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|data
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|res
return|;
block|}
DECL|method|getMBeanServer ()
specifier|private
name|MBeanServer
name|getMBeanServer
parameter_list|()
block|{
return|return
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|getMBeanServer
argument_list|()
return|;
block|}
DECL|method|getReactiveStreamsServiceName (MBeanServer mbeanServer)
specifier|private
name|ObjectName
name|getReactiveStreamsServiceName
parameter_list|(
name|MBeanServer
name|mbeanServer
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectName
name|on
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:type=services,*"
argument_list|)
decl_stmt|;
name|QueryExp
name|queryExp
init|=
name|Query
operator|.
name|match
argument_list|(
operator|new
name|AttributeValueExp
argument_list|(
literal|"ServiceType"
argument_list|)
argument_list|,
operator|new
name|StringValueExp
argument_list|(
literal|"DefaultCamelReactiveStreamsService"
argument_list|)
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|names
init|=
name|mbeanServer
operator|.
name|queryNames
argument_list|(
name|on
argument_list|,
name|queryExp
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|names
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|useJmx ()
specifier|protected
name|boolean
name|useJmx
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
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
literal|"reactive-streams:unbounded?maxInflightExchanges=-1"
argument_list|)
operator|.
name|delayer
argument_list|(
literal|1
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unbounded-endpoint"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"timer:tick"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|simple
argument_list|(
literal|"Hello world ${header.CamelTimerCounter}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"reactive-streams:strings"
argument_list|)
expr_stmt|;
name|CamelReactiveStreamsService
name|rxCamel
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
name|Flux
operator|.
name|from
argument_list|(
name|rxCamel
operator|.
name|fromStream
argument_list|(
literal|"strings"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|System
operator|.
name|out
operator|::
name|println
argument_list|)
operator|.
name|subscribe
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

