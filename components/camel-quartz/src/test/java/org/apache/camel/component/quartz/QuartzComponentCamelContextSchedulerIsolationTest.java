begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quartz
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|quartz
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|impl
operator|.
name|DefaultCamelContext
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
name|management
operator|.
name|JmxSystemPropertyKeys
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
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
name|org
operator|.
name|quartz
operator|.
name|Scheduler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|SchedulerException
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotSame
import|;
end_import

begin_class
DECL|class|QuartzComponentCamelContextSchedulerIsolationTest
specifier|public
class|class
name|QuartzComponentCamelContextSchedulerIsolationTest
block|{
annotation|@
name|AfterClass
DECL|method|afterTests ()
specifier|public
specifier|static
name|void
name|afterTests
parameter_list|()
block|{
name|System
operator|.
name|clearProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|DISABLED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSchedulerIsolationUnmanaged ()
specifier|public
name|void
name|testSchedulerIsolationUnmanaged
parameter_list|()
throws|throws
name|Exception
block|{
name|disableJMX
argument_list|()
expr_stmt|;
name|testSchedulerIsolation
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSchedulerIsolationManaged ()
specifier|public
name|void
name|testSchedulerIsolationManaged
parameter_list|()
throws|throws
name|Exception
block|{
name|enableJMX
argument_list|()
expr_stmt|;
name|testSchedulerIsolation
argument_list|()
expr_stmt|;
block|}
DECL|method|testSchedulerIsolation ()
specifier|private
name|void
name|testSchedulerIsolation
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|CamelContext
name|anotherContext
init|=
name|createCamelContext
argument_list|()
decl_stmt|;
name|assertNotEquals
argument_list|(
name|anotherContext
operator|.
name|getName
argument_list|()
argument_list|,
name|context
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
name|anotherContext
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|getDefaultScheduler
argument_list|(
name|context
argument_list|)
argument_list|,
name|getDefaultScheduler
argument_list|(
name|anotherContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create a new camel context instance.      */
DECL|method|createCamelContext ()
specifier|private
name|DefaultCamelContext
name|createCamelContext
parameter_list|()
block|{
return|return
operator|new
name|DefaultCamelContext
argument_list|()
return|;
block|}
comment|/**      * Get the quartz component for the provided camel context.      */
DECL|method|getQuartzComponent (CamelContext context)
specifier|private
name|QuartzComponent
name|getQuartzComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
return|return
name|context
operator|.
name|getComponent
argument_list|(
literal|"quartz"
argument_list|,
name|QuartzComponent
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Get the default scheduler for the provided camel context.      */
DECL|method|getDefaultScheduler (CamelContext context)
specifier|private
name|Scheduler
name|getDefaultScheduler
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|SchedulerException
block|{
return|return
name|getQuartzComponent
argument_list|(
name|context
argument_list|)
operator|.
name|getFactory
argument_list|()
operator|.
name|getScheduler
argument_list|()
return|;
block|}
comment|/**      * Disables the JMX agent.      */
DECL|method|disableJMX ()
specifier|private
name|void
name|disableJMX
parameter_list|()
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|DISABLED
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Enables the JMX agent.      */
DECL|method|enableJMX ()
specifier|private
name|void
name|enableJMX
parameter_list|()
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|DISABLED
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

