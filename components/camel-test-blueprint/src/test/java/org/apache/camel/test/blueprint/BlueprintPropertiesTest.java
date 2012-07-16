begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|BlueprintContainer
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|BlueprintPropertiesTest
specifier|public
class|class
name|BlueprintPropertiesTest
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
return|return
literal|"org/apache/camel/test/blueprint/configadmin.xml"
return|;
block|}
annotation|@
name|Test
DECL|method|testProperties ()
specifier|public
name|void
name|testProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|Bundle
name|camelCore
init|=
name|getBundleBySymbolicName
argument_list|(
literal|"org.apache.camel.camel-core"
argument_list|)
decl_stmt|;
name|Bundle
name|test
init|=
name|getBundleBySymbolicName
argument_list|(
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
decl_stmt|;
name|camelCore
operator|.
name|stop
argument_list|()
expr_stmt|;
name|test
operator|.
name|stop
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|test
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|getOsgiService
argument_list|(
name|BlueprintContainer
operator|.
name|class
argument_list|,
literal|"(osgi.blueprint.container.symbolicname="
operator|+
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|")"
argument_list|,
literal|500
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected a timeout"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
comment|// Expected timeout
block|}
name|camelCore
operator|.
name|start
argument_list|()
expr_stmt|;
name|getOsgiService
argument_list|(
name|BlueprintContainer
operator|.
name|class
argument_list|,
literal|"(osgi.blueprint.container.symbolicname="
operator|+
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|")"
argument_list|,
literal|500
argument_list|)
expr_stmt|;
block|}
DECL|method|getBundleBySymbolicName (String name)
specifier|private
name|Bundle
name|getBundleBySymbolicName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
for|for
control|(
name|Bundle
name|bundle
range|:
name|getBundleContext
argument_list|()
operator|.
name|getBundles
argument_list|()
control|)
block|{
if|if
condition|(
name|bundle
operator|.
name|getSymbolicName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|bundle
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

