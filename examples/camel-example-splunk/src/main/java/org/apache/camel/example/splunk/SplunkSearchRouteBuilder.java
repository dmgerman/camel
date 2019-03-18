begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.splunk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|splunk
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
name|properties
operator|.
name|PropertiesComponent
import|;
end_import

begin_class
DECL|class|SplunkSearchRouteBuilder
specifier|public
class|class
name|SplunkSearchRouteBuilder
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|info
argument_list|(
literal|"About to setup Splunk search route: Splunk Server --> log{results}"
argument_list|)
expr_stmt|;
comment|// configure properties component
name|PropertiesComponent
name|pc
init|=
name|getContext
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"properties"
argument_list|,
name|PropertiesComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|pc
operator|.
name|setLocation
argument_list|(
literal|"classpath:application.properties"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"splunk://normal?host={{splunk.host}}&port={{splunk.port}}&delay=10s"
operator|+
literal|"&username={{splunk.username}}&password={{splunk.password}}&initEarliestTime=08/17/13 08:35:46:456"
operator|+
literal|"&sourceType=access_combined_wcookie&search=search Code=D | head 5"
argument_list|)
operator|.
name|log
argument_list|(
literal|"${body}"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

