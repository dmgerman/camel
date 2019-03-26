begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
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

begin_comment
comment|/**  * This is an alternative example to show how you can use a public static void main class  * to run Camel standalone (without help from its Main class). This is to demonstrate  * what code you need to write to startup Camel without any help (or magic).  *<p/>  * Compare this example with {@link MyApplication} which uses Camel's main class to  * run Camel standalone in a easier way.  */
end_comment

begin_class
DECL|class|StandaloneCamel
specifier|public
class|class
name|StandaloneCamel
block|{
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
comment|// create a new CamelContext
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
comment|// configure where to load properties file in the properties component
name|camelContext
operator|.
name|getPropertiesComponent
argument_list|()
operator|.
name|setLocation
argument_list|(
literal|"classpath:application.properties"
argument_list|)
expr_stmt|;
comment|// resolve property placeholder
name|String
name|hello
init|=
name|camelContext
operator|.
name|resolvePropertyPlaceholders
argument_list|(
literal|"{{hi}}"
argument_list|)
decl_stmt|;
comment|// and create bean with the placeholder
name|MyBean
name|myBean
init|=
operator|new
name|MyBean
argument_list|(
name|hello
argument_list|)
decl_stmt|;
comment|// register bean to Camel
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"myBean"
argument_list|,
name|myBean
argument_list|)
expr_stmt|;
comment|// add routes to Camel
name|camelContext
operator|.
name|addRoutes
argument_list|(
operator|new
name|MyRouteBuilder
argument_list|()
argument_list|)
expr_stmt|;
comment|// start Camel
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// just run for 10 seconds and stop
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Running for 10 seconds and then stopping"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|10000
argument_list|)
expr_stmt|;
comment|// stop and shutdown Camel
name|camelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

