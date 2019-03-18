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
comment|/**  * This is the first of two tests which will load a Blueprint .cfg file (which will initialize configadmin), containing  * multiple property placeholders and also override its property placeholders directly (the change will reload blueprint  * container).  */
end_comment

begin_class
DECL|class|ConfigAdminLoadMultiConfigurationFileAndOverrideTest
specifier|public
class|class
name|ConfigAdminLoadMultiConfigurationFileAndOverrideTest
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
DECL|method|useOverridePropertiesWithConfigAdmin (Dictionary properties)
specifier|protected
name|String
name|useOverridePropertiesWithConfigAdmin
parameter_list|(
name|Dictionary
name|properties
parameter_list|)
throws|throws
name|Exception
block|{
comment|// override / add extra properties
name|properties
operator|.
name|put
argument_list|(
literal|"arrive"
argument_list|,
literal|"mock:otherExtra"
argument_list|)
expr_stmt|;
comment|// return the persistence-id to use
return|return
literal|"otherstuff"
return|;
block|}
annotation|@
name|Test
DECL|method|testConfigAdminWithMultiplePids ()
specifier|public
name|void
name|testConfigAdminWithMultiplePids
parameter_list|()
throws|throws
name|Exception
block|{
comment|// mock:otherOriginal comes from<cm:default-properties>/<cm:property name="arrive" value="mock:otherOriginal" />
name|getMockEndpoint
argument_list|(
literal|"mock:otherOriginal"
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
literal|"mock:otherResult"
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
literal|"mock:otherExtra"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Adieu World"
argument_list|,
literal|"tiens! Adieu Worldtiens! Adieu World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:otherStart"
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

