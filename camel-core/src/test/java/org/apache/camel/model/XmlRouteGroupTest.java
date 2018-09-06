begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
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
DECL|class|XmlRouteGroupTest
specifier|public
class|class
name|XmlRouteGroupTest
extends|extends
name|XmlTestSupport
block|{
annotation|@
name|Test
DECL|method|testXmlRouteGroup ()
specifier|public
name|void
name|testXmlRouteGroup
parameter_list|()
throws|throws
name|JAXBException
block|{
name|RouteContainer
name|context
init|=
name|assertParseAsJaxb
argument_list|(
literal|"routeGroup.xml"
argument_list|)
decl_stmt|;
name|RouteDefinition
name|route
init|=
name|assertOneElement
argument_list|(
name|context
operator|.
name|getRoutes
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"route-id"
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"route-group"
argument_list|,
name|route
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

