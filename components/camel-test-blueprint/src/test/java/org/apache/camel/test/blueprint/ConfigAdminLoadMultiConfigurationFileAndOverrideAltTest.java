begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|blueprint
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Dictionary
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
comment|/**  * This test should be run in tandem with ConfigAdminLoadConfigurationFileAndOverrideTest.  These examples will load a  * Blueprint .cfg file with multiple property placeholders defined.   We need two tests to make sure we  * process both of them correctly  */
end_comment

begin_class
DECL|class|ConfigAdminLoadMultiConfigurationFileAndOverrideAltTest
specifier|public
class|class
name|ConfigAdminLoadMultiConfigurationFileAndOverrideAltTest
extends|extends
name|CamelBlueprintTestSupport
block|{
annotation|@
name|Override
DECL|method|getBlueprintDescriptor ()
specifier|protected
name|String
name|getBlueprintDescriptor
parameter_list|()
block|{
comment|// which blueprint XML file to use for this test
return|return
literal|"org/apache/camel/test/blueprint/configadmin-loadmultifileoverride.xml"
return|;
block|}
annotation|@
name|Override
DECL|method|loadConfigAdminConfigurationFile ()
specifier|protected
name|String
index|[]
name|loadConfigAdminConfigurationFile
parameter_list|()
block|{
comment|// which .cfg files to use, and their corresponding persistence-ids
return|return
operator|new
name|String
index|[]
block|{
literal|"src/test/resources/etc/stuff.cfg"
block|,
literal|"stuff"
block|,
literal|"src/test/resources/etc/otherstuff.cfg"
block|,
literal|"otherstuff"
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|useOverridePropertiesWithConfigAdmin (Dictionary props)
specifier|protected
name|String
name|useOverridePropertiesWithConfigAdmin
parameter_list|(
name|Dictionary
name|props
parameter_list|)
throws|throws
name|Exception
block|{
comment|// override / add extra properties
name|props
operator|.
name|put
argument_list|(
literal|"destination"
argument_list|,
literal|"mock:extra"
argument_list|)
expr_stmt|;
comment|// return the persistence-id to use
return|return
literal|"stuff"
return|;
block|}
annotation|@
name|Test
DECL|method|testConfigAdmin ()
specifier|public
name|void
name|testConfigAdmin
parameter_list|()
throws|throws
name|Exception
block|{
comment|// mock:original comes from<cm:default-properties>/<cm:property name="destination" value="mock:original" />
name|getMockEndpoint
argument_list|(
literal|"mock:original"
argument_list|)
operator|.
name|setExpectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// mock:result comes from loadConfigAdminConfigurationFile()
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|setExpectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// mock:extra comes from useOverridePropertiesWithConfigAdmin()
name|getMockEndpoint
argument_list|(
literal|"mock:extra"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|,
literal|"Yay Bye WorldYay Bye World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

