begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.resources
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|resources
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|model
operator|.
name|RouteDefinition
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
name|model
operator|.
name|RoutesDefinition
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|RoutesTest
specifier|public
class|class
name|RoutesTest
extends|extends
name|TestSupport
block|{
DECL|method|testRoutes ()
specifier|public
name|void
name|testRoutes
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|text
init|=
name|resource
operator|.
name|path
argument_list|(
literal|"routes"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/xml"
argument_list|)
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Routes XML: "
operator|+
name|text
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"XML should not be null"
argument_list|,
name|text
argument_list|)
expr_stmt|;
name|RoutesDefinition
name|routes
init|=
name|resource
operator|.
name|path
argument_list|(
literal|"routes"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/xml"
argument_list|)
operator|.
name|get
argument_list|(
name|RoutesDefinition
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have found routes"
argument_list|,
name|routes
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|routeList
init|=
name|routes
operator|.
name|getRoutes
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have more than one route"
argument_list|,
name|routeList
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Have routes: "
operator|+
name|routeList
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

