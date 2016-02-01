begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.cdi.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|cdi
operator|.
name|osgi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
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
name|ServiceStatus
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedRouteMBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|console
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|console
operator|.
name|SessionFactory
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
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|PaxExam
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|spi
operator|.
name|reactors
operator|.
name|ExamReactorStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|spi
operator|.
name|reactors
operator|.
name|PerClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|tinybundles
operator|.
name|core
operator|.
name|TinyBundles
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|cdi
operator|.
name|osgi
operator|.
name|PaxExamOptions
operator|.
name|ACTIVEMQ
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|cdi
operator|.
name|osgi
operator|.
name|PaxExamOptions
operator|.
name|CAMEL_CDI
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|cdi
operator|.
name|osgi
operator|.
name|PaxExamOptions
operator|.
name|CAMEL_COMMANDS
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|cdi
operator|.
name|osgi
operator|.
name|PaxExamOptions
operator|.
name|CAMEL_SJMS
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|cdi
operator|.
name|osgi
operator|.
name|PaxExamOptions
operator|.
name|KARAF
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|cdi
operator|.
name|osgi
operator|.
name|PaxExamOptions
operator|.
name|PAX_CDI_IMPL
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|IsEqual
operator|.
name|equalTo
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
name|assertThat
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|options
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|streamBundle
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|when
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|debugConfiguration
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|PaxExam
operator|.
name|class
argument_list|)
annotation|@
name|ExamReactorStrategy
argument_list|(
name|PerClass
operator|.
name|class
argument_list|)
DECL|class|CdiOsgiIT
specifier|public
class|class
name|CdiOsgiIT
block|{
annotation|@
name|Inject
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
annotation|@
name|Inject
DECL|field|sessionFactory
specifier|private
name|SessionFactory
name|sessionFactory
decl_stmt|;
annotation|@
name|Configuration
DECL|method|config ()
specifier|public
name|Option
index|[]
name|config
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|options
argument_list|(
name|KARAF
operator|.
name|option
argument_list|()
argument_list|,
name|CAMEL_COMMANDS
operator|.
name|option
argument_list|()
argument_list|,
name|PAX_CDI_IMPL
operator|.
name|option
argument_list|()
argument_list|,
name|CAMEL_CDI
operator|.
name|option
argument_list|()
argument_list|,
name|CAMEL_SJMS
operator|.
name|option
argument_list|()
argument_list|,
name|ACTIVEMQ
operator|.
name|option
argument_list|()
argument_list|,
name|streamBundle
argument_list|(
name|TinyBundles
operator|.
name|bundle
argument_list|()
operator|.
name|read
argument_list|(
name|Files
operator|.
name|newInputStream
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"target"
argument_list|)
operator|.
name|resolve
argument_list|(
literal|"camel-example-cdi-osgi.jar"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
argument_list|,
name|when
argument_list|(
literal|false
argument_list|)
operator|.
name|useOptions
argument_list|(
name|debugConfiguration
argument_list|(
literal|"5005"
argument_list|,
literal|true
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testRouteStatus ()
specifier|public
name|void
name|testRouteStatus
parameter_list|()
block|{
name|assertThat
argument_list|(
literal|"Route status is incorrect!"
argument_list|,
name|context
operator|.
name|getRouteStatus
argument_list|(
literal|"consumer-route"
argument_list|)
argument_list|,
name|equalTo
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExchangesCompleted ()
specifier|public
name|void
name|testExchangesCompleted
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRouteMBean
name|route
init|=
name|context
operator|.
name|getManagedRoute
argument_list|(
name|context
operator|.
name|getRoute
argument_list|(
literal|"consumer-route"
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|,
name|ManagedRouteMBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
literal|"Number of exchanges completed is incorrect!"
argument_list|,
name|route
operator|.
name|getExchangesCompleted
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|1L
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExecuteCommands ()
specifier|public
name|void
name|testExecuteCommands
parameter_list|()
throws|throws
name|Exception
block|{
name|Session
name|session
init|=
name|sessionFactory
operator|.
name|create
argument_list|(
name|System
operator|.
name|in
argument_list|,
name|System
operator|.
name|out
argument_list|,
name|System
operator|.
name|err
argument_list|)
decl_stmt|;
name|session
operator|.
name|execute
argument_list|(
literal|"camel:context-list"
argument_list|)
expr_stmt|;
name|session
operator|.
name|execute
argument_list|(
literal|"camel:route-list"
argument_list|)
expr_stmt|;
name|session
operator|.
name|execute
argument_list|(
literal|"camel:route-info consumer-route"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

