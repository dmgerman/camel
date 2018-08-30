begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.undertow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|undertow
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|CamelContext
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
name|properties
operator|.
name|PropertiesComponent
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
name|impl
operator|.
name|JndiRegistry
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
name|AvailablePortFinder
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
name|BeforeClass
import|;
end_import

begin_comment
comment|/**  * Base class of tests which allocates ports  */
end_comment

begin_class
DECL|class|BaseUndertowTest
specifier|public
class|class
name|BaseUndertowTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|port
specifier|private
specifier|static
specifier|volatile
name|int
name|port
decl_stmt|;
DECL|field|port2
specifier|private
specifier|static
specifier|volatile
name|int
name|port2
decl_stmt|;
DECL|field|counter
specifier|private
specifier|final
name|AtomicInteger
name|counter
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|1
argument_list|)
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|initPort ()
specifier|public
specifier|static
name|void
name|initPort
parameter_list|()
throws|throws
name|Exception
block|{
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|8000
argument_list|)
expr_stmt|;
name|port2
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|9000
argument_list|)
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|protected
specifier|static
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|getPort2 ()
specifier|protected
specifier|static
name|int
name|getPort2
parameter_list|()
block|{
return|return
name|port2
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"properties"
argument_list|,
operator|new
name|PropertiesComponent
argument_list|(
literal|"ref:prop"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|Properties
name|prop
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|prop
operator|.
name|setProperty
argument_list|(
literal|"port"
argument_list|,
literal|""
operator|+
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|prop
operator|.
name|setProperty
argument_list|(
literal|"port2"
argument_list|,
literal|""
operator|+
name|getPort2
argument_list|()
argument_list|)
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"prop"
argument_list|,
name|prop
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
DECL|method|getNextPort ()
specifier|protected
name|int
name|getNextPort
parameter_list|()
block|{
return|return
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
name|port
operator|+
name|counter
operator|.
name|getAndIncrement
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getNextPort (int startWithPort)
specifier|protected
name|int
name|getNextPort
parameter_list|(
name|int
name|startWithPort
parameter_list|)
block|{
return|return
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
name|startWithPort
argument_list|)
return|;
block|}
block|}
end_class

end_unit

